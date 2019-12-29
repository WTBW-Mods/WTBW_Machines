package com.wtbw.machines.gui.screen;


import com.wtbw.lib.gui.screen.BaseContainerScreen;
import com.wtbw.lib.gui.util.GuiUtil;
import com.wtbw.lib.gui.util.RedstoneButton;
import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.gui.container.BlockBreakerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class BlockBreakerScreen extends BaseContainerScreen<BlockBreakerContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation(WTBWMachines.MODID, "textures/gui/basic3x3with1butt.png");
  
  public BlockBreakerScreen(BlockBreakerContainer container, PlayerInventory inventory, ITextComponent title)
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
  }
}
