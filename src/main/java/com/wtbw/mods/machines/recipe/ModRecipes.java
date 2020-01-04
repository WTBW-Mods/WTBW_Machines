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
  
  public static final IRecipeType<DryerRecipe> DRYING = register("drying");
  public static final IRecipeType<CompressingRecipe> COMPRESSING = register("compressing");

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
