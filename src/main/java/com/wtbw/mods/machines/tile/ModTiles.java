package com.wtbw.mods.machines.tile;

import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.tile.furnace.BaseFurnaceTileEntity;
import com.wtbw.mods.machines.tile.machine.DryerTileEntity;
import com.wtbw.mods.machines.tile.machine.QuarryTileEntity;
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
  
  public static final TileEntityType<SolarPanelTileEntity> SOLAR_PANEL = null;
  public static final TileEntityType<SimpleBatteryTileEntity> SIMPLE_BATTERY = null;

  public static final TileEntityType<QuarryTileEntity> QUARRY = null;
  public static final TileEntityType<DryerTileEntity> DRYER = null;

  public static final TileEntityType<PoweredFurnaceEntity> POWERED_FURNACE = null;
  public static final TileEntityType<PoweredCrusherEntity> POWERED_CRUSHER = null;

}
