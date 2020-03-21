package com.wtbw.mods.machines.tile.generator;

import com.wtbw.mods.lib.tile.util.GuiUpdateHelper;
import com.wtbw.mods.lib.tile.util.IContentHolder;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.gui.container.FuelGeneratorContainer;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.Generator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class FuelGeneratorEntity extends Generator implements IWTBWNamedContainerProvider, IContentHolder, IGuiUpdateHandler
{
  private ItemStackHandler inventory;
  private LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  private int genTime = 400;
  private int genCounter = 0;
  
  public FuelGeneratorEntity()
  {
    super(ModTiles.FUEL_GENERATOR, 150000, 1000, 45);
    
    manager
      .registerInt("genTime", () -> genTime, (i) -> genTime = i)
      .registerInt("genCounter", () -> genCounter, (i) -> genCounter = i)
      .register("inventory", getInventory());
  }
  
  @Override
  protected void onGenerate()
  { }
  
  @Override
  public void tick()
  {
    boolean dirty = false;
    
    if (!world.isRemote)
    {
      if (genCounter > 0)
      {
        canGenerate = true;
        genCounter--;
        dirty = true;
      }
      else
      {
        dirty = true;
        canGenerate = false;
        ItemStack fuel = getInventory().getStackInSlot(0);
        if (!fuel.isEmpty())
        {
          int time = Utilities.getBurnTime(fuel);
          genTime = time;
          
          if (getEnergyMissing() > generate)
          {
            fuel.shrink(1);
            genCounter = genTime;
          }
        }
      }
    }
    
    if (dirty)
    {
      if (canGenerate != isOn())
      {
        setOn(canGenerate);
      }
      
      markDirty();
    }
    
    super.tick();
  }
  
  @Nonnull
  public ItemStackHandler getInventory()
  {
    if (inventory == null)
    {
      inventory = new ItemStackHandler()
      {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
          if (isItemValid(slot, stack))
          {
            return super.insertItem(slot, stack, simulate);
          }
          
          return stack;
        }
  
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
          return ItemStack.EMPTY;
        }
  
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          return slot == 0 && stack.getItem() != Items.LAVA_BUCKET && Utilities.getBurnTime(stack) > 0;
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
      return inventoryCap.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    super.read(compound);
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
    return new FuelGeneratorContainer(id, world, pos, inventory);
  }

  public int getGenTime()
  {
    return genTime;
  }
  
  public int getGenCounter()
  {
    return genCounter;
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putIntArray("storage", GuiUpdateHelper.getEnergyUpdateValues(storage, true));
    nbt.putInt("genTime", genTime);
    nbt.putInt("gentCounter", genCounter);
    
    return nbt;
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    GuiUpdateHelper.updateEnergy(storage, nbt.getIntArray("storage"));
    genTime = nbt.getInt("genTime");
    genCounter = nbt.getInt("genCounter");
  }
}
