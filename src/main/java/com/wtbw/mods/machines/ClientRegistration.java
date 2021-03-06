package com.wtbw.mods.machines;

import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.container.ModContainers;
import com.wtbw.mods.machines.gui.screen.*;
import com.wtbw.mods.machines.render.RenderBattery;
import com.wtbw.mods.machines.tile.ModTiles;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class ClientRegistration
{
  public static void init()
  {
    registerScreens();
    registerRenderLayers();
    registerBind();
  }

  private static void registerBind() {
    ClientRegistry.bindTileEntityRenderer(ModTiles.SIMPLE_BATTERY, RenderBattery::new);
  }

  public static void registerScreens()
  {
    ScreenManager.registerFactory(ModContainers.TIERED_FURNACE, TieredFurnaceScreen::new);

    ScreenManager.registerFactory(ModContainers.VACUUM_CHEST, VacuumChestScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_BREAKER, BlockBreakerScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_PLACER, BlockPlacerScreen::new);
    ScreenManager.registerFactory(ModContainers.BLOCK_DETECTOR, BlockDetectorScreen::new);

    ScreenManager.registerFactory(ModContainers.QUARRY, QuarryScreen::new);
    ScreenManager.registerFactory(ModContainers.DEHYDRATOR, DehydratorScreen::new);
    ScreenManager.registerFactory(ModContainers.HYDRATOR, HydratorScreen::new);
    
    ScreenManager.registerFactory(ModContainers.SOLAR_PANEL, SolarPanelScreen::new);
    ScreenManager.registerFactory(ModContainers.BATTERY, BatteryScreen::new);
    ScreenManager.registerFactory(ModContainers.COMPRESSOR, CompressorScreen::new);
    ScreenManager.registerFactory(ModContainers.CRUSHER, CrusherScreen::new);
    ScreenManager.registerFactory(ModContainers.POWERED_FURNACE, PoweredFurnaceScreen::new);
    
    ScreenManager.registerFactory(ModContainers.FUEL_GENERATOR, FuelGeneratorScreen::new);
    
    ScreenManager.registerFactory(ModContainers.CHARGER, ChargerScreen::new);
    
    ScreenManager.registerFactory(ModContainers.XP_PYLON, XpPylonScreen::new);
    
    ScreenManager.registerFactory(ModContainers.ENERGY_INPUT_HATCH, EnergyInputHatchScreen::new);
    ScreenManager.registerFactory(ModContainers.FLUID_INPUT_HATCH, FluidInputHatchScreen::new);
    ScreenManager.registerFactory(ModContainers.ITEM_OUTPUT_HATCH, ItemOutputHatchScreen::new);
    
    ScreenManager.registerFactory(ModContainers.MICRO_MINER, MicroMinerScreen::new);
  }

  public static void registerRenderLayers()
  {
    RenderType cutout = RenderType.getCutoutMipped();
    
    RenderTypeLookup.setRenderLayer(ModBlocks.QUARRY, cutout);
    RenderTypeLookup.setRenderLayer(ModBlocks.TELEPORT_INHIBITOR, cutout);
    //RenderTypeLookup.setRenderLayer(ModBlocks.QUARRY_MARKER, cutout);
  }


}
