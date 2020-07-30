package com.wtbw.mods.machines.tile.micro_miner;

import com.google.common.base.Strings;
import com.wtbw.mods.core.WTBWCore;
import com.wtbw.mods.core.block.WTBWCoreBlocks;
import com.wtbw.mods.lib.network.RequestGuiUpdatePacket;
import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.config.CommonConfig;
import com.wtbw.mods.machines.gui.container.MicroMinerContainer;
import com.wtbw.mods.machines.recipe.MiningRecipe;
import com.wtbw.mods.machines.recipe.ModRecipes;
import com.wtbw.mods.machines.tile.ModTiles;
import com.wtbw.mods.machines.tile.base.BaseMachineEntity;
import com.wtbw.mods.machines.tile.base.multi.BlockPatternFeedback;
import com.wtbw.mods.machines.tile.base.multi.MultiBlockPattern;
import com.wtbw.mods.machines.tile.multi.EnergyInputHatchTile;
import com.wtbw.mods.machines.tile.multi.FluidInputHatchTile;
import com.wtbw.mods.machines.tile.multi.ItemOutputHatchTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class MicroMinerTile extends BaseMachineEntity
{
  public static final int MINER_SLOT = 0;
  
  private final BlockPos itemOutputPos = new BlockPos(-1, 0, 1);
  private final BlockPos fluidInputPos = new BlockPos(1, 0, 1);
  private final BlockPos energyInputPos = new BlockPos(0, 0, 2);
  
  private ItemOutputHatchTile itemOutput;
  private FluidInputHatchTile fluidInput;
  private EnergyInputHatchTile energyInput;
  
  private MultiBlockPattern pattern = new MultiBlockPattern()
    .add(-1, 0, 0, WTBWCoreBlocks.REINFORCED_GROUNDIUM)
    .add(1, 0, 0, WTBWCoreBlocks.REINFORCED_GROUNDIUM)
    .add(itemOutputPos, ModBlocks.ITEM_OUTPUT_HATCH)
    .add(0, 0, 1, WTBWCoreBlocks.REINFORCED_GROUNDIUM)
    .add(fluidInputPos, ModBlocks.FLUID_INPUT_HATCH)
    .add(-1, 0, 2, WTBWCoreBlocks.REINFORCED_GROUNDIUM)
    .add(energyInputPos, ModBlocks.ENERGY_INPUT_HATCH)
    .add(1, 0, 2, WTBWCoreBlocks.REINFORCED_GROUNDIUM)
    
    .add(-1, 1, 0, ModBlocks.DARK_CRYSTAL_BLOCK)
    .add(0, 1, 0, WTBWCoreBlocks.GROUNDIUM)
    .add(1, 1, 0, ModBlocks.DARK_CRYSTAL_BLOCK)
    .add(-1, 1, 1, WTBWCoreBlocks.GROUNDIUM)
    .add(0, 1, 1, ModBlocks.DARK_CRYSTAL_BLOCK)
    .add(1, 1, 1, WTBWCoreBlocks.GROUNDIUM)
    .add(-1, 1, 2, ModBlocks.DARK_CRYSTAL_BLOCK)
    .add(0, 1, 2, WTBWCoreBlocks.GROUNDIUM)
    .add(1, 1, 2, ModBlocks.DARK_CRYSTAL_BLOCK)
    
    .add(-1, 2, 0, WTBWCoreBlocks.GROUNDIUM)
    .add(-1, 2, 2, WTBWCoreBlocks.GROUNDIUM)
    .add(1, 2, 0, WTBWCoreBlocks.GROUNDIUM)
    .add(1, 2, 2, WTBWCoreBlocks.GROUNDIUM)
    
    .add(-1, 3, 0, WTBWCoreBlocks.GROUNDIUM)
    .add(-1, 3, 2, WTBWCoreBlocks.GROUNDIUM)
    .add(1, 3, 0, WTBWCoreBlocks.GROUNDIUM)
    .add(1, 3, 2, WTBWCoreBlocks.GROUNDIUM);

  private BlockPatternFeedback lastCheck;
  private ItemStackHandler inventory;
  private final LazyOptional<ItemStackHandler> inventoryOpt = LazyOptional.of(this::getInventory);
  
  private MiningRecipe recipe = null;
  
  private boolean mining = false;
  private int duration;
  private int progress;
  
  public MicroMinerTile()
  {
    super(ModTiles.MICRO_MINER, 0, 0, RedstoneMode.IGNORE);
    
    manager
      .register("inventory", getInventory())
      .registerInt("duration", () -> duration, (i) -> duration = i)
      .registerInt("progress", () -> progress, (i) -> progress = i)
      .registerBoolean("mining", () -> mining, (b) -> mining = b);
  }
  
  @Override
  public void tick()
  {
    boolean lastSuccess = lastCheck != null && lastCheck.success;
    BlockPatternFeedback check = checkPattern();
    if (check.success != lastSuccess)
    {
      if (check.success)
      {
        WTBWMachines.LOGGER.info("Multiblock found!");
  
        // todo: LazyOptional?
        TileEntity te = world.getTileEntity(getRelative(itemOutputPos));
        itemOutput = te instanceof ItemOutputHatchTile ? (ItemOutputHatchTile) te : null;
        te = world.getTileEntity(getRelative(fluidInputPos));
        fluidInput = te instanceof FluidInputHatchTile ? (FluidInputHatchTile) te : null;
        te = world.getTileEntity(getRelative(energyInputPos));
        energyInput = te instanceof EnergyInputHatchTile ? (EnergyInputHatchTile) te : null;
      }
      else
      {
        WTBWMachines.LOGGER.info("Invalid multiblock! expected {} at {}", check.validator.getBlock(), check.errored);
  
        itemOutput = null;
        fluidInput = null;
        energyInput = null;
  
        setOn(false);
  
        return;
      }
    }
  
  
    if (check.success)
    {
      // check for current recipe or a new one
      if (recipe == null)
      {
        recipe = getRecipe(getMiner());
      }
      else if (!mining && !recipe.miner.test(getMiner()))
      {
        // invalid miner for current recipe
        recipe = null;
      }
    
      if (recipe != null)
      {
        if (!mining)
        {
          if (enoughPower() && enoughCoolant() && isOutputEmpty() && isCoolantValid())
          {
            mining = true;
            progress = 0;
            duration = recipe.duration;
            consumeResources();
          }
        }
      
        if (mining)
        {
          process();
        }
        
        markDirty();
      }
    }
  }
  
  private boolean isOutputEmpty()
  {
    ItemStackHandler handler = getOutputInventory();
    
    for (int i = 0; i < handler.getSlots(); i++)
    {
      if (!handler.getStackInSlot(i).isEmpty())
      {
        return false;
      }
    }
    
    return true;
  }
  
  private ItemStackHandler getOutputInventory()
  {
    return itemOutput.getInventory();
  }
  
  private boolean isCoolantValid()
  {
    return fluidInput.getTank().getFluid().getFluid() == Fluids.WATER.getStillFluid();
  }
  
  private void process()
  {
    progress++;
    if (progress >= duration)
    {
      ItemStackHandler handler = getOutputInventory();
      for (int i = 0; i < recipe.output.size() && i < 9; i++)
      {
        handler.setStackInSlot(i, recipe.output.get(i).copy());
      }
      
      reset();
      
      //todo: do durability damage/consume items if needed
    }
  }
  
  private void reset()
  {
    mining = false;
    progress = 0;
    duration = 0;
    recipe = null;
  }
  
  private void consumeResources()
  {
    energyInput.getEnergyStorage().extractInternal(recipe.powerCost, false);
    FluidTank tank = fluidInput.getTank();
    FluidStack fluid = tank.getFluid();
    fluid.setAmount(fluid.getAmount() - recipe.coolantCost);
    tank.setFluid(fluid);
//    tank.drain(recipe.coolantCost, IFluidHandler.FluidAction.EXECUTE);
  
    ItemStack miner = inventory.getStackInSlot(MINER_SLOT);
    if (!miner.isEmpty())
    {
      CommonConfig config = CommonConfig.instance();
      boolean attemptDurability = config.microMinerUseDurability.get();
      if (attemptDurability)
      {
        if (miner.getMaxDamage() > 0)
        {
          miner.attemptDamageItem(1, world.rand, null);
          if (miner.getDamage() >= miner.getMaxDamage())
          {
            miner.shrink(1);
          }
        }
      }
  
      boolean consumeOnNoDurability = config.microMinerConsume.get();
      if (consumeOnNoDurability)
      {
        if (miner.getMaxDamage() == 0 || !attemptDurability)
        {
          miner.shrink(1);
        }
      }
    }
  }
  
  private boolean enoughCoolant()
  {
    return getCoolantStored() >= recipe.coolantCost;
  }
  
  private int getCoolantStored()
  {
    return fluidInput == null ? 0 : fluidInput.getTank().getFluidAmount();
  }
  
  private boolean enoughPower()
  {
    return getEnergyStored() >= recipe.powerCost;
  }
  
  private int getEnergyStored()
  {
    return energyInput == null ? 0 : energyInput.getEnergyStorage().getEnergyStored();
  }
  
  private ItemStack getMiner()
  {
    return getInventory().getStackInSlot(MINER_SLOT);
  }
  
  private BlockPatternFeedback checkPattern()
  {
    return lastCheck = pattern.check(world, pos, getFacing());
  }
  
  private BlockPos getRelative(BlockPos pos)
  {
    return MultiBlockPattern.getRelative(this.pos, pos, getFacing());
  }
  
  @Override
  protected List<ItemStackHandler> getInventories()
  {
    return Collections.singletonList(inventory);
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
  
  @Override
  protected void getExtraGuiUpdateTag(CompoundNBT nbt)
  {
    if (energyInput != null)
    {
      RequestGuiUpdatePacket.request(getRelative(energyInputPos));
    }
    
    if (fluidInput != null)
    {
      RequestGuiUpdatePacket.request(getRelative(fluidInputPos));
    }
    
    if (itemOutput != null)
    {
      RequestGuiUpdatePacket.request(getRelative(itemOutputPos));
    }
    
    manager.read(nbt);
  }
  
  @Override
  protected void handleExtraGuiUpdateTag(CompoundNBT nbt)
  {
    manager.write(nbt);
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player)
  {
    return new MicroMinerContainer(id, world, pos, inventory);
  }
  
  @Nonnull
  public List<String> getInfo()
  {
    // todo: localize it!
    List<String> info = new ArrayList<>();
    
    if (lastCheck != null)
    {
      if (lastCheck.success)
      {
        info.add("Valid multiblock");
        
        if (recipe == null)
        {
          info.add("Please provide a miner.");
        }
        else
        {
          if (!isOutputEmpty())
          {
            info.add("Waiting for output is cleared");
          }
          else
          {
            if (mining)
            {
              int perc = (int) ((progress / (float) duration) * 100);
              info.add("Mining: " + perc + "%");
            }
            else
            {
              int energy = getEnergyStored();
              if (energy < recipe.powerCost)
              {
                int perc = (int) ((energy / (float) recipe.powerCost) * 100);
                info.add("Power: " + perc + "%");
              }
              else
              {
                info.add("Power: Ready");
              }
  
              if (!isCoolantValid())
              {
                info.add("Invalid Coolant!");
                info.add(I18n.format(Fluids.WATER.getStillFluid().getAttributes().getTranslationKey()) + " expected!");
              }
              else
              {
                int coolant = getCoolantStored();
                if (coolant < recipe.coolantCost)
                {
                  int perc = (int) ((coolant / (float) recipe.coolantCost) * 100);
                  info.add("Coolant: " + perc + "%");
                }
                else
                {
                  info.add("Coolant: Ready");
                }
              }
            }
          }
        }
      }
      else
      {
        info.add("Invalid multi block.");
        info.add("Expected " + I18n.format(lastCheck.validator.getBlock().getTranslationKey()));
        info.add("At " + prettyfy(lastCheck.errored));
      }
    }
    
    return info;
  }
  
  private String prettyfy(BlockPos pos)
  {
    return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
  }
  
  @Nonnull
  public ItemStackHandler getInventory()
  {
    if (inventory == null)
    {
      inventory = new ItemStackHandler(1)
      {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
          return MicroMinerTile.this.isItemValid(slot, stack);
        }
      };
    }
    
    return inventory;
  }
  
  private boolean isItemValid(int slot, ItemStack stack)
  {
    return slot == MINER_SLOT && getRecipe(stack) != null;
  }
  
  private MiningRecipe getRecipe(ItemStack stack)
  {
    return Utilities.getRecipe(world, ModRecipes.MINING, stack);
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inventoryOpt.cast();
    }
    
    return super.getCapability(cap, side);
  }
}
