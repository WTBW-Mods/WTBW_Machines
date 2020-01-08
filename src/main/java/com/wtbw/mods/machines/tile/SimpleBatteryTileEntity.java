package com.wtbw.mods.machines.tile;

import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.machines.gui.container.BatteryContainer;
import com.wtbw.mods.machines.tile.base.Battery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class SimpleBatteryTileEntity extends Battery implements IWTBWNamedContainerProvider
{
  public SimpleBatteryTileEntity()
  {
    super(ModTiles.SIMPLE_BATTERY, 32000, 1000, 1000);
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new BatteryContainer(id, world, pos, inventory);
  }
}
