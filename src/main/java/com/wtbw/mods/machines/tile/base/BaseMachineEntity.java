package com.wtbw.mods.machines.tile.base;

import com.wtbw.mods.lib.tile.util.*;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.upgrade.IUpgradeable;
import com.wtbw.mods.lib.upgrade.ModifierType;
import com.wtbw.mods.lib.upgrade.UpgradeManager;
import com.wtbw.mods.lib.util.StackUtil;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.block.base.BaseMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

/*
  @author: Naxanria
*/
public abstract class BaseMachineEntity extends TileEntity implements ITickableTileEntity, IRedstoneControlled, IContentHolder, IWTBWNamedContainerProvider, IGuiUpdateHandler
{
  public static final UpgradeManager.Filter DEFAULT_MACHINE_FILTER = UpgradeManager.Filter.create(ModifierType.SPEED, ModifierType.POWER_USAGE, ModifierType.POWER_CAPACITY);
  
  public static final int DEFAULT_CAPACITY = 100000;
  
  protected RedstoneControl control;
  protected BaseEnergyStorage storage;
  protected NBTManager manager;
  
  public BaseMachineEntity(TileEntityType<?> tileEntityTypeIn, int capacity, int maxReceive)
  {
    this(tileEntityTypeIn, capacity, maxReceive, RedstoneMode.ON);
  }
  
  public BaseMachineEntity(TileEntityType<?> tileEntityTypeIn, int capacity, int maxReceive, RedstoneMode defaultMode)
  {
    super(tileEntityTypeIn);
  
    storage = new BaseEnergyStorage(capacity, maxReceive, 0);
    control = new RedstoneControl(this, defaultMode);
    
    manager = new NBTManager()
      .register("storage", storage, false)
      .register("control", control);
  }
  
  @Override
  public void dropContents()
  {
    getInventories().forEach(handler -> Utilities.dropItems(world, handler, pos));
    
    if (this instanceof IUpgradeable)
    {
      Utilities.dropItems(world, ((IUpgradeable) this).getUpgradeManager().getUpgradeInventory(), pos);
    }
  }
  
  @Nonnull
  protected abstract List<ItemStackHandler> getInventories();
  
  @Override
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Override
  public void read(BlockState state, CompoundNBT compound)
  {
    manager.read(compound);
    super.read(state, compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    manager.write(compound);
    return super.write(compound);
  }
  
  protected InventoryWrapper getFakeInventory(int size)
  {
    return new InventoryWrapper(new ItemStackHandler(size));
  }
  
  protected InventoryWrapper getFakeInventory(List<ItemStack> stacks)
  {
    InventoryWrapper wrapper = new InventoryWrapper(new ItemStackHandler(stacks.size()));
    
    for (int i = 0; i < stacks.size(); i++)
    {
      wrapper.handler.setStackInSlot(i, stacks.get(i));
    }
    
    return wrapper;
  }
  
  protected InventoryWrapper getFakeInventory(ItemStack... stacks)
  {
    InventoryWrapper wrapper = new InventoryWrapper(new ItemStackHandler(stacks.length));
  
    for (int i = 0; i < stacks.length; i++)
    {
      wrapper.handler.setStackInSlot(i, stacks[i]);
    }
    
    return wrapper;
  }
  
  protected boolean isOn()
  {
    return getBlockState().get(BaseMachineBlock.ON);
  }
  
  protected void setOn(boolean on)
  {
    world.setBlockState(pos, getBlockState().with(BaseMachineBlock.ON, on), Constants.BlockFlags.DEFAULT_AND_RERENDER);
  }
  
  protected Direction getFacing()
  {
    return getBlockState().get(BaseMachineBlock.FACING);
  }
  
  public NBTManager getManager()
  {
    return manager;
  }
  
  @Nonnull
  public BaseEnergyStorage getStorage()
  {
    return storage;
  }
  
  protected boolean canOutput(int slot, ItemStackHandler output, IRecipe<?> recipe)
  {
    ItemStack current = output.getStackInSlot(slot);
    if (current.isEmpty())
    {
      return true;
    }
  
    int maxSize = current.getMaxStackSize();
    int size = current.getCount() + recipe.getRecipeOutput().getCount();
  
    return size <= maxSize && StackUtil.doItemsStack(current, recipe.getRecipeOutput());
  }
  
  protected boolean canAndUsePower(int power)
  {
    if (storage.getEnergyStored() >= power)
    {
      storage.extractInternal(power, false);
      return true;
    }
    
    return false;
  }
  
  @Override
  public CompoundNBT getGuiUpdateTag()
  {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putIntArray("storage", GuiUpdateHelper.getEnergyUpdateValues(storage, true));
    CompoundNBT extra = new CompoundNBT();
    getExtraGuiUpdateTag(extra);
    nbt.put("extra", extra);
    return nbt;
  }
  
  protected abstract void getExtraGuiUpdateTag(CompoundNBT nbt);
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    GuiUpdateHelper.updateEnergy(storage, nbt.getIntArray("storage"));
    if (nbt.contains("extra", Constants.NBT.TAG_COMPOUND))
    {
      handleExtraGuiUpdateTag(nbt.getCompound("extra"));
    }
  }
  
  protected abstract void handleExtraGuiUpdateTag(CompoundNBT nbt);
}
