package com.wtbw.mods.machines.tile.base.multi;

import net.minecraft.util.math.BlockPos;

/*
  @author: Naxanria
*/
public class BlockPatternFeedback
{
  public final boolean success;
  public final BlockPos errored;
  public final BlockValidator validator;
  
  private BlockPatternFeedback(boolean success, BlockPos errored, BlockValidator validator)
  {
    this.success = success;
    this.errored = errored;
    this.validator = validator;
  }
  
  public static BlockPatternFeedback success()
  {
    return new BlockPatternFeedback(true, null, null);
  }
  
  public static BlockPatternFeedback failure(BlockPos errored, BlockValidator validator)
  {
    return new BlockPatternFeedback(false, errored, validator);
  }
}
