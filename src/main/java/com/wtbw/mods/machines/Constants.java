package com.wtbw.mods.machines;

import net.minecraft.util.ResourceLocation;

/*
  @author: Naxanria
*/
public class Constants
{
  public static ResourceLocation getLocation(String key)
  {
    return new ResourceLocation(WTBWMachines.MODID, key);
  }
}
