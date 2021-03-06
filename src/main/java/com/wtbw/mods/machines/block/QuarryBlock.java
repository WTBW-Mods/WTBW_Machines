package com.wtbw.mods.machines.block;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.machines.block.base.TierBlock;
import com.wtbw.mods.machines.tile.machine.QuarryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
  
  @Override
  public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
  {
    updateState(state, worldIn, pos);
  }
  
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
  {
    return updateState(state, world, pos);
  }
  
  private BlockState updateState(BlockState state, IWorld world, BlockPos pos)
  {
    TileEntity tileEntity = world.getTileEntity(pos);
    
    if (tileEntity instanceof QuarryTileEntity)
    {
      QuarryTileEntity quarry = (QuarryTileEntity) tileEntity;
      
      int highestLevel = 0;
      for (Direction direction : Direction.values())
      {
        Block b = world.getBlockState(pos.offset(direction)).getBlock();
        if (b instanceof TierBlock)
        {
          int tier = ((TierBlock) b).TIER;
          if (tier > highestLevel)
          {
            highestLevel = tier;
          }
        }
        
//        if (b == ModBlocks.TIER1_UPGRADE)
//        {
//          quarry.upgradeLevel = Math.max(quarry.upgradeLevel, 1);
//        }
//        else if (b == ModBlocks.TIER2_UPGRADE)
//        {
//          quarry.upgradeLevel = Math.max(quarry.upgradeLevel, 2);
//        }
//        else if (b == ModBlocks.TIER3_UPGRADE)
//        {
//          quarry.upgradeLevel = Math.max(quarry.upgradeLevel, 3);
//        }
//        else if (b == ModBlocks.TIER4_UPGRADE)
//        {
//          quarry.upgradeLevel = Math.max(quarry.upgradeLevel, 4);
//        }
      }
      
      if(highestLevel != quarry.upgradeLevel)
      {
        quarry.upgradeLevel = highestLevel;
        quarry.upgradeLevelUpdated();
        quarry.markDirty();
        WTBWLib.LOGGER.debug("Quarry at " + pos + " has changed Level to " + quarry.upgradeLevel);
      }
    }
    return state;
  }
}
