package com.wtbw.machines.gui.screen;


import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.gui.container.BlockPlacerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/*
  @author: Naxanria
*/
public class BlockPlacerScreen extends BaseContainerScreen<BlockPlacerContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation(WTBWMachines.MODID, "textures/gui/basic3x3with1butt.png");
  
  public BlockPlacerScreen(BlockPlacerContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    addButton(new RedstoneButton<>(guiLeft - 21 + 5, guiTop + 17, container.tileEntity));
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    GuiUtil.renderTexture(guiLeft - 21, guiTop, xSize + 21, ySize, 0, 0, 256, 256, GUI);
    String blockName = new TranslationTextComponent("block.wtbw_machines.block_placer").getUnformattedComponentText();
    this.font.drawString(blockName, guiLeft + 8, guiTop + 6, 0xff404040);
    this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), guiLeft + 8, guiTop + 73, 0xff404040);
  }
}
