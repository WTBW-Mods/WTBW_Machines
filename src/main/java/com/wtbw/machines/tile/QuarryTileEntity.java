package com.wtbw.machines.tile;

import com.wtbw.lib.block.SixWayTileBlock;
import com.wtbw.lib.gui.util.ClickType;
import com.wtbw.lib.tile.util.IContentHolder;
import com.wtbw.lib.tile.util.IRedstoneControlled;
import com.wtbw.lib.tile.util.RedstoneControl;
import com.wtbw.lib.tile.util.RedstoneMode;
import com.wtbw.lib.util.Area;
import com.wtbw.lib.util.NBTHelper;
import com.wtbw.lib.util.StackUtil;
import com.wtbw.lib.util.Utilities;
import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.block.QuarryBlock;
import com.wtbw.machines.gui.container.BlockBreakerContainer;
import com.wtbw.machines.gui.container.QuarryContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
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
  //TODO Make it use Forge Energy.
  //TODO Make facing + move the quarry area
  //TODO Make Bounding border
  
  private RedstoneControl control;
  private BlockPos currentPos;
  private Area area;

  private int tick;
  //TODO Config for quarrySize
  private int quarrySize = ((16 * 1) / 2);
  private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createInventory);
  
  private Direction facing = null;
  
  public QuarryTileEntity()
  {
    super(ModTiles.QUARRY);
    
    control = new RedstoneControl(this, RedstoneMode.IGNORE);
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
      tick++;
      if (area == null)
      {
        area = Utilities.getArea(pos.offset(getFacing()).offset(Direction.DOWN), getFacing(), quarrySize, pos.getY() - 1);
//
//        startPos = new BlockPos(this.getPos().getX() - quarrySize, this.getPos().down().getY(), this.getPos().getZ() - quarrySize);
//        endPos = new BlockPos(this.getPos().getX() + quarrySize, 0, this.getPos().getZ() + quarrySize);
        currentPos = new BlockPos(area.start.getX(), area.getSide(Direction.UP), area.start.getZ());
        markDirty();
      }
      
      if (control.update())
      {   //TODO Change 15 to config.
        if (tick % 1 == 0)
        {
          if (area.isInside(currentPos))
          {
            if (breakBlock())
            {
              
              //Setting currentPos to the next block there should be mined
              BlockPos startPos = new BlockPos(area.start.getX(), area.getSide(Direction.UP), area.start.getZ());
              
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
  
              WTBWMachines.LOGGER.info("Current pos {} {}", currentPos, getFacing());
              
              markDirty();
            }
          }
          else
          {
            WTBWMachines.LOGGER.info("Outside area: {} {}-{} ", currentPos, area.start, area.end);
          }
          control.resetCooldown();
        }
      }
    }
  }
  
  private boolean breakBlock()
  {
    BlockState blockState = world.getBlockState(currentPos);
    
    // todo: config for breaking tiles, blacklist
    boolean breakTiles = false;
    Block block = blockState.getBlock();
    
    if (breakTiles || world.getTileEntity(currentPos) == null)
    {
      if (!block.equals(Blocks.AIR) && !block.equals(Blocks.BEDROCK))
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
    area.deserializeNBT(compound.getCompound("area"));
    currentPos = NBTHelper.getBlockPos(compound, "current");

    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    compound.put("area", area.serializeNBT());
    NBTHelper.putBlockPos(compound, "current", currentPos);

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
    return new RedstoneMode[]{RedstoneMode.IGNORE, RedstoneMode.ON, RedstoneMode.OFF};
  }
  
}
