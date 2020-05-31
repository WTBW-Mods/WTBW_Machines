package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.multi.FluidInputHatchTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/*
  @author: Naxanria
*/
public class FluidInputHatchContainer extends BaseTileContainer<FluidInputHatchTile>
{
  public final SlotItemHandler BUCKET_INPUT;
  
  public FluidInputHatchContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.FLUID_INPUT_HATCH, id, world, pos, playerInventory);
    ItemStackHandler handler = tileEntity.getInventory();
    
    BUCKET_INPUT = addSlot(handler, FluidInputHatchTile.BUCKET_INPUT, 40, 17);
    addSlot(handler, FluidInputHatchTile.BUCKET_OUTPUT, 40, 53);
    
    layoutPlayerInventorySlots();
  }
}
