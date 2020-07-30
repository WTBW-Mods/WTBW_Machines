package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseUpgradeScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.CrusherContainer;
import com.wtbw.mods.machines.tile.machine.PoweredCrusherEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Sunekaer, Naxanria
*/
public class CrusherScreen extends BaseUpgradeScreen<CrusherContainer>
{
  public static final Sprite PROGRESS_BACKGROUND = ClientConstants.Gui.ICONS.getSprite(10, 0, 10, 10);
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
  protected void drawGuiBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    defaultGui(stack);
    progressBar.draw(stack);
    energyBar.draw(stack);
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
}
