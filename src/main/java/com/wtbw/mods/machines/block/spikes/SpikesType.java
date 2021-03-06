package com.wtbw.mods.machines.block.spikes;

/*
  @author: Naxanria
*/
public class SpikesType
{
  public static final SpikesType WOODEN = new SpikesType("wooden", 0.5f, false, false, false);
  public static final SpikesType BAMBOO = new SpikesType("bamboo", 1f, false, false, false);
  public static final SpikesType IRON = new SpikesType("iron", 1.5f, true, false, false);
  public static final SpikesType GOLD = new SpikesType("gold", 2f, true, true, false);
  public static final SpikesType DIAMOND = new SpikesType("diamond", 2.5f, true, true, true);
  
  public final String name;
  public final float damage;
  public final boolean lethal;
  public final boolean isPlayer;
  public final boolean dropXP;
  
  public SpikesType(String name, float damage, boolean lethal, boolean dropXP, boolean isPlayer)
  {
    this.name = name;
    this.damage = damage;
    this.lethal = lethal;
    this.dropXP = dropXP;
    this.isPlayer = isPlayer;
  }
}
