package com.wtbw.mods.machines.tile;

import com.google.common.collect.MapMaker;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import java.util.Collections;
import java.util.Set;

/*
  @author: Naxanria
*/
public class TeleportInhibitorTile extends TileEntity implements ITickableTileEntity
{
  public static final int range = 16;
  public static final int rangeSqr = range * range;
  private static Set<TeleportInhibitorTile> inhibitors = Collections.newSetFromMap(new MapMaker().concurrencyLevel(2).weakKeys().makeMap());
  
  public TeleportInhibitorTile()
  {
    super(ModTiles.TELEPORT_INHIBITOR);
  }
  
  public static boolean inhibitorInRange(LivingEntity entity)
  {
    return inhibitors.stream()
      .filter(inhibitor -> !inhibitor.isRemoved())
      .filter(inhibitor -> inhibitor.world == entity.world && inhibitor.world.getTileEntity(inhibitor.pos) == inhibitor)
      .anyMatch(inhibitor -> inhibitor.inRange(entity));
  }
  
  public static void onTeleport(EnderTeleportEvent event)
  {
    if (inhibitorInRange(event.getEntityLiving()))
    {
      event.setCanceled(true);
    }
  }
  
  public TeleportInhibitorTile(TileEntityType<?> tileEntityTypeIn)
  {
    super(tileEntityTypeIn);
  }
  
  private boolean inRange(LivingEntity entity)
  {
    return Utilities.distanceSqr(pos, entity.getPosition()) <= rangeSqr;
  }
  
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      if (!inhibitors.contains(this))
      {
        inhibitors.add(this);
      }
    }
  }
}
