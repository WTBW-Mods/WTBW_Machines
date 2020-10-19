package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.FluidInventoryWrapper;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.tile.util.fluid.BaseFluidTank;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.gui.container.ChemicalPlantContainer;
import com.wtbw.mods.machines.recipe.ChemicalRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
  
  public ChemicalPlantEntity()
  {
    super(ModTiles.CHEMICAL_PLANT, 10000000, 1000000);
    
    manager
      .register("inventory", getInventory())
      .register("inputTank", getFluidInput())
      .register("outputTank", getFluidOutput())
      .registerInt("durationLeft", () -> durationLeft, (i) -> durationLeft = i);
  }
  
  @Override
  protected List<ItemStackHandler> getInventories()
  {
    return Utilities.listOf(getInventory());
  }
  
  @Override
  protected void getExtraGuiUpdateTag(CompoundNBT nbt)
  {
    manager.write(nbt);
  }
  
  @Override
  protected void handleExtraGuiUpdateTag(CompoundNBT nbt)
  {
    manager.read(nbt);
  }
  
  @Nullable
  @Override
  public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
  {
    return new ChemicalPlantContainer(p_createMenu_1_, world, pos, p_createMenu_2_);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      boolean on = false;
      
      if (getInventory().getStackInSlot(INPUT_SLOT).isEmpty() && fluidInputTank.getRemainingCapacity() == fluidInputTank.getCapacity())
      {
        durationLeft = -1;
        recipe = null;
      }
      else
      {
        ChemicalRecipe old = recipe;
        if (recipe == null || !recipe.matches(getInventoryWrapper(), world))
        {
          recipe = getRecipe();
        }
        
        if (recipe != null)
        {
          if (recipe != old || durationLeft < 0)
          {
            durationLeft = recipe.duration;
          }
          
          if (validInputs(recipe) && canOutput(recipe))
          {
            int powerUsage = recipe.energy / recipe.duration;
            if (powerUsage <= 0)
            {
              powerUsage = 1;
            }
            
            if (getStorage().getEnergyStored() >= powerUsage)
            {
              getStorage().extractInternal(powerUsage, false);
              doProgress();
              
              on = true;
            }
          }
        }
      }
      
      if (isOn() != on)
      {
        setOn(on);
      }
    }
  }
  
  private void doProgress()
  {
    durationLeft--;
    if (durationLeft <= 0)
    {
      ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
      input.shrink(recipe.ingredientCost);
      inventory.setStackInSlot(INPUT_SLOT, input);
      
      fluidInputTank.drainInternally(recipe.fluidInputAmount, IFluidHandler.FluidAction.EXECUTE);
      
      ItemStack output = getInventory().getStackInSlot(OUTPUT_SLOT);
      if (recipe.outputItem != null)
      {
        if (!output.isEmpty())
        {
          if (output.getItem() == recipe.outputItem.getItem())
          {
            output.grow(recipe.outputItem.getCount());
          }
        }
        else
        {
          output = recipe.outputItem.copy();
        }
        
        getInventory().setStackInSlot(OUTPUT_SLOT, output);
      }
  
      if (recipe.outputFluid != null)
      {
        fluidOutputTank.fillInternally(recipe.outputFluid, IFluidHandler.FluidAction.EXECUTE);
      }
    }
  }
  
  private boolean validInputs(ChemicalRecipe recipe)
  {
    boolean item = false;
    if (recipe.ingredient != null)
    {
      item = recipe.ingredient.test(inventory.getStackInSlot(INPUT_SLOT)) && inventory.getStackInSlot(INPUT_SLOT).getCount() >= recipe.ingredientCost;
    }
    boolean fluid = false;
    if (recipe.fluidInput != null)
    {
      fluid = recipe.fluidInput.test(fluidInputTank.getFluid(), recipe.fluidInputAmount);
    }
    
    return item || fluid;
  }
  
  private boolean canOutput(ChemicalRecipe recipe)
  {
    if (canOutput(OUTPUT_SLOT, getInventory(), recipe))
    {
      return fluidOutputTank.isFluidValid(recipe.outputFluid) && fluidOutputTank.getRemainingCapacity() <= recipe.outputFluid.getAmount();
    }
    
    return false;
  }
  
  private ChemicalRecipe getRecipe()
  {
    return Utilities.getRecipe(world, ModRecipes.CHEMICAL, getInventoryWrapper());
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
  
  @Nonnull
  public BaseFluidTank getFluidInput()
  {
    if (fluidInputTank == null)
    {
      fluidInputTank = new BaseFluidTank(100000);
      fluidInputTank.setFluid(new FluidStack(Fluids.LAVA, 5000));
    }
    
    return fluidInputTank;
  }
  
  @Nonnull
  public BaseFluidTank getFluidOutput()
  {
    if (fluidOutputTank == null)
    {
      fluidOutputTank = new BaseFluidTank(10000).extractOnly();
      fluidOutputTank.setFluid(new FluidStack(Fluids.WATER, 5000));
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
  
  public int getDuration()
  {
    return recipe != null ? recipe.duration : 1;
  }
  
  public int getProgress()
  {
    return recipe != null ? recipe.duration - durationLeft : 0;
  }
}
