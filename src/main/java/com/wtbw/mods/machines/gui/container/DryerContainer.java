package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.machine.DryerTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Naxanria
*/
public class DryerContainer extends BaseTileContainer<DryerTileEntity>
{
  public DryerContainer(int id, World world, BlockPos pos, PlayerInventory inventory)
  {
    super(ModContainers.DRYER, id, world, pos, inventory);
  
    NBTManager manager = tileEntity.getManager();
    manager.referenceHolders().forEach(iIntReferenceHolder -> trackInt(iIntReferenceHolder.getReferenceHolder()));
    
    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getStorage().getEnergyStored();
      }
  
      @Override
      public void set(int value)
      {
        tileEntity.getStorage().setEnergy(value);
      }
    });
  
    ItemStackHandler handler = tileEntity.getInventory();
    
    addInputSlot(handler, DryerTileEntity.INPUT_SLOT, 46, 50);

    addSlot(handler, DryerTileEntity.OUTPUT_SLOT, 146, 50);
    
    layoutPlayerInventorySlots(8, 84);
  }
}
