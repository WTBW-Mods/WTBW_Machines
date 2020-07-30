package com.wtbw.mods.machines.tile.multi;

import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.gui.container.EnergyInputHatchContainer;
import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class EnergyInputHatchTile extends TileEntity implements IWTBWNamedContainerProvider, IGuiUpdateHandler
{
  protected BaseEnergyStorage energyStorage;
  
  protected LazyOptional<BaseEnergyStorage> energyStorageOpt = LazyOptional.of(this::getEnergyStorage);
  
  protected final NBTManager manager = new NBTManager();
  
  public EnergyInputHatchTile()
  {
    super(ModTiles.ENERGY_INPUT_HATCH);
    
    manager.register("energy", getEnergyStorage());
  }
  
  @Nonnull
  public BaseEnergyStorage getEnergyStorage()
  {
    if (energyStorage == null)
    {
      energyStorage = new BaseEnergyStorage(1000000, 10000, 0);
    }
    
    return energyStorage;
  }
  
  @Override
  public void read(BlockState state, CompoundNBT compound)
  {
    manager.read(compound);
    super.read(state, compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    manager.write(compound);
    return super.write(compound);
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityEnergy.ENERGY)
    {
      return energyStorageOpt.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new  EnergyInputHatchContainer(id, world, pos, inventory);
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    return  manager.write(new CompoundNBT());
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    manager.read(nbt);
  }
}
