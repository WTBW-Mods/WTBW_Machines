package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.FuelGeneratorContainer;
import com.wtbw.mods.machines.tile.generator.FuelGeneratorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

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
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    energyBar.draw();
    progressBackgroundSprite.render(guiLeft + 81, guiTop + 30);
    progressBar.draw(guiLeft, 0);
  }
}
