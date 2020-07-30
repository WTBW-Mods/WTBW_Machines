package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ITooltipProvider;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.TooltipRegion;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.FuelGeneratorContainer;
import com.wtbw.mods.machines.tile.generator.FuelGeneratorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

/*
  @author: Naxanria
*/
public class FuelGeneratorScreen extends BaseContainerScreen<FuelGeneratorContainer>
{
  private Sprite progressBackgroundSprite = ClientConstants.Gui.ICONS.getSprite(20, 0, 14, 13);
  private Sprite progressSprite = progressBackgroundSprite.getBelow(14, 13);
  private EnergyBar energyBar;
  private SpriteProgressBar progressBar;
  
  public FuelGeneratorScreen(FuelGeneratorContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  public void tick()
  {
    super.tick();
    
    if (energyBar != null)
    {
      energyBar.update();
    }
    
    if (progressBar != null)
    {
      progressBar.update();
    }
  }
  
  @Override
  protected void init()
  {
    super.init();
    FuelGeneratorEntity tileEntity = container.tileEntity;
    energyBar = getDefaultBar(tileEntity.getStorage());
    progressBar = new SpriteProgressBar(81, guiTop + 30, progressSprite, progressBackgroundSprite, tileEntity::getGenTime, tileEntity::getGenCounter);
    addTooltipProvider(energyBar);
    addTooltipProvider(new TooltipRegion(guiLeft + 81, guiTop + 30, 14,14)
    {
      @Override
      public List<String> getTooltip()
      {
        return Utilities.listOf( TextComponentBuilder.createTranslated("wtbw_machines.tooltip.burn_time", String.valueOf(tileEntity.getGenCounter())).build().getUnformattedComponentText());
      }
    });
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    defaultGui(stack);
    energyBar.draw(stack);
    progressBackgroundSprite.render(stack, guiLeft + 81, guiTop + 30);
    progressBar.draw(stack, guiLeft, 0);
  }
}
