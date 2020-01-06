package com.wtbw.mods.machines.block;

import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.tile.SolarPanelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

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
}
