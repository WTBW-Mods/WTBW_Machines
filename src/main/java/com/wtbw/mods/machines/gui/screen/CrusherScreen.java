package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.*;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.gui.container.CrusherContainer;
import com.wtbw.mods.machines.tile.machine.PoweredCrusherEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Sunekaer
*/
public class CrusherScreen extends BaseContainerScreen<CrusherContainer>
{
  public static final SpriteMap ICONS = new SpriteMap(64, new ResourceLocation(WTBWMachines.MODID, "textures/gui/icons.png"));
  public static final Sprite PROGRESS_BACKGROUND = ICONS.getSprite(10, 0, 10, 10);
  public static final Sprite PROGRESS = PROGRESS_BACKGROUND.getBelow(10, 10);
  
  private SpriteProgressBar progressBar;
  private EnergyBar energyBar;

  private RedstoneButton<PoweredCrusherEntity> redstoneButton;

  public CrusherScreen(CrusherContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    PoweredCrusherEntity tileEntity = container.tileEntity;
    BaseEnergyStorage storage = tileEntity.getStorage();
    progressBar = new SpriteProgressBar(guiLeft + 175 / 2 - 3 , guiTop + 39, PROGRESS, PROGRESS_BACKGROUND, tileEntity::getDuration, tileEntity::getProgress)
      .setFillDirection(ProgressBar.FillDirection.TOP_BOTTOM).cast();
    energyBar = new EnergyBar(storage, guiLeft + 12, guiTop + 15);
    addTooltipProvider(energyBar);
    redstoneButton = new RedstoneButton<>(guiLeft - 20, guiTop + 10, tileEntity);
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
    //fill(guiLeft + 175 / 2, guiTop + 69, guiLeft + 175 / 2 ,guiTop + 70 ,0xffffffff);
//    PoweredCompressorEntity tileEntity = container.tileEntity;
  
//    drawString(font,"Current heat " + tileEntity.getHeat(), guiLeft + 50, guiTop + 50, 0xffffffff);
//    drawString(font,"Target heat " + tileEntity.getTargetHeat(), guiLeft + 50, guiTop + 60, 0xffffffff);
//
//    font.drawString("Heat " + tileEntity.getHeat() + "/" + tileEntity.getTargetHeat(), 0, 0, 0xffffffff);
//    font.drawString("PowerUsage " + tileEntity.getPowerUsage() + " RF", 0, 15, 0xffffffff);
  }
}
