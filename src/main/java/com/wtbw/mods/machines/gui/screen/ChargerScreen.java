package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.machines.gui.container.ChargerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class ChargerScreen extends BaseContainerScreen<ChargerContainer>
{
  protected EnergyBar energyBar;
  
  public ChargerScreen(ChargerContainer container, PlayerInventory inventory, ITextComponent title)
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
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
  {
    defaultGui(matrixStack);
    energyBar.draw(matrixStack);
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
}
