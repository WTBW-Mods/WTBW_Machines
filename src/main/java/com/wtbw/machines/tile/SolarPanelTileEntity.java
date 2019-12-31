package com.wtbw.machines.tile;

import com.wtbw.machines.tile.base.Generator;
import net.minecraft.util.Direction;

/*
  @author: Naxanria
*/
public class SolarPanelTileEntity extends Generator
{
  public static final Direction[] PROVIDING_SIDES = new Direction[]{ Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST, Direction.DOWN };
  
  public SolarPanelTileEntity(int capacity, int maxExtract, int generate)
  {
    super(ModTiles.SOLAR_PANEL, capacity, maxExtract, generate);
  }
  
  @Override
  protected boolean canGenerate()
  {
    return world.canBlockSeeSky(pos) && world.isDaytime();
  }
  
  @Override
  protected Direction[] providingSides()
  {
    return PROVIDING_SIDES;
  }
  
  @Override
  protected void onGenerate()
  {
//    WTBWMachines.LOGGER.info("Generated! {} {}/{} {}%", getGenerate(), getEnergy(), getCapacity(), (int) (getPercentageFilled() * 100));
  }
}
