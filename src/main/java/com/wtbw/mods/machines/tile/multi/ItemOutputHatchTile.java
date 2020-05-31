package com.wtbw.mods.machines.tile.multi;

import com.wtbw.mods.lib.gui.util.ClickType;
import com.wtbw.mods.lib.tile.util.IButtonHandler;
import com.wtbw.mods.lib.tile.util.IContentHolder;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.lib.util.InventoryItemStackHandler;
import com.wtbw.mods.lib.util.StackUtil;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.config.CommonConfig;
import com.wtbw.mods.machines.gui.container.ItemOutputHatchContainer;
import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class ItemOutputHatchTile extends TileEntity implements IContentHolder, IWTBWNamedContainerProvider, IGuiUpdateHandler, IButtonHandler, ITickableTileEntity
{
  public static final int EJECT_TOGGLE_BUTTON = 0;
  protected final NBTManager manager = new NBTManager();
  
  protected ItemStackHandler inventory;
  protected LazyOptional<ItemStackHandler> inventoryOpt = LazyOptional.of(this::getInventory);
  
  private boolean autoEject = false;
  private int maxAutoEject = 16;
  private int delay = 8;
  
  public ItemOutputHatchTile()
  {
    super(ModTiles.ITEM_OUTPUT_HATCH);
    
    manager
      .register("inventory", getInventory())
      .registerBoolean("autoEject", () -> autoEject, (b) -> autoEject = b);
  }
  
  @Nonnull
  public ItemStackHandler getInventory()
  {
    if (inventory == null)
    {
      inventory = new ItemStackHandler(9)
      {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
          return stack;
        }
  
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          return false;
        }
      };
    }
    
    return inventory;
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventoryOpt.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    manager.read(compound);
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    manager.write(compound);
    return super.write(compound);
  }
  
  @Override
  public void dropContents()
  {
    Utilities.dropItems(world, getInventory(), pos);
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new ItemOutputHatchContainer(id, world, pos, inventory);
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    return manager.write(new CompoundNBT());
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    manager.read(nbt);
  }
  
  @Override
  public boolean handleButton(int buttonID, ClickType clickType)
  {
    if (buttonID == EJECT_TOGGLE_BUTTON)
    {
      autoEject = !autoEject;
      return true;
    }
    
    return false;
  }
  
  @Override
  public void tick()
  {
    if (!autoEject || world.isRemote)
    {
      return;
    }
    
    if (--delay <= 0)
    {
      delay = CommonConfig.instance().itemOutputHatchAutoEjectDelay.get();
    }
    else
    {
      return;
    }
    
    int count = 0;
  
    List<IItemHandler> itemHandlers = new ArrayList<>();
    
    for (Direction direction : Direction.values())
    {
      BlockPos position = pos.offset(direction);
      TileEntity tileEntity = world.getTileEntity(position);
      if (tileEntity != null)
      {
        LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite());
        capability.ifPresent(itemHandlers::add);
      }
    }
    
    if (itemHandlers.isEmpty())
    {
      return;
    }
    
    maxAutoEject = CommonConfig.instance().itemOutputHatchAutoEjectAmount.get();
    for (int i = 0; i < inventory.getSlots() && count < maxAutoEject; i++)
    {
      ItemStack stack = inventory.getStackInSlot(i);
      if (stack.isEmpty())
      {
        continue;
      }
      
      ItemStack toMove = stack.copy();
      toMove.setCount(Math.min(maxAutoEject - count, stack.getCount()));
  
      for (IItemHandler itemHandler : itemHandlers)
      {
        ItemStack insert = StackUtil.insert(toMove, itemHandler);
        int moved = toMove.getCount() - insert.getCount();
        count += moved;
        stack.shrink(moved);
        if (count >= maxAutoEject)
        {
          return;
        }
    
        if (insert.isEmpty())
        {
          break;
        }
      }
    }
    
    markDirty();
  }
  
  public boolean isAutoEject()
  {
    return autoEject;
  }
  
  public int getMaxAutoEject()
  {
    return maxAutoEject;
  }
}
