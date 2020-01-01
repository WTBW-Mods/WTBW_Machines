package com.wtbw.machines.block;

import com.wtbw.lib.block.BaseTileBlock;
import com.wtbw.machines.tile.machine.QuarryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/*
  @author: Sunekaer
*/
public class QuarryBlock extends BaseTileBlock<QuarryTileEntity>
{
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  private static final VoxelShape LEG_1 = Block.makeCuboidShape(0, 0, 0, 2, 11, 2);
  private static final VoxelShape LEG_2 = Block.makeCuboidShape(14, 0, 0, 16, 11, 2);
  private static final VoxelShape LEG_3 = Block.makeCuboidShape(0, 0, 14, 2, 11, 16);
  private static final VoxelShape LEG_4 = Block.makeCuboidShape(14, 0, 14, 16, 11, 16);
  private static final VoxelShape TOP = Block.makeCuboidShape(0, 11, 0, 16, 16, 16);
  private static final VoxelShape CORE = Block.makeCuboidShape(4, 1, 4, 12, 12, 12);
  private static final VoxelShape SHAPE = VoxelShapes.or(LEG_1, LEG_2, LEG_3, LEG_4, TOP, CORE);

  public QuarryBlock(Properties properties) {
    super(properties, (world, state) -> new QuarryTileEntity());
    setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
  {
    return SHAPE;
  }

  @Override
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return SHAPE;
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    // use opposite to face towards placer
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
  }

  public BlockState rotate(BlockState state, Rotation rot)
  {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  public BlockState mirror(BlockState state, Mirror mirrorIn)
  {
    return state.rotate(mirrorIn.toRotation(state.get(FACING)));
  }

  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  {
    builder.add(FACING);
  }
}
