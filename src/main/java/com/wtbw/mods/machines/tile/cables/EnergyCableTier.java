package com.wtbw.mods.machines.tile.cables;

import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class EnergyCableTier
{
  public static final EnergyCableTier MK1 = new EnergyCableTier( 512, () -> ModTiles.ENERGY_CABLE_MK1);
  public static final EnergyCableTier MK2 = new EnergyCableTier(1024, () -> ModTiles.ENERGY_CABLE_MK2);
  public static final EnergyCableTier MK3 = new EnergyCableTier( 4096, () -> ModTiles.ENERGY_CABLE_MK3);
  public static final EnergyCableTier MK4 = new EnergyCableTier(16384, () -> ModTiles.ENERGY_CABLE_MK4);
  public static final EnergyCableTier MK5 = new EnergyCableTier(65536, () -> ModTiles.ENERGY_CABLE_MK5);
  public static final EnergyCableTier MK6 = new EnergyCableTier( 512000, () -> ModTiles.ENERGY_CABLE_MK6);
  
  public final Supplier<TileEntityType<?>> type;
  
  private static int nextID = 0;
  public final int id = nextID++;
  
  public int capacity;
  public int transfer;
  
  public EnergyCableTier(int transfer, @Nonnull Supplier<TileEntityType<?>> type)
  {
    this(4 * transfer, transfer, type);
  }
  
  public EnergyCableTier(int capacity, int transfer, @Nonnull Supplier<TileEntityType<?>> type)
  {
    this.type = type;
    this.capacity = capacity;
    this.transfer = transfer;
  }
}
