package com.wtbw.mods.machines.gui.screen;


import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.RedstoneButton;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.gui.container.BlockPlacerContainer;
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
    addButton(new RedstoneButton<>(guiLeft - 22, guiTop + 17, container.tileEntity));
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
  }
}
