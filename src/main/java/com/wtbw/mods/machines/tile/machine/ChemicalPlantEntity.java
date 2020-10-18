package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.FluidInventoryWrapper;
import com.wtbw.mods.lib.tile.util.InventoryWrapper;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.tile.util.fluid.BaseFluidTank;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.recipe.ChemicalRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class ChemicalPlantEntity extends BaseMachineEntity
{
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  
  private ItemStackHandler inventory;
  private FluidInventoryWrapper inventoryWrapper;
  private FluidInventoryWrapper fakeInventory;
  private BaseFluidTank fluidInputTank;
  private BaseFluidTank fluidOutputTank;
  
  private final LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  private final LazyOptional<BaseFluidTank> inputFluidCap = LazyOptional.of(this::getFluidInput);
  private final LazyOptional<BaseFluidTank> outputFluidCap = LazyOptional.of(this::getFluidOutput);
  
  private ChemicalRecipe recipe = null;
  private int durationLeft = -1;
  
  public ChemicalPlantEntity(TileEntityType<?> tileEntityTypeIn, int capacity, int maxReceive)
  {
    super(tileEntityTypeIn, capacity, maxReceive);
  }
  
  @Override
  protected List<ItemStackHandler> getInventories()
  {
    return Utilities.listOf(getInventory());
  }
  
  @Override
  protected void getExtraGuiUpdateTag(CompoundNBT nbt)
  { }
  
  @Override
  protected void handleExtraGuiUpdateTag(CompoundNBT nbt)
  { }
  
  @Nullable
  @Override
  public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
  {
    return null;
  }
  
  @Override
  public void tick()
  {
    if (recipe == null)
    {
      recipe = Utilities.getRecipe(world, ModRecipes.CHEMICAL, getInventoryWrapper());
    }
    
    if (recipe != null)
    {
    
    }
  }
  
  public FluidInventoryWrapper getInventoryWrapper()
  {
    if (inventoryWrapper == null)
    {
      inventoryWrapper = new FluidInventoryWrapper(getInventory(), fluidInputTank);
    }
    
    return inventoryWrapper;
  }
  
  public FluidInventoryWrapper getFakeInventory()
  {
    if (fakeInventory == null)
    {
      fakeInventory = new FluidInventoryWrapper(new ItemStackHandler(1), fluidInputTank);
    }
    
    return fakeInventory;
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
          if (slot == INPUT_SLOT)
          {
            return super.insertItem(slot, stack, simulate);
          }
          
          return stack;
        }
  
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
          if (slot == OUTPUT_SLOT)
          {
            return super.extractItem(slot, amount, simulate);
          }
          
          return ItemStack.EMPTY;
        }
  
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          return slot == INPUT_SLOT && validRecipeInput(stack);
        }
      };
    }
    
    return inventory;
  }
  
  private boolean validRecipeInput(ItemStack stack)
  {
    // todo make matching recipe only?
    return true;
  }
  
  public BaseFluidTank getFluidInput()
  {
    if (fluidInputTank == null)
    {
      fluidInputTank = new BaseFluidTank(100000);
    }
    
    return fluidInputTank;
  }
  
  public BaseFluidTank getFluidOutput()
  {
    if (fluidOutputTank == null)
    {
      fluidOutputTank = new BaseFluidTank(8).extractOnly();
    }
    
    return fluidOutputTank;
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityEnergy.ENERGY)
    {
      return storageCap.cast();
    }
    
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventoryCap.cast();
    }
    
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
    {
      if (side == Direction.UP)
      {
        return inputFluidCap.cast();
      }
      else
      {
        return outputFluidCap.cast();
      }
    }
    
    return super.getCapability(cap, side);
  }
}
