package com.wtbw.mods.machines.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wtbw.mods.lib.util.BiValue;
import com.wtbw.mods.lib.util.rand.ItemStackChanceMap;
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
import java.util.List;

/*
  @author: Sunekaer
*/
public class CrushingRecipe implements IRecipe<IInventory>
{
  public static final CrushingRecipe.Serializer SERIALIZER = new Serializer();
  public final ResourceLocation location;
  public final Ingredient ingredient;
  public final ItemStackChanceMap chanceMap;
  
  public final int duration;
  public final int powerCost;
  public final int ingredientCost;
  
  public final List<BiValue<ItemStack, Float>> itemChances;
  private final List<ItemStack> maxRoll;
  
  
  public CrushingRecipe(ResourceLocation location, Ingredient ingredient, int ingredientCost, ItemStackChanceMap chanceMap, int duration, int powerCost)
  {
    this.location = location;
    this.ingredient = ingredient;
    this.chanceMap = chanceMap;
    this.duration = duration;
    this.powerCost = powerCost;
    this.ingredientCost = ingredientCost;
    
    chanceMap.setAttemptsAsCount(true);
    maxRoll = chanceMap.getMaxRoll();
    itemChances = chanceMap.getItemChances();
  }
  
  @Override
  public boolean matches(IInventory inv, World worldIn)
  {
    return ingredient.test(inv.getStackInSlot(0));
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
  public NonNullList<Ingredient> getIngredients()
  {
    NonNullList<Ingredient> nonNullList = NonNullList.create();
    nonNullList.add(ingredient);
    return nonNullList;
  }
  
  public List<ItemStack> getRecipeOutputList()
  {
    return chanceMap.getRoll();
  }
  
  public List<ItemStack> getRecipeOutputMaxList()
  {
    return maxRoll;
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
    return ModRecipes.CRUSHING;
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrushingRecipe>
  {
    
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "crushing");
    }
    
    @Override
    public CrushingRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      JsonElement element =
        JSONUtils.isJsonArray(json, "ingredient") ?
        JSONUtils.getJsonArray(json, "ingredient") :
        JSONUtils.getJsonObject(json, "ingredient");
      
      Ingredient ingredient = Ingredient.deserialize(element);
      int ingredientCost = JSONUtils.getInt(json.getAsJsonObject("ingredient"), "count", 1);
      int powerCost = JSONUtils.getInt(json, "power_cost", 100);
      ItemStackChanceMap map = new ItemStackChanceMap();
      map.setAttemptsAsCount(true);

      for (JsonElement ele : JSONUtils.getJsonArray(json, "result"))
      {
        JsonObject obj = ele.getAsJsonObject();
        String item = JSONUtils.getString(obj, "item");
        int count = JSONUtils.getInt(obj, "count", 1);
        float chance = JSONUtils.getFloat(obj, "chance", 1);
        
        ResourceLocation resultLocation = new ResourceLocation(item);
        ItemStack aStack = new ItemStack(Registry.ITEM.func_241873_b(resultLocation)
          .orElseThrow(() -> new IllegalArgumentException("Item " + item + " does not exist")), count);
        
        map.add(chance, count, aStack);
      }
      
      if (map.getEntries() > 6)
      {
        throw new IllegalArgumentException("Maximum of 6 entries! [" + map.getEntries() + "]");
      }
      
      int duration = JSONUtils.getInt(json, "duration", 1200);
      return new CrushingRecipe(recipeId, ingredient, ingredientCost, map, duration, powerCost);
    }
    
    @Nullable
    @Override
    public CrushingRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient ingredient = Ingredient.read(buffer);
      int ingredientCost = buffer.readInt();
      ItemStackChanceMap output = ItemStackChanceMap.read(buffer);
      int duration = buffer.readInt();
      int powerCost = buffer.readInt();
      return new CrushingRecipe(recipeId, ingredient, ingredientCost, output, duration, powerCost);
    }
    
    @Override
    public void write(PacketBuffer buffer, CrushingRecipe recipe)
    {
      recipe.ingredient.write(buffer);
      buffer.writeInt(recipe.ingredientCost);
      ItemStackChanceMap.write(buffer, recipe.chanceMap);
      buffer.writeInt(recipe.duration);
      buffer.writeInt(recipe.powerCost);
    }
  }
}
