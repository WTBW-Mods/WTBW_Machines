package com.wtbw.mods.machines.block.util;

import com.wtbw.mods.lib.gui.util.sprite.Direction;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.Constants;
import com.wtbw.mods.machines.item.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class WrenchHelper
{
  public static boolean isUsingWrench(PlayerEntity playerEntity, Hand hand)
  {
    ItemStack stack = playerEntity.getHeldItem(hand);
    return  (stack.getItem() instanceof WrenchItem);
  }
  
  public static WrenchItem.WrenchAction horizontalRotationWrenchAction(DirectionProperty facing)
  {
    
    return (world, pos, playerEntity, context) ->
    {
      if (!playerEntity.isCrouching())
      {
        BlockState blockState = world.getBlockState(pos);
        if (!world.isRemote)
        {
          net.minecraft.util.Direction direction = blockState.get(facing);
          direction = Utilities.getNext(Constants.rotationOrderHorizontal, direction);
          BlockState newState = blockState.with(facing, direction);
          world.setBlockState(pos, newState, 3);
        }
      }
      
      return true;
    };
  }
  
  public static WrenchItem.WrenchAction rotationWrenchAction(DirectionProperty facing)
  {
    return (world, pos, playerEntity, context) ->
    {
      if (!playerEntity.isCrouching())
      {
        BlockState blockState = world.getBlockState(pos);
        if (!world.isRemote)
        {
          net.minecraft.util.Direction direction = blockState.get(facing);
          direction = Utilities.getNext(Constants.rotationOrder, direction);
          BlockState newState = blockState.with(facing, direction);
          world.setBlockState(pos, newState, 3);
        }
      }
      
      return true;
    };
  }
  
  public static WrenchItem.WrenchAction dropWrenchAction()
  {
    return (world, pos, playerEntity, context) ->
    {
      if (playerEntity.isCrouching())
      {
        if (!world.isRemote)
        {
          world.destroyBlock(pos, true);
        }
      }
      
      return true;
    };
  }
}
