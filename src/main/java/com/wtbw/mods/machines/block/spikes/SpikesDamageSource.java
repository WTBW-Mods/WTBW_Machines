package com.wtbw.mods.machines.block.spikes;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class SpikesDamageSource extends DamageSource
{
  private SpikesType type;
  private Entity source = null;
  // todo: have an option for fakeplayer kills
  public SpikesDamageSource(SpikesType type)
  {
    super("spikes." + type.name);
    this.type = type;
  
    setDamageBypassesArmor();
    setDamageIsAbsolute();
  }
  
  @Nullable
  @Override
  public Entity getTrueSource()
  {
    return source;
  }
  
  public SpikesDamageSource setSource(Entity source)
  {
    this.source = source;
    return this;
  }
}
