package com.wtbw.mods.machines.tile.machine;

import net.minecraft.tileentity.TileEntityType;

/*
  @author: Naxanria
*/
public class MachineTier
{
  public static final Type BASIC = new Type();
  public static final Type ADVANCED = new Type();
  public static final Type EXPERT = new Type();
  
  public final TileEntityType<?> tileEntityType;
  public final Type type;
  
  public MachineTier(TileEntityType<?> tileEntityType, Type type)
  {
    this.tileEntityType = tileEntityType;
    this.type = type;
  }
  
  public static class Type
  { }
  
  
}
