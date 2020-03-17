package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseUpgradeContainer;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.machine.PoweredCompressorEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Sunekaer
*/
public class CompressorContainer extends BaseUpgradeContainer<PoweredCompressorEntity>
{
    public CompressorContainer(int id, World world, BlockPos pos, PlayerInventory inventory) {
        super(ModContainers.COMPRESSOR, id, world, pos, inventory);

        NBTManager manager = tileEntity.getManager();

        track(manager);

        ItemStackHandler handler = tileEntity.getInventory();

        addInputSlot(handler, PoweredCompressorEntity.INPUT_SLOT, 175 / 2 - 6, 20);

        addSlot(handler, PoweredCompressorEntity.OUTPUT_SLOT, 175 / 2 - 6, 52);

        layoutPlayerInventorySlots();
    }
}
