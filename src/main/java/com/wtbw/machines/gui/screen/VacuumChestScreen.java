package com.wtbw.machines.gui.screen;

import com.wtbw.lib.gui.screen.BaseContainerScreen;
import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.gui.container.VacuumChestContainer;
import com.wtbw.lib.gui.util.GuiUtil;
import com.wtbw.lib.gui.util.RedstoneButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class VacuumChestScreen extends BaseContainerScreen<VacuumChestContainer>
{
  public static final ResourceLocation GUI = new ResourceLocation(WTBWMachines.MODID, "textures/gui/vacuum_chest.png");
  
  public VacuumChestScreen(VacuumChestContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }
  
  @Override
  protected void init()
  {
    super.init();
    addButton(new RedstoneButton<>(guiLeft - 22 + 4, guiTop + 17, container.tileEntity));
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    GuiUtil.renderTexture(guiLeft - 22, guiTop, xSize + 22, ySize, 0, 0, 256, 256, GUI);
  }
}
