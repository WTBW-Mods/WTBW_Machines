package com.wtbw.mods.machines.tile;

import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.machines.gui.container.SolarPanelContainer;
import com.wtbw.mods.machines.tile.base.Generator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class SolarPanelTileEntity extends Generator implements IWTBWNamedContainerProvider
{
  public static final Direction[] PROVIDING_SIDES = new Direction[]{ Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST, Direction.DOWN };
  
  public SolarPanelTileEntity(int capacity, int maxExtract, int generate)
  {
    super(ModTiles.SOLAR_PANEL, capacity, maxExtract, generate);
  }
  
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
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
}
