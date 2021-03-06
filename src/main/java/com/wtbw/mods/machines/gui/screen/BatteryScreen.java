package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.machines.gui.container.BatteryContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class BatteryScreen extends BaseContainerScreen<BatteryContainer>
{
  private EnergyBar energyBar;
  
  public BatteryScreen(BatteryContainer container, PlayerInventory inventory, ITextComponent title)
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
  }
  
  @Override
  protected void init()
  {
    super.init();
    
    energyBar = getDefaultBar(container.tileEntity.getStorage());
    addTooltipProvider(energyBar);
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    energyBar.draw();
  }
}
