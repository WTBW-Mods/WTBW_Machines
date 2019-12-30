package com.wtbw.machines;

import com.wtbw.lib.Registrator;
import com.wtbw.lib.block.BaseTileBlock;
import com.wtbw.lib.block.SixWayTileBlock;
import com.wtbw.machines.block.ModBlocks;
import com.wtbw.machines.block.PushBlock;
import com.wtbw.machines.block.TieredFurnaceBlock;
import com.wtbw.machines.block.redstone.BlockDetectorBlock;
import com.wtbw.machines.block.redstone.RedstoneEmitterBlock;
import com.wtbw.machines.block.redstone.RedstoneTimerBlock;
import com.wtbw.machines.block.spikes.SpikesBlock;
import com.wtbw.machines.block.spikes.SpikesType;
import com.wtbw.machines.gui.container.*;
import com.wtbw.machines.tile.*;
import com.wtbw.machines.tile.furnace.FurnaceTier;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;

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

    //TODO Make Textures
    //TODO Make Model
    //TODO Make Recipe
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new QuarryTileEntity()), "quarry");
  
    register(new PushBlock(getBlockProperties(Material.IRON, 1), EntityPusherTileEntity.PushMode.PUSH), "pusher");
    register(new PushBlock(getBlockProperties(Material.IRON, 1), EntityPusherTileEntity.PushMode.PULL), "puller");
  
    register(new SpikesBlock(getBlockProperties(Material.ROCK, 3), SpikesType.BAMBOO), "bamboo_spikes");
    register(new SpikesBlock(getBlockProperties(Material.ROCK, 3), SpikesType.WOODEN), "wooden_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 4), SpikesType.IRON), "iron_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 5), SpikesType.GOLD), "gold_spikes");
    register(new SpikesBlock(getBlockProperties(Material.IRON, 6), SpikesType.DIAMOND), "diamond_spikes");
  }
  
  @Override
  protected void registerAllItems()
  { }
  
  @Override
  protected void registerAllTiles()
  {
    register(ModBlocks.IRON_FURNACE);
    register(ModBlocks.GOLD_FURNACE);
    register(ModBlocks.DIAMOND_FURNACE);
    register(ModBlocks.END_FURNACE);
    
    register(ModBlocks.REDSTONE_TIMER);
    register(ModBlocks.BLOCK_BREAKER);
    register(ModBlocks.BLOCK_PLACER);
    register(ModBlocks.BLOCK_DETECTOR);
    
    register(ModBlocks.PUSHER);
    register(ModBlocks.PULLER);
    
    register(ModBlocks.VACUUM_CHEST);

    register(ModBlocks.QUARRY);
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
  }
}
