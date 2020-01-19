package com.wtbw.mods.machines;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/*
  @author: Naxanria
*/
public class Constants
{
  public static final Direction[] rotationOrderHorizontal = new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
  public static final Direction[] rotationOrder = new Direction[]{ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN };
  
  public static ResourceLocation getLocation(String key)
  {
    return new ResourceLocation(WTBWMachines.MODID, key);
  }
}
