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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class DehydratingRecipe implements IRecipe<IInventory>
{
  public static final Serializer SERIALIZER = new Serializer();
  public final ResourceLocation location;
  public final Ingredient ingredient;
  public final ItemStack output;
  public final int duration;
  public final int powerCost;
  
  public DehydratingRecipe(ResourceLocation location, Ingredient ingredient, ItemStack output, int duration, int powerCost)
  {
    this.location = location;
    this.ingredient = ingredient;
    this.output = output;
    this.duration = duration;
    this.powerCost = powerCost;
  }
  
  @Override
  public boolean matches(IInventory inv, World world)
  {
    return ingredient.test(inv.getStackInSlot(0));
  }
  
  @Nonnull
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
  
  @Nonnull
  @Override
  public ItemStack getRecipeOutput()
  {
    return output;
  }
  
  @Nonnull
  @Override
  public ResourceLocation getId()
  {
    return location;
  }
  
  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer()
  {
    return SERIALIZER;
  }
  
  @Nonnull
  @Override
  public IRecipeType<?> getType()
  {
    return ModRecipes.DEHYDRATING;
  }
  
  @Override
  public NonNullList<Ingredient> getIngredients()
  {
    NonNullList<Ingredient> nonNullList = NonNullList.create();
    nonNullList.add(ingredient);
    return nonNullList;
  }
  
  @Override
  public String getGroup()
  {
    return "dehydrating";
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DehydratingRecipe>
  {
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "dehydrating");
    }
  
    @Override
    public DehydratingRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      JsonElement element = (JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
      Ingredient ingredient = Ingredient.deserialize(element);
      String result = JSONUtils.getString(json, "result");
      int count = JSONUtils.getInt(json, "count", 1);
      
      ResourceLocation resultLocation = new ResourceLocation(result);
      
      ItemStack resultStack = new ItemStack(Registry.ITEM.getValue(resultLocation)
        .orElseThrow(() -> new IllegalArgumentException("Item " + result + " does not exist")), count);
  
      int powerCost = JSONUtils.getInt(json, "powerCost", 500);
      int duration = JSONUtils.getInt(json, "duration", 1200);
      
      return new DehydratingRecipe(recipeId, ingredient, resultStack, duration, powerCost);
    }
  
    @Nullable
    @Override
    public DehydratingRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient ingredient = Ingredient.read(buffer);
      ItemStack output = buffer.readItemStack();
      int duration = buffer.readInt();
      int powerCost = buffer.readInt();
      return new DehydratingRecipe(recipeId, ingredient, output, duration, powerCost);
    }
  
    @Override
    public void write(PacketBuffer buffer, DehydratingRecipe recipe)
    {
      recipe.ingredient.write(buffer);
      buffer.writeItemStack(recipe.output);
      buffer.writeInt(recipe.duration);
      buffer.writeInt(recipe.powerCost);
    }
  }
}
