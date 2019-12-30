package com.wtbw.machines.tile;

import com.wtbw.lib.block.SixWayTileBlock;
import com.wtbw.lib.gui.util.ClickType;
import com.wtbw.lib.tile.util.IContentHolder;
import com.wtbw.lib.tile.util.IRedstoneControlled;
import com.wtbw.lib.tile.util.RedstoneControl;
import com.wtbw.lib.tile.util.RedstoneMode;
import com.wtbw.lib.util.NBTHelper;
import com.wtbw.lib.util.StackUtil;
import com.wtbw.lib.util.Utilities;
import com.wtbw.machines.gui.container.BlockBreakerContainer;
import com.wtbw.machines.gui.container.QuarryContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Sunekaer
*/
public class QuarryTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IRedstoneControlled, IContentHolder
{
    //TODO Make it use Forge Energy.
    //TODO Make facing + move the quarry area
    //TODO Make Bounding boarder

    private RedstoneControl control;
    private BlockPos startPos;
    private BlockPos currentPos;
    private BlockPos endPos;
    private int tick;
    //TODO Config for quarrySize
    private double quarrySize = ((16 * 1) / 2);
    private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createInventory);

    private ItemStackHandler createInventory()
    {
        return new ItemStackHandler(9);
    }


    public QuarryTileEntity()
    {
        super(ModTiles.QUARRY);

        control = new RedstoneControl(this, RedstoneMode.ON);
    }

    @Override
    public RedstoneControl getControl() {
        return control;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(getType().getRegistryName().toString());
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new QuarryContainer(id, world, pos, inventory);
    }

    @Override
    public void tick() {
        if (!world.isRemote)
        {
            tick++;
            if ( startPos == null || endPos == null) {
                startPos = new BlockPos(this.getPos().getX() - quarrySize, this.getPos().down().getY(), this.getPos().getZ() - quarrySize);
                endPos = new BlockPos(this.getPos().getX() + quarrySize, 0, this.getPos().getZ() + quarrySize);
                currentPos = startPos;
                markDirty();
            }

            if (control.update())
            {   //TODO Change 15 to config.
                if (tick % 15 == 0) {
                    if (Utilities.isInside(startPos, endPos, currentPos)) {
                        if (breakBlock()) {

                            //Setting currentPos to the next block there should be mined
                            BlockPos nextX = new BlockPos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ());
                            BlockPos nextZ = new BlockPos(startPos.getX(), currentPos.getY(), currentPos.getZ() + 1);
                            BlockPos nextY = new BlockPos(startPos.getX(), currentPos.getY() - 1, startPos.getZ());

                            if (Utilities.isInside(startPos, endPos, nextX)) {
                                currentPos = nextX;
                                markDirty();
                            } else if (Utilities.isInside(startPos, endPos, nextZ)) {
                                currentPos = nextZ;
                                markDirty();
                            } else if (Utilities.isInside(startPos, endPos, nextY)) {
                                currentPos = nextY;
                                markDirty();
                            }
                        }
                    }
                    control.resetCooldown();
                }
            }
        }
    }

    private boolean breakBlock()
    {
        BlockState blockState = world.getBlockState(currentPos);

        // todo: config for breaking tiles, blacklist
        boolean breakTiles = false;
        Block block = blockState.getBlock();

        if (breakTiles || world.getTileEntity(currentPos) == null) {
            if (!block.equals(Blocks.AIR) && !block.equals(Blocks.BEDROCK)) {
                List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, currentPos, blockState.hasTileEntity() ? world.getTileEntity(currentPos) : null);
                for (ItemStack drop : drops) {
                    if (!StackUtil.canInsert(inventory, drop, true)) {
                        return false;
                    }
                }
                world.destroyBlock(currentPos, false);
                inventory.ifPresent(handler ->
                {
                    for (ItemStack drop : drops) {
                        for (int i = 0; i < handler.getSlots(); i++) {
                            drop = handler.insertItem(i, drop, false);
                            if (drop.isEmpty()) {
                                break;
                            }
                        }
                    }
                });
            }
        }
        return true;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        startPos = NBTHelper.getBlockPos(compound, "start");
        currentPos = NBTHelper.getBlockPos(compound, "current");
        endPos = NBTHelper.getBlockPos(compound, "end");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        NBTHelper.putBlockPos(compound, "start", startPos);
        NBTHelper.putBlockPos(compound, "current", currentPos);
        NBTHelper.putBlockPos(compound, "end", endPos);
        return super.write(compound);
    }

    @Override
    public boolean handleButton(int buttonID, ClickType clickType)
    {
        if (control.handleButton(buttonID, clickType))
        {
            markDirty();
            return true;
        }

        return false;
    }

    public LazyOptional<ItemStackHandler> getInventory()
    {
        return inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return inventory.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void dropContents()
    {
        inventory.ifPresent(handler -> Utilities.dropItems(world, handler, pos));
    }

    @Override
    public RedstoneMode[] availableModes()
    {
        return new RedstoneMode[]{ RedstoneMode.IGNORE, RedstoneMode.ON, RedstoneMode.OFF };
    }
}
