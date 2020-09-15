package com.wtbw.mods.machines.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wtbw.mods.machines.WTBWMachines;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class HydratingRecipe implements IRecipe<IInventory>
{
  public static final Serializer SERIALIZER = new Serializer();
  
  public final ResourceLocation location;
  public final Ingredient ingredient;
  public final ItemStack output;
  public final int duration;
  public final int powerCost;
  public final int waterCost;
  
  public HydratingRecipe(ResourceLocation location, Ingredient ingredient, ItemStack output, int duration, int powerCost, int waterCost)
  {
    this.location = location;
    this.ingredient = ingredient;
    this.output = output;
    this.duration = duration;
    this.powerCost = powerCost;
    this.waterCost = waterCost;
  }
  
  @Override
  public boolean matches(IInventory inv, World worldIn)
  {
    return ingredient.test(inv.getStackInSlot(0));
  }
  
  @Override
  public ItemStack getCraftingResult(IInventory inv)
  {
    return output.copy();
  }
  
  @Override
  public boolean canFit(int width, int height)
  {
    return true;
  }
  
  @Override
  public ItemStack getRecipeOutput()
  {
    return output;
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
    return ModRecipes.HYDRATING;
  }
  
  @Override
  public NonNullList<Ingredient> getIngredients()
  {
    return Util.make(NonNullList.create(), list -> list.add(ingredient));
  }
  
  @Override
  public String getGroup()
  {
    return "hydrating";
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<HydratingRecipe>
  {
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "hydrating");
    }
  
    @Override
    public HydratingRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      JsonElement element = (JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
      Ingredient ingredient = Ingredient.deserialize(element);
      String result = JSONUtils.getString(json, "result");
      int count = JSONUtils.getInt(json, "count", 1);
      ResourceLocation resultLocation = new ResourceLocation(result);
      
      ItemStack resultStack = new ItemStack(Registry.ITEM.func_241873_b(resultLocation)
        .orElseThrow(() -> new IllegalArgumentException("Item " + result + " does not exist")), count);
  
      int powerCost = JSONUtils.getInt(json, "powerCost", 500);
      int duration = JSONUtils.getInt(json, "duration", 1200);
      int waterCost = JSONUtils.getInt(json, "water", 1000);
      
      return new HydratingRecipe(recipeId, ingredient, resultStack, duration, powerCost, waterCost);
    }
  
    @Nullable
    @Override
    public HydratingRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient ingredient = Ingredient.read(buffer);
      ItemStack output = buffer.readItemStack();
      int duration = buffer.readInt();
      int powerCost = buffer.readInt();
      int waterCost = buffer.readInt();
      
      return new HydratingRecipe(recipeId, ingredient, output, duration, powerCost, waterCost);
    }
  
    @Override
    public void write(PacketBuffer buffer, HydratingRecipe recipe)
    {
      recipe.ingredient.write(buffer);
      buffer.writeItemStack(recipe.output);
      buffer.writeInt(recipe.duration);
      buffer.writeInt(recipe.powerCost);
      buffer.writeInt(recipe.waterCost);
    }
  }
}
