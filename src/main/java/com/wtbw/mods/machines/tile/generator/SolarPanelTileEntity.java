package com.wtbw.mods.machines.tile.generator;

import com.wtbw.mods.lib.tile.util.GuiUpdateHelper;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.gui.container.SolarPanelContainer;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.Generator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class SolarPanelTileEntity extends Generator implements IWTBWNamedContainerProvider, IGuiUpdateHandler
{
  public static final Direction[] PROVIDING_SIDES = new Direction[]{ Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST, Direction.DOWN };
  
  public static final Tier MK1 = new Tier(150000, 80, 10, () -> ModTiles.SOLAR_PANEL_MK1);
  public static final Tier MK2 = new Tier(250000, 240, 40, () -> ModTiles.SOLAR_PANEL_MK2);
  public static final Tier MK3 = new Tier(500000, 850, 350, () -> ModTiles.SOLAR_PANEL_MK3);
  public static final Tier MK4 = new Tier(1000000, 1500, 2500, () -> ModTiles.SOLAR_PANEL_MK4);
  public static final Tier MK5 = new Tier(2000000, 15000, 8000, () -> ModTiles.SOLAR_PANEL_MK5);
  
  public static class Tier
  {
    public int capacity;
    public int maxExtract;
    public int generate;
    public final Supplier<TileEntityType<SolarPanelTileEntity>> typeSupplier;
  
    public Tier(int capacity, int maxExtract, int generate, Supplier<TileEntityType<SolarPanelTileEntity>> typeSupplier)
    {
      this.capacity = capacity;
      this.maxExtract = maxExtract;
      this.generate = generate;
      this.typeSupplier = typeSupplier;
    }
  }
  
  public final Tier tier;
  
  public SolarPanelTileEntity(Tier tier)
  {
    super(tier.typeSupplier.get(), tier.capacity, tier.maxExtract, tier.generate);
    this.tier = tier;
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      generate = tier.generate;
      storage.setCapacity(tier.capacity);
      storage.setExtract(tier.maxExtract);
      
      boolean prev = canGenerate;
      canGenerate = (world.canBlockSeeSky(pos) && world.isDaytime());
      if (prev != canGenerate)
      {
        markDirty();
      }
    }
    
    super.tick();
  }
  
  @Override
  protected Direction[] providingSides()
  {
    return PROVIDING_SIDES;
  }
  
  @Override
  protected void onGenerate()
  {  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new SolarPanelContainer(id, world, pos, inventory);
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putIntArray("energy", GuiUpdateHelper.getEnergyUpdateValues(storage, true));
    return nbt;
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    GuiUpdateHelper.updateEnergy(storage, nbt.getIntArray("energy"));
  }
}
