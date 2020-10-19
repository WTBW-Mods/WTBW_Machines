package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.machine.CrusherEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Sunekaer
*/
public class CrusherContainer extends BaseTileContainer<CrusherEntity>
{
  public CrusherContainer(int id, World world, BlockPos pos, PlayerInventory inventory)
  {
    super(ModContainers.CRUSHER, id, world, pos, inventory);
    
    NBTManager manager = tileEntity.getManager();
    
    track(manager);
    
    ItemStackHandler handler = tileEntity.getInventory();
    
    addInputSlot(handler, CrusherEntity.INPUT_SLOT, 175 / 2 - 6, 20);
    
    addSlot(handler, CrusherEntity.OUTPUT_SLOT, 175 / 2 - 24, 52);
    addSlot(handler, CrusherEntity.OUTPUT_SLOT2, 175 / 2 - 6, 52);
    addSlot(handler, CrusherEntity.OUTPUT_SLOT3, 175 / 2 + 12, 52);
    
    layoutPlayerInventorySlots();
  }
}
