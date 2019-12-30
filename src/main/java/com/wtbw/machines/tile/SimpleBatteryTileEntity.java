package com.wtbw.machines.tile;

import com.wtbw.machines.WTBWMachines;
import net.minecraft.tileentity.TileEntityType;

/*
  @author: Naxanria
*/
public class SimpleBatteryTileEntity extends Battery
{
  public SimpleBatteryTileEntity()
  {
    super(ModTiles.SIMPLE_BATTERY, 100000, 1000, 1000);
  }
}
