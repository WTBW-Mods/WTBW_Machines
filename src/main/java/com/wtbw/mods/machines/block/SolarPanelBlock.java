package com.wtbw.mods.machines.block;

import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.util.WrenchHelper;
import com.wtbw.mods.machines.item.WrenchItem;
import com.wtbw.mods.machines.tile.generator.SolarPanelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class SolarPanelBlock extends BaseTileBlock<SolarPanelTileEntity>
{
  public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 3, 16);
  public final SolarPanelTileEntity.Tier tier;
  
  public SolarPanelBlock(Properties properties, SolarPanelTileEntity.Tier tier)
  {
    super(properties, (world, state) -> new SolarPanelTileEntity(tier));
    this.tier = tier;
  
    WrenchItem.registerWrenchAction(this, WrenchHelper.dropWrenchAction());
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
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    if (Screen.hasShiftDown())
    {
      tooltip.add(TextComponentBuilder.createTranslated(WTBWMachines.MODID + ".tooltip.generating").white().next(tier.generate).aqua().build());
      tooltip.add(TextComponentBuilder.createTranslated(WTBWMachines.MODID + ".tooltip.capacity").white().next(tier.capacity).aqua().build());
    }
    else
    {
      tooltip.add(TextComponentBuilder.createTranslated(WTBWMachines.MODID + ".tooltip.shift_more_info").white().build());
    }
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  @Override
  public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit)
  {
    return WrenchHelper.isUsingWrench(playerEntity, hand) ? ActionResultType.PASS : super.func_225533_a_(state, world, pos, playerEntity, hand, hit);
  }
}
