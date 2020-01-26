package com.wtbw.mods.machines.network;

import com.wtbw.mods.lib.network.Packet;
import com.wtbw.mods.machines.tile.SimpleBatteryTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Sunekaer
*/
public class SyncBatteryBlockBar extends Packet {

    public final BlockPos pos;
    public final int energy;

    public SyncBatteryBlockBar (BlockPos pos, int energy){
        this.pos = pos;
        this.energy = energy;
    }

    public SyncBatteryBlockBar (PacketBuffer buffer){
        pos = buffer.readBlockPos();
        energy = buffer.readInt();
    }

    @Override
    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(energy);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
        {
            TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(pos);
            if (tileEntity != null)
            {
                if (tileEntity instanceof SimpleBatteryTileEntity)
                {
                    ((SimpleBatteryTileEntity) tileEntity).getStorage().setEnergy(energy);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
