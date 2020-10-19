package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.InventoryWrapper;
import com.wtbw.mods.lib.tile.util.RedstoneControl;
import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.gui.container.DehydratorContainer;
import com.wtbw.mods.machines.recipe.DehydratingRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class DehydratorTileEntity extends BaseMachineEntity
{
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  
  private ItemStackHandler inventory;
  private InventoryWrapper inventoryWrapper;
  private InventoryWrapper fakeInventory;
  private LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  private LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  
  private int duration;
  private int progress;
  private int powerUsage = 40;
  
  private DehydratingRecipe recipe;
  
  public DehydratorTileEntity()
  {
    super(ModTiles.DEHYDRATOR, DEFAULT_CAPACITY, 5000, RedstoneMode.IGNORE);
    
    manager
      .registerInt("duration",() -> duration, i -> duration = i)
      .registerInt("PROGRESS", () -> progress, i -> progress = i)
      .registerInt("powerUsage", () -> powerUsage, i -> powerUsage = i)
      .register("inventory", getInventory());
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
      fakeInventory = new InventoryWrapper(new ItemStackHandler(1));
    }
    
    return fakeInventory;
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
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
  
  protected boolean validRecipeInput(ItemStack stack)
  {
    getFakeInventory().setInventorySlotContents(0, stack);
    return !stack.isEmpty() && getRecipe(getFakeInventory()) != null;
  }
  
  @Override
  protected List<ItemStackHandler> getInventories()
  {
    return Utilities.listOf(inventory);
  }
  
  @Override
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Override
  public RedstoneMode[] availableModes()
  {
    return RedstoneMode.noPulse;
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new DehydratorContainer(id, world, pos, inventory);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      boolean on = false;
      if (inventory.getStackInSlot(INPUT_SLOT).isEmpty())
      {
        progress = 0;
        recipe = null;
      }
      else
      {
        DehydratingRecipe old = recipe;
        if (recipe == null)
        {
          recipe = getRecipe();
        }
        else
        {
          if (!recipe.ingredient.test(inventory.getStackInSlot(0)))
          {
            recipe = getRecipe();
          }
        }
  
        if (recipe != null)
        {
          duration = recipe.duration;
          if (duration < 1)
          {
            duration = 1;
          }
          
          powerUsage = recipe.powerCost / duration;
          
          if (powerUsage <= 0)
          {
            powerUsage = 1;
          }
  
          if (recipe != old)
          {
            progress = 0;
          }
  
        }
  
        if (canOutput())
        {
          if (storage.getEnergyStored() >= powerUsage)
          {
            storage.extractInternal(powerUsage, false);
            doProgress();
            on = true;
          }
        }
      }
      
      if (isOn() != on)
      {
        setOn(on);
      }
      
      markDirty();
    }
  }
  
  private boolean canOutput()
  {
    return canOutput(OUTPUT_SLOT, inventory, recipe);
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
  
  private DehydratingRecipe getRecipe()
  {
    return getRecipe(getInventoryWrapper());
  }
  
  private DehydratingRecipe getRecipe(IInventory inventory)
  {
    return Utilities.getRecipe(world, ModRecipes.DEHYDRATING, inventory);
  }
  
  
  public NBTManager getManager()
  {
    return manager;
  }
  
  @Override
  protected void getExtraGuiUpdateTag(CompoundNBT nbt)
  {
    nbt.putInt("duration", duration);
    nbt.putInt("progress", progress);
    nbt.putInt("power_usage", powerUsage);
  }
  
  @Override
  protected void handleExtraGuiUpdateTag(CompoundNBT nbt)
  {
    duration = nbt.getInt("duration");
    progress = nbt.getInt("progress");
    powerUsage = nbt.getInt("power_usage");
  }
  
  public int getDuration()
  {
    return duration;
  }
  
  public int getProgress()
  {
    return progress;
  }
  
  public int getPowerUsage()
  {
    return powerUsage;
  }
}
