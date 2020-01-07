package com.wtbw.mods.machines.block.base;

import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.config.CommonConfig;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

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

  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    CommonConfig config = CommonConfig.instance();
    String baseKey = WTBWMachines.MODID + ".tooltip.tier";

    switch (TIER){
      case 1:
        tooltip.add(TextComponentBuilder.createTranslated(baseKey, config.quarryTier1.get()).aqua().build());
        break;
      case 2:
        tooltip.add(TextComponentBuilder.createTranslated(baseKey, config.quarryTier2.get()).aqua().build());
        break;
      case 3:
        tooltip.add(TextComponentBuilder.createTranslated(baseKey, config.quarryTier3.get()).aqua().build());
        break;
      case 4:
        tooltip.add(TextComponentBuilder.createTranslated(baseKey, config.quarryTier4.get()).aqua().build());
        break;
    }


    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
