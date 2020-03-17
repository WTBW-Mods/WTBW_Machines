package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseUpgradeScreen;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.gui.container.QuarryContainer;
import com.wtbw.mods.machines.tile.machine.QuarryTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/*
  @author: Sunekaer
*/
public class QuarryScreen extends BaseUpgradeScreen<QuarryContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation(WTBWMachines.MODID, "textures/gui/basic3x3with1butt.png");
  private EnergyBar energyBar;
  
  public QuarryScreen(QuarryContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    final QuarryTileEntity tileEntity = container.tileEntity;
    addButton(new RedstoneButton<>(guiLeft - 22, guiTop, tileEntity));
  
    final BaseEnergyStorage storage = tileEntity.getStorage();
    energyBar = getDefaultBar(storage);
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
  protected void drawGuiBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    int green = 0xff00B300;
    int textColor = 0xff404040;
  
    String mining = new TranslationTextComponent("wtbw_machines.gui.quarry.current_block").getUnformattedComponentText();
  
    defaultGui();
  
    energyBar.draw();
  
    int xp = guiLeft + 122;
    int yp = guiTop + 20;
  
    if (container.tileEntity.getCurrentPos() != null)
    {
      this.font.drawString(mining, xp, yp, textColor);
      if (container.tileEntity.getDone())
      {
        this.font.drawString("Done", xp, yp + 9, green);
      }
      else
      {
        this.font.drawString("X: " + this.container.tileEntity.getCurrentPos().getX(), xp, yp + 9, textColor);
        this.font.drawString("Y: " + this.container.tileEntity.getCurrentPos().getY(), xp, yp + 18, textColor);
        this.font.drawString("Z: " + this.container.tileEntity.getCurrentPos().getZ(), xp, yp + 27, textColor);
      }
    }
  }
}
