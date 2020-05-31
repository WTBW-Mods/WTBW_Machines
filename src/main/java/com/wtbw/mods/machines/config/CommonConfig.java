package com.wtbw.mods.machines.config;

import com.wtbw.mods.lib.config.BaseConfig;
import com.wtbw.mods.lib.config.SubConfig;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.tile.furnace.FurnaceTier;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
    
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
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
  
  // quarry //
  public ForgeConfigSpec.IntValue quarryMaxSize;
  public ForgeConfigSpec.IntValue quarrySpeed;
  public ForgeConfigSpec.IntValue quarryPowerUsage;
  public ForgeConfigSpec.IntValue quarryCapacity;
  public ForgeConfigSpec.BooleanValue quarryBreakTileEntities;
  public ForgeConfigSpec.IntValue quarryDefaultSize;
  public ForgeConfigSpec.IntValue quarryTier1;
  public ForgeConfigSpec.IntValue quarryTier2;
  public ForgeConfigSpec.IntValue quarryTier3;
  public ForgeConfigSpec.IntValue quarryTier4;
  
  public ForgeConfigSpec.IntValue itemOutputHatchAutoEjectAmount;
  public ForgeConfigSpec.IntValue itemOutputHatchAutoEjectDelay;
  
  public ForgeConfigSpec.BooleanValue microMinerUseDurability;
  public ForgeConfigSpec.BooleanValue microMinerConsume;
  
  // general blacklist for breaking blocks //
  public ForgeConfigSpec.ConfigValue<List<? extends String>> blockBreakBlacklist;
  
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
    
    List<String> defaultList = Utilities.listOf("minecraft:bedrock", "minecraft:end_portal_frame", "minecraft:end_portal", "minecraft:nether_portal", "minecraft:barrier_block");
    blockBreakBlacklist = builder
      .comment("The black list of blocks that can not be broken by machines (e.g. BlockBreaker, Quarry)")
      .translation(key("blocks.block_break_blacklist"))
      .defineList("blockBreakBlacklist",
        defaultList,
        o -> o instanceof String && isResourceLocation((String) o)
      );
    
    furnaces();
    redstone();
    vacuumChest();
    puller();
    quarry();
    
    multiBlock();
    
    pop();
  }
  
  private void multiBlock()
  {
    push("multi").comment("Multi block configuration");
    
    itemOutputHatchAutoEjectAmount = builder
      .comment("The amount to eject automatically per cycle.", "Default: 16")
      .translation(key("blocks.multi.item_eject_amount"))
      .defineInRange("item_eject_amount", 16, 1, 128);
    
    itemOutputHatchAutoEjectDelay = builder
      .comment("The delay in ticks between trying to auto eject", "Default: 8")
      .translation(key("blocks.multi.item_eject_delay"))
      .defineInRange("item_eject_delay", 8, 1, 60);
    
    miner();
    
    pop();
  }
  
  private void miner()
  {
    push("miner").comment("Micro miner config");
    
    microMinerUseDurability = builder
      .comment("Should the miner attempt to damage the drill item?", "Default: true")
      .translation(key("blocks.multi.miner.durability"))
      .define("durability", true);
    
    microMinerConsume = builder
      .comment("Should the miner consume the item if it has no durability, or not set to using durability?", "Default: true")
      .translation(key("blocks.multi.miner.consume"))
      .define("consume", true);
    
    pop();
  }
  
  private void quarry()
  {
    push("quarry");
    
    quarryMaxSize = builder
      .comment("The maximum size of the quarry", "default:  128")
      .translation(key("blocks.quarry.max_size"))
      .defineInRange("max_size", 128, 16, 1024);
    
    quarryPowerUsage = builder
      .comment("The power it uses to break a block", "default: 1000")
      .translation(key("blocks.quarry.power_usage"))
      .defineInRange("power_usage", 1000, 1, Integer.MAX_VALUE);
    
    quarryCapacity = builder
      .comment("The capacity of the quarry's battery", "always at least as big as the power usage", "default: 1000000")
      .translation(key("blocks.quarry.capacity"))
      .defineInRange("capacity", 1000000, 1, Integer.MAX_VALUE);
    
    quarrySpeed = builder
      .comment("The time in ticks between breaking a block", "default: 15")
      .translation(key("blocks.quarry.speed"))
      .defineInRange("speed", 15, 1, 1000);
    
    quarryBreakTileEntities = builder
      .comment("If the quarry can break tile entities (like chests and spawners)", "default: false")
      .translation(key("blocks.quarry.break_tiles"))
      .define("break_tiles", false);

    quarryDefaultSize = builder
            .comment("Quarry's default size", "default: 1")
            .translation(key("blocks.quarry.power_usage"))
            .defineInRange("blocks.quarry.default_size", 1, 1, Integer.MAX_VALUE);

    quarryTier1 = builder
            .comment("Tier 1 upgrade size ", "default: 3")
            .translation(key("blocks.quarry.power_usage"))
            .defineInRange("blocks.quarry.tier1", 3, 1, Integer.MAX_VALUE);

    quarryTier2 = builder
            .comment("Tier 2 upgrade size", "default: 9")
            .translation(key("blocks.quarry.power_usage"))
            .defineInRange("blocks.quarry.tier2", 9, 1, Integer.MAX_VALUE);

    quarryTier3 = builder
            .comment("Tier 3 upgrade size", "default: 17")
            .translation(key("blocks.quarry.power_usage"))
            .defineInRange("blocks.quarry.tier3", 17, 1, Integer.MAX_VALUE);

    quarryTier4 = builder
            .comment("Tier 4 upgrade size", "default: 33")
            .translation(key("blocks.quarry.power_usage"))
            .defineInRange("blocks.quarry.tier4", 33, 1, Integer.MAX_VALUE);
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
  
  public static boolean isInBlacklist(@Nonnull Block block)
  {
    String id = block.getRegistryName().toString();
    return instance.blockBreakBlacklist.get().contains(id);
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
