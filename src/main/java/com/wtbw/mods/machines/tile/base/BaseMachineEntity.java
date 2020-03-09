package com.wtbw.mods.machines.tile.base;

import com.wtbw.mods.lib.tile.util.*;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.StackUtil;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import com.wtbw.mods.machines.block.base.BaseMachineBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/*
  @author: Naxanria
*/
public abstract class BaseMachineEntity extends TileEntity implements ITickableTileEntity, IRedstoneControlled, IContentHolder, IWTBWNamedContainerProvider, IGuiUpdateHandler
{
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
  }
  
  protected abstract List<ItemStackHandler> getInventories();
  
  @Override
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    manager.read(compound);
    super.read(compound);
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
    world.setBlockState(pos, getBlockState().with(BaseMachineBlock.ON, on), 3);
  }
  
  public NBTManager getManager()
  {
    return manager;
  }
  
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
    return nbt;
  }
  
  @Override
  public void handleGuiUpdateTag(CompoundNBT nbt)
  {
    GuiUpdateHelper.updateEnergy(storage, nbt.getIntArray("storage"));
  }
}
