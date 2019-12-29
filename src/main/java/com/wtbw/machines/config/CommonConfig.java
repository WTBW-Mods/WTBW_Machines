package com.wtbw.machines.config;

import com.wtbw.lib.config.BaseConfig;
import com.wtbw.lib.config.SubConfig;
import com.wtbw.machines.WTBWMachines;
import com.wtbw.machines.tile.furnace.FurnaceTier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

/*
  @author: Naxanria
*/
public class CommonConfig extends BaseConfig
{
  private static CommonConfig instance;
  public static CommonConfig instance()
  {
    return instance;
  }
  
  public static void init()
  {
    final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    instance = specPair.getLeft();
    
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
  }
  
  public ForgeConfigSpec.IntValue vacuumRange;
  public ForgeConfigSpec.IntValue vacuumTickRate;
  
  public ForgeConfigSpec.IntValue pusherRange;
  public ForgeConfigSpec.DoubleValue pusherStrength;
  public ForgeConfigSpec.IntValue pusherTickRate;
  
  // furnaces //
  public FurnaceConfig ironFurnace;
  public FurnaceConfig goldFurnace;
  public FurnaceConfig diamondFurnace;
  public FurnaceConfig endFurnace;
  
  // redstone //
  public ForgeConfigSpec.IntValue redstoneClockRepeat;
  public ForgeConfigSpec.IntValue redstoneClockDuration;
  
  private CommonConfig(ForgeConfigSpec.Builder builder)
  {
    super(WTBWMachines.MODID, builder);
    builder.comment("WTBW common config");
    instance = this;

    blocks();
  }
  
  public void reload()
  {
    ironFurnace.reload();
    goldFurnace.reload();
    diamondFurnace.reload();
    endFurnace.reload();
  }
  
  private void blocks()
  {
    push("blocks");
    
    furnaces();
    redstone();
    vacuumChest();
    puller();
    
    pop();
  }
  
  private void puller()
  {
    push("pusher_puller").comment("For both pusher and puller");
    
    pusherStrength = builder
      .comment("Strength of pull/push", "default: 0.8")
      .translation(key("pusher.strength"))
      .defineInRange("strength", 0.8, 0.01, 5);
    
    pusherRange = builder
      .comment("Range of pusher/puller", "default: 6")
      .translation(key("pusher.range"))
      .defineInRange("range", 6, 1, 16);
    
    pusherTickRate = builder
      .comment("Ticks between pulls/pushes", "default: 10")
      .translation(key("pusher.tickRate"))
      .defineInRange("tick_rate", 10, 1, 100);
    
    pop();
  }
  
  private void vacuumChest()
  {
    push("vacuum");
    
    vacuumTickRate = builder
      .comment("The time in ticks between trying to suck up items", "default: 10")
      .translation(key("blocks.vacuum.tick_rate"))
      .defineInRange("tick_rate", 10, 1, 100);
    
    vacuumRange = builder
      .comment("The radius in which the vacuum can check", "default: 6")
      .translation(key("blocks.vacuum.range"))
      .defineInRange("range", 6, 1, 16);
    
    pop();
  }
  
  
  private void redstone()
  {
    push("redstone");
    
    push("timer");
    
    redstoneClockRepeat = builder
      .comment("The time in ticks for the delay between pulses")
      .translation(key("redstone.redstone_timer_repeat"))
      .defineInRange("repeat", 10, 5, 100);
    
    redstoneClockDuration = builder
      .comment("The duration of the redstone pulse for the timer")
      .translation(key("redstone.redstone_timer_length"))
      .defineInRange("duration", 6, 1, 100);
    
    pop();
    
    pop();
  }
  
  private void furnaces()
  {
    push("furnace");
    
    ironFurnace = new FurnaceConfig(FurnaceTier.IRON, builder);
    goldFurnace = new FurnaceConfig(FurnaceTier.GOLD, builder);
    diamondFurnace = new FurnaceConfig(FurnaceTier.DIAMOND, builder);
    endFurnace = new FurnaceConfig(FurnaceTier.END, builder);
    
    pop();
  }
  
  public static class FurnaceConfig extends SubConfig
  {
    public final FurnaceTier tier;
    public final String name;
    public ForgeConfigSpec.IntValue speed;
    
    public FurnaceConfig(FurnaceTier tier, ForgeConfigSpec.Builder builder)
    {
      super(builder);
      this.tier = tier;
      this.name = tier.name;
      init();
    }
    
    @Override
    protected void init()
    {
      push(name);
      
      CommonConfig parent = CommonConfig.instance();
      
      speed = builder
        .comment(name + " furnace smelting speed, in how many ticks it takes to smelt")
        .translation(parent.key("furnace." + name + "_furnace_speed"))
        .defineInRange("speed", tier.getCookTime(), 1, 500);
      
      pop();
    }
    
    @Override
    public void reload()
    {
      tier.setCookTime(speed.get());
    }
  }
}
