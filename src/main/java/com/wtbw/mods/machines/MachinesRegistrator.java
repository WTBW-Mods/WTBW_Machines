package com.wtbw.mods.machines;

import com.wtbw.mods.lib.Registrator;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.block.SixWayTileBlock;
import com.wtbw.mods.lib.item.BaseItem;
import com.wtbw.mods.machines.block.*;
import com.wtbw.mods.machines.block.base.BaseMachineBlock;
import com.wtbw.mods.machines.block.base.TierBlock;
import com.wtbw.mods.machines.block.redstone.BlockDetectorBlock;
import com.wtbw.mods.machines.block.redstone.RedstoneEmitterBlock;
import com.wtbw.mods.machines.block.redstone.RedstoneTimerBlock;
import com.wtbw.mods.machines.block.spikes.SpikesBlock;
import com.wtbw.mods.machines.block.spikes.SpikesType;
import com.wtbw.mods.machines.gui.container.*;
import com.wtbw.mods.machines.recipe.CompressingRecipe;
import com.wtbw.mods.machines.recipe.DryerRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.*;
import com.wtbw.mods.machines.tile.furnace.FurnaceTier;
import com.wtbw.mods.machines.tile.machine.DryerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;

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
  
    register(new SixWayTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new BlockBreakerTileEntity()), "block_breaker");
    register(new SixWayTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new BlockPlacerTileEntity()), "block_placer");

    register(new BlockDetectorBlock(getBlockProperties(Material.IRON, 4)), "block_detector");
  
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new VacuumChestTileEntity()), "vacuum_chest");

    register(new QuarryBlock(getBlockProperties(Material.IRON, 4).func_226896_b_()), "quarry");
  
    register(new PushBlock(getBlockProperties(Material.IRON, 1), EntityPusherTileEntity.PushMode.PUSH), "pusher");
    register(new PushBlock(getBlockProperties(Material.IRON, 1), EntityPusherTileEntity.PushMode.PULL), "puller");
  
    register(new SpikesBlock(getBlockProperties(Material.ROCK, 3), SpikesType.BAMBOO), "bamboo_spikes");
    register(new SpikesBlock(getBlockProperties(Material.ROCK, 3), SpikesType.WOODEN), "wooden_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 4), SpikesType.IRON), "iron_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 5), SpikesType.GOLD), "gold_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 6), SpikesType.DIAMOND), "diamond_spikes");
    
    register(new SolarPanelBlock(getBlockProperties(Material.IRON, 3), 150000, 80, 20).comparator(), "solar_panel");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 3), (world, state) -> new SimpleBatteryTileEntity()).comparator(), "simple_battery");
    
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 5), (world, state) -> new DryerTileEntity()).mirrored(), "dryer");

    //TODO Make Recipe
    register(new Block(getBlockProperties(Material.IRON, 3)), "machine_block");
    
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 1), "tier1_upgrade");
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 2), "tier2_upgrade");
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 3), "tier3_upgrade");
    register(new TierBlock(getBlockProperties(Material.IRON, 3), 4), "tier4_upgrade");
    //register(new QuarryMarker(getBlockProperties(Material.IRON, 1)), "quarry_marker");

    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, starte) -> new PoweredFurnaceEntity()).mirrored(), "powered_furnace");
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, starte) -> new PoweredCrusherEntity()).mirrored(), "powered_crusher");
    register(new BaseMachineBlock<>(getBlockProperties(Material.IRON, 4), (world, starte) -> new PoweredCompressorEntity()).mirrored(), "powered_compressor");
  }
  
  @Override
  protected void registerAllItems()
  {
    register(new Item(getItemProperties()), "iron_plate");
    register(new Item(getItemProperties()), "gold_plate");
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
    registerContainer(DryerContainer::new, "dryer");
    registerContainer(SolarPanelContainer::new, "solar_panel");
    registerContainer(BatteryContainer::new, "battery");
    registerContainer(CompressorContainer::new, "compressor");
  }
  
  public void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event)
  {
    ModRecipes.init();
    
    event.getRegistry().register(DryerRecipe.SERIALIZER);
    event.getRegistry().register(CompressingRecipe.SERIALIZER);
  }
}
