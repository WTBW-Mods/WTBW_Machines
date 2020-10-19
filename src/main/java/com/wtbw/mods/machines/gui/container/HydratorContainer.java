package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.machine.HydratorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Naxanria
*/
public class HydratorContainer extends BaseTileContainer<HydratorEntity>
{
  public HydratorContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.HYDRATOR, id, world, pos, playerInventory);
  
    ItemStackHandler handler = tileEntity.getInventory();
    
    addInputSlot(handler, HydratorEntity.INPUT_SLOT, 175 / 2 - 6, 18);
    addSlot(handler, HydratorEntity.OUTPUT_SLOT, 175 / 2 - 6, 53);
    layoutPlayerInventorySlots();
  }
}
