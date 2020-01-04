package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.gui.container.CompressorContainer;
import com.wtbw.mods.machines.tile.PoweredCompressorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Sunekaer
*/
public class CompressorScreen extends BaseContainerScreen<CompressorContainer>
{
  private ProgressBar progressBar;
  private EnergyBar energyBar;

  private RedstoneButton<PoweredCompressorEntity> redstoneButton;

  public CompressorScreen(CompressorContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    PoweredCompressorEntity tileEntity = container.tileEntity;
    BaseEnergyStorage storage = tileEntity.getStorage();
    progressBar = new ProgressBar(guiLeft + 69, guiTop + 35, 70, 3, tileEntity::getDuration, tileEntity::getProgress)
      .setColor(0xffffffff).setFillDirection(ProgressBar.FillDirection.LEFT_RIGHT).setBorder(false);
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

//    PoweredCompressorEntity tileEntity = container.tileEntity;
  
//    drawString(font,"Current heat " + tileEntity.getHeat(), guiLeft + 50, guiTop + 50, 0xffffffff);
//    drawString(font,"Target heat " + tileEntity.getTargetHeat(), guiLeft + 50, guiTop + 60, 0xffffffff);
//
//    font.drawString("Heat " + tileEntity.getHeat() + "/" + tileEntity.getTargetHeat(), 0, 0, 0xffffffff);
//    font.drawString("PowerUsage " + tileEntity.getPowerUsage() + " RF", 0, 15, 0xffffffff);
  }
}
