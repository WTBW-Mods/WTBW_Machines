package com.wtbw.mods.machines.tile.base;

import com.wtbw.mods.lib.tile.util.IComparatorProvider;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.Manager;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.block.base.BaseMachineBlock;
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
  
  protected NBTManager manager;
  
  protected boolean canGenerate;
  
  public Generator(TileEntityType<?> tileEntityTypeIn, int capacity, int maxExtract, int generate)
  {
    super(tileEntityTypeIn);
    storage = new BaseEnergyStorage(capacity, 0, maxExtract);
    this.generate = generate;
    
    manager = new NBTManager();
    manager.register("storage", (Manager.Serializable) new Manager.Serializable(storage).noGuiTracking());
    manager.register("generate", new Manager.Int()
    {
      @Override
      public Integer get()
      {
        return Generator.this.generate;
      }
  
      @Override
      public void set(Integer value)
      {
        Generator.this.generate = value;
      }
    });
    
    manager.register("canGenerate", new Manager.Bool()
    {
      @Override
      public Boolean get()
      {
        return Generator.this.canGenerate;
      }
  
      @Override
      public void set(Boolean value)
      {
        Generator.this.canGenerate = value;
      }
    });
  }
  
  @Override
  public void tick()
  {
    boolean dirty = false;
    if (!world.isRemote)
    {
      if (generate > 0 && canGenerate)
      {
        storage.insertInternal(generate, false);
        onGenerate();
        dirty = true;
      }
  
      int energy = storage.getEnergyStored();
      sendPowerAround();
      if (energy != storage.getEnergyStored())
      {
        dirty = true;
      }
  
      if (dirty)
      {
        markDirty();
      }
    }
  }
  
  public final boolean canGenerate()
  {
    return canGenerate;
  }
  
  protected abstract void onGenerate();
  
  public final int getGenerate()
  {
    return generate;
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
  
  public int getEnergyMissing()
  {
    return storage.getMaxEnergyStored() - storage.getEnergyStored();
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
    toShare -= Utilities.sendPowerAround(world, pos, toShare, shareEqually, providingSides());
    storage.extractInternal(toShare, false);
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
  
  protected boolean isOn()
  {
    return getBlockState().get(BaseMachineBlock.ON);
  }
  
  protected void setOn(boolean on)
  {
    world.setBlockState(pos, getBlockState().with(BaseMachineBlock.ON, on), 3);
  }
  
  public NBTManager getManager()
  {
    return manager;
  }
}
