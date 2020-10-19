package com.wtbw.mods.machines.tile.machine;

import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.gui.container.ChargerContainer;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import java.util.concurrent.atomic.AtomicBoolean;

/*
  @author: Naxanria
*/
public class ChargerEntity extends BaseMachineEntity
{
  public static final int DEFAULT_CAPACITY = 2000000;
  public static final int DEFAULT_TRANSFER = 100000;
  
  protected int transfer = DEFAULT_TRANSFER;
  protected ItemStackHandler inventory;
  
  private LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(this::getInventory);
  private LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  
  public ChargerEntity()
  {
    super(ModTiles.CHARGER, DEFAULT_CAPACITY, DEFAULT_TRANSFER / 2, RedstoneMode.IGNORE);
    
    manager.register("inventory", getInventory());
  }
  
  @Nonnull
  public ItemStackHandler getInventory()
  {
    if (inventory == null)
    {
      inventory = new ItemStackHandler(1)
      {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }
      };
    }
    
    return inventory;
  }

  @Override
  protected List<ItemStackHandler> getInventories()
  {
    return Utilities.listOf(inventory);
  }
  
  @Override
  protected void getExtraGuiUpdateTag(CompoundNBT nbt)
  { }
  
  @Override
  protected void handleExtraGuiUpdateTag(CompoundNBT nbt)
  { }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new ChargerContainer(id, world, pos, inventory);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      BaseEnergyStorage storage = getStorage();
      if (storage.getEnergyStored() > 0)
      {
        ItemStack stack = getInventory().getStackInSlot(0);
        if (!stack.isEmpty())
        {
          AtomicBoolean charging = new AtomicBoolean(false);
          stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(
            energyStorage ->
            {
              if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored())
              {
                charging.set(true);
                int insert = Math.min(transfer, storage.getEnergyStored());
                insert = energyStorage.receiveEnergy(insert, false);
                storage.extractInternal(insert, false);
              }
            });
          if (charging.get() != isOn())
          {
            setOn(charging.get());
          }
          markDirty();
        }
        else
        {
          if (isOn())
          {
            setOn(false);
            markDirty();
          }
        }
      }
    }
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
}
