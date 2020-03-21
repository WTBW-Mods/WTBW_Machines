package com.wtbw.mods.machines;

import com.wtbw.mods.lib.Registrator;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.item.BaseItemProperties;
import com.wtbw.mods.lib.item.upgrade.BaseUpgradeItem;
import com.wtbw.mods.lib.upgrade.ModifierType;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.machines.block.*;
import com.wtbw.mods.machines.block.base.BaseMachineBlock;
import com.wtbw.mods.machines.block.base.TierBlock;
import com.wtbw.mods.machines.block.base.WrenchableSixWayTileBlock;
import com.wtbw.mods.machines.block.redstone.BlockDetectorBlock;
import com.wtbw.mods.machines.block.redstone.RedstoneEmitterBlock;
import com.wtbw.mods.machines.block.redstone.RedstoneTimerBlock;
import com.wtbw.mods.machines.block.spikes.SpikesBlock;
import com.wtbw.mods.machines.block.spikes.SpikesType;
import com.wtbw.mods.machines.block.util.WrenchHelper;
import com.wtbw.mods.machines.gui.container.*;
import com.wtbw.mods.machines.item.WrenchItem;
import com.wtbw.mods.machines.recipe.*;
import com.wtbw.mods.machines.tile.*;
import com.wtbw.mods.machines.tile.cables.EnergyCableTier;
import com.wtbw.mods.machines.tile.furnace.FurnaceTier;
import com.wtbw.mods.machines.tile.generator.FuelGeneratorEntity;
import com.wtbw.mods.machines.tile.generator.SolarPanelTileEntity;
import com.wtbw.mods.machines.tile.machine.DehydratorTileEntity;
import com.wtbw.mods.machines.tile.machine.PoweredCompressorEntity;
import com.wtbw.mods.machines.tile.machine.PoweredCrusherEntity;
import com.wtbw.mods.machines.tile.machine.PoweredFurnaceEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.Util;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;

/*
  @author: Naxanria
*/
public class MachinesRegistrator extends Registrator
{
  public MachinesRegistrator(ItemGroup group, String modid)
  {
    super(group, modid);
  }
  
  @Override
  protected void registerAllBlocks()
  {
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7), FurnaceTier.IRON), "iron_furnace");
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7), FurnaceTier.GOLD), "gold_furnace");
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7), FurnaceTier.DIAMOND), "diamond_furnace");
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7), FurnaceTier.END), "end_furnace");
  
    register(new RedstoneTimerBlock(getBlockProperties(Material.IRON, 4)), "redstone_timer");
    register(new RedstoneEmitterBlock(getBlockProperties(Material.IRON, 4)), "redstone_emitter");
    
    
    register(new WrenchableSixWayTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new BlockBreakerTileEntity()), "block_breaker");
    register(new WrenchableSixWayTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new BlockPlacerTileEntity()), "block_placer");
    
    register(new BlockDetectorBlock(getBlockProperties(Material.IRON, 4)), "block_detector");
  
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new VacuumChestTileEntity()), "vacuum_chest");

    register(new QuarryBlock(getBlockProperties(Material.IRON, 4).variableOpacity()), "quarry");
  
    register(new PushBlock(getBlockProperties(Material.IRON, 1), EntityPusherTileEntity.PushMode.PUSH), "pusher");
    register(new PushBlock(getBlockProperties(Material.IRON, 1), EntityPusherTileEntity.PushMode.PULL), "puller");
  
    register(new SpikesBlock(getBlockProperties(Material.ROCK, 3), SpikesType.BAMBOO), "bamboo_spikes");
    register(new SpikesBlock(getBlockProperties(Material.ROCK, 3), SpikesType.WOODEN), "wooden_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 4), SpikesType.IRON), "iron_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 5), SpikesType.GOLD), "gold_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 6), SpikesType.DIAMOND), "diamond_spikes");
    
    register(new SolarPanelBlock(getBlockProperties(Material.IRON, 3), SolarPanelTileEntity.MK1).comparator(), "solar_panel_mk1");
    register(new SolarPanelBlock(getBlockProperties(Material.IRON, 5), SolarPanelTileEntity.MK2).comparator(), "solar_panel_mk2");
    register(new SolarPanelBlock(getBlockProperties(Material.IRON, 7), SolarPanelTileEntity.MK3).comparator(), "solar_panel_mk3");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 3), (world, state) -> new SimpleBatteryTileEntity()).comparator(), "simple_battery");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 5), (world, state) -> new DehydratorTileEntity()).mirrored(), "dehydrator");
    
    register(new Block(getBlockProperties(Material.IRON, 3)), "machine_block");
    
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 1), "tier1_upgrade");
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 2), "tier2_upgrade");
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 3), "tier3_upgrade");
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 4), "tier4_upgrade");

    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new PoweredFurnaceEntity()).mirrored(), "powered_furnace");
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new PoweredCrusherEntity()).mirrored(), "crusher");
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new PoweredCompressorEntity()).mirrored(), "compressor");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new FuelGeneratorEntity()).mirrored(), "fuel_generator",
      getItemProperties().addTooltip(TextComponentBuilder.createTranslated(WTBWMachines.MODID + ".tooltip.fuel_generator", 45).green().build()));
    
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).variableOpacity(), EnergyCableTier.MK1), "energy_cable_mk1");
    /*
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK2), "energy_cable_mk2");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK3), "energy_cable_mk3");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK4), "energy_cable_mk4");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK5), "energy_cable_mk5");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK6), "energy_cable_mk6");
    */
    
    register(new BaseTileBlock<TeleportInhibitorTile>(getBlockProperties(Material.IRON, 4), (world, state) -> new TeleportInhibitorTile()), "teleport_inhibitor");
  }
  
  @Override
  protected void registerAllItems()
  {
    BaseItemProperties baseProperties = getItemProperties();
    register(new Item(baseProperties), "iron_plate");
    register(new Item(baseProperties), "gold_plate");
    
    register(new Item(baseProperties), "gold_dust");
    register(new Item(baseProperties), "iron_dust");
    register(new Item(baseProperties), "obsidian_dust");
    register(new Item(baseProperties), "ender_pearl_dust");
    register(new Item(baseProperties), "charcoal_dust");
    register(new Item(baseProperties), "coal_dust");
    register(new Item(baseProperties), "emerald_dust");
    register(new Item(baseProperties), "diamond_dust");
    register(new Item(baseProperties), "quartz_dust");
    register(new Item(baseProperties), "copper_dust");
    register(new Item(baseProperties), "cobalt_dust");
    
    register(new Item(baseProperties), "lapis_wafer");
    register(new Item(baseProperties), "glowstone_wafer");
    
    register(new WrenchItem(getItemProperties()), "wrench");
  
    register(new Item(baseProperties), "base_upgrade");
    
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.SPEED, 1.15f);
      map.put(ModifierType.POWER_USAGE, 1.25f);
    })), "speed_upgrade_mk1");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.SPEED, 1.25f);
      map.put(ModifierType.POWER_USAGE, 1.60f);
    })), "speed_upgrade_mk2");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.SPEED, 1.7f);
      map.put(ModifierType.POWER_USAGE, 2f);
    })), "speed_upgrade_mk3");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.POWER_USAGE, 0.87f);
    })), "power_usage_upgrade_mk1");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.POWER_USAGE, 0.79f);
    })), "power_usage_upgrade_mk2");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.POWER_USAGE, 0.40f);
    })), "power_usage_upgrade_mk3");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.POWER_CAPACITY, 1.10f);
    })), "power_capacity_upgrade_mk1");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.POWER_CAPACITY, 1.25f);
    })), "power_capacity_upgrade_mk2");
  
    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
    {
      map.put(ModifierType.POWER_CAPACITY, 1.5f);
    })), "power_capacity_upgrade_mk3");
  
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.RANGE, 3f);
//      map.put(ModifierType.POWER_USAGE, 1.1f);
//      map.put(ModifierType.SPEED, 0.9f);
//    })), "range_upgrade_mk1");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.RANGE, 6f);
//      map.put(ModifierType.POWER_USAGE, 1.5f);
//      map.put(ModifierType.SPEED, 0.9f);
//    })), "range_upgrade_mk2");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.RANGE, 10f);
//      map.put(ModifierType.POWER_USAGE, 2f);
//      map.put(ModifierType.SPEED, 0.9f);
//    })), "range_upgrade_mk3");
  }
 
  @Override
  protected void registerAllContainers()
  {
    registerContainer(TieredFurnaceContainer::new, "tiered_furnace");
    registerContainer(VacuumChestContainer::new, "vacuum_chest");
    registerContainer(BlockBreakerContainer::new, "block_breaker");
    registerContainer(BlockPlacerContainer::new, "block_placer");
    registerContainer(BlockDetectorContainer::new, "block_detector");
    registerContainer(QuarryContainer::new, "quarry");
    registerContainer(DehydratorContainer::new, "dehydrator");
    registerContainer(SolarPanelContainer::new, "solar_panel");
    registerContainer(BatteryContainer::new, "battery");
    registerContainer(CompressorContainer::new, "compressor");
    registerContainer(CrusherContainer::new, "crusher");
    registerContainer(PoweredFurnaceContainer::new, "powered_furnace");
    registerContainer(FuelGeneratorContainer::new, "fuel_generator");
  }
  
  public void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event)
  {
    ModRecipes.init();
  
    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
  
    registry.register(DehydratingRecipe.SERIALIZER);
    registry.register(CompressingRecipe.SERIALIZER);
    registry.register(CrushingRecipe.SERIALIZER);
    registry.register(PoweredFurnaceRecipe.SERIALIZER);
  }
  
  public void registerWrenchActions()
  {
    WrenchItem.registerWrenchAction(ModBlocks.BLOCK_BREAKER, WrenchHelper.rotationWrenchAction(WrenchableSixWayTileBlock.FACING).and(WrenchHelper.dropWrenchAction()));
    WrenchItem.registerWrenchAction(ModBlocks.BLOCK_PLACER, WrenchHelper.rotationWrenchAction(WrenchableSixWayTileBlock.FACING).and(WrenchHelper.dropWrenchAction()));
    WrenchItem.registerWrenchAction(ModBlocks.BLOCK_DETECTOR, WrenchHelper.rotationWrenchAction(WrenchableSixWayTileBlock.FACING).and(WrenchHelper.dropWrenchAction()));
    WrenchItem.registerWrenchAction(ModBlocks.VACUUM_CHEST, WrenchHelper.dropWrenchAction());
  }
}
