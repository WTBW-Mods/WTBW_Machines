package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.machine.ChemicalPlantEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;


/*
  @author: Naxanria
*/
public class ChemicalPlantContainer extends BaseTileContainer<ChemicalPlantEntity>
{
  public ChemicalPlantContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.CHEMICAL_PLANT, id, world, pos, playerInventory);
  
    ItemStackHandler handler = tileEntity.getInventory();
    
    addInputSlot(handler, ChemicalPlantEntity.INPUT_SLOT, 20 * 2 + 3 + 3, 18);
    addSlot(handler, ChemicalPlantEntity.OUTPUT_SLOT, 175 - 5 - 20 - 3, 18);
    layoutPlayerInventorySlots();
  }
}
