package com.wtbw.mods.machines.block.base;

import net.minecraft.block.Block;

/*
  @author: Naxanria
*/
public class TierBlock extends Block
{
  public final int TIER;
  
  public TierBlock(Properties properties, int tier)
  {
    super(properties);
    TIER = tier;
  }
}
