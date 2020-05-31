package com.wtbw.mods.machines.integration.jei;

import com.google.common.base.Stopwatch;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.integration.jei.category.*;
import com.wtbw.mods.machines.recipe.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

/*
  @author: Sunekaer, Naxanria
*/
@JeiPlugin
public class JEIPlugin implements IModPlugin
{
  private static final ResourceLocation UID = new ResourceLocation(WTBWMachines.MODID, WTBWMachines.MODID);
  
  @Override
  public ResourceLocation getPluginUid()
  {
    return UID;
  }
  
  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
  {
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_FURNACE), VanillaRecipeCategoryUid.FURNACE);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.GOLD_FURNACE), VanillaRecipeCategoryUid.FURNACE);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.DIAMOND_FURNACE), VanillaRecipeCategoryUid.FURNACE);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.END_FURNACE), VanillaRecipeCategoryUid.FURNACE);
    
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_FURNACE), VanillaRecipeCategoryUid.FURNACE);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_FURNACE), PoweredFurnaceCategory.UID);
    
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRUSHER), CrusherCategory.UID);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.DEHYDRATOR), DehydratingCategory.UID);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR), CompressingCategory.UID);
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.HYDRATOR), HydratingCategory.UID);
    
    registration.addRecipeCatalyst(new ItemStack(ModBlocks.MICRO_MINER), MiningCategory.UID);
  }
  
  @Override
  public void registerCategories(IRecipeCategoryRegistration registration)
  {
    IJeiHelpers jeiHelpers = registration.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
    
    registration.addRecipeCategories(new CrusherCategory(guiHelper));
    registration.addRecipeCategories(new DehydratingCategory(guiHelper));
    registration.addRecipeCategories(new HydratingCategory(guiHelper));
    registration.addRecipeCategories(new CompressingCategory(guiHelper));
    registration.addRecipeCategories(new PoweredFurnaceCategory(guiHelper));
    registration.addRecipeCategories(new MiningCategory(guiHelper));
  }
  
  @Override
  public void registerRecipes(IRecipeRegistration registration)
  {
    ClientWorld world = Minecraft.getInstance().world;
    RecipeManager recipeManager = world.getRecipeManager();
    Stopwatch sw = Stopwatch.createStarted();
    
    registration.addRecipes(Utilities.getRecipes(recipeManager, ModRecipes.CRUSHING), CrusherCategory.UID);
    registration.addRecipes(Utilities.getRecipes(recipeManager, ModRecipes.DEHYDRATING), DehydratingCategory.UID);
    registration.addRecipes(Utilities.getRecipes(recipeManager, ModRecipes.HYDRATING), HydratingCategory.UID);
    registration.addRecipes(Utilities.getRecipes(recipeManager, ModRecipes.COMPRESSING), CompressingCategory.UID);
    registration.addRecipes(Utilities.getRecipes(recipeManager, ModRecipes.POWERED_FURNACE), PoweredFurnaceCategory.UID);
    registration.addRecipes(Utilities.getRecipes(recipeManager, ModRecipes.MINING), MiningCategory.UID);
    
    sw.stop();
    WTBWMachines.LOGGER.info("Loaded jei recipe integration in {}", sw);
  }
}
