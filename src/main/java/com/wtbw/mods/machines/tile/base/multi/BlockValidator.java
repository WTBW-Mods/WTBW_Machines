package com.wtbw.mods.machines.tile.base.multi;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/*
  @author: Naxanria
*/
public class BlockValidator
{
  private Block block;
  private Tag<Block> tag;
  private ResourceLocation tagLocation;
  
  public BlockValidator(Block block)
  {
    this.block = block;
  }
  
  public BlockValidator(Tag<Block> tag)
  {
    this.tag = tag;
  }
  
  public boolean check(Block block)
  {
    return (block == this.block) || tag != null && tag.contains(block);
  }
  
  public static BlockValidator get(String id)
  {
    // tag
    if (id.startsWith("#"))
    {
      return new BlockValidator(BlockTags.getCollection().get(new ResourceLocation(id.substring(1))));
    }
    else // block id
    {
      return new BlockValidator(Registry.BLOCK.getOrDefault(new ResourceLocation(id)));
    }
  }
  
  public Block getBlock()
  {
    return block;
  }
  
  public Tag<Block> getTag()
  {
    return tag;
  }
}
