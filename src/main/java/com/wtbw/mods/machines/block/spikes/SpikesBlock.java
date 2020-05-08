package com.wtbw.mods.machines.block.spikes;

import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWFakePlayer;
import com.wtbw.mods.machines.WTBWMachines;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class SpikesBlock extends Block
{
  private static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 5, 16);
  
  private final SpikesType type;
  
  public SpikesBlock(Properties properties, SpikesType type)
  {
    super(properties);
    this.type = type;
  }
  
  @Override
  public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
  {
    if (!worldIn.isRemote)
    {
      if (entityIn instanceof LivingEntity)
      {
        LivingEntity livingEntity = (LivingEntity) entityIn;
        if (type.lethal || livingEntity.getHealth() > 1f)
        {
          SpikesDamageSource spikeDmgSource = new SpikesDamageSource(type);
          if (type.isPlayer)
          {
            WTBWFakePlayer fakePlayer = WTBWFakePlayer.getInstance((ServerWorld) worldIn).get();
        
            if (fakePlayer != null)
            {
              spikeDmgSource.setSource(fakePlayer);
            }
          }
      
          livingEntity.attackEntityFrom(spikeDmgSource, type.damage);
          // being a player already drops xp, dont want to have it drop double
//          if (type.dropXP && !type.isPlayer)
//          {
//            if (livingEntity.getHealth() <= 0)
//            {
//              WTBWFakePlayer fakePlayer = WTBWFakePlayer.getInstance((ServerWorld) worldIn).get();
//              if (fakePlayer != null)
//              {
//                int xp = livingEntity.getExperiencePoints(fakePlayer);
//                int xp = 1;
//                if (xp > 0)
//                {
//                  Utilities.spawnExp(worldIn, entityIn.getPosition(), xp);
//                }
//              }
//            }
//          }
        }
      }
    }
  }
//
//  @Override
//  public BlockRenderLayer getRenderLayer()
//  {
//    return BlockRenderLayer.CUTOUT;
//  }
  
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
  {
    return SHAPE;
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    String baseKey = WTBWMachines.MODID + ".tooltip.spikes";
    tooltip.add(TextComponentBuilder.createTranslated(baseKey).aqua().build());
    
    String lethal = baseKey + "_lethal";
    String nonLethal = baseKey + "_non_lethal";
    tooltip.add(TextComponentBuilder.createTranslated(type.lethal ? lethal : nonLethal).yellow().build());
    
    if (type.isPlayer)
    {
      tooltip.add(TextComponentBuilder.createTranslated(baseKey + "_player_drops").gold().build());
    }
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
