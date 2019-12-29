package com.wtbw.machines.gui.container;

import com.wtbw.lib.gui.container.BaseTileContainer;
import com.wtbw.lib.tile.util.RedstoneMode;
import com.wtbw.machines.tile.VacuumChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

/*
  @author: Naxanria
*/
public class VacuumChestContainer extends BaseTileContainer<VacuumChestTileEntity>
{
  public VacuumChestContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.VACUUM_CHEST, id, world, pos, playerInventory);
    
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
    
    addSlot(new SlotItemHandler(tileEntity.getFilter(), 0, 18, 35));
    
    tileEntity.getInventory().ifPresent(handler -> addSlotBox(handler, 0, 62, 26, 3, 2, 18, 18));
    
    layoutPlayerInventorySlots(8, 84);
  }
  
  @Override
  public boolean canInteractWith(PlayerEntity playerIn)
  {
    return true;
  }
  
  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
  {
    ItemStack stack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(index);
    
    if (slot != null && slot.getHasStack())
    {
      ItemStack itemStack = slot.getStack();
      stack = itemStack.copy();
  
      if (index == 0)
      {
        if (!mergeItemStack(itemStack, 7, inventorySlots.size(), true))
        {
          return ItemStack.EMPTY;
        }
      }
      else if (index < 7)
      {
        // insert in player inventory
        if (!mergeItemStack(itemStack, 7, inventorySlots.size(), true))
        {
          return ItemStack.EMPTY;
        }
      }
      // insert into chest
      else if (!mergeItemStack(itemStack, 1, 7, false))
      {
        return ItemStack.EMPTY;
      }
  
  
      if (stack.isEmpty())
      {
        slot.putStack(ItemStack.EMPTY);
      }
      else
      {
        slot.onSlotChanged();
      }
    }
    
    return stack;
  }
}
