package com.wtbw.mods.machines.tile.redstone;

import com.wtbw.mods.machines.block.redstone.RedstoneTimerBlock;
import com.wtbw.mods.machines.config.CommonConfig;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.lib.util.Colors;
import com.wtbw.mods.lib.util.Cooldown;
import com.wtbw.mods.lib.util.PlayEvent;
import com.wtbw.mods.lib.util.RandomUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class RedstoneTimerTileEntity extends TileEntity implements ITickableTileEntity
{
  private Cooldown cooldown;
  private Cooldown windDown;
  private int sendPower = 0;

  public RedstoneTimerTileEntity()
  {
    super(ModTiles.REDSTONE_TIMER);

    cooldown = new Cooldown(CommonConfig.instance().redstoneClockRepeat.get()).start();
    windDown = new Cooldown(CommonConfig.instance().redstoneClockDuration.get()).start();
  }

  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
//      int power = world.getRedstonePowerFromNeighbors(pos);
      boolean powered = world.isBlockPowered(pos);

      if (powered && !cooldown.isFinished())
      {
        cooldown.setCount(0);
        updatePower(0);
        return;
      }

      cooldown.start();
      cooldown.update();
      if (cooldown.isFinished())
      {
        updatePower(15);
        windDown.update();
        if (windDown.isFinished())
        {
          windDown.restart();
          cooldown.restart();
        }
      }
      else //if (cooldown.getCount() > 1)
      {
        updatePower(0);
      }
    }
  }

  private void updatePower(int newPower)
  {
    if (newPower != sendPower)
    {
      sendPower = newPower;
      world.notifyNeighbors(pos, world.getBlockState(pos).getBlock());
      world.setBlockState(pos, world.getBlockState(pos).with(RedstoneTimerBlock.ACTIVE, newPower > 0), 3);
      if (newPower > 0)
      {
        Random rand = world.rand;
  
        if (RandomUtil.chance(rand, 85))
        {
          PlayEvent.redstoneParticle(world, pos.getX() + rand.nextDouble(), pos.getY() + 1.1, pos.getZ() + rand.nextDouble(), 0, rand.nextDouble() * .2, 0, Colors.RED);
        }
      }
    }
    else
    {
      sendPower = newPower;
    }
  }

  public int getPower()
  {
    return sendPower;
  }
}
