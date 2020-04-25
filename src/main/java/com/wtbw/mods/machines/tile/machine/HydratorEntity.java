package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.InventoryWrapper;
import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.upgrade.IUpgradeable;
import com.wtbw.mods.lib.upgrade.ModifierType;
import com.wtbw.mods.lib.upgrade.UpgradeManager;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.gui.container.HydratorContainer;
import com.wtbw.mods.machines.recipe.HydratingRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class HydratorEntity extends BaseMachineEntity implements IUpgradeable
{
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  
  private FluidTank waterTank;
  private ItemStackHandler inventory;
  private InventoryWrapper inventoryWrapper;
  private InventoryWrapper fakeInventory;
  
  private final LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  private final LazyOptional<FluidTank> waterTankCap = LazyOptional.of(this::getWaterTank);
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  
  private int duration;
  private int progress;
  private int waterUsage;
  private int powerUsage = 40;
  
  private HydratingRecipe recipe;
  
  private UpgradeManager upgradeManager = new UpgradeManager().setFilter(DEFAULT_MACHINE_FILTER);
  
  public HydratorEntity()
  {
    super(ModTiles.HYDRATOR, DEFAULT_CAPACITY, 2000, RedstoneMode.IGNORE);
    
    manager
      .registerInt("duration", () -> duration, integer -> duration = integer)
      .registerInt("progress", () -> progress, integer -> progress = integer)
      .registerInt("waterUsage", () -> waterUsage, integer -> waterUsage = integer)
      .registerInt("powerUsage", () -> powerUsage, integer -> powerUsage = integer)
      .register("inventory", getInventory())
      .register("upgrades", upgradeManager)
      .register("tank", getWaterTank());
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
  
  @Nonnull
  public FluidTank getWaterTank()
  {
    if (waterTank == null)
    {
      waterTank = new FluidTank(1000 * 5, fluidStack -> fluidStack.getFluid() == Fluids.WATER);
    }
    
    return waterTank;
  }
  
  protected boolean validRecipeInput(ItemStack stack)
  {
    getFakeInventory().setInventorySlotContents(0, stack);
    return !stack.isEmpty() && getRecipe(getFakeInventory()) != null;
  }
  
  public InventoryWrapper getInventoryWrapper()
  {
    if (inventoryWrapper == null)
    {
      inventoryWrapper = new InventoryWrapper(getInventory());
    }
    
    return inventoryWrapper;
  }
  
  public InventoryWrapper getFakeInventory()
  {
    if (fakeInventory == null)
    {
      fakeInventory = getFakeInventory(1);
    }
    
    return fakeInventory;
  }
  
  private HydratingRecipe getRecipe()
  {
    return getRecipe(getInventoryWrapper());
  }
  
  private HydratingRecipe getRecipe(IInventory inventory)
  {
    return Utilities.getRecipe(world, ModRecipes.HYDRATING, inventory);
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
    {
      return waterTankCap.cast();
    }
    
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventoryCap.cast();
    }
    
    if (cap == CapabilityEnergy.ENERGY)
    {
      return storageCap.cast();
    }
    
    return super.getCapability(cap, side);
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
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new HydratorContainer(id, world, pos, inventory);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      boolean on = false;
      
      if (getInventory().getStackInSlot(INPUT_SLOT).isEmpty())
      {
        progress = 0;
        recipe = null;
      }
      else
      {
        HydratingRecipe old = recipe;
        if (recipe == null)
        {
          recipe = getRecipe();
        }
        else if (!recipe.ingredient.test(getInventory().getStackInSlot(INPUT_SLOT)))
        {
          recipe = getRecipe();
        }
        
        if (recipe != null)
        {
          float durationMod = getUpgradeManager().getValueOrDefault(ModifierType.SPEED);
  
          duration = (int) (recipe.duration / durationMod);
          if (duration < 1)
          {
            duration = 1;
          }
  
          float powerMod = getUpgradeManager().getValueOrDefault(ModifierType.POWER_USAGE);
  
          powerUsage = recipe.powerCost / duration;
          powerUsage *= powerMod;
          
          waterUsage = recipe.waterCost / duration;
  
          if (recipe != old)
          {
            progress = 0;
          }
        }
        
        if (canOutput(OUTPUT_SLOT, inventory, recipe))
        {
          if (getStorage().getEnergyStored() >= powerUsage && getWaterTank().getFluidAmount() >= waterUsage)
          {
            getStorage().extractInternal(powerUsage, false);
            getWaterTank().drain(waterUsage, IFluidHandler.FluidAction.EXECUTE);
            
            doProgress();
            
            on = true;
          }
        }
      }
      
      if (isOn() != on)
      {
        setOn(on);
      }
  
      getStorage().setCapacity((int) (DEFAULT_CAPACITY * getUpgradeManager().getValueOrDefault(ModifierType.POWER_CAPACITY)));
    }
  }
  
  private void doProgress()
  {
    progress++;
    
    if (progress >= duration)
    {
      progress = 0;
  
      ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
      input.shrink(1);
      inventory.setStackInSlot(INPUT_SLOT, input);
  
      ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
      if (!output.isEmpty())
      {
        if (output.getItem() == recipe.output.getItem())
        {
          output.grow(recipe.output.getCount());
        }
      }
      else
      {
        output = recipe.getRecipeOutput().copy();
      }
  
      inventory.setStackInSlot(OUTPUT_SLOT, output);
    }
  }
  
  @Override
  public UpgradeManager getUpgradeManager()
  {
    return upgradeManager;
  }
  
  public int getDuration()
  {
    return duration;
  }
  
  public int getProgress()
  {
    return progress;
  }
  
  public int getWaterUsage()
  {
    return waterUsage;
  }
  
  public int getPowerUsage()
  {
    return powerUsage;
  }
}
