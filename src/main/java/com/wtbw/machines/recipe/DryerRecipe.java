package com.wtbw.machines.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wtbw.machines.WTBWMachines;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class DryerRecipe implements IRecipe<IInventory>
{
  public static final Serializer SERIALIZER = new Serializer();
  public final ResourceLocation location;
  public final Ingredient ingredient;
  public final ItemStack output;
  public final int duration;
  
  public DryerRecipe(ResourceLocation location, Ingredient ingredient, ItemStack output, int duration)
  {
    this.location = location;
    this.ingredient = ingredient;
    this.output = output;
    this.duration = duration;
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
    return ModRecipes.DRYING;
  }
  
  @Override
  public String getGroup()
  {
    return "drying";
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DryerRecipe>
  {
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "drying");
    }
  
    @Override
    public DryerRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      JsonElement element = (JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
      Ingredient ingredient = Ingredient.deserialize(element);
      String result = JSONUtils.getString(json, "result");
      int count = JSONUtils.getInt(json, "count", 1);
      ResourceLocation resultLocation = new ResourceLocation(result);
      
      ItemStack resultStack = new ItemStack(Registry.ITEM.getValue(resultLocation)
        .orElseThrow(() -> new IllegalArgumentException("Item " + result + " does not exist")), count);
      
      int duration = JSONUtils.getInt(json, "duration", 1200);
      return new DryerRecipe(recipeId, ingredient, resultStack, duration);
    }
  
    @Nullable
    @Override
    public DryerRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient ingredient = Ingredient.read(buffer);
      ItemStack output = buffer.readItemStack();
      int duration = buffer.readInt();
      return new DryerRecipe(recipeId, ingredient, output, duration);
    }
  
    @Override
    public void write(PacketBuffer buffer, DryerRecipe recipe)
    {
      recipe.ingredient.write(buffer);
      buffer.writeItemStack(recipe.output);
      buffer.writeInt(recipe.duration);
    }
  }
}
