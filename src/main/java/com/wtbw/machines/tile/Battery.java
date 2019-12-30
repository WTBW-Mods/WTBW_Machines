package com.wtbw.machines.tile;

import com.wtbw.lib.tile.util.IComparatorProvider;
import com.wtbw.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.lib.util.Utilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class Battery extends TileEntity implements ITickableTileEntity, IComparatorProvider
{
  protected BaseEnergyStorage storage;
  protected LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  protected boolean shareEqually = true;
  
  public Battery(TileEntityType<?> tileEntityTypeIn, int capacity, int maxExtract, int maxInsert)
  {
    super(tileEntityTypeIn);
    storage = new BaseEnergyStorage(capacity, maxExtract, maxInsert);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      sendPowerAround();
      markDirty();
    }
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    storage.deserializeNBT(compound.getCompound("storage"));
    
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    compound.put("storage", storage.serializeNBT());
    
    return super.write(compound);
  }
  
  public int getEnergy()
  {
    return storage.getEnergyStored();
  }
  
  public int getCapacity()
  {
    return storage.getMaxEnergyStored();
  }
  
  public float getPercentageFilled()
  {
    return getEnergy() / (float) getCapacity();
  }
  
  public BaseEnergyStorage getStorage()
  {
    return storage;
  }
  
  @Override
  public int getComparatorStrength()
  {
    float p = getPercentageFilled();
    int c = (int) (p * 15);
    if (c == 0)
    {
      return (getEnergy() > 0) ? 1 : 0;
    }
    
    if (c == 15)
    {
      return (getEnergy() == getCapacity()) ? 15 : 14;
    }
    
    return c;
  }
  
  protected Direction[] providingSides()
  {
    return Direction.values();
  }
  
  protected void sendPowerAround()
  {
    int toShare = Math.min(getEnergy(), storage.getMaxExtract());
    if (Utilities.sendPowerAround(world, pos, toShare, shareEqually, providingSides()) > 0)
    {
      markDirty();
    }
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityEnergy.ENERGY && Utilities.contains(providingSides(), side))
    {
      return storageCap.cast();
    }
    
    return super.getCapability(cap, side);
  }
}
