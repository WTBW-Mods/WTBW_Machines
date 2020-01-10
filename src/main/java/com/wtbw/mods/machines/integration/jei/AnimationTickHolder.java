package com.wtbw.mods.machines.integration.jei;

import net.minecraft.client.Minecraft;

/*
  @author: Sunekaer
*/
public class AnimationTickHolder
{
  public static int ticks;
  
  public static void tick()
  {
    ticks++;
  }
  
  public static int getTicks()
  {
    return ticks;
  }
  
  public static int getSecondTicks()
  {
    return ticks % 20;
  }
  
  public static float getRenderTick()
  {
    return (ticks + Minecraft.getInstance().getRenderPartialTicks()) / 20;
  }
}
