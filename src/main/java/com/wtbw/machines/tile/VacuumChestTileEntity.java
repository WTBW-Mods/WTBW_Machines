package com.wtbw.machines.tile;

import com.wtbw.machines.config.CommonConfig;
import com.wtbw.machines.gui.container.VacuumChestContainer;
import com.wtbw.lib.tile.util.IContentHolder;
import com.wtbw.lib.tile.util.IRedstoneControlled;
import com.wtbw.lib.tile.util.RedstoneControl;
import com.wtbw.lib.tile.util.RedstoneMode;
import com.wtbw.lib.util.NBTHelper;
import com.wtbw.lib.util.PlayEvent;
import com.wtbw.lib.util.StackUtil;
import com.wtbw.lib.util.Utilities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

//import com.wtbw.compat.item_filters.ItemFiltersWrapper;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class VacuumChestTileEntity extends TileEntity implements ITickableTileEntity, IContentHolder, INamedContainerProvider, IRedstoneControlled
{
  private AxisAlignedBB bound;
  private int radius = 6;
  private int tick;
  private RedstoneControl control;
  
//  private ItemStack filter = ItemStack.EMPTY;
  private ItemStackHandler filter = new ItemStackHandler();
  
  private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createInventory);
  
  public VacuumChestTileEntity()
  {
    super(ModTiles.VACUUM_CHEST);
    
    control = new RedstoneControl(this, RedstoneMode.IGNORE);
  }
  
  private ItemStackHandler createInventory()
  {
    return new ItemStackHandler(6);
  }
  
  @Override
  public void tick()
  {
    if (!world.isRemote)
    {
      tick++;
  
      CommonConfig config = CommonConfig.instance();
  
      int r = config.vacuumRange.get();
      if (r != radius)
      {
        bound = null;
        radius = r;
      }
  
      if (bound == null)
      {
        bound = Utilities.getBoundingBox(pos, radius);
      }
  
      if (control.update())
      {
        if (tick % config.vacuumTickRate.get() == 0)
        {
          List<ItemEntity> entities = world.getEntitiesWithinAABB(EntityType.ITEM, bound, (e) -> canInsert(((ItemEntity) e).getItem()));
          for (ItemEntity entity : entities)
          {
            if (!filter(entity.getItem()))
            {
              continue;
            }
      
            inventory.ifPresent(
              handler ->
              {
                ItemStack stack = entity.getItem();
                for (int i = 0; i < handler.getSlots(); i++)
                {
                  stack = handler.insertItem(i, stack, false);
                  if (stack == ItemStack.EMPTY)
                  {
                    break;
                  }
                }
                if (stack == ItemStack.EMPTY)
                {
                  entity.remove();
                  PlayEvent.redstoneParticle(world, entity.getPositionVec(), new Vec3d(0, .1 * world.rand.nextDouble(), 0), 0xffffffff);
                }
                else
                {
                  entity.setItem(stack);
                }
              }
            );
          }
          
          control.resetCooldown();
        }
      }
    }
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
  
  private boolean canInsert(ItemStack item)
  {
    return StackUtil.canInsert(inventory, item, false);
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    tick = NBTHelper.getInt(compound, "tick");
    
    if (compound.contains("inventory"))
    {
      inventory.ifPresent(handler -> handler.deserializeNBT(compound.getCompound("inventory")));
    }
    
    if (compound.contains("filter"))
    {
      filter.deserializeNBT(compound.getCompound("filter"));
    }
    
    if (compound.contains("control"))
    {
      control.deserialize(compound.getCompound("control"));
    }
    
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    compound.putInt("tick", tick);
    
    inventory.ifPresent(handler -> compound.put("inventory", handler.serializeNBT()));
    
    compound.put("filter", filter.serializeNBT());
    
    compound.put("control", control.serialize());
    
    return super.write(compound);
  }
  
  @Override
  public void dropContents()
  {
    inventory.ifPresent(handler ->
    {
      for (int i = 0; i < handler.getSlots(); i++)
      {
        InventoryHelper.spawnItemStack(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, handler.getStackInSlot(i));
      }
    });
    
    if (!filter.getStackInSlot(0).isEmpty())
    {
      InventoryHelper.spawnItemStack(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, filter.getStackInSlot(0));
    }
  }
  
  public LazyOptional<ItemStackHandler> getInventory()
  {
    return inventory;
  }
  
  @Override
  public ITextComponent getDisplayName()
  {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player)
  {
    return new VacuumChestContainer(id, world, pos, inventory);
  }
  
  private boolean filter(ItemStack stack)
  {
    ItemStack filter = this.filter.getStackInSlot(0);
    
    // Fixme: when Item Filters are updated
//    if (Flags.isItemFiltersLoaded())
//    {
//      return ItemFiltersWrapper.filter(filter, stack);
//    }
    
    return filter.isEmpty() || filter.getItem() == stack.getItem() && Objects.equals(stack.getTag(), filter.getTag());
  }
  
  public ItemStackHandler getFilter()
  {
    return filter;
  }
  
  @Override
  public RedstoneControl getControl()
  {
    return control;
  }
  
  @Override
  public RedstoneMode[] availableModes()
  {
    return new RedstoneMode[]{ RedstoneMode.IGNORE, RedstoneMode.ON, RedstoneMode.OFF };
  }
}
