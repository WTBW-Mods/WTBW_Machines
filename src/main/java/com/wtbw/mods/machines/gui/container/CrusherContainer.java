package com.wtbw.mods.machines.gui.container;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.tile.machine.PoweredCompressorEntity;
import com.wtbw.mods.machines.tile.machine.PoweredCrusherEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Sunekaer
*/
public class CrusherContainer extends BaseTileContainer<PoweredCrusherEntity> {
    public CrusherContainer(int id, World world, BlockPos pos, PlayerInventory inventory) {
        super(ModContainers.CRUSHER, id, world, pos, inventory);

        NBTManager manager = tileEntity.getManager();

        track(manager);

        ItemStackHandler handler = tileEntity.getInventory();

        addInputSlot(handler, PoweredCrusherEntity.INPUT_SLOT, 46, 30);

        addSlot(handler, PoweredCrusherEntity.OUTPUT_SLOT, 146, 30);
        addSlot(handler, PoweredCrusherEntity.OUTPUT_SLOT2, 162, 30);
        addSlot(handler, PoweredCrusherEntity.OUTPUT_SLOT3, 146, 46);

        layoutPlayerInventorySlots();
    }
}
