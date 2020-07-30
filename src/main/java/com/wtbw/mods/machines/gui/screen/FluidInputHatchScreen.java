package com.wtbw.mods.machines.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.ClickType;
import com.wtbw.mods.lib.gui.util.FluidBar;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.gui.container.FluidInputHatchContainer;
import com.wtbw.mods.machines.tile.multi.FluidInputHatchTile;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

/*
  @author: Naxanria
*/
public class FluidInputHatchScreen extends BaseContainerScreen<FluidInputHatchContainer>
{
  public Sprite BUCKET_FILL_ICON = ClientConstants.Gui.ICONS.getSprite(0, 39, 11, 16);
  public Sprite BUCKET_ICON = ClientConstants.Gui.ICONS.getSprite(11, 39, 18, 18);
  private final FluidInputHatchTile hatch;
  private FluidBar fluidBar;
  private Button clearButton;
  
  public FluidInputHatchScreen(FluidInputHatchContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    
    hatch = container.tileEntity;
    
    setBackground(container.BUCKET_INPUT, BUCKET_ICON);
  }
  
  @Override
  protected void init()
  {
    super.init();
    
    fluidBar = new FluidBar(hatch.getTank(), guiLeft + 10, guiTop + 16);
    addTooltipProvider(fluidBar);
    
    String clearString = I18n.format(ClientConstants.Gui.getKey("clear_fluids"));
    int w = font.getStringWidth(clearString) + 4;
    clearButton = new Button(guiLeft + xSize - w - 4, guiTop + 60, w, 20, new TranslationTextComponent(clearString),
      p -> GuiUtil.sendButton(FluidInputHatchTile.BUTTON_CLEAR_FLUID, container.tileEntity.getPos(), ClickType.LEFT));
    
    addButton(clearButton);
  }
  
  @Override
  public void tick()
  {
    super.tick();
    
    if (fluidBar != null)
    {
      fluidBar.update();
    }
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    defaultGui(stack);
    if (fluidBar != null)
    {
      fluidBar.draw(stack);
    }

    BUCKET_FILL_ICON.render(stack, guiLeft + 40 + 2, guiTop + 17 + 18);
  }
}
