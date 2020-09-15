package com.wtbw.mods.machines.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.tile.micro_miner.MicroMinerTile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class MiningRecipe implements IRecipe<IInventory>
{
  public static final Serializer SERIALIZER = new Serializer();
  public final ResourceLocation location;
  public final Ingredient miner;
  public final ImmutableList<ItemStack> output;
  
  public final int duration;
  public final int powerCost;
  public final int coolantCost;
  
  public MiningRecipe(ResourceLocation location, Ingredient miner, ImmutableList<ItemStack> output, int duration, int powerCost, int coolantCost)
  {
    this.location = location;
    this.miner = miner;
    this.output = output;
    this.duration = duration;
    this.powerCost = powerCost;
    this.coolantCost = coolantCost;
  }
  
  @Override
  public boolean matches(IInventory inv, World worldIn)
  {
    return miner.test(inv.getStackInSlot(MicroMinerTile.MINER_SLOT));
  }
  
  @Override
  public ItemStack getCraftingResult(IInventory inv)
  {
    return ItemStack.EMPTY;
  }
  
  @Override
  public boolean canFit(int width, int height)
  {
    return true;
  }
  
  @Override
  public ItemStack getRecipeOutput()
  {
    return ItemStack.EMPTY;
  }
  
  @Override
  public ResourceLocation getId()
  {
    return location;
  }
  
  @Override
  public IRecipeSerializer<?> getSerializer()
  {
    return SERIALIZER;
  }
  
  @Override
  public IRecipeType<?> getType()
  {
    return ModRecipes.MINING;
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MiningRecipe>
  {
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "mining");
    }
  
    @Override
    public MiningRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      
      JsonElement element =
        JSONUtils.isJsonArray(json, "miner") ?
        JSONUtils.getJsonArray(json, "miner") :
        JSONUtils.getJsonObject(json, "miner");
      
      Ingredient miner = Ingredient.deserialize(element);
      
      int powerCost = JSONUtils.getInt(json, "power_cost", 500000);
      int duration = JSONUtils.getInt(json, "duration", 400);
      int coolantCost = JSONUtils.getInt(json, "coolant_cost", 2000);
  
      ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
  
      for (JsonElement ele : JSONUtils.getJsonArray(json, "result"))
      {
        JsonObject obj = ele.getAsJsonObject();
        String item = JSONUtils.getString(obj, "item");
        int count = JSONUtils.getInt(obj, "count", 1);
        ResourceLocation resultLocation = new ResourceLocation(item);
        ItemStack stack = new ItemStack(Registry.ITEM.func_241873_b(resultLocation)
          .orElseThrow(() -> new IllegalArgumentException("Item " + item + " does not exist")), count);
        
        builder.add(stack);
      }
      
      ImmutableList<ItemStack> output = builder.build();
      
      if (output.size() > 9)
      {
        throw new IllegalArgumentException("Maximum of 9 entries!");
      }
      
      return new MiningRecipe(recipeId, miner, output, duration, powerCost, coolantCost);
    }
  
    @Nullable
    @Override
    public MiningRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient ingredient = Ingredient.read(buffer);
      ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
      
      int size = buffer.readInt();
      for (int i = 0; i < size; i++)
      {
        ItemStack stack = buffer.readItemStack();
        builder.add(stack);
      }
  
      ImmutableList<ItemStack> output = builder.build();
  
      int powerCost = buffer.readInt();
      int duration = buffer.readInt();
      int coolantCost = buffer.readInt();
      
      return new MiningRecipe(recipeId, ingredient, output, duration, powerCost, coolantCost);
    }
  
    @Override
    public void write(PacketBuffer buffer, MiningRecipe recipe)
    {
      recipe.miner.write(buffer);
      
      buffer.writeInt(recipe.output.size());
      for (ItemStack stack : recipe.output)
      {
        buffer.writeItemStack(stack);
      }
      
      buffer.writeInt(recipe.powerCost);
      buffer.writeInt(recipe.duration);
      buffer.writeInt(recipe.coolantCost);
    }
  }
}
