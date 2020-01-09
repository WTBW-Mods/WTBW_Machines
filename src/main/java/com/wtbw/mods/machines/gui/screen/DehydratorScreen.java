package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.gui.container.DehydratorContainer;
import com.wtbw.mods.machines.tile.machine.DehydratorTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class DehydratorScreen extends BaseContainerScreen<DehydratorContainer>
{
  private ProgressBar progressBar;
  private EnergyBar energyBar;

  private RedstoneButton<DehydratorTileEntity> redstoneButton;
  
  public DehydratorScreen(DehydratorContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    DehydratorTileEntity tileEntity = container.tileEntity;
    BaseEnergyStorage storage = tileEntity.getStorage();
    Sprite bg = CrusherScreen.ICONS.getSprite(0, 0, 10, 10);
    Sprite progress = bg.getBelow(10, 10);
    progressBar = new SpriteProgressBar(guiLeft + 175 / 2 -3, guiTop + 39, progress, bg, tileEntity::getDuration, tileEntity::getProgress)
      .setFillDirection(ProgressBar.FillDirection.TOP_BOTTOM);
    
//    progressBar = new ProgressBar(guiLeft + 69, guiTop + 35, 70, 3, tileEntity::getDuration, tileEntity::getProgress)
//      .setColor(0xffffffff).setFillDirection(ProgressBar.FillDirection.LEFT_RIGHT).setBorder(false);
    energyBar = getDefaultBar(storage);
    
    addTooltipProvider(energyBar);
//    redstoneButton = new RedstoneButton<>(guiLeft - 20, guiTop + 10, tileEntity);
  }
  
  @Override
  public void tick()
  {
    super.tick();
    if (progressBar != null)
    {
      progressBar.update();
    }
    
    if (energyBar != null)
    {
      energyBar.update();
    }
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    
    progressBar.draw();
    energyBar.draw();
  }
}
