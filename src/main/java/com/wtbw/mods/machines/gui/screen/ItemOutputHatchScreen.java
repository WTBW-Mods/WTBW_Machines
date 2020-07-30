package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.ClickType;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.ITooltipProvider;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.ItemOutputHatchContainer;
import com.wtbw.mods.machines.tile.multi.ItemOutputHatchTile;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
  @author: Naxanria
*/
public class ItemOutputHatchScreen extends BaseContainerScreen<ItemOutputHatchContainer>
{
  private Button autoEjectButton;
  
  public ItemOutputHatchScreen(ItemOutputHatchContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  public void tick()
  {
    super.tick();
    setAutoEjectButtonColor();
  }
  
  @Override
  protected void init()
  {
    super.init();
    autoEjectButton = new Button(guiLeft + 3, guiTop + 23, 20, 20, new StringTextComponent("E"),
      p -> GuiUtil.sendButton(ItemOutputHatchTile.EJECT_TOGGLE_BUTTON, container.tileEntity.getPos(), ClickType.LEFT));
    setAutoEjectButtonColor();
    addButton(autoEjectButton);
    
    ITooltipProvider tooltip = new ITooltipProvider()
    {
      @Override
      public boolean isHover(int mouseX, int mouseY)
      {
        return autoEjectButton.isMouseOver(mouseX, mouseY);
      }
  
      @Override
      public List<String> getTooltip()
      {
        return Collections.singletonList(I18n.format(ClientConstants.getTooltipKey("auto_eject_" + (container.tileEntity.isAutoEject() ? "on" : "off"))));
      }
    };
    addTooltipProvider(tooltip);
  }
  
  private void setAutoEjectButtonColor()
  {
    if (autoEjectButton != null)
    {
      autoEjectButton.setFGColor(container.tileEntity.isAutoEject() ? 0xff00aa00 : 0xffaa0000);
    }
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    defaultGui(stack);
  }
}
