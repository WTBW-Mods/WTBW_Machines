package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.machines.gui.container.SolarPanelContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class SolarPanelScreen extends BaseContainerScreen<SolarPanelContainer>
{
  private EnergyBar energyBar;
  
  public SolarPanelScreen(SolarPanelContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    energyBar = getDefaultBar(container.tileEntity.getStorage());
    addTooltipProvider(energyBar);
  }
  
  @Override
  public void tick()
  {
    if (energyBar != null)
    {
      energyBar.update();
    }
    
    super.tick();
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    energyBar.draw();
    
    int gen = container.tileEntity.getGenerate();
    if (!container.tileEntity.canGenerate())
    {
      gen = 0;
    }
    
    drawString(font, "Producing: " + gen, guiLeft + 50, guiTop + 20, 0xffffffff);
  }
}
