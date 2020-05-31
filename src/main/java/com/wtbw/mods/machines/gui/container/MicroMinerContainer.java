package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.micro_miner.MicroMinerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class MicroMinerContainer extends BaseTileContainer<MicroMinerTile>
{
  public MicroMinerContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.MICRO_MINER, id, world, pos, playerInventory);
    
    addSlot(tileEntity.getInventory(), 0, -20, 20);
    
    layoutPlayerInventorySlots();
  }
}
