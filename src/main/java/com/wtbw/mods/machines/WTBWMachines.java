package com.wtbw.mods.machines;

import com.wtbw.mods.lib.network.Networking;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.config.CommonConfig;
import com.wtbw.mods.machines.network.SyncBatteryBlockBar;
import com.wtbw.mods.machines.network.UpdateDetectorPacket;
import com.wtbw.mods.machines.network.UpdateQuarryPacket;
import com.wtbw.mods.machines.tile.TeleportInhibitorTile;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
  @author: Sunekaer
*/
@SuppressWarnings("Convert2MethodRef")
@Mod(WTBWMachines.MODID)
public class WTBWMachines
{
  public static final String MODID = "wtbw_machines";
  
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  private MachinesRegistrator machinesRegistrator;
  public static final ItemGroup GROUP = new ItemGroup(MODID)
  {
    @Override
    public ItemStack createIcon()
    {
      return new ItemStack(ModBlocks.REDSTONE_TIMER);
    }
  };
  
  public WTBWMachines()
  {
    CommonConfig.init();
    
    machinesRegistrator = new MachinesRegistrator(GROUP, MODID);
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setup);
    
    eventBus.addGenericListener(IRecipeSerializer.class, machinesRegistrator::registerRecipes);
  
    eventBus = MinecraftForge.EVENT_BUS;
    eventBus.addListener(TeleportInhibitorTile::onTeleport);
  }
  
  private void setup(final FMLCommonSetupEvent event)
  {
    Networking.registerMessage(UpdateDetectorPacket.class, UpdateDetectorPacket::toBytes, UpdateDetectorPacket::new, UpdateDetectorPacket::handle);
    Networking.registerMessage(UpdateQuarryPacket.class, UpdateQuarryPacket::toBytes, UpdateQuarryPacket::new, UpdateQuarryPacket::handle);
    Networking.registerMessage(SyncBatteryBlockBar.class, SyncBatteryBlockBar::toBytes, SyncBatteryBlockBar::new, SyncBatteryBlockBar::handle);
    
    machinesRegistrator.registerWrenchActions();
    
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientRegistration.init());
  }
}
