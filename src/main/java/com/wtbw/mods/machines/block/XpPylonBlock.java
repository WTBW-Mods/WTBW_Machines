package com.wtbw.mods.machines.block;

import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.machines.tile.XpPylonTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/*
  @author: Naxanria
*/
public class XpPylonBlock extends BaseTileBlock<XpPylonTile>
{
  public static final VoxelShape SHAPE = VoxelShapes.or
    (
      Block.makeCuboidShape(7, 0, 7, 9, 1, 9),
      Block.makeCuboidShape(6, 1, 6, 10, 2, 10),
      Block.makeCuboidShape(5, 2, 5, 11, 3, 11),
      Block.makeCuboidShape(4, 3, 4, 12, 5, 12),
      Block.makeCuboidShape(3, 5, 3, 13, 11, 13),
      Block.makeCuboidShape(4, 11, 4, 12, 13, 12),
      Block.makeCuboidShape(5, 13, 5, 11, 14, 11),
      Block.makeCuboidShape(6, 14, 6, 10, 15, 10),
      Block.makeCuboidShape(7, 15, 7, 9, 16, 9)
    );
  
  public XpPylonBlock(Properties properties)
  {
    super(properties, (world, state) -> new XpPylonTile());
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
  
  @Override
  public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
  {
    return SHAPE;
  }
  
  @Override
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos)
  {
    return SHAPE;
  }
}
