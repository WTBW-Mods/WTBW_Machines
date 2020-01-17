package com.wtbw.mods.machines.tile;

import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.tile.cables.EnergyCableEntity;
import com.wtbw.mods.machines.tile.furnace.BaseFurnaceTileEntity;
import com.wtbw.mods.machines.tile.generator.FuelGeneratorEntity;
import com.wtbw.mods.machines.tile.generator.SolarPanelTileEntity;
import com.wtbw.mods.machines.tile.machine.*;
import com.wtbw.mods.machines.tile.redstone.RedstoneTimerTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

/*
  @author: Naxanria
*/
@ObjectHolder(WTBWMachines.MODID)
public class ModTiles
{
  public static final TileEntityType<BaseFurnaceTileEntity> IRON_FURNACE = null;
  public static final TileEntityType<BaseFurnaceTileEntity> GOLD_FURNACE = null;
  public static final TileEntityType<BaseFurnaceTileEntity> DIAMOND_FURNACE = null;
  public static final TileEntityType<BaseFurnaceTileEntity> END_FURNACE = null;

  public static final TileEntityType<RedstoneTimerTileEntity> REDSTONE_TIMER = null;
  
  public static final TileEntityType<EntityPusherTileEntity> PUSHER = null;
  public static final TileEntityType<EntityPusherTileEntity> PULLER = null;
  
  public static final TileEntityType<VacuumChestTileEntity> VACUUM_CHEST = null;
  public static final TileEntityType<BlockBreakerTileEntity> BLOCK_BREAKER = null;
  public static final TileEntityType<BlockPlacerTileEntity> BLOCK_PLACER = null;
  public static final TileEntityType<BlockDetectorTileEntity> BLOCK_DETECTOR = null;
  
  public static final TileEntityType<SolarPanelTileEntity> SOLAR_PANEL_MK1 = null;
  public static final TileEntityType<SolarPanelTileEntity> SOLAR_PANEL_MK2 = null;
  public static final TileEntityType<SolarPanelTileEntity> SOLAR_PANEL_MK3 = null;
  
  public static final TileEntityType<FuelGeneratorEntity> FUEL_GENERATOR = null;
  
  public static final TileEntityType<SimpleBatteryTileEntity> SIMPLE_BATTERY = null;

  public static final TileEntityType<QuarryTileEntity> QUARRY = null;
  public static final TileEntityType<DehydratorTileEntity> DEHYDRATOR = null;

  public static final TileEntityType<PoweredFurnaceEntity> POWERED_FURNACE = null;
  public static final TileEntityType<PoweredCrusherEntity> CRUSHER = null;
  public static final TileEntityType<PoweredCompressorEntity> COMPRESSOR = null;
  
  
  public static final TileEntityType<EnergyCableEntity> ENERGY_CABLE_MK1 = null;
  public static final TileEntityType<EnergyCableEntity> ENERGY_CABLE_MK2 = null;
  public static final TileEntityType<EnergyCableEntity> ENERGY_CABLE_MK3 = null;
  public static final TileEntityType<EnergyCableEntity> ENERGY_CABLE_MK4 = null;
  public static final TileEntityType<EnergyCableEntity> ENERGY_CABLE_MK5 = null;
  public static final TileEntityType<EnergyCableEntity> ENERGY_CABLE_MK6 = null;
}
