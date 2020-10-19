package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.InventoryWrapper;
import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.gui.container.PoweredFurnaceContainer;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.recipe.PoweredFurnaceRecipe;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
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
  @author: Sunekaer
*/
public class PoweredFurnaceEntity extends BaseMachineEntity
{
  public static final MachineTier BASIC = new MachineTier(ModTiles.POWERED_FURNACE_BASIC, MachineTier.BASIC);
  
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  
  private ItemStackHandler inventory;
  private InventoryWrapper inventoryWrapper;
  private InventoryWrapper fakeInventory;
  private LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  private LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  
  private PoweredFurnaceRecipe poweredFurnaceRecipe;
  private FurnaceRecipe furnaceRecipe;
  private int tick;
  private int duration;
  private int progress;
  private int powerCost;
  private int ingredientCost;
  
  public final MachineTier tier;
  
  public PoweredFurnaceEntity(MachineTier tier)
  {
    super(tier.tileEntityType, DEFAULT_CAPACITY, 50000, RedstoneMode.IGNORE);
    this.tier = tier;
  
    manager
      .registerInt("duration", () -> duration, i -> duration = i)
      .registerInt("PROGRESS", () -> progress, i -> progress = i)
      .registerInt("powerCost", () -> powerCost, i -> powerCost = i)
      .registerInt("ingredientCost", () -> ingredientCost, i -> ingredientCost = i)
      .register("inventory", getInventory())
      .registerInt("tick", () -> tick, i -> tick = i);
  }
  
  @Override
  protected List<ItemStackHandler> getInventories()
  {
    return Utilities.listOf(inventory);
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new PoweredFurnaceContainer(id, world, pos, inventory);
  }
  
  @Override
  public RedstoneMode[] availableModes()
  {
    return RedstoneMode.noPulse;
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
    return !stack.isEmpty() && getRecipe(getFakeInventory()) != null || getFurnaceRecipe(getFakeInventory()) != null;
  }
  
  private PoweredFurnaceRecipe getRecipe()
  {
    return getRecipe(getInventoryWrapper());
  }
  
  private PoweredFurnaceRecipe getRecipe(IInventory inventory)
  {
    return Utilities.getRecipe(world, ModRecipes.POWERED_FURNACE, inventory);
  }
  
  private FurnaceRecipe getFurnaceRecipe()
  {
    return getFurnaceRecipe(getInventoryWrapper());
  }
  
  private FurnaceRecipe getFurnaceRecipe(IInventory inventory)
  {
    return Utilities.getRecipe(world, IRecipeType.SMELTING, inventory);
  }
  
  private boolean canOutput()
  {
    return canOutput(OUTPUT_SLOT, inventory, poweredFurnaceRecipe);
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
    nbt.putInt("power_usage", powerCost);
  }
  
  @Override
  protected void handleExtraGuiUpdateTag(CompoundNBT nbt)
  {
    duration = nbt.getInt("duration");
    progress = nbt.getInt("progress");
    powerCost = nbt.getInt("power_usage");
  }
  
  public int getDuration()
  {
    return duration;
  }
  
  public int getProgress()
  {
    return progress;
  }
  
  public int getPowerCost()
  {
    return powerCost;
  }
  
  public int getIngredientCost()
  {
    return ingredientCost;
  }
  
  public int getTick()
  {
    return tick;
  }
  
  private void doProgress()
  {
    progress++;
    if (progress >= duration)
    {
      progress = 0;
      
      ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
      input.shrink(ingredientCost);
      inventory.setStackInSlot(INPUT_SLOT, input);
      
      ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
      if (poweredFurnaceRecipe != null)
      {
        if (!output.isEmpty())
        {
          if (output.getItem() == poweredFurnaceRecipe.output.getItem())
          {
            output.grow(poweredFurnaceRecipe.output.getCount());
          }
        }
        else
        {
          output = poweredFurnaceRecipe.getRecipeOutput().copy();
        }
      }
      else
      {
        if (!output.isEmpty())
        {
          if (output.getItem() == furnaceRecipe.getRecipeOutput().getItem())
          {
            output.grow(furnaceRecipe.getRecipeOutput().getCount());
          }
        }
        else
        {
          output = furnaceRecipe.getRecipeOutput().copy();
        }
      }
      inventory.setStackInSlot(OUTPUT_SLOT, output);
    }
  }
  
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      boolean dirty = false;
      tick++;
      if (!inventory.getStackInSlot(INPUT_SLOT).isEmpty())
      {
        PoweredFurnaceRecipe oldPoweredRecipe = poweredFurnaceRecipe;
        
        if (poweredFurnaceRecipe == null)
        {
          poweredFurnaceRecipe = getRecipe();
        }
        else
        {
          if (!poweredFurnaceRecipe.ingredient.test(inventory.getStackInSlot(0)))
          {
            poweredFurnaceRecipe = getRecipe();
            dirty = true;
          }
        }
        
        if (poweredFurnaceRecipe != null)
        {
          duration = poweredFurnaceRecipe.duration;
          if (duration < 0)
          {
            duration = 1;
          }
          powerCost = poweredFurnaceRecipe.powerCost;
          ingredientCost = poweredFurnaceRecipe.ingredientCost;
          
          if (poweredFurnaceRecipe != oldPoweredRecipe)
          {
            progress = 0;
            dirty = true;
          }
          
          if (canOutput())
          {
            if (inventory.getStackInSlot(INPUT_SLOT).getCount() >= ingredientCost)
            {
              if (storage.getEnergyStored() >= powerCost / duration)
              {
                doProgress();
                storage.extractInternal(powerCost / duration, false);
              }
            }
          }
        }
        else
        {
          getFakeInventory().setInventorySlotContents(0, inventory.getStackInSlot(INPUT_SLOT));
          furnaceRecipe = Utilities.getRecipe(world, IRecipeType.SMELTING, getFakeInventory());
          net.minecraft.item.crafting.FurnaceRecipe oldFurnaceRecipe = furnaceRecipe;
          
          if (furnaceRecipe == null)
          {
            furnaceRecipe = getFurnaceRecipe();
          }
          else
          {
            if (!furnaceRecipe.matches(getInventoryWrapper(), world))
            {
              furnaceRecipe = getFurnaceRecipe();
              dirty = true;
            }
          }
          
          if (furnaceRecipe != null)
          {
            duration = furnaceRecipe.getCookTime();
            if (duration < 1)
            {
              duration = 1;
            }
            
            ingredientCost = 1;
            
            powerCost = 40;
            
            if (furnaceRecipe != oldFurnaceRecipe)
            {
              progress = 0;
              dirty = true;
            }
            if (canOutputFurnace())
            {
              if (inventory.getStackInSlot(INPUT_SLOT).getCount() >= ingredientCost)
              {
                if (storage.getEnergyStored() >= powerCost)
                {
                  doProgress();
                  storage.extractInternal(powerCost, false);
                }
              }
            }
          }
        }
        
      }
      else
      {
        if (tick % 4 == 0)
        {
          progress = 0;
          dirty = true;
        }
      }
      
      boolean on = (poweredFurnaceRecipe != null || furnaceRecipe != null) && !getInventory().getStackInSlot(INPUT_SLOT).isEmpty();
      if (on != isOn())
      {
        setOn(on);
      }
      
      if (dirty)
      {
        markDirty();
      }
    }
  }
  
  private boolean canOutputFurnace()
  {
    return canOutput(OUTPUT_SLOT, inventory, furnaceRecipe);
  }
}
