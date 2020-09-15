package com.wtbw.mods.machines;

import com.wtbw.mods.lib.Registrator;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.item.BaseItemProperties;
import com.wtbw.mods.lib.item.BatteryItem;
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
import com.wtbw.mods.machines.tile.machine.*;
import com.wtbw.mods.machines.tile.micro_miner.MicroMinerTile;
import com.wtbw.mods.machines.tile.multi.EnergyInputHatchTile;
import com.wtbw.mods.machines.tile.multi.FluidInputHatchTile;
import com.wtbw.mods.machines.tile.multi.ItemOutputHatchTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

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
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7).setLightLevel(BaseFurnaceBlock::getLightValue), FurnaceTier.IRON), "iron_furnace");
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7).setLightLevel(BaseFurnaceBlock::getLightValue), FurnaceTier.GOLD), "gold_furnace");
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7).setLightLevel(BaseFurnaceBlock::getLightValue), FurnaceTier.DIAMOND), "diamond_furnace");
    register(new TieredFurnaceBlock(getBlockProperties(Material.IRON, 7).setLightLevel(BaseFurnaceBlock::getLightValue), FurnaceTier.END), "end_furnace");
  
    register(new RedstoneTimerBlock(getBlockProperties(Material.IRON, 4)), "redstone_timer");
    register(new RedstoneEmitterBlock(getBlockProperties(Material.IRON, 4)), "redstone_emitter");
    
    
    register(new WrenchableSixWayTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new BlockBreakerTileEntity()), "block_breaker");
    register(new WrenchableSixWayTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new BlockPlacerTileEntity()), "block_placer");
    
    register(new BlockDetectorBlock(getBlockProperties(Material.IRON, 4)), "block_detector");
  
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new VacuumChestTileEntity()), "vacuum_chest");

    register(new QuarryBlock(getBlockProperties(Material.IRON, 4).notSolid()), "quarry");
  
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
    register(new SolarPanelBlock(getBlockProperties(Material.IRON, 9), SolarPanelTileEntity.MK4).comparator(), "solar_panel_mk4");
    register(new SolarPanelBlock(getBlockProperties(Material.IRON, 11), SolarPanelTileEntity.MK5).comparator(), "solar_panel_mk5");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 3), (world, state) -> new SimpleBatteryTileEntity()).comparator(), "simple_battery");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 5), (world, state) -> new DehydratorTileEntity()).mirrored(), "dehydrator");
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 5), (world, state) -> new HydratorEntity()).mirrored(), "hydrator");
    
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
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).variableOpacity(), EnergyCableTier.MK2), "energy_cable_mk2");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).variableOpacity(), EnergyCableTier.MK3), "energy_cable_mk3");
    
    /*
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK4), "energy_cable_mk4");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK5), "energy_cable_mk5");
    register(new EnergyCableBlock(getBlockProperties(Material.IRON, 4).func_226896_b_(), EnergyCableTier.MK6), "energy_cable_mk6");
    */
    
    register(new DarkCrystalBlock(getBlockProperties(Material.IRON, 30, 3600000.0F, MaterialColor.CYAN_TERRACOTTA).harvestLevel(3).harvestTool(ToolType.PICKAXE)),
      "dark_crystal_block",
      getItemProperties().addTooltip(TextComponentBuilder.createTranslated(WTBWMachines.MODID + ".tooltip.wither_proof").gold().build()).rarity(Rarity.EPIC));
    
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4).notSolid(), (world, state) -> new TeleportInhibitorTile()), "teleport_inhibitor");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 5), (world, state) -> new ChargerEntity()).mirrored(), "charger");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new MicroMinerTile()).mirrored(), "micro_miner");
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new EnergyInputHatchTile()), "energy_input_hatch");
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new FluidInputHatchTile()), "fluid_input_hatch");
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new ItemOutputHatchTile()), "item_output_hatch");
    
    register(new Block(getBlockProperties(Material.ROCK, 6)), "aluminium_ore");
    
//    register(new XpPylonBlock(getBlockProperties(Material.IRON, 5)), "xp_pylon");
  }
  
  @Override
  protected void registerAllItems()
  {
    BaseItemProperties baseProperties = getItemProperties();
    
    register(new Item(baseProperties), "aluminium_dust_pile");
    register(new Item(baseProperties), "aluminium_dust");
    register(new Item(baseProperties), "aluminium_ingot");
    
    register(new Item(baseProperties), "iron_plate");
    register(new Item(baseProperties), "gold_plate");
    register(new Item(baseProperties), "copper_plate");
    register(new Item(baseProperties), "cobalt_plate");
    register(new Item(baseProperties), "dark_crystal_plate");
    register(new Item(baseProperties), "energetic_copper_plate");
    
    register(new Item(baseProperties), "iron_beam");
    register(new Item(baseProperties), "steel_beam");
    register(new Item(baseProperties), "aluminium_beam");
    
    register(new Item(baseProperties), "iron_frame");
    register(new Item(baseProperties), "steel_frame");
    register(new Item(baseProperties), "aluminium_frame");
    
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
    register(new Item(baseProperties), "energetic_copper_dust");
    register(new Item(baseProperties), "cobalt_dust");
    
    register(new Item(baseProperties), "lapis_wafer");
    register(new Item(baseProperties), "glowstone_wafer");
    register(new Item(baseProperties), "improved_glowstone_wafer");
    register(new Item(baseProperties), "obsidian_wafer");
    register(new Item(baseProperties), "quartz_wafer");
    
    register(new Item(baseProperties), "copper_coil");
    
    register(new Item(getItemProperties().rarity(Rarity.EPIC)), "dark_crystal");
    register(new Item(baseProperties), "dark_crystal_blend");
    
    register(new WrenchItem(getItemProperties()), "wrench");
  
    register(new Item(baseProperties), "base_upgrade");
    
    register(new Item(baseProperties), "energetic_copper_ingot");
    
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.SPEED, 1.15f);
//      map.put(ModifierType.POWER_USAGE, 1.25f);
//    })), "speed_upgrade_mk1");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.SPEED, 1.25f);
//      map.put(ModifierType.POWER_USAGE, 1.60f);
//    })), "speed_upgrade_mk2");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.SPEED, 1.7f);
//      map.put(ModifierType.POWER_USAGE, 2f);
//    })), "speed_upgrade_mk3");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.POWER_USAGE, 0.87f);
//    })), "power_usage_upgrade_mk1");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.POWER_USAGE, 0.79f);
//    })), "power_usage_upgrade_mk2");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.POWER_USAGE, 0.40f);
//    })), "power_usage_upgrade_mk3");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.POWER_CAPACITY, 500000f);
//    })), "power_capacity_upgrade_mk1");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.POWER_CAPACITY, 2000000f);
//    })), "power_capacity_upgrade_mk2");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.POWER_CAPACITY, 5000000f);
//    })), "power_capacity_upgrade_mk3");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.TRANSFER, 1.5f);
//    })), "transfer_upgrade_mk1");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.TRANSFER, 2f);
//    })), "transfer_upgrade_mk2");
//
//    register(new BaseUpgradeItem(getItemProperties(), Util.make(new HashMap<>(), map ->
//    {
//      map.put(ModifierType.TRANSFER, 2.5f);
//    })), "transfer_upgrade_mk3");
  
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
    
    register(new BatteryItem(getItemProperties(), 250000, 1600), "battery_mk1");
    register(new BatteryItem(getItemProperties(), 1000000, 4000), "battery_mk2");
    register(new BatteryItem(getItemProperties(), 4000000, 12000), "battery_mk3");
    register(new BatteryItem(getItemProperties(), 16000000, 40000), "battery_mk4");
    register(new BatteryItem(getItemProperties(), 32000000, 128000), "battery_mk5");
    
    int drillUsages = 65;
    register(new Item(getItemProperties().maxDamage(drillUsages)), "copper_drill");
    register(new Item(getItemProperties().maxDamage(drillUsages)), "iron_drill");
    register(new Item(getItemProperties().maxDamage(drillUsages)), "gold_drill");
    register(new Item(getItemProperties().maxDamage(drillUsages)), "diamond_drill");
    register(new Item(getItemProperties().maxDamage(drillUsages)), "cobalt_drill");
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
    registerContainer(ChargerContainer::new, "charger");
    registerContainer(HydratorContainer::new, "hydrator");
    registerContainer(XpPylonContainer::new, "xp_pylon");
    
    registerContainer(EnergyInputHatchContainer::new, "energy_input_hatch");
    registerContainer(FluidInputHatchContainer::new, "fluid_input_hatch");
    registerContainer(ItemOutputHatchContainer::new, "item_output_hatch");
    
    registerContainer(MicroMinerContainer::new, "micro_miner");
  }
  
  public void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event)
  {
    ModRecipes.init();
  
    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
  
    registry.register(DehydratingRecipe.SERIALIZER);
    registry.register(CompressingRecipe.SERIALIZER);
    registry.register(CrushingRecipe.SERIALIZER);
    registry.register(PoweredFurnaceRecipe.SERIALIZER);
    registry.register(HydratingRecipe.SERIALIZER);
    registry.register(MiningRecipe.SERIALIZER);
  }
  
  public void registerWrenchActions()
  {
    WrenchItem.registerWrenchAction(ModBlocks.BLOCK_BREAKER, WrenchHelper.rotationWrenchAction(WrenchableSixWayTileBlock.FACING).and(WrenchHelper.dropWrenchAction()));
    WrenchItem.registerWrenchAction(ModBlocks.BLOCK_PLACER, WrenchHelper.rotationWrenchAction(WrenchableSixWayTileBlock.FACING).and(WrenchHelper.dropWrenchAction()));
    WrenchItem.registerWrenchAction(ModBlocks.BLOCK_DETECTOR, WrenchHelper.rotationWrenchAction(WrenchableSixWayTileBlock.FACING).and(WrenchHelper.dropWrenchAction()));
    WrenchItem.registerWrenchAction(ModBlocks.VACUUM_CHEST, WrenchHelper.dropWrenchAction());
  }
}
