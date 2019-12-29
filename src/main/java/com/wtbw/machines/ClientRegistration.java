package com.wtbw.machines;

import com.wtbw.machines.gui.container.ModContainers;
import com.wtbw.machines.gui.screen.*;
import net.minecraft.client.gui.ScreenManager;

/*
  @author: Naxanria
*/
public class ClientRegistration
{
  public static void registerScreens()
  {
    ScreenManager.registerFactory(ModContainers.TIERED_FURNACE, TieredFurnaceScreen::new);

    ScreenManager.registerFactory(ModContainers.VACUUM_CHEST, VacuumChestScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_BREAKER, BlockBreakerScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_PLACER, BlockPlacerScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_DETECTOR, BlockDetectorScreen::new);
  }
}
