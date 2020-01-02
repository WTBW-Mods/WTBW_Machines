package com.wtbw.machines.gui.screen;

import com.wtbw.lib.gui.screen.BaseContainerScreen;
import com.wtbw.lib.gui.util.EnergyBar;
import com.wtbw.lib.gui.util.ProgressBar;
import com.wtbw.lib.gui.util.RedstoneButton;
import com.wtbw.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.machines.gui.container.DryerContainer;
import com.wtbw.machines.tile.machine.DryerTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class DryerScreen extends BaseContainerScreen<DryerContainer>
{
  private ProgressBar progressBar;
  private EnergyBar energyBar;
  private RedstoneButton<DryerTileEntity> redstoneButton;
  
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
    progressBar = new ProgressBar(guiLeft + 66, guiTop + 55, 70, 3, tileEntity::getDuration, tileEntity::getProgress)
      .setColor(0xffffffff).setFillDirection(ProgressBar.FillDirection.LEFT_RIGHT).setBorder(false);
    energyBar = new EnergyBar(storage, guiLeft + 12, guiTop + 15);
    addTooltipProvider(energyBar);
    redstoneButton = new RedstoneButton<>(guiLeft - 20, guiTop + 10, tileEntity);
//    addButton(redstoneButton);
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
