package com.wtbw.machines.block;

import com.wtbw.machines.tile.furnace.BaseFurnaceTileEntity;
import com.wtbw.machines.tile.furnace.FurnaceTier;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipeType;

/*
  @author: Naxanria
*/
public class TieredFurnaceBlock extends BaseFurnaceBlock<BaseFurnaceTileEntity>
{
  public TieredFurnaceBlock(Block.Properties properties, FurnaceTier tier)
  {
    super(properties, (world, state) -> new BaseFurnaceTileEntity(tier, IRecipeType.SMELTING));
  }
}
