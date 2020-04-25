package com.wtbw.mods.machines.recipe;

import com.wtbw.mods.machines.WTBWMachines;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/*
  @author: Naxanria
*/

public class ModRecipes
{
  
  
  public static void init()
  {}
  
  public static final IRecipeType<DehydratingRecipe> DEHYDRATING = register("dehydrating");
  public static final IRecipeType<CompressingRecipe> COMPRESSING = register("compressing");
  public static final IRecipeType<CrushingRecipe> CRUSHING = register("crushing");
  public static final IRecipeType<PoweredFurnaceRecipe> POWERED_FURNACE = register("powered_furnace");
  public static final IRecipeType<HydratingRecipe> HYDRATING = register("hydrating");

  private static <T extends IRecipe<?>> IRecipeType<T> register(final String key)
  {
    return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(WTBWMachines.MODID, key), new IRecipeType<T>()
    {
      @Override
      public String toString()
      {
        return key;
      }
    });
  }
}
