package com.wtbw.machines.gui.screen;


import com.wtbw.lib.gui.screen.BaseContainerScreen;
import com.wtbw.lib.gui.util.GuiUtil;
import com.wtbw.lib.gui.util.ProgressBar;
import com.wtbw.lib.gui.util.RedstoneButton;
import com.wtbw.lib.gui.util.TooltipRegion;
import com.wtbw.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.gui.container.BlockBreakerContainer;
import com.wtbw.machines.gui.container.QuarryContainer;
import com.wtbw.machines.tile.QuarryTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Sunekaer
*/
public class QuarryScreen extends BaseContainerScreen<QuarryContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation(WTBWMachines.MODID, "textures/gui/basic3x3with1butt.png");
  private ProgressBar energyBar;
  
  public QuarryScreen(QuarryContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    final QuarryTileEntity tileEntity = container.tileEntity;
    addButton(new RedstoneButton<>(guiLeft - 21 + 5, guiTop + 17, tileEntity));
    final BaseEnergyStorage storage = tileEntity.getStorage();
    energyBar = new ProgressBar(guiLeft + 10, guiTop + 16, 20, 54, storage::getMaxEnergyStored, storage::getEnergyStored)
    .setEmptyColor(0xffff0000).setFullColor(0xff00ff00).setGradient(true);
    TooltipRegion energyTooltip = new TooltipRegion(energyBar.getX(), energyBar.getY(), energyBar.getWidth(), energyBar.getHeight())
    {
      @Override
      public List<String> getTooltip()
      {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + " RF");
        return tooltip;
      }
    };
    addTooltipProvider(energyTooltip);
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
    String blockName = new TranslationTextComponent("block.wtbw_machines.quarry").getUnformattedComponentText();
    String mining = new TranslationTextComponent("wtbw_machines.quarry.gui.current_block").getUnformattedComponentText();

    GuiUtil.renderTexture(guiLeft - 21, guiTop, xSize + 21, ySize, 0, 0, 256, 256, GUI);
    this.font.drawString(blockName, guiLeft + 8, guiTop + 6, 4210752);
    this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), guiLeft + 8, guiTop + 73, 4210752);
    energyBar.draw();

    if (container.tileEntity.getCurrentPos() != null){
      this.font.drawString(mining,guiLeft + 122, guiTop + 20, 4210752);
    if (container.tileEntity.getDone()){
      this.font.drawString("Done",guiLeft + 122, guiTop + 29, 45824);
    } else {
      this.font.drawString("X: " + this.container.tileEntity.getCurrentPos().getX(),guiLeft + 122, guiTop + 29, 4210752);
      this.font.drawString("Y: " + this.container.tileEntity.getCurrentPos().getY(),guiLeft + 122, guiTop + 38, 4210752);
      this.font.drawString("Z: " + this.container.tileEntity.getCurrentPos().getZ(),guiLeft + 122, guiTop + 47, 4210752);
    }
    }
  }
}
