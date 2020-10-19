package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.screen.BaseUpgradeScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.gui.container.PoweredFurnaceContainer;
import com.wtbw.mods.machines.tile.machine.PoweredFurnaceEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Sunekaer
*/
public class PoweredFurnaceScreen extends BaseContainerScreen<PoweredFurnaceContainer>
{
  public static final SpriteMap ICONS = new SpriteMap(256, new ResourceLocation(WTBWMachines.MODID, "textures/gui/icons.png"));
  public static final Sprite PROGRESS_BACKGROUND = ICONS.getSprite(20, 0, 14, 14);
  public static final Sprite PROGRESS = PROGRESS_BACKGROUND.getBelow(14, 14);

  private SpriteProgressBar progressBar;
  private EnergyBar energyBar;

  private RedstoneButton<PoweredFurnaceEntity> redstoneButton;

  public PoweredFurnaceScreen(PoweredFurnaceContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    PoweredFurnaceEntity tileEntity = container.tileEntity;
    BaseEnergyStorage storage = tileEntity.getStorage();
    progressBar = new SpriteProgressBar(guiLeft + 175 / 2 - 5 , guiTop + 37
            , PROGRESS, PROGRESS_BACKGROUND, tileEntity::getDuration, tileEntity::getProgress)
      .setFillDirection(ProgressBar.FillDirection.BOTTOM_TOP).cast();
    energyBar = new EnergyBar(storage, guiLeft + 12, guiTop + 15);
    addTooltipProvider(energyBar);
    redstoneButton = new RedstoneButton<>(guiLeft - 20, guiTop + 10, tileEntity);
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y)
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
