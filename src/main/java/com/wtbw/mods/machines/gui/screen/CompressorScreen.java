package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.screen.BaseUpgradeScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.CompressorContainer;
import com.wtbw.mods.machines.tile.machine.PoweredCompressorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Sunekaer
*/
public class CompressorScreen extends BaseUpgradeScreen<CompressorContainer>
{
  public static final Sprite PROGRESS_BACKGROUND_LEFT = ClientConstants.Gui.ICONS.getSprite(0, 20, 10, 10);
  public static final Sprite PROGRESS_BACKGROUND_RIGHT = PROGRESS_BACKGROUND_LEFT.getRight(10, 10);
  public static final Sprite PROGRESS_LEFT = PROGRESS_BACKGROUND_LEFT.getBelow(10, 10);
  public static final Sprite PROGRESS_RIGHT = PROGRESS_BACKGROUND_RIGHT.getBelow(10, 10);

  private ProgressBar progressBarLeft;
  private ProgressBar progressBarRight;
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
    progressBarLeft = new SpriteProgressBar(guiLeft + 175 / 2 - 8 , guiTop + 39, PROGRESS_LEFT, PROGRESS_BACKGROUND_LEFT, tileEntity::getDuration, tileEntity::getProgress)
            .setFillDirection(ProgressBar.FillDirection.LEFT_RIGHT).cast();
    progressBarRight = new SpriteProgressBar(guiLeft + 175 / 2 + 2 , guiTop + 39, PROGRESS_RIGHT, PROGRESS_BACKGROUND_RIGHT, tileEntity::getDuration, tileEntity::getProgress)
            .setFillDirection(ProgressBar.FillDirection.RIGHT_LEFT).cast();
    energyBar = new EnergyBar(storage, guiLeft + 12, guiTop + 15);
    addTooltipProvider(energyBar);
    redstoneButton = new RedstoneButton<>(guiLeft - 20, guiTop + 10, tileEntity);
  }
  
  @Override
  public void tick()
  {
    super.tick();
    if (progressBarLeft != null)
    {
      progressBarLeft.update();
    }

    if (progressBarRight != null)
    {
      progressBarRight.update();
    }
    
    if (energyBar != null)
    {
      energyBar.update();
    }
  }
  
  @Override
  protected void drawGuiBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    defaultGui(stack);
  
    progressBarLeft.draw(stack);
    progressBarRight.draw(stack);
    energyBar.draw(stack);
  }
}
