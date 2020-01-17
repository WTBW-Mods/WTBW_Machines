package com.wtbw.mods.machines.tile.cables;

import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.nbt.Manager;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  @author: Naxanria
*/
public class EnergyCableEntity extends TileEntity implements ITickableTileEntity
{
  private BaseEnergyStorage storage;
  private LazyOptional<BaseEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  
  private NBTManager manager;
  
  public final EnergyCableTier tier;
  
  public EnergyCableEntity(EnergyCableTier tier)
  {
    super(tier.type.get());
    this.tier = tier;
  
    storage = getStorage();
    
    manager = new NBTManager()
      .register("storage", storage);
  }
  
  @Nonnull
  private BaseEnergyStorage getStorage()
  {
    if (storage == null)
    {
      storage = new BaseEnergyStorage(tier.capacity, tier.transfer);
    }
    
    return storage;
  }
  
  @Override
  public void tick()
  {
    if (world.isRemote)
    {
      return;
    }
  
    List<IEnergyStorage> acceptors = new ArrayList<>();
  
    for (Direction direction : Direction.values())
    {
      TileEntity tileEntity = world.getTileEntity(pos.offset(direction));
  
      if (tileEntity != null)
      {
        LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
  
        capability.ifPresent(storage1 ->
        {
          if (tileEntity instanceof EnergyCableEntity)
          {
            // don't push into cables having more energy than us
            if (storage.getEnergyStored() < storage1.getEnergyStored())
            {
              return;
            }
          }
  
          if (storage1.receiveEnergy(storage.getMaxExtract(), true) > 0)
          {
            acceptors.add(storage1);
          }
        });
      }
    }
    if (acceptors.isEmpty())
    {
      return;
    }
  
    Collections.shuffle(acceptors);
  
    acceptors.forEach(energyStorage ->
    {
      int insert = Math.min(storage.getMaxExtract(), storage.getEnergyStored());
      if (insert == 0)
      {
        return;
      }
    
      insert = energyStorage.receiveEnergy(insert, false);
    
      storage.extractInternal(insert, false);
    });
  
  
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityEnergy.ENERGY)
    {
      return storageCap.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    manager.read(compound);
    
    if (world != null && !world.isRemote)
    {
      storage.setCapacity(tier.capacity);
      storage.setExtract(tier.transfer);
      storage.setReceive(tier.transfer);
    }
    
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    manager.write(compound);
    
    return super.write(compound);
  }
}
