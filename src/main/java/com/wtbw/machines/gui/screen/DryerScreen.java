package com.wtbw.machines.gui.screen;

import com.wtbw.lib.gui.screen.BaseContainerScreen;
import com.wtbw.lib.gui.util.EnergyBar;
import com.wtbw.lib.gui.util.ProgressBar;
import com.wtbw.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.machines.gui.container.DryerContainer;
import com.wtbw.machines.tile.machine.DryerTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/*
  @author: Naxanria
*/
public class DryerScreen extends BaseContainerScreen<DryerContainer>
{
  private ProgressBar progressBar;
  private EnergyBar energyBar;
  
  public DryerScreen(DryerContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    DryerTileEntity tileEntity = container.tileEntity;
    BaseEnergyStorage storage = tileEntity.getStorage();
    progressBar = new ProgressBar(guiLeft + 30, guiTop + 55, 80, 10, tileEntity::getDuration, tileEntity::getProgress)
      .setColor(0xffffffff).setFillDirection(ProgressBar.FillDirection.LEFT_RIGHT);
    energyBar = new EnergyBar(storage, guiLeft + 2, guiTop + 15);
    addTooltipProvider(energyBar);
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
    progressBar.draw();
    energyBar.draw();
    String blockName = new TranslationTextComponent("block.wtbw_machines.dryer").getUnformattedComponentText();
    this.font.drawString(blockName, guiLeft + 8, guiTop + 6, 0xff404040);
    this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), guiLeft + 8, guiTop + 73, 0xff404040);
  }
}
