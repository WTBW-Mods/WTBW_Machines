package com.wtbw.mods.machines.block;

import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.machines.tile.SolarPanelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/*
  @author: Naxanria
*/
public class SolarPanelBlock extends BaseTileBlock<SolarPanelTileEntity>
{
  public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 3, 16);
  
  public SolarPanelBlock(Properties properties, int capacity, int maxTransfer, int generate)
  {
    super(properties, (world, state) -> new SolarPanelTileEntity(capacity, maxTransfer, generate));
  }
  
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
  {
    return SHAPE;
  }
  
  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
  {
    return SHAPE;
  }
}
