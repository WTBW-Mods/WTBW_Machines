package com.wtbw.mods.machines.network;

import com.wtbw.mods.lib.network.Packet;
import com.wtbw.mods.machines.tile.XpPylonTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class TransferXpPacket extends Packet
{
  public final BlockPos pos;
  public final int amount;
  
  public TransferXpPacket(BlockPos pos, int amount)
  {
    this.pos = pos;
    this.amount = amount;
  }
  
  public TransferXpPacket(PacketBuffer buffer)
  {
    pos = buffer.readBlockPos();
    amount = buffer.readInt();
  }
  
  @Override
  public void toBytes(PacketBuffer buffer)
  {
    buffer.writeBlockPos(pos);
    buffer.writeInt(amount);
  }
  
  @Override
  public void handle(Supplier<NetworkEvent.Context> ctx)
  {
    ctx.get().enqueueWork(() ->
    {
      if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
      {
        ServerPlayerEntity sender = ctx.get().getSender();
        if (sender != null)
        {
          TileEntity tileEntity = sender.world.getTileEntity(pos);
          if (tileEntity instanceof XpPylonTile)
          {
            XpPylonTile pylon = (XpPylonTile) tileEntity;
            pylon.transferXP(sender, amount, true);
          }
        }
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
