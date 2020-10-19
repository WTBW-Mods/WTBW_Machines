package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseUpgradeContainer;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.machine.PoweredFurnaceEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Sunekaer
*/
public class PoweredFurnaceContainer extends BaseUpgradeContainer<PoweredFurnaceEntity>
{
    public PoweredFurnaceContainer(int id, World world, BlockPos pos, PlayerInventory inventory) {
        super(ModContainers.POWERED_FURNACE, id, world, pos, inventory);

        NBTManager manager = tileEntity.getManager();

        track(manager);

        ItemStackHandler handler = tileEntity.getInventory();

        addInputSlot(handler, PoweredFurnaceEntity.INPUT_SLOT, 175 / 2 - 6, 18);

        addSlot(handler, PoweredFurnaceEntity.OUTPUT_SLOT, 175 / 2 - 6, 54);


        layoutPlayerInventorySlots();
    }
}
