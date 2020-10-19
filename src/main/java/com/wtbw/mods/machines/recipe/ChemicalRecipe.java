package com.wtbw.mods.machines.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.wtbw.mods.lib.recipe.FluidIngredient;
import com.wtbw.mods.lib.tile.util.IFluidInventory;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.tile.machine.ChemicalPlantEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class ChemicalRecipe implements IRecipe<IFluidInventory>
{
  public static final Serializer SERIALIZER = new Serializer();
  
  public final ResourceLocation location;
  public final Ingredient ingredient;
  public final int ingredientCost;
  public final FluidIngredient fluidInput;
  public final int fluidInputAmount;
  public final int duration;
  public final int energy;
  public final ItemStack outputItem;
  public final FluidStack outputFluid;
  
  public ChemicalRecipe(ResourceLocation location, Ingredient ingredient, int ingredientCost, FluidIngredient fluidInput, int fluidInputAmount, int duration, int energy, ItemStack outputItem, FluidStack outputFluid)
  {
    this.location = location;
    this.ingredient = ingredient;
    this.ingredientCost = ingredientCost;
    this.fluidInput = fluidInput;
    this.fluidInputAmount = fluidInputAmount;
    this.duration = duration;
    this.energy = energy;
    this.outputItem = outputItem;
    this.outputFluid = outputFluid;
  }
  
  @Override
  public boolean matches(IFluidInventory inv, World worldIn)
  {
    return ingredient.test(inv.getStackInSlot(ChemicalPlantEntity.INPUT_SLOT)) && fluidInput.test(inv.getFluidInTank(0));
  }
  
  @Override
  public ItemStack getCraftingResult(IFluidInventory inv)
  {
    return outputItem.copy();
  }
  
  @Override
  public boolean canFit(int width, int height)
  {
    return true;
  }
  
  @Override
  public ItemStack getRecipeOutput()
  {
    return outputItem;
  }
  
  @Override
  public NonNullList<Ingredient> getIngredients()
  {
    return NonNullList.create();
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
    return ModRecipes.CHEMICAL;
  }
  
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ChemicalRecipe>
  {
    public Serializer()
    {
      setRegistryName(WTBWMachines.MODID, "chemical");
    }
  
    @Override
    public ChemicalRecipe read(ResourceLocation recipeId, JsonObject json)
    {
      Ingredient ingredient = null;
      int ingredientCount = 1;
      FluidIngredient fluidIngredient = null;
      int fluidInputAmount = 0;
      
      ItemStack outputItem = null;
      int outputItemCount = 1;
      FluidStack outputFluid = null;
      int fluidOutputAmount = 0;
  
      JsonElement input = json.get("input");
      if (input == null || input.isJsonNull() || !input.isJsonObject())
      {
        throw new JsonSyntaxException("Expected input object!");
      }
      else
      {
        JsonObject inputObject = input.getAsJsonObject();
        JsonElement itemInput = inputObject.get("item");
        JsonElement fluidInput = inputObject.get("fluid");
    
        if (itemInput != null && !itemInput.isJsonNull())
        {
          if (!itemInput.isJsonObject())
          {
            throw new JsonSyntaxException("Item input is required to be a json object");
          }
      
          JsonObject object = itemInput.getAsJsonObject();
          ingredient = Ingredient.deserialize(itemInput);
          if (object.has("amount"))
          {
            ingredientCount = object.get("amount").getAsInt();
          }
          
          WTBWMachines.LOGGER.info("{}: input item {} ({})",
            recipeId.toString(),
            JSONUtils.getString(object, "item", "#" + JSONUtils.getString(object,"tag")),
            ingredientCount);
        }
    
        if (fluidInput != null && !fluidInput.isJsonNull())
        {
          if (!fluidInput.isJsonObject())
          {
            throw new JsonSyntaxException("Fluid input is required to be a json object");
          }
      
          String location;
          boolean tag;
          JsonObject object = fluidInput.getAsJsonObject();
          if (object.has("fluid"))
          {
            tag = false;
            location = object.get("fluid").getAsString();
          }
          else if (object.has("tag"))
          {
            tag = true;
            location = object.get("tag").getAsString();
          }
          else
          {
            throw new JsonParseException("Invalid fluid format");
          }
      
          fluidIngredient = new FluidIngredient(new ResourceLocation(location), tag);
      
          if (!object.has("amount"))
          {
            throw new JsonParseException("No input fluid amount specified");
          }
      
          fluidInputAmount = object.get("amount").getAsInt();
          
          WTBWMachines.LOGGER.info("{}: input fluid {} ({})",
            recipeId,
            (tag ? "#" : "") + location,
            fluidInputAmount);
        }
    
        if (ingredient == null && fluidIngredient == null)
        {
          throw new JsonParseException("The recipe requires at least 1 ingredient, fluid or item.");
        }
      }
  
      JsonElement output = json.get("output");
      if (output == null || output.isJsonNull() || !output.isJsonObject())
      {
        throw new JsonSyntaxException("Expected output object!");
      }
      else
      {
        JsonObject outputObject = output.getAsJsonObject();
        JsonElement itemOutput = outputObject.get("item");
        JsonElement fluidOutput = outputObject.get("fluid");
    
        if (itemOutput != null && !itemOutput.isJsonNull())
        {
          if (!itemOutput.isJsonObject())
          {
            throw new JsonSyntaxException("Item output is required to be a json object");
          }
      
          JsonObject object = itemOutput.getAsJsonObject();
          ingredient = Ingredient.deserialize(itemOutput);
          if (object.has("amount"))
          {
            outputItemCount = object.get("amount").getAsInt();
          }
  
          String location = object.get("item").getAsString();
  
          Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(location));
          if (item == null)
          {
            throw new JsonParseException("Could not find item \"" + location + "\"");
          }
          outputItem = new ItemStack(item, outputItemCount);
          
          WTBWMachines.LOGGER.info("{}: item output {} ({})",
            recipeId,
            location,
            outputItemCount);
        }
    
        if (fluidOutput != null && !fluidOutput.isJsonNull())
        {
          if (!fluidOutput.isJsonObject())
          {
            throw new JsonSyntaxException("Fluid output is required to be a json object");
          }
      
          String location;
          JsonObject object = fluidOutput.getAsJsonObject();
          if (object.has("fluid"))
          {
            location = object.get("fluid").getAsString();
          }
          else
          {
            throw new JsonParseException("Invalid fluid format");
          }
      
          if (!object.has("amount"))
          {
            throw new JsonParseException("No output fluid amount specified");
          }
      
          fluidOutputAmount = object.get("amount").getAsInt();
          Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(location));
          if (fluid == null)
          {
            throw new JsonParseException("Could not find fluid \"" + location + "\"");
          }
          
          outputFluid = new FluidStack(fluid, fluidOutputAmount);
          
          WTBWMachines.LOGGER.info("{}: fluid output {} ({})",
            recipeId,
            location,
            fluidOutputAmount
          );
        }
    
        if (itemOutput == null && outputFluid == null)
        {
          throw new JsonParseException("The recipe requires at least 1 output, fluid or item.");
        }
      }
      
      if (!json.has("duration"))
      {
        throw new JsonParseException("No duration specified");
      }
      
      int duration = json.get("duration").getAsInt();
      
      if (!json.has("power"))
      {
        throw new JsonParseException("No power cost specified");
      }
      
      int power = json.get("power").getAsInt();
      
      return new ChemicalRecipe(recipeId, ingredient, ingredientCount, fluidIngredient, fluidInputAmount, duration, power, outputItem, outputFluid);
    }
  
    @Nullable
    @Override
    public ChemicalRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
      Ingredient inputItem = null;
      int inputItemCount = 0;
      if (buffer.readBoolean())
      {
        inputItem = Ingredient.read(buffer);
        inputItemCount = buffer.readInt();
      }
      
      FluidIngredient inputFluid = null;
      int inputFluidCount = 0;
      if (buffer.readBoolean())
      {
        inputFluid = FluidIngredient.read(buffer);
        inputFluidCount = buffer.readInt();
      }
      
      ItemStack outputItem = null;
      if (buffer.readBoolean())
      {
        outputItem = buffer.readItemStack();
      }
      
      FluidStack outputFluid = null;
      if (buffer.readBoolean())
      {
        outputFluid = buffer.readFluidStack();
      }
      
      int duration = buffer.readInt();
      int power = buffer.readInt();
      
      return new ChemicalRecipe(recipeId, inputItem, inputItemCount, inputFluid, inputFluidCount, duration, power, outputItem, outputFluid);
    }
  
    @Override
    public void write(PacketBuffer buffer, ChemicalRecipe recipe)
    {
      boolean itemIn = recipe.ingredient != null;
      buffer.writeBoolean(itemIn);
      if (itemIn)
      {
        recipe.ingredient.write(buffer);
        buffer.writeInt(recipe.ingredientCost);
      }
      
      boolean fluidIn = recipe.fluidInput != null;
      buffer.writeBoolean(fluidIn);
      if (fluidIn)
      {
        recipe.fluidInput.write(buffer);
        buffer.writeInt(recipe.fluidInputAmount);
      }
      
      boolean itemOut = recipe.outputItem != null;
      buffer.writeBoolean(itemOut);
      if (itemOut)
      {
        buffer.writeItemStack(recipe.outputItem);
      }
      
      boolean fluidOut = recipe.outputFluid != null;
      buffer.writeBoolean(fluidOut);
      if (fluidOut)
      {
        buffer.writeFluidStack(recipe.outputFluid);
      }
      
      buffer.writeInt(recipe.duration);
      buffer.writeInt(recipe.energy);
    }
  }
}
