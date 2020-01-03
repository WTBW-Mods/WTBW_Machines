package com.wtbw.machines.gui.screen;


import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.machines.gui.container.TieredFurnaceContainer;
import com.wtbw.machines.tile.furnace.BaseFurnaceTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/*
  @author: Naxanria
*/
public class TieredFurnaceScreen extends BaseContainerScreen<TieredFurnaceContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
  
  private final BaseFurnaceTileEntity furnace;
  
  public TieredFurnaceScreen(TieredFurnaceContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    
    furnace = container.tileEntity;
    
    
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
//    GlStateManager.color4f(1, 1, 1, 1);
//    minecraft.getTextureManager().bindTexture(GUI);
    int xp = guiLeft;
    int yp = guiTop;
    
//    blit(xp, yp, 0, 0, xSize, ySize);
    GuiUtil.renderTexture(xp, yp, xSize, ySize, 0, 0, 256, 256, GUI);
    if (furnace.isBurning())
    {
      float progress = furnace.getBurnTime() / (float) furnace.getBurnTimeTotal();
      int l = (int) (progress * 14);
      GuiUtil.renderTexture(xp + 56, yp + 36 + 12 - l, 14, l + 1, 176, 12 - l, 256, 256, GUI);
//      blit(xp + 56, yp + 36 + 12 - l, 176, 12 - l, 14, l + 1);
    }
    
    float progress = furnace.getCookTime() / (float) furnace.getCookTimeTotal();
    int l = (int) (progress * 24);
    GuiUtil.renderTexture(xp + 79, yp + 34, l + 1, 16, 176, 14, 256, 256, GUI);
//    this.blit(xp + 79, yp + 34, 176, 14, l + 1, 16);
    String blockName = new TranslationTextComponent("block.wtbw_machines." + this.furnace.getDisplayName().getFormattedText()).getUnformattedComponentText();
    this.font.drawString(blockName, guiLeft + 8, guiTop + 6, 0xff404040);
    this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), guiLeft + 8, guiTop + 73, 0xff404040);
  }
}
