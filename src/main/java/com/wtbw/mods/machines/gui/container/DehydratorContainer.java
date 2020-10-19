package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.machine.DehydratorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Naxanria
*/
public class DehydratorContainer extends BaseTileContainer<DehydratorEntity>
{
  public DehydratorContainer(int id, World world, BlockPos pos, PlayerInventory inventory)
  {
    super(ModContainers.DEHYDRATOR, id, world, pos, inventory);
  
//    NBTManager manager = tileEntity.getManager();
//    manager.referenceHolders().forEach(iIntReferenceHolder -> trackInt(iIntReferenceHolder.getReferenceHolder()));
//
//    trackInt(new IntReferenceHolder()
//    {
//      @Override
//      public int get()
//      {
//        return tileEntity.getStorage().getEnergyStored();
//      }
//
//      @Override
//      public void set(int value)
//      {
//        tileEntity.getStorage().setEnergy(value);
//      }
//    });
  
    ItemStackHandler handler = tileEntity.getInventory();
    
    addInputSlot(handler, DehydratorEntity.INPUT_SLOT, 175 / 2 - 6, 20);

    addSlot(handler, DehydratorEntity.OUTPUT_SLOT, 175 / 2 - 6, 52);
    
    layoutPlayerInventorySlots();
  }
}
