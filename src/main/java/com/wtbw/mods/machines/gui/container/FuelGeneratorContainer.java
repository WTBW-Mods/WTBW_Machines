package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.generator.FuelGeneratorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class FuelGeneratorContainer extends BaseTileContainer<FuelGeneratorEntity>
{
  public FuelGeneratorContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.FUEL_GENERATOR, id, world, pos, playerInventory);
    track(tileEntity.getManager());
    addInputSlot(tileEntity.getInventory(), 0, 81, 54);
    layoutPlayerInventorySlots();
  }
}
