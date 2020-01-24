package com.wtbw.mods.machines.block.base;

import com.wtbw.mods.lib.block.SixWayTileBlock;
import com.wtbw.mods.machines.block.util.WrenchHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class WrenchableSixWayTileBlock<TE extends TileEntity> extends SixWayTileBlock<TE>
{
  public WrenchableSixWayTileBlock(Properties properties, TileEntityProvider<TE> tileEntityProvider)
  {
    super(properties, tileEntityProvider);
  }
  
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit)
  {
    return WrenchHelper.isUsingWrench(playerEntity, hand) ? ActionResultType.PASS : super.onBlockActivated(state, world, pos, playerEntity, hand, hit);
  }
}
