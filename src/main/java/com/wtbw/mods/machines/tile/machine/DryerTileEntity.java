package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.*;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.nbt.Manager;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.lib.util.StackUtil;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.block.base.BaseMachineBlock;
import com.wtbw.mods.machines.gui.container.DryerContainer;
import com.wtbw.mods.machines.recipe.DryerRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class DryerTileEntity extends BaseMachineEntity
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
  
  private int heat;
  private int subHeat;
  private int targetHeat;
  
  private DryerRecipe recipe;
  
  public DryerTileEntity()
  {
    super(ModTiles.DRYER, 1000000, 50000, RedstoneMode.IGNORE);
    
    manager
      .registerInt("duration",() -> duration, i -> duration = i)
      .registerInt("progress", () -> progress, i -> progress = i)
      .registerInt("powerUsage", () -> powerUsage, i -> powerUsage = i)
      .registerInt("heat", () -> heat, i -> heat = i)
      .registerInt("targetHeat", () -> targetHeat, i -> targetHeat = i)
      .registerInt("subHeat", () -> subHeat, i -> subHeat = i)
      .register("inventory", getInventory());
  }
  
  @Nonnull
  public BaseEnergyStorage getStorage()
  {
    if (storage == null)
    {
      storage = new BaseEnergyStorage(1000000, 50000, 0);
    }
    
    return storage;
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
    // todo: only accept recipe valid items
    getFakeInventory().setInventorySlotContents(0, stack);
    return !stack.isEmpty() && getRecipe(getFakeInventory()) != null;
  }
  
  @Override
  public void dropContents()
  {
    Utilities.dropItems(world, inventory, pos);
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
    return new DryerContainer(id, world, pos, inventory);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      if (heat <= targetHeat)
      {
        powerUsage = targetHeat - heat / 2 + targetHeat / 2;
      }
      else
      {
        powerUsage = 25;
      }
      
      
      if (storage.getEnergyStored() >= powerUsage)
      {
        storage.extractInternal(powerUsage, false);
      }
      else
      {
        if (isOn())
        {
          setOn(false);
        }
        decayHeat();
        decayHeat();
      }
      
      if (inventory.getStackInSlot(INPUT_SLOT).isEmpty())
      {
        progress = 0;
        targetHeat = 20;
        recipe = null;
      }
      else
      {
        DryerRecipe old = recipe;
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
          targetHeat = recipe.heat;
          
          if (recipe != old)
          {
            progress = 0;
          }
          
          if (properHeat())
          {
            if (powerUsage < 0)
            {
              powerUsage = 0;
            }
      
            if (canOutput())
            {
              if (storage.getEnergyStored() >= powerUsage)
              {

                doProgress();
              }
            }
          }
        }
      }
      
      if (targetHeat < 20)
      {
        targetHeat = 20;
      }
      
      if (heat < 20)
      {
        heat = 20;
      }
      
      if (heat > targetHeat)
      {
        decayHeat();
      }

      markDirty();
      
    }
  }
  
  private void decayHeat()
  {
    if (isOn())
    {
      setOn(false);
    }
    
    heat -= (int) (Math.sqrt(Math.abs(heat - targetHeat))) / 5 + 1;
  }
  
  private void generateHeat()
  {
    if (!isOn())
    {
      setOn(true);
    }
    
    heat += (int) (Math.sqrt(Math.abs(targetHeat - heat))) / 5 + 1;
  }
  
  private boolean properHeat()
  {
    if (heat < targetHeat)
    {
      generateHeat();
    }
    
    return heat >= targetHeat;
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
  
  private DryerRecipe getRecipe()
  {
    return getRecipe(getInventoryWrapper());
  }
  
  private DryerRecipe getRecipe(IInventory inventory)
  {
    return Utilities.getRecipe(world, ModRecipes.DRYING, inventory);
  }
  
  
  public NBTManager getManager()
  {
    return manager;
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
  
  public int getHeat()
  {
    return heat;
  }
  
  public int getSubHeat()
  {
    return subHeat;
  }
  
  public int getTargetHeat()
  {
    return targetHeat;
  }
}
