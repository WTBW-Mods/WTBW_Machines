package com.wtbw.mods.machines.gui.screen;

import com.google.common.base.Strings;
import com.wtbw.mods.lib.gui.screen.BaseContainerScreen;
import com.wtbw.mods.machines.gui.container.MicroMinerContainer;
import com.wtbw.mods.machines.tile.micro_miner.MicroMinerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

/*
  @author: Naxanria
*/
public class MicroMinerScreen extends BaseContainerScreen<MicroMinerContainer>
{
  private final MicroMinerTile microMiner;
  private List<String> info;
  private int tick = 0;
  
  public MicroMinerScreen(MicroMinerContainer container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    microMiner = container.tileEntity;
  }
  
  @Override
  public void tick()
  {
    super.tick();
    info = microMiner.getInfo();
    tick++;
  }
  
  @Override
  protected void init()
  {
    super.init();
    info = Collections.emptyList();
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    defaultGui();
    
    int screenX = guiLeft + 10;
    int screenY = guiTop + 15;
    int screenW = xSize - 20;
    int screenH = 84 - 32;
    int screenR = screenX + screenW;
    int screenB = screenY + screenH;
    
    drawRect(screenX, screenY, screenW, screenH, 0xff000000);
  
    
    if (info.size() == 0)
    {
      drawStringNoShadow("Loading" + Strings.repeat(".", (tick / 10) % 4), screenX + 2, screenY + 2, 0xff00cc00);
    }
    else
    {
      int x = screenX + 2;
      int y = screenY + 2;
      int ySpacing = font.FONT_HEIGHT + 2;
      for (String line : info)
      {
        drawStringNoShadow(line, x, y, 0xff00cc00);
        y += ySpacing;
      }
    }
  }
}
