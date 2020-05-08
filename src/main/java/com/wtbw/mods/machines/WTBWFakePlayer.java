package com.wtbw.mods.machines;

import com.mojang.authlib.GameProfile;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;
import java.util.UUID;

/*
  @author: Naxanria
*/
@SuppressWarnings("EntityConstructor")
public class WTBWFakePlayer extends FakePlayer
{
  private final static GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes("wtbw.common".getBytes()), "[WTBW]");
  
  private static WTBWFakePlayer INSTANCE;
  
  private WTBWFakePlayer(ServerWorld world)
  {
    super(world, profile);
  }
  
  private static void checkInstance(ServerWorld world)
  {
    if (INSTANCE == null)
    {
      INSTANCE = new WTBWFakePlayer(world);
    }
  }
  
  public static WeakReference<WTBWFakePlayer> getInstance(ServerWorld world)
  {
    checkInstance(world);
    INSTANCE.world = world;
    return new WeakReference<>(INSTANCE);
  }
  
  public static WeakReference<WTBWFakePlayer> getInstance(ServerWorld world, double x, double y, double z)
  {
    checkInstance(world);
    INSTANCE.world = world;
    INSTANCE.setRawPosition(x, y, z);
    return new WeakReference<>(INSTANCE);
  }
  
  public static void invalidate(ServerWorld world)
  {
    if (INSTANCE != null && INSTANCE.world == world)
    {
      INSTANCE = null;
    }
  }
  
  @Override
  public boolean isPotionApplicable(EffectInstance potion)
  {
    return false;
  }
}
