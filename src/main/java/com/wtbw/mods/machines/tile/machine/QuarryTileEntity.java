package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.gui.util.ClickType;
import com.wtbw.mods.lib.network.Networking;
import com.wtbw.mods.lib.tile.util.*;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.upgrade.IUpgradeable;
import com.wtbw.mods.lib.upgrade.ModifierType;
import com.wtbw.mods.lib.upgrade.UpgradeManager;
import com.wtbw.mods.lib.util.Area;
import com.wtbw.mods.lib.util.StackUtil;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.Manager;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.block.QuarryBlock;
import com.wtbw.mods.machines.config.CommonConfig;
import com.wtbw.mods.machines.gui.container.QuarryContainer;
import com.wtbw.mods.machines.network.UpdateQuarryPacket;
import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Sunekaer
*/
@SuppressWarnings("ConstantConditions")
public class QuarryTileEntity extends TileEntity implements ITickableTileEntity, IWTBWNamedContainerProvider, IRedstoneControlled, IContentHolder, IGuiUpdateHandler, IUpgradeable
{
  //TODO Make Bounding border
  
  public int upgradeLevel;
  
  private RedstoneControl control;
  private BlockPos currentPos;
  private Area area;
  private boolean isDone;
  
  private int quarrySize = CommonConfig.instance().quarryDefaultSize.get();
  private BaseEnergyStorage storage;
  private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createInventory);
  private LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  private Direction facing = null;
  
  private NBTManager nbtManager;
  private UpgradeManager upgradeManager = new UpgradeManager(3).setFilter(ModifierType.POWER_CAPACITY, ModifierType.SPEED, ModifierType.POWER_USAGE);
  
  private int work = 0;
  private int workTotal = 0;
  
  public QuarryTileEntity()
  {
    super(ModTiles.QUARRY);
    
    control = new RedstoneControl(this, RedstoneMode.ON);
    
    nbtManager = new NBTManager()
      .registerBlockPos("current", () -> currentPos, i -> currentPos = i)
      .registerBoolean("finished", () -> isDone, i -> isDone = i)
      .registerInt("upgradeLevel", () -> upgradeLevel, i -> upgradeLevel = i)
      .registerInt("quarrySize", () -> quarrySize, i -> quarrySize = i)
      .register("storage", getStorage(), false)
      .register("inventory", inventory.orElseGet(ItemStackHandler::new))
      .register("control", control)
      .register("upgrades", upgradeManager)
      .registerInt("work", () -> work, i -> work = i)
      .registerInt("workTotal", () -> workTotal, i -> workTotal = i);
    
    nbtManager.register("area", new Manager()
    {
      @Override
      public void read(String name, CompoundNBT nbt)
      {
        if (area == null)
        {
          area = new Area(0, 0, 0, 0, 0, 0);
          area.deserializeNBT(nbt.getCompound(name));
        }
      }
      
      @Override
      public void write(String name, CompoundNBT nbt)
      {
        if (area != null)
        {
          nbt.put(name, area.serializeNBT());
        }
      }
    });
  }
  
  public BaseEnergyStorage getStorage()
  {
    if (storage == null)
    {
      return storage = new BaseEnergyStorage(CommonConfig.instance().quarryCapacity.get(), Integer.MAX_VALUE, 0);
    }
    return storage;
  }
  
  public Boolean getDone()
  {
    return isDone;
  }
  
  public void setDone(Boolean done)
  {
    isDone = done;
  }
  
  public BlockPos getCurrentPos()
  {
    return currentPos;
  }
  
  public void setCurrentPos(BlockPos currentPos)
  {
    this.currentPos = currentPos;
  }
  
  private ItemStackHandler createInventory()
  {
    return new ItemStackHandler(9);
  }
  
  @Override
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new QuarryContainer(id, world, pos, inventory);
  }
  
  protected Direction getFacing()
  {
    if (facing == null)
    {
      facing = getBlockState().get(QuarryBlock.FACING);
    }
    
    return facing;
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      if (isDone)
      {
        return;
      }
      
      if (area == null)
      {
        area = Utilities.getArea(pos.offset(getFacing()).offset(Direction.DOWN), getFacing(), quarrySize, pos.getY() - 1);
        currentPos = new BlockPos(area.start.getX(), pos.getY() - 1, area.start.getZ());
        sendUpdate();
        markDirty();
      }
  
      CommonConfig config = CommonConfig.instance();
      storage.setCapacity((int) (config.quarryCapacity.get() * upgradeManager.getValueOrDefault(ModifierType.POWER_CAPACITY)));
      
      if (area != null)
      {
        if (control.update())
        {
          
          workTotal = ((int) (config.quarrySpeed.get() / upgradeManager.getValueOrDefault(ModifierType.SPEED)));
          
          if (work++ >= workTotal)
          {
            work = 0;
            if (area.isInside(currentPos))
            {
              int powerUsage = (int) (config.quarryPowerUsage.get() * upgradeManager.getValueOrDefault(ModifierType.POWER_USAGE));
              if (getStorage().getEnergyStored() >= powerUsage)
              {
                if (breakBlock())
                {
                  storage.extractInternal(powerUsage, false);
                  BlockPos startPos = new BlockPos(area.start.getX(), area.getSide(Direction.UP), area.start.getZ());
                  
                  if (currentPos.getX() == area.end.getX() && currentPos.getZ() == area.end.getZ() && currentPos.getY() == area.start.getY())
                  {
                    isDone = true;
                    sendUpdate();
                    markDirty();
                  }
                  
                  BlockPos nextX = new BlockPos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ());
                  BlockPos nextZ = new BlockPos(startPos.getX(), currentPos.getY(), currentPos.getZ() + 1);
                  BlockPos nextY = new BlockPos(startPos.getX(), currentPos.getY() - 1, startPos.getZ());
                  
                  if (area.isInside(nextX))
                  {
                    currentPos = nextX;
                  }
                  else if (area.isInside(nextZ))
                  {
                    currentPos = nextZ;
                  }
                  else if (area.isInside(nextY))
                  {
                    currentPos = nextY;
                  }
                  sendUpdate();
                  markDirty();
                }
              }
              control.resetCooldown();
            }
          }
        }
      }
    }
  }
  
  private boolean sendUpdate()
  {
    Networking.sendAround(world, pos, 12, new UpdateQuarryPacket(pos, currentPos, isDone));
    return true;
  }
  
  private boolean breakBlock()
  {
    BlockState blockState = world.getBlockState(currentPos);
    
    CommonConfig config = CommonConfig.instance();
    boolean breakTiles = config.quarryBreakTileEntities.get();
    Block block = blockState.getBlock();
    
    if (breakTiles || world.getTileEntity(currentPos) == null)
    {
      if (!block.equals(Blocks.AIR) && !CommonConfig.isInBlacklist(block))
      {
        List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, currentPos, blockState.hasTileEntity() ? world.getTileEntity(currentPos) : null);
        for (ItemStack drop : drops)
        {
          if (!StackUtil.canInsert(inventory, drop, true))
          {
            return false;
          }
        }
        // todo: respect protections etc
        world.destroyBlock(currentPos, false);
        inventory.ifPresent(handler ->
        {
          for (ItemStack drop : drops)
          {
            for (int i = 0; i < handler.getSlots(); i++)
            {
              drop = handler.insertItem(i, drop, false);
              if (drop.isEmpty())
              {
                break;
              }
            }
          }
        });
      }
    }
    return true;
  }
  
  
  @Override
  public void read(CompoundNBT compound)
  {
    nbtManager.read(compound);
    
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    nbtManager.write(compound);
    
    return super.write(compound);
  }
  
  @Override
  public boolean handleButton(int buttonID, ClickType clickType)
  {
    if (control.handleButton(buttonID, clickType))
    {
      markDirty();
      return true;
    }
    
    return false;
  }
  
  public LazyOptional<ItemStackHandler> getInventory()
  {
    return inventory;
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventory.cast();
    }
    
    if (cap == CapabilityEnergy.ENERGY)
    {
      return storageCap.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Override
  public void dropContents()
  {
    inventory.ifPresent(handler -> Utilities.dropItems(world, handler, pos));
    Utilities.dropItems(world, upgradeManager.getUpgradeInventory(), pos);
  }
  
  @Override
  public RedstoneMode[] availableModes()
  {
    return RedstoneMode.noPulse;
  }
  
  public void upgradeLevelUpdated()
  {
    switch (upgradeLevel)
    {
      case 0:
        quarrySize = CommonConfig.instance().quarryDefaultSize.get();
        break;
      case 1:
        quarrySize = CommonConfig.instance().quarryTier1.get();
        break;
      case 2:
        quarrySize = CommonConfig.instance().quarryTier2.get();
        break;
      case 3:
        quarrySize = CommonConfig.instance().quarryTier3.get();
        break;
      case 4:
        quarrySize = CommonConfig.instance().quarryTier4.get();
        break;
    }
    
    area = Utilities.getArea(pos.offset(getFacing()).offset(Direction.DOWN), getFacing(), quarrySize, pos.getY() - 1);
    currentPos = new BlockPos(area.start.getX(), pos.getY() - 1, area.start.getZ());
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putIntArray("storage", GuiUpdateHelper.getEnergyUpdateValues(storage));
    return nbt;
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    GuiUpdateHelper.updateEnergy(storage, nbt.getIntArray("storage"));
  }
  
  @Override
  public UpgradeManager getUpgradeManager()
  {
    return upgradeManager;
  }
}
