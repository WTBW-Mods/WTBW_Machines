package com.wtbw.mods.machines.tile.base.multi;

import net.minecraft.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*
  @author: Naxanria
*/
public class MultiBlockPattern
{
  protected int width;
  protected int height;
  protected int depth;
  
  protected int minX;
  protected int minY;
  protected int minZ;
  protected int maxX;
  protected int maxY;
  protected int maxZ;
  
  private final Map<BlockPos, BlockValidator> patternValidator;
  
  public MultiBlockPattern()
  {
    patternValidator = new LinkedHashMap<>();
  }
  
  public MultiBlockPattern(Map<BlockPos, BlockValidator> patternValidator)
  {
    this.patternValidator = patternValidator;
    validateSize();
  }
  
  public MultiBlockPattern add(int x, int y, int z, Block block)
  {
    return add(new BlockPos(x, y, z), block);
  }
  
  public MultiBlockPattern add(BlockPos pos, Block block)
  {
    return add(pos, new BlockValidator(block));
  }
  
  public MultiBlockPattern add(int x, int y, int z, Tag<Block> tag)
  {
    return add(new BlockPos(x, y, z), tag);
  }
  
  public MultiBlockPattern add(BlockPos pos, Tag<Block> tag)
  {
    return add(pos, new BlockValidator(tag));
  }
  
  public MultiBlockPattern add(BlockPos pos, BlockValidator validator)
  {
    patternValidator.put(pos, validator);
    validateSize(pos);
    
    return this;
  }
  
  private void validateSize(BlockPos pos)
  {
    minX = Math.min(minX, pos.getX());
    maxX = Math.max(maxX, pos.getX());
    minY = Math.min(minY, pos.getY());
    maxY = Math.max(maxY, pos.getY());
    minZ = Math.min(minZ, pos.getZ());
    maxZ = Math.max(maxZ, pos.getZ());
  
    width = maxX - minX + 1;
    height = maxY - minY + 1;
    depth = maxZ - minZ + 1;
  }
  
  private void validateSize()
  {
    minX = Integer.MAX_VALUE;
    minY = Integer.MAX_VALUE;
    minZ = Integer.MAX_VALUE;
    maxX = Integer.MIN_VALUE;
    maxY = Integer.MIN_VALUE;
    maxZ = Integer.MIN_VALUE;
  
    for (BlockPos pos : patternValidator.keySet())
    {
      minX = Math.min(minX, pos.getX());
      maxX = Math.max(maxX, pos.getX());
      minY = Math.min(minY, pos.getY());
      maxY = Math.max(maxY, pos.getY());
      minZ = Math.min(minZ, pos.getZ());
      maxZ = Math.max(maxZ, pos.getZ());
    }
    
    width = maxX - minX + 1;
    height = maxY - minY + 1;
    depth = maxZ - minZ + 1;
  }
  
  public BlockPatternFeedback check(World world, BlockPos start, Direction direction)
  {
    for (Map.Entry<BlockPos, BlockValidator> entry : patternValidator.entrySet())
    {
      BlockPos pos = getRelative(start, entry.getKey(), direction);
      Block block = world.getBlockState(pos).getBlock();
      if (!entry.getValue().check(block))
      {
        return BlockPatternFeedback.failure(pos, entry.getValue());
      }
    }
    
    return BlockPatternFeedback.success();
  }
  
  public static BlockPos getRelative(BlockPos start, BlockPos pos, Direction direction)
  {
    BlockPos rotated;
    
    switch (direction)
    {
      case DOWN:
      case UP:
      case NORTH:
      default:
        rotated = pos;
        break;
        
      case SOUTH:
        rotated = pos.rotate(Rotation.CLOCKWISE_180);
        break;
        
      case WEST:
        rotated = pos.rotate(Rotation.COUNTERCLOCKWISE_90);
        break;
        
      case EAST:
        rotated = pos.rotate(Rotation.CLOCKWISE_90);
        break;
    }
    
    return start.add(rotated);
  }
  
  public int getWidth()
  {
    return width;
  }
  
  public int getHeight()
  {
    return height;
  }
  
  public int getDepth()
  {
    return depth;
  }
  
  public int getMinX()
  {
    return minX;
  }
  
  public int getMinY()
  {
    return minY;
  }
  
  public int getMinZ()
  {
    return minZ;
  }
  
  public int getMaxX()
  {
    return maxX;
  }
  
  public int getMaxY()
  {
    return maxY;
  }
  
  public int getMaxZ()
  {
    return maxZ;
  }
}
