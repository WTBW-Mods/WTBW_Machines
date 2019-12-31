package com.wtbw.machines.network;

import com.wtbw.lib.network.Packet;
import com.wtbw.machines.tile.QuarryTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Sunekaer
*/
public class UpdateQuarryPacket extends Packet {

    public final BlockPos pos;
    public final BlockPos current;
    public final Boolean done;

    public UpdateQuarryPacket (BlockPos pos, BlockPos current, Boolean done){
        this.pos = pos;
        this.current = current;
        this.done = done;
    }

    public UpdateQuarryPacket (PacketBuffer buffer){
        pos = buffer.readBlockPos();
        current = buffer.readBlockPos();
        done = buffer.readBoolean();
    }

    @Override
    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBlockPos(current);
        buffer.writeBoolean(done);

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
        {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            {
                TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(pos);
                if (tileEntity != null)
                {
                    if (tileEntity instanceof QuarryTileEntity)
                    {
                        ((QuarryTileEntity) tileEntity).setCurrentPos(current);
                        ((QuarryTileEntity) tileEntity).setDone(done);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
