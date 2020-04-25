package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseUpgradeScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.FluidBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.HydratorContainer;
import com.wtbw.mods.machines.tile.machine.HydratorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class HydratorScreen extends BaseUpgradeScreen<HydratorContainer>
{
  public static final Sprite PROGRESS = ClientConstants.Gui.ICONS.getSprite(48, 0, 16);
  public static final Sprite PROGRESS_BACKGROUND = ClientConstants.Gui.ICONS.getSprite(48, 18, 16);
  
  private ProgressBar progressBar;
  private EnergyBar energyBar;
  private FluidBar waterBar;
  
  public HydratorScreen(HydratorContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void drawGuiBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    
    progressBar.draw();
    energyBar.draw();
    waterBar.draw();
  }
  
  @Override
  public void tick()
  {
    super.tick();
  
    if (energyBar != null)
    {
      energyBar.update();
    }
  
    if (waterBar != null)
    {
      waterBar.update();
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
  
    HydratorEntity tileEntity = container.tileEntity;
    energyBar = getDefaultBar(tileEntity.getStorage());
    addTooltipProvider(energyBar);
    waterBar = new FluidBar(tileEntity.getWaterTank(), guiLeft + xSize - 25, guiTop + 16);
    addTooltipProvider(waterBar);
    progressBar = new SpriteProgressBar(guiLeft + xSize / 2 - 8, guiTop + 35, PROGRESS, PROGRESS_BACKGROUND,
      tileEntity::getDuration, tileEntity::getProgress)
      .setFillDirection(ProgressBar.FillDirection.TOP_BOTTOM);
//      new ProgressBar(guiLeft + xSize / 2 - 3, guiTop + 39, 5, 15, tileEntity::getDuration, tileEntity::getProgress)
//      .setFillDirection(ProgressBar.FillDirection.TOP_BOTTOM);
  }
}
