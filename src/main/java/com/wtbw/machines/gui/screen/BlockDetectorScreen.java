package com.wtbw.machines.gui.screen;

import com.google.common.collect.ImmutableMap;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.ClickType;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.network.BufferHelper;
import com.wtbw.machines.gui.container.BlockDetectorContainer;
import com.wtbw.machines.tile.BlockDetectorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.Map;

/*
  @author: Naxanria
*/
public class BlockDetectorScreen extends BaseContainerScreen<BlockDetectorContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation("wtbw_machines:textures/gui/block_detector.png");
  
  private GuiButtonExt currentButton;
  private GuiButtonExt matchButton;
  
  public BlockDetectorScreen(BlockDetectorContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
  
    currentButton = addButton(new GuiButtonExt(guiLeft + 80, guiTop + 4, 50, 22, I18n.format("wtbw_machines.gui.current"),
      button -> GuiUtil.sendButton(BlockDetectorTileEntity.BUTTON_CURRENT, container.tileEntity.getPos(), ClickType.LEFT)));
    matchButton = addButton(new GuiButtonExt(guiLeft + 80 + 50 + 1, guiTop + 4, 40, 22, I18n.format("wtbw_machines.gui.exact"),
      button -> GuiUtil.sendButton(BlockDetectorTileEntity.BUTTON_MATCH, container.tileEntity.getPos(), ClickType.LEFT)));
    matchButton.setFGColor(container.tileEntity.isExactMatch() ? 0xff00ff00 : 0xffff0000);
  }
  
  @Override
  public void tick()
  {
    super.tick();
    if (matchButton != null)
    {
      matchButton.setFGColor(container.tileEntity.isExactMatch() ? 0xff00ff00 : 0xffff0000);
    }
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
//    GuiUtil.renderTexture();
//    fill(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xff888888);
//    drawString(font, "Target: " + container.tileEntity.getTarget().toString(), guiLeft + 10, guiTop + 10 + 22 + 1, 0xffffffff);
    GuiUtil.renderTexture(guiLeft, guiTop, xSize, xSize, 0, 0, 256, 256, GUI);
    drawBlockState(guiLeft + 10, guiTop + 35, container.tileEntity.getTarget());
    String blockName = new TranslationTextComponent("block.wtbw_machines.block_detector").getUnformattedComponentText();
    this.font.drawString(blockName, guiLeft + 5, guiTop + 6, 0xff404040);
//    drawString(font, container.tileEntity.getPower() + "", guiLeft + 10, guiTop + ySize - 10, 0xff8888FF);
  }
  
  private void drawBlockState(int x, int y, BlockState state)
  {
    Block block = state.getBlock();
    drawString(font, I18n.format(block.getTranslationKey()), x, y, 0xffffffff);
    ImmutableMap<IProperty<?>, Comparable<?>> values = state.getValues();
    int yp = y + 10;
    int xp = x + 10;
    for(Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet())
    {
      IProperty<?> property = entry.getKey();
      String name = property.getName();
      String value = BufferHelper.getName(property, entry.getValue());
      
      drawString(font, name + "=" + value, xp, yp, 0xffffffff);
      yp += 10;
    }
  }
}
