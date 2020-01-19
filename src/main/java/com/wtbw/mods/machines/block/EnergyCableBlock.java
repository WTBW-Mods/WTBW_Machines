package com.wtbw.mods.machines.block;

import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.block.util.WrenchHelper;
import com.wtbw.mods.machines.item.WrenchItem;
import com.wtbw.mods.machines.tile.cables.EnergyCableEntity;
import com.wtbw.mods.machines.tile.cables.EnergyCableTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class EnergyCableBlock extends BaseTileBlock<EnergyCableEntity>
{
  public static final BooleanProperty NORTH = BooleanProperty.create("north");
  public static final BooleanProperty SOUTH = BooleanProperty.create("south");
  public static final BooleanProperty WEST = BooleanProperty.create("west");
  public static final BooleanProperty EAST = BooleanProperty.create("east");
  public static final BooleanProperty UP = BooleanProperty.create("up");
  public static final BooleanProperty DOWN = BooleanProperty.create("down");
  
  private static final Map<Direction, BooleanProperty> PROPERTY_MAP = Utilities.make(new HashMap<>(), map ->
  {
    map.put(Direction.NORTH, NORTH);
    map.put(Direction.SOUTH, SOUTH);
    map.put(Direction.WEST, WEST);
    map.put(Direction.EAST, EAST);
    map.put(Direction.UP, UP);
    map.put(Direction.DOWN, DOWN);
  });
  
  private final ShapeHelper shapeHelper = new ShapeHelper();
  
  public final EnergyCableTier tier;
  
  public EnergyCableBlock(Properties properties, EnergyCableTier tier)
  {
    super(properties, ((world, state) -> new EnergyCableEntity(tier)));
    
    setDefaultState
    (
      getStateContainer().getBaseState()
      .with(NORTH, false)
      .with(SOUTH, false)
      .with(WEST, false)
      .with(EAST, false)
      .with(UP, false)
      .with(DOWN, false)
    );
    
    this.tier = tier;
  
    WrenchItem.registerWrenchAction(this, WrenchHelper.dropWrenchAction());
    // todo: make faces be "blocked" to connect
  }
  
  private BlockState makeConnections(IWorldReader world, BlockPos pos)
  {
    boolean north = canConnect(world, pos.offset(Direction.NORTH), Direction.SOUTH);
    boolean south = canConnect(world, pos.offset(Direction.SOUTH), Direction.NORTH);
    boolean west = canConnect(world, pos.offset(Direction.WEST), Direction.EAST);
    boolean east = canConnect(world, pos.offset(Direction.EAST), Direction.WEST);
    boolean up = canConnect(world, pos.offset(Direction.UP), Direction.DOWN);
    boolean down = canConnect(world, pos.offset(Direction.DOWN), Direction.UP);
    
    return getDefaultState()
      .with(NORTH, north)
      .with(SOUTH, south)
      .with(WEST, west)
      .with(EAST, east)
      .with(UP, up)
      .with(DOWN, down);
  }
  
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  {
    builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
  }
  
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    return makeConnections(context.getWorld(), context.getPos());
  }
  
  private boolean canConnect(IWorldReader world, BlockPos pos, Direction facing)
  {
    TileEntity tileEntity = world.getTileEntity(pos);
    return tileEntity != null && tileEntity.getCapability(CapabilityEnergy.ENERGY, facing).isPresent();
  }
  
  public BooleanProperty getProperty(Direction direction)
  {
    return PROPERTY_MAP.getOrDefault(direction, NORTH);
  }
  
  @Override
  public BlockState updatePostPlacement(BlockState currentState, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
  {
//    boolean connected = canConnect(worldIn, currentPos, facing.getOpposite());
    
    return makeConnections(worldIn, currentPos);
  }
  
  @Override
  public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
  {
  
  }
  
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
  {
    return shapeHelper.getShape(state);
  }
  
  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
  {
    return shapeHelper.getShape(state);
  }
  
  @Override
  public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
  {
    return shapeHelper.getShape(state);
  }
  
  @Override
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos)
  {
    return shapeHelper.getShape(state);
  }
  
  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
  {
    return true;
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    String transfer = (Screen.hasShiftDown()) ? String.valueOf(tier.transfer) : Utilities.abbreviate(tier.transfer);
    tooltip.add(TextComponentBuilder.createTranslated(ClientConstants.getTooltipKey("energy_cable_transfer")).white().insertSpaces()
      .next(transfer).aqua()
      .nextTranslate(ClientConstants.Tooltips.EF_TICK).build());
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  private static class ShapeHelper
  {
    private Map<BlockState, VoxelShape> stateMap = new HashMap<>();
    
    public VoxelShape getShape(BlockState state)
    {
      VoxelShape shape;
      if (stateMap.containsKey(state))
      {
        shape = stateMap.get(state);
      }
      else
      {
        shape = createShape(state);
        stateMap.put(state, shape);
      }
      
      return shape;
    }
    
    private VoxelShape createShape(BlockState state)
    {
      int partSize = 4;
      int centerSize = 6;
      
      VoxelShape centerShape = Block.makeCuboidShape(centerSize, centerSize, centerSize, 16 - centerSize, 16 - centerSize, 16 - centerSize);
      List<VoxelShape> parts = new ArrayList<>();
  
      for (Direction direction : Direction.values())
      {
        if (state.get(PROPERTY_MAP.get(direction)))
        {
          double x = direction == Direction.WEST ? 0 : direction == Direction.EAST ? 16D : partSize;
          double z = direction == Direction.NORTH ? 0 : direction == Direction.SOUTH ? 16D : partSize;
          double y = direction == Direction.DOWN ? 0 : direction == Direction.UP ? 16D : partSize;
          
          parts.add(Block.makeCuboidShape(x, y, z, 16 - partSize, 16 - partSize, 16 - partSize));
        }
      }
      
      return VoxelShapes.or(centerShape, parts.toArray(new VoxelShape[]{}));
    }
  }
}
