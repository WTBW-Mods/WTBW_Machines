package com.wtbw.machines;

import com.wtbw.lib.network.Networking;
import com.wtbw.machines.block.ModBlocks;
import com.wtbw.machines.config.CommonConfig;
import com.wtbw.machines.network.UpdateDetectorPacket;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
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
    new MachinesRegistrator(GROUP, MODID);
    
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
  }
  
  private void setup(final FMLCommonSetupEvent event)
  {
    Networking.registerMessage(UpdateDetectorPacket.class, UpdateDetectorPacket::toBytes, UpdateDetectorPacket::new, UpdateDetectorPacket::handle);
  
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientRegistration.registerScreens());
  }
}
