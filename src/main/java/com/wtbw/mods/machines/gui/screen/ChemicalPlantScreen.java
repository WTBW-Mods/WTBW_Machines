package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.FluidBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.machines.gui.container.ChemicalPlantContainer;
import com.wtbw.mods.machines.tile.machine.ChemicalPlantEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class ChemicalPlantScreen extends BaseContainerScreen<ChemicalPlantContainer>
{
  private ProgressBar progressBar;
  private EnergyBar energyBar;
  private FluidBar fluidInputBar;
  private FluidBar fluidOutputBar;
  
  public ChemicalPlantScreen(ChemicalPlantContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
  
    ChemicalPlantEntity tileEntity = container.tileEntity;
    
    energyBar = getDefaultBar(tileEntity.getStorage());
    addTooltipProvider(energyBar);
    
    fluidInputBar = new FluidBar(tileEntity.getFluidInput(), energyBar.getWidth() + energyBar.getX() + 3, energyBar.getY());
    addTooltipProvider(fluidInputBar);
    
    fluidOutputBar = new FluidBar(tileEntity.getFluidOutput(), guiLeft + xSize - energyBar.getWidth() - 5, energyBar.getY());
    addTooltipProvider(fluidOutputBar);
    
    int w = xSize - 3 * 20 - 15;
    progressBar = new ProgressBar(guiLeft + xSize / 2 - w / 2, guiTop + 40, w, 5, tileEntity::getDuration, tileEntity::getProgress);
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
  {
    defaultGui(matrixStack);
    
    progressBar.draw(matrixStack);
    energyBar.draw(matrixStack);
    fluidInputBar.draw(matrixStack);
    fluidOutputBar.draw(matrixStack);
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
  
    if (fluidInputBar != null)
    {
      fluidInputBar.update();
    }
  
    if (fluidOutputBar != null)
    {
      fluidOutputBar.update();
    }
  }
}
