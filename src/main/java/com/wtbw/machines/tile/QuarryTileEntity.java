package com.wtbw.machines.tile;

import com.wtbw.lib.gui.util.ClickType;
import com.wtbw.lib.tile.util.IContentHolder;
import com.wtbw.lib.tile.util.IRedstoneControlled;
import com.wtbw.lib.tile.util.RedstoneControl;
import com.wtbw.lib.tile.util.RedstoneMode;
import com.wtbw.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.lib.util.*;
import com.wtbw.lib.util.nbt.Manager;
import com.wtbw.lib.util.nbt.NBTHelper;
import com.wtbw.lib.util.nbt.NBTManager;
import com.wtbw.machines.block.QuarryBlock;
import com.wtbw.machines.config.CommonConfig;
import com.wtbw.machines.gui.container.QuarryContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
public class QuarryTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IRedstoneControlled, IContentHolder
{
  //TODO Make Bounding border
  //TODO Add is done state

  private RedstoneControl control;
  private BlockPos currentPos;
  private Area area;
  private boolean isDone;

  private int tick;
  //TODO Config for quarrySize
  private int quarrySize = 15;

  private BaseEnergyStorage storage;

  private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createInventory);
  private LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);

  private Direction facing = null;
  
  private NBTManager nbtManager;

  public QuarryTileEntity()
  {
    super(ModTiles.QUARRY);
    
    control = new RedstoneControl(this, RedstoneMode.ON);
    getStorage();

    nbtManager = new NBTManager();
    nbtManager.register("energy", new Manager.Serializable(getStorage()));
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

    nbtManager.register("current", new Manager.BlockPos()
    {
      @Override
      public net.minecraft.util.math.BlockPos get()
      {
        return currentPos;
      }
  
      @Override
      public void set(net.minecraft.util.math.BlockPos value)
      {
        currentPos = value;
      }
    });
    
//    {
//      @Override
//      public void read(String name, CompoundNBT nbt)
//      {
//        currentPos = NBTHelper.getBlockPos(nbt, name);
//      }
//
//      @Override
//      public void write(String name, CompoundNBT nbt)
//      {
//        if (currentPos != null)
//        {
//          NBTHelper.putBlockPos(nbt, name, pos);
//        }
//      }
//    });

      nbtManager.register("finished", new Manager.Bool()
      {
        @Override
        public Boolean get()
        {
          return isDone;
        }
  
        @Override
        public void set(Boolean value)
        {
          isDone = value;
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

  private ItemStackHandler createInventory()
  {
    return new ItemStackHandler(9);
  }
  
  @Override
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Override
  public ITextComponent getDisplayName()
  {
    return new TranslationTextComponent(getType().getRegistryName().toString());
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

      tick++;
      if (area == null)
      {
        area = Utilities.getArea(pos.offset(getFacing()).offset(Direction.DOWN), getFacing(), quarrySize, pos.getY() - 1);
        currentPos = new BlockPos(area.start.getX(), area.getSide(Direction.UP), area.start.getZ());
        markDirty();
      }


      if (control.update())
      {
        CommonConfig config = CommonConfig.instance();
        
        if (tick % config.quarrySpeed.get() == 0)
        {
          if (area.isInside(currentPos))
          {
            if (getStorage().getEnergyStored() >= config.quarryPowerUsage.get())
            {
              if (breakBlock())
              {
                storage.extractInternal(config.quarryPowerUsage.get(), false);
                BlockPos startPos = new BlockPos(area.start.getX(), area.getSide(Direction.UP), area.start.getZ());

                if (currentPos.getX() == area.end.getX() && currentPos.getZ() == area.end.getZ() && currentPos.getY() == area.start.getY()){
                    isDone = true;
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

                markDirty();
              }
            }
            control.resetCooldown();
          }
        }
      }
    }
  }
  
  private boolean breakBlock()
  {
    BlockState blockState = world.getBlockState(currentPos);
    
    // todo: config for breaking tiles, blacklist
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
  }
  
  @Override
  public RedstoneMode[] availableModes()
  {
    return RedstoneMode.noPulse;
  }
  
}
