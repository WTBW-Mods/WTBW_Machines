package com.wtbw.machines.tile.base;

import com.wtbw.lib.tile.util.IComparatorProvider;
import com.wtbw.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.lib.util.NBTHelper;
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
public abstract class Generator extends TileEntity implements ITickableTileEntity, IComparatorProvider
{
  protected BaseEnergyStorage storage;
  protected int generate;
  protected LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  
  protected boolean shareEqually = true;
  
  public Generator(TileEntityType<?> tileEntityTypeIn, int capacity, int maxExtract, int generate)
  {
    super(tileEntityTypeIn);
    storage = new BaseEnergyStorage(capacity, 0, maxExtract);
    this.generate = generate;
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      if (canGenerate())
      {
        storage.insertInternal(getGenerate(), false);
        onGenerate();
        sendPowerAround();
        markDirty();
      }
    }
  }
  
  protected abstract boolean canGenerate();
  protected abstract void onGenerate();
  
  protected int getGenerate()
  {
    return generate;
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    storage.deserializeNBT(compound.getCompound("storage"));
    generate = NBTHelper.getInt(compound, "generate");
    
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    compound.put("storage", storage.serializeNBT());
    compound.putInt("generate", generate);
    
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
    return storage.getPercentageFilled();
  }
  
  public BaseEnergyStorage getStorage()
  {
    return storage;
  }
  
  @Override
  public int getComparatorStrength()
  {
    return storage.getComparatorStrength();
  }
  
  protected Direction[] providingSides()
  {
    return Direction.values();
  }
  
  protected void sendPowerAround()
  {
    int toShare = Math.min(getEnergy(), storage.getMaxExtract());
    Utilities.sendPowerAround(world, pos, toShare, shareEqually, providingSides());
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
