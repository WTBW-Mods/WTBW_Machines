package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.base.Battery;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class BatteryContainer extends BaseTileContainer<Battery>
{
  public BatteryContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.BATTERY, id, world, pos, playerInventory);
    
    track(tileEntity.getManager());
    
    layoutPlayerInventorySlots();
  }
}
