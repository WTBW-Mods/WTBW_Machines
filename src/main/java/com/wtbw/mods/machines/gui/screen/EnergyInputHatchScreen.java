package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.machines.gui.container.EnergyInputHatchContainer;
import com.wtbw.mods.machines.tile.multi.EnergyInputHatchTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class EnergyInputHatchScreen extends BaseContainerScreen<EnergyInputHatchContainer>
{
  private EnergyBar energyBar;
  private final EnergyInputHatchTile hatch;
  
  public EnergyInputHatchScreen(EnergyInputHatchContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    
    hatch = container.tileEntity;
  }
  
  @Override
  protected void init()
  {
    super.init();
    energyBar = getDefaultBar(hatch.getEnergyStorage());
    addTooltipProvider(energyBar);
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
  protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    defaultGui(stack);
    if (energyBar != null)
    {
      energyBar.draw(stack);
    }
  }
}
