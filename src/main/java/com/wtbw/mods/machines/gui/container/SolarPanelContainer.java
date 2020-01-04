package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.SolarPanelTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class SolarPanelContainer extends BaseTileContainer<SolarPanelTileEntity>
{
  public SolarPanelContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.SOLAR_PANEL, id, world, pos, playerInventory);
  
    track(tileEntity.getManager());
//    NBTManager manager = tileEntity.getManager();
//    manager.referenceHolders().forEach(iIntReferenceHolder -> trackInt(iIntReferenceHolder.getReferenceHolder()));
//
    layoutPlayerInventorySlots();
  }
}
