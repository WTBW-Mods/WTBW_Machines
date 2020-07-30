package com.wtbw.mods.machines.tile.multi;

import com.wtbw.mods.lib.gui.util.ClickType;
import com.wtbw.mods.lib.tile.util.IButtonHandler;
import com.wtbw.mods.lib.tile.util.IContentHolder;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.gui.container.FluidInputHatchContainer;
import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class FluidInputHatchTile extends TileEntity implements IWTBWNamedContainerProvider, IGuiUpdateHandler, IButtonHandler, IContentHolder
{
  public static final int BUCKET_INPUT = 0;
  public static final int BUCKET_OUTPUT = 1;
  
  public static final int BUTTON_CLEAR_FLUID = 0;
  
  protected final NBTManager manager = new NBTManager();
  
  protected FluidTank tank;
  protected LazyOptional<FluidTank> tankOpt = LazyOptional.of(this::getTank);
  
  protected ItemStackHandler inventory;
  protected LazyOptional<ItemStackHandler> inventoryOpt = LazyOptional.of(this::getInventory);
  
  private boolean filling = false;
  
  public FluidInputHatchTile()
  {
    super(ModTiles.FLUID_INPUT_HATCH);
    
    manager.register("tank", getTank());
    manager.register("inventory", getInventory());
  }
  
  @Nonnull
  public FluidTank getTank()
  {
    if (tank == null)
    {
      tank = new FluidTank(1000 * 10)
      {
        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
          return FluidStack.EMPTY;
        }
  
        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
          return FluidStack.EMPTY;
        }
  
        @Override
        public void setFluid(FluidStack stack)
        {
          super.setFluid(stack);
          onContentsChanged();
        }
  
        @Override
        protected void onContentsChanged()
        {
          checkCanFill();
        }
      };
    }
    
    return tank;
  }
  
  @Nonnull
  public ItemStackHandler getInventory()
  {
    if (inventory == null)
    {
      inventory = new ItemStackHandler(2)
      {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
          if (slot == BUCKET_INPUT)
          {
            return super.insertItem(slot, stack, simulate);
          }
          else
          {
            return ItemStack.EMPTY;
          }
        }
    
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          if (slot == BUCKET_OUTPUT)
          {
            return false;
          }
          else
          {
            if (slot == BUCKET_INPUT)
            {
              Item item = stack.getItem();
              if (item == Items.BUCKET)
              {
                return false;
              }
              
              if (item instanceof BucketItem)
              {
                BucketItem bucketItem = (BucketItem) item;
                Fluid fluid = bucketItem.getFluid();
  
                return tank.isFluidValid(new FluidStack(fluid, 1000));
              }
              return false;
            }
          }
          
          return false;
        }
  
        @Override
        protected void onContentsChanged(int slot)
        {
          checkCanFill();
        }
      };
    }
    
    return inventory;
  }
  
  private void checkCanFill()
  {
    // to prevent filling it more than 1 bucket worth
    if (filling)
    {
      return;
    }
    
    filling = true;
    
    if (tank.getCapacity() - tank.getFluidAmount() >= 1000)
    {
      ItemStack stackInSlot = getInventory().getStackInSlot(BUCKET_INPUT);
      ItemStack outputStack = getInventory().getStackInSlot(BUCKET_OUTPUT);
      if (!stackInSlot.isEmpty())
      {
        BucketItem bucket = (BucketItem) stackInSlot.getItem();
        if (tank.isFluidValid(new FluidStack(bucket.getFluid(), 1000))
          && (outputStack.isEmpty() || outputStack.getCount() <= outputStack.getMaxStackSize() - 1))
        {
          if (tank.fill(new FluidStack(bucket.getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE) > 0)
          {
            getInventory().setStackInSlot(BUCKET_INPUT, ItemStack.EMPTY);
            if (outputStack.isEmpty())
            {
              getInventory().setStackInSlot(BUCKET_OUTPUT, new ItemStack(Items.BUCKET));
            }
            else
            {
              outputStack.grow(1);
              getInventory().setStackInSlot(BUCKET_OUTPUT, outputStack);
            }
          }
        }
      }
    }
    
    filling = false;
  }
  
  @Override
  public void read(BlockState state, CompoundNBT compound)
  {
    manager.read(compound);
    super.read(state, compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    manager.write(compound);
    return super.write(compound);
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
    {
      return tankOpt.cast();
    }
    
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventoryOpt.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new FluidInputHatchContainer(id, world, pos, inventory);
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
    if (buttonID == BUTTON_CLEAR_FLUID)
    {
      tank.setFluid(FluidStack.EMPTY);
      
      return true;
    }
    
    return false;
  }
  
  @Override
  public void dropContents()
  {
    Utilities.dropItems(world, inventory, pos);
  }
}
