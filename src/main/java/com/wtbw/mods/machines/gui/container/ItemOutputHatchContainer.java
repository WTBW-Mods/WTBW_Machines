package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.multi.ItemOutputHatchTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Naxanria
*/
public class ItemOutputHatchContainer extends BaseTileContainer<ItemOutputHatchTile>
{
  public ItemOutputHatchContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.ITEM_OUTPUT_HATCH, id, world, pos, playerInventory);
  
    ItemStackHandler inventory = tileEntity.getInventory();
  
    addSlotBox(inventory, 0, 84 - 21, 17, 3, 3, 18, 18);
    
    layoutPlayerInventorySlots();
  }
}
