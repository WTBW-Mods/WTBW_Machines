package com.wtbw.mods.machines.item;

import com.wtbw.mods.lib.util.Action;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class WrenchItem extends Item
{
  public interface WrenchAction
  {
    List<WrenchAction> actions = new ArrayList<>();
    
    default List<WrenchAction> getActions()
    {
      return actions;
    }
    
    default WrenchAction and(WrenchAction action)
    {
      actions.add(action);
      return this;
    }
    
    boolean action(World world, BlockPos pos, PlayerEntity playerEntity, ItemUseContext context);
  }
  
  protected static Map<Block, WrenchAction> wrenchActions = new HashMap<>();
  
  public static WrenchAction registerWrenchAction(Block block, WrenchAction action)
  {
    wrenchActions.put(block, action);
    
    return action;
  }
  
  public WrenchItem(Properties properties)
  {
    super(properties.maxStackSize(1));
  }
  
  @Override
  public ActionResultType onItemUse(ItemUseContext context)
  {
    World world = context.getWorld();
    BlockPos pos = context.getPos();
    BlockState blockState = world.getBlockState(pos);
    if (!blockState.isAir(world, pos))
    {
      if (wrenchActions.containsKey(blockState.getBlock()))
      {
        WrenchAction wrenchAction = wrenchActions.get(blockState.getBlock());
        boolean success = wrenchAction.action(world, pos, context.getPlayer(), context);
        
        if (wrenchAction.getActions().size() > 0)
        {
          for (WrenchAction action : wrenchAction.getActions())
          {
            if (!action.action(world, pos, context.getPlayer(), context))
            {
              success = false;
            }
          }
        }
        
        return success ? ActionResultType.SUCCESS : ActionResultType.FAIL;
      }
    }
  
    return super.onItemUse(context);
  }
}
