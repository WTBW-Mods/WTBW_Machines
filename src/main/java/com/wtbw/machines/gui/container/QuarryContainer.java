package com.wtbw.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.machines.tile.machine.QuarryTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
  @author: Sunekaer
*/
public class QuarryContainer extends BaseTileContainer<QuarryTileEntity>
{
  public QuarryContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.QUARRY, id, world, pos, playerInventory);
    
    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getRedstoneMode().ordinal();
      }
      
      @Override
      public void set(int value)
      {
        tileEntity.getControl().setMode(RedstoneMode.values()[value % RedstoneMode.values().length]);
      }
    });
    
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
    
    tileEntity.getInventory().ifPresent(handler ->
    {
      addSlotBox(handler, 0, 84 - 21, 17, 3, 3, 18, 18);
    });
    
    layoutPlayerInventorySlots(30 - 21, 84);
  }
  
  @Override
  public boolean canInteractWith(PlayerEntity playerIn)
  {
    return true;
  }
}
