package com.wtbw.mods.machines;

import com.wtbw.mods.lib.Registrator;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.block.SixWayTileBlock;
import com.wtbw.mods.machines.block.*;
import com.wtbw.mods.machines.block.redstone.BlockDetectorBlock;
import com.wtbw.mods.machines.block.redstone.RedstoneEmitterBlock;
import com.wtbw.mods.machines.block.redstone.RedstoneTimerBlock;
import com.wtbw.mods.machines.block.spikes.SpikesBlock;
import com.wtbw.mods.machines.block.spikes.SpikesType;
import com.wtbw.mods.machines.gui.container.*;
import com.wtbw.mods.machines.recipe.DryerRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.*;
import com.wtbw.mods.machines.tile.furnace.FurnaceTier;
import com.wtbw.mods.machines.tile.machine.DryerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
    
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 3), (world, state) -> new SimpleBatteryTileEntity()).comparator(), "simple_battery");
    
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 5), (world, state) -> new DryerTileEntity()), "dryer");

    //TODO Make Recipe
    register(new Block(getBlockProperties(Material.IRON, 3)), "machine_block");
    register(new QuarryMarker(getBlockProperties(Material.IRON, 1)), "quarry_marker");
  }
  
  @Override
  protected void registerAllItems()
  { }
  
//  @Override
//  protected void registerAllTiles()
//  {
//    register(ModBlocks.IRON_FURNACE);
//    register(ModBlocks.GOLD_FURNACE);
//    register(ModBlocks.DIAMOND_FURNACE);
//    register(ModBlocks.END_FURNACE);
//
//    register(ModBlocks.REDSTONE_TIMER);
//    register(ModBlocks.BLOCK_BREAKER);
//    register(ModBlocks.BLOCK_PLACER);
//    register(ModBlocks.BLOCK_DETECTOR);
//
//    register(ModBlocks.PUSHER);
//    register(ModBlocks.PULLER);
//
//    register(ModBlocks.VACUUM_CHEST);
//
//    register(ModBlocks.SOLAR_PANEL);
//
//    register(ModBlocks.QUARRY);
//  }
  
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
  }
  
  public void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event)
  {
    ModRecipes.init();
    
    event.getRegistry().register(DryerRecipe.SERIALIZER);
  
    WTBWMachines.LOGGER.info("registered types: ");
    for (ResourceLocation id : Registry.RECIPE_TYPE.keySet())
    {
      WTBWMachines.LOGGER.info("\t{}", id.toString());
    }
  }
}
