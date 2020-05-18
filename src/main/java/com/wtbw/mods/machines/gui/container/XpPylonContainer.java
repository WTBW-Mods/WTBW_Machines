package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.machines.tile.XpPylonTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class XpPylonContainer extends BaseTileContainer<XpPylonTile>
{
  public XpPylonContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.XP_PYLON, id, world, pos, playerInventory);
  }
}
