package com.wtbw.mods.machines.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wtbw.mods.lib.util.Cache;
import com.wtbw.mods.lib.util.Utilities;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/*
  @author: Sunekaer
*/
public class CompressingRecipe implements IRecipe<IInventory>
{
  public static final CompressingRecipe.Serializer SERIALIZER = new Serializer();
  public final ResourceLocation location;
  public final Ingredient ingredient;
  public final ItemStack output;
  
  public final int duration;
  public final int powerCost;
  public final int ingredientCost;
  
  private Cache<NonNullList<Ingredient>> ingredientList;
  
  public CompressingRecipe(ResourceLocation location, Ingredient ingredient, int ingredientCost, ItemStack output, int duration, int powerCost)
  {
    this.location = location;
    this.ingredient = ingredient;
    this.output = output;
    this.duration = duration;
    this.powerCost = powerCost;
    this.ingredientCost = ingredientCost;
  
    ingredientList = Cache.create(() -> Utilities.nnListOf(this.ingredient));
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
    return ModRecipes.COMPRESSING;
  }
  
  @Override
  public NonNullList<Ingredient> getIngredients()
  {
    return ingredientList.get();
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CompressingRecipe>
  {
    
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "compressing");
    }
    
    @Override
    public CompressingRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      JsonElement element = (JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
      Ingredient ingredient = Ingredient.deserialize(element);
      int ingredientCost = JSONUtils.getInt(json.getAsJsonObject("ingredient"), "count", 1);
      String result = JSONUtils.getString(json, "result");
      int count = JSONUtils.getInt(json, "count", 1);
      int powerCost = JSONUtils.getInt(json, "power_cost", 100);
      
      ResourceLocation resultLocation = new ResourceLocation(result);
      
      ItemStack resultStack = new ItemStack(Registry.ITEM.getValue(resultLocation)
        .orElseThrow(() -> new IllegalArgumentException("Item " + result + " does not exist")), count);
      
      int duration = JSONUtils.getInt(json, "duration", 1200);
      return new CompressingRecipe(recipeId, ingredient, ingredientCost, resultStack, duration, powerCost);
    }
    
    @Nullable
    @Override
    public CompressingRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient ingredient = Ingredient.read(buffer);
      int ingredientCost = buffer.readInt();
      ItemStack output = buffer.readItemStack();
      int duration = buffer.readInt();
      int powerCost = buffer.readInt();
      return new CompressingRecipe(recipeId, ingredient, ingredientCost, output, duration, powerCost);
    }
    
    @Override
    public void write(PacketBuffer buffer, CompressingRecipe recipe)
    {
      recipe.ingredient.write(buffer);
      buffer.writeInt(recipe.ingredientCost);
      buffer.writeItemStack(recipe.output);
      buffer.writeInt(recipe.duration);
      buffer.writeInt(recipe.powerCost);
    }
  }
}
