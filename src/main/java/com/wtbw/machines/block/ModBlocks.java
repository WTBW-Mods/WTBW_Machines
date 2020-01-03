package com.wtbw.machines.block;

import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.block.redstone.BlockDetectorBlock;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.block.SixWayTileBlock;
import com.wtbw.machines.tile.BlockBreakerTileEntity;
import com.wtbw.machines.tile.BlockPlacerTileEntity;
import com.wtbw.machines.tile.SolarPanelTileEntity;
import com.wtbw.machines.tile.VacuumChestTileEntity;
import com.wtbw.machines.tile.redstone.RedstoneTimerTileEntity;
import net.minecraftforge.registries.ObjectHolder;

/*
  @author: Naxanria
*/
@ObjectHolder(WTBWMachines.MODID)
public class ModBlocks
{
  public static final TieredFurnaceBlock IRON_FURNACE = null;
  public static final TieredFurnaceBlock GOLD_FURNACE = null;
  public static final TieredFurnaceBlock DIAMOND_FURNACE = null;
  public static final TieredFurnaceBlock END_FURNACE = null;

  public static final BaseTileBlock<RedstoneTimerTileEntity> REDSTONE_TIMER = null;
  
  public static final SixWayTileBlock<BlockBreakerTileEntity> BLOCK_BREAKER = null;
  public static final SixWayTileBlock<BlockPlacerTileEntity> BLOCK_PLACER = null;
  public static final BlockDetectorBlock BLOCK_DETECTOR = null;

  public static final BaseTileBlock<VacuumChestTileEntity> VACUUM_CHEST = null;
  
  public static final PushBlock PUSHER = null;
  public static final PushBlock PULLER = null;
  
  public static final BaseTileBlock<SolarPanelTileEntity> SOLAR_PANEL = null;

  public static final QuarryBlock QUARRY = null;
  public static final QuarryMarker QUARRY_MARKER = null;
}
