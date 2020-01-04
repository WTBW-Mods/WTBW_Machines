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
import java.util.Optional;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class DryerTileEntity extends TileEntity implements ITickableTileEntity, IWTBWNamedContainerProvider, IRedstoneControlled, IContentHolder
{
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  
  private RedstoneControl control;
  private NBTManager manager;
  private BaseEnergyStorage storage;
  
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
    super(ModTiles.DRYER);
    
    control = new RedstoneControl(this, RedstoneMode.IGNORE);
    manager = new NBTManager()
      .registerInt("duration",() -> duration, i -> duration = i)
      .registerInt("progress", () -> progress, i -> progress = i)
      .registerInt("powerUsage", () -> powerUsage, i -> powerUsage = i)
      .registerInt("heat", () -> heat, i -> heat = i)
      .registerInt("targetHeat", () -> targetHeat, i -> targetHeat = i)
      .registerInt("subHeat", () -> subHeat, i -> subHeat = i)
      .register("control", control)
      .register("inventory", getInventory())
      .register("storage", getStorage());
    
    
//
//
//    manager.register("duration", new Manager.Int()
//    {
//      @Override
//      public Integer get()
//      {
//        return duration;
//      }
//
//      @Override
//      public void set(Integer value)
//      {
//        duration = value;
//      }
//    });
//
//    manager.register("progress", new Manager.Int()
//    {
//      @Override
//      public Integer get()
//      {
//        return progress;
//      }
//
//      @Override
//      public void set(Integer value)
//      {
//        progress = value;
//      }
//    });
//
//    manager.register("powerUsage", new Manager.Int()
//    {
//      @Override
//      public Integer get()
//      {
//        return powerUsage;
//      }
//
//      @Override
//      public void set(Integer value)
//      {
//        powerUsage = value;
//      }
//    });
//
//    manager.register("heat", new Manager.Int()
//    {
//      @Override
//      public Integer get()
//      {
//        return heat;
//      }
//
//      @Override
//      public void set(Integer value)
//      {
//        heat = value;
//      }
//    });
//
//    manager.register("targetHeat", new Manager.Int()
//    {
//      @Override
//      public Integer get()
//      {
//        return targetHeat;
//      }
//
//      @Override
//      public void set(Integer value)
//      {
//        targetHeat = value;
//      }
//    });
//
//    manager.register("subHeat", new Manager.Int()
//    {
//      @Override
//      public Integer get()
//      {
//        return subHeat;
//      }
//
//      @Override
//      public void set(Integer value)
//      {
//        subHeat = value;
//      }
//    });
//
//    manager.register("control", new Manager.Redstone(control));
//    manager.register("inventory", new Manager.Serializable(getInventory()));
//    manager.register("energy", new Manager.Serializable(getStorage()));
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
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Override
  public RedstoneMode[] availableModes()
  {
    return RedstoneMode.noPulse;
  }
  
//  @Override
//  public ITextComponent getDisplayName()
//  {
//    return new TranslationTextComponent("block." + getType().getRegistryName().toString().replace(":", "."));
//  }
  
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
      boolean dirty = false;
      boolean on = false;
  
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
            dirty = true;
          }
        }
  
        if (recipe != null)
        {
          duration = recipe.duration;
          targetHeat = recipe.heat;
          
          if (recipe != old)
          {
            progress = 0;

            dirty = true;
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
                on = true;
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
      
//      if (on != isOn())
//      {
//        setOn(on);
//      }
      
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
    ItemStack current = inventory.getStackInSlot(OUTPUT_SLOT);
    if (current.isEmpty())
    {
      return true;
    }
    
    int maxSize = current.getMaxStackSize();
    int size = current.getCount() + recipe.output.getCount();
    
    return size <= maxSize && StackUtil.doItemsStack(current, recipe.output);
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
  
  private boolean isOn()
  {
    return getBlockState().get(BaseMachineBlock.ON);
  }
  
  private void setOn(boolean on)
  {
    world.setBlockState(pos, getBlockState().with(BaseMachineBlock.ON, on), 3);
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
