package com.wtbw.mods.machines.gui.screen;

import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.lib.network.Networking;
import com.wtbw.mods.machines.gui.container.XpPylonContainer;
import com.wtbw.mods.machines.network.TransferXpPacket;
import com.wtbw.mods.machines.tile.XpPylonTile;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public class XpPylonScreen extends BaseContainerScreen<XpPylonContainer>
{
  private int xp;
  private int levels;
  private int xpLeftOver;
  
  private Button insert, extract;
  
  private final XpPylonTile pylon;
  
  public XpPylonScreen(XpPylonContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    pylon = (XpPylonTile) tileEntity;
  }
  
  @Override
  protected void init()
  {
    super.init();
    
    addButton(insert = new Button(guiLeft + 10, guiTop + 50, 24, 20, "/\\", press -> insert()));
    addButton(extract = new Button(guiLeft + xSize - 10 - 24, guiTop + 50, 24, 20, "\\/", press -> extract()));
  }
  
  private int amount()
  {
    return Screen.hasShiftDown() ? 10 : Screen.hasControlDown() ? 5 : 1;
  }
  
  private void extract()
  {
    transfer(amount());
  }
  
  private void insert()
  {
    transfer(-amount());
  }
  
  private void transfer(int amount)
  {
    Networking.INSTANCE.sendToServer(new TransferXpPacket(tileEntity.getPos(), amount));
  }
  
  @Override
  public void tick()
  {
    super.tick();
    
    if (xp != pylon.getXp())
    {
      xp = pylon.getXp();
      xpLeftOver = xp;
      levels = pylon.getLevels();
      for (int i = 0; i < levels; i++)
      {
        xpLeftOver -= pylon.xpBarCap(i);
      }
    }
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    
    drawString(font, "XP: " + xp + " levels: " + levels + " xpLeft: " + xpLeftOver, guiLeft + 20, guiTop + ySize - 20, 0xff808080);
  }
}
