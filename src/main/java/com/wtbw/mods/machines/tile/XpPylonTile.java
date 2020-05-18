package com.wtbw.mods.machines.tile;

import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWFakePlayer;
import com.wtbw.mods.machines.gui.container.XpPylonContainer;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.HealthBoostEffect;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class XpPylonTile extends TileEntity implements ITickableTileEntity, IWTBWNamedContainerProvider, IGuiUpdateHandler
{
  private int xp = 0;
  private int radius = 5;
  
  public XpPylonTile()
  {
    super(ModTiles.XP_PYLON);
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    xp = compound.getInt("xp");
    radius = compound.getInt("radius");
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    compound.putInt("xp", xp);
    compound.putInt("radius", radius);
    return super.write(compound);
  }
  
  public int getXp()
  {
    return xp;
  }
  
  public int transferXP(PlayerEntity player, int amount, boolean levels)
  {
    // into player
    if (amount > 0)
    {
      if (levels)
      {
        int cap = player.xpBarCap();
        if (cap <= xp)
        {
          player.giveExperiencePoints(cap);
          xp -= cap;
          amount--;
        }
    
        while (amount > 0)
        {
          cap = player.xpBarCap();
          if (cap <= xp)
          {
            player.addExperienceLevel(1);
            xp -= cap;
          }
          else
          {
            break;
          }
        }
      }
      else if (amount <= xp)
      {
        player.giveExperiencePoints(amount);
        xp -= amount;
      }
    }
    else // from player
    {
      if (levels)
      {
        amount = -amount;
        while (amount > 0 && player.experienceLevel > 0)
        {
          int xpForLevel = xpBarCap(player.experienceLevel);
          player.experienceLevel--;
          amount--;
          xp += xpForLevel;
        }
      }
      else
      {
        amount = -amount;
        while (amount > 0 && (player.experienceLevel > 0 || player.experience > 0))
        {
          if (player.experience > amount)
          {
            player.experience -= amount;
            break;
          }
          else
          {
            if (player.experience > 0)
            {
              amount -= player.experience;
            }
            else
            {
              int cap = player.xpBarCap();
              player.experienceLevel--;
              if (cap >= amount)
              {
                player.experience = cap - amount;
                break;
              }
              else
              {
                amount -= cap;
              }
            }
          }
        }
      }
    }
    
    markDirty();
    
    return xp;
  }
  
  public int getLevels()
  {
    int xp = this.xp;
    int l = 0;
    int cap;
    while ((cap = xpBarCap(l)) <= xp)
    {
      l++;
      xp -= cap;
    }
    
    return l;
  }
  
  public int xpBarCap(int level)
  {
    if (level >= 30)
    {
      return 112 + (level - 30) * 9;
    }
    else
    {
      return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
    }
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new XpPylonContainer(id, world, pos, inventory);
  }
  
  @Override
  public void tick()
  {
    if (world != null && !world.isRemote)
    {
      List<ExperienceOrbEntity> orbs = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, Utilities.getBoundingBox(pos, radius));
      orbs.forEach(orb ->
      {
        int orbXp = orb.getXpValue();
        if (xp + orbXp > 0)
        {
          xp += orbXp;
          
          orb.remove();
        }
      });
    }
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    return write(new CompoundNBT());
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    read(nbt);
  }
}
