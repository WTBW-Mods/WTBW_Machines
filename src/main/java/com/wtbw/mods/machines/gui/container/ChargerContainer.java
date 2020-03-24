package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseUpgradeContainer;
import com.wtbw.mods.machines.tile.machine.ChargerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

/*
  @author: Naxanria
*/
public class ChargerContainer extends BaseUpgradeContainer<ChargerEntity>
{
  public final SlotItemHandler chargeSlot;
  
  public ChargerContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.CHARGER, id, world, pos, playerInventory);
    
    chargeSlot = addSlot(tileEntity.getInventory(), 0, 45, 20);
    
    layoutPlayerInventorySlots();
  }
}
