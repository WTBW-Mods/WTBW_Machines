package com.wtbw.machines;

import com.wtbw.machines.block.ModBlocks;
import com.wtbw.machines.gui.container.ModContainers;
import com.wtbw.machines.gui.container.QuarryContainer;
import com.wtbw.machines.gui.screen.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

/*
  @author: Naxanria
*/
public class ClientRegistration
{
  public static void init(){
    registerScreens();
    regiserRenderlayers();
  }
  public static void registerScreens()
  {
    ScreenManager.registerFactory(ModContainers.TIERED_FURNACE, TieredFurnaceScreen::new);

    ScreenManager.registerFactory(ModContainers.VACUUM_CHEST, VacuumChestScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_BREAKER, BlockBreakerScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_PLACER, BlockPlacerScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_DETECTOR, BlockDetectorScreen::new);

    ScreenManager.registerFactory(ModContainers.QUARRY, QuarryScreen::new);
  }

  public static void regiserRenderlayers()
  {
    RenderType cutout = RenderType.func_228643_e_();
    RenderTypeLookup.setRenderLayer(ModBlocks.QUARRY, cutout);
  }
}
