package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.multi.EnergyInputHatchTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class EnergyInputHatchContainer extends BaseTileContainer<EnergyInputHatchTile>
{
  public EnergyInputHatchContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.ENERGY_INPUT_HATCH, id, world, pos, playerInventory);
    
    layoutPlayerInventorySlots();
  }
}
