package com.wtbw.mods.machines.tile;

import com.wtbw.mods.lib.network.Networking;
import com.wtbw.mods.lib.tile.util.GuiUpdateHelper;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.IWTBWNamedContainerProvider;
import com.wtbw.mods.machines.gui.container.BatteryContainer;
import com.wtbw.mods.machines.network.SyncBatteryBlockBar;
import com.wtbw.mods.machines.tile.base.Battery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class SimpleBatteryTileEntity extends Battery implements IWTBWNamedContainerProvider, IGuiUpdateHandler
{
    public SimpleBatteryTileEntity()
    {
        super(ModTiles.SIMPLE_BATTERY, 32000, 1000, 1000);
    }
    
    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
    {
        return new BatteryContainer(id, world, pos, inventory);
    }
    
    private int PEnergy = -1;
    private int tick;
    
    @Override
    public void tick()
    {
        super.tick();
        int e = getEnergy();
        
        if (tick % 20 == 0)
        {
            PEnergy = e;
            if (!world.isRemote)
            {
                Networking.sendAround(world, pos, 46, new SyncBatteryBlockBar(pos, e));
            }
        }
    }
    
    @Override
    public CompoundNBT getGuiUpdateTag()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putIntArray("storage", GuiUpdateHelper.getEnergyUpdateValues(storage, true));
        return nbt;
    }
    
    @Override
    public void handleGuiUpdateTag(CompoundNBT nbt)
    {
        GuiUpdateHelper.updateEnergy(storage, nbt.getIntArray("storage"));
    }
}

