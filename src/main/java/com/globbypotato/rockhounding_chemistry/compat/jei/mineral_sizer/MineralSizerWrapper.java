package com.globbypotato.rockhounding_chemistry.compat.jei.mineral_sizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.globbypotato.rockhounding_chemistry.compat.jei.RHRecipeWrapper;
import com.globbypotato.rockhounding_chemistry.machines.recipe.MineralSizerGearRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.MineralSizerRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.MineralSizerGearRecipe;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.MineralSizerRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MineralSizerWrapper extends RHRecipeWrapper<MineralSizerRecipe>{

	public MineralSizerWrapper(@Nonnull MineralSizerRecipe recipe) {
		super(recipe);
	}

	public static List<MineralSizerWrapper> getRecipes() {
		List<MineralSizerWrapper> recipes = new ArrayList<>();
		for (MineralSizerRecipe recipe : MineralSizerRecipes.mineral_sizer_recipes) {
			if(isValidRecipe(recipe)){
				recipes.add(new MineralSizerWrapper(recipe));
			}
		}
		return recipes;
	}

	private static boolean isValidRecipe(MineralSizerRecipe recipe){
		return ((!recipe.getType() && !recipe.getInput().isEmpty()) || (recipe.getType() && OreDictionary.getOres(recipe.getOredict()).size() > 0))
			&& recipe.getOutput() != null
			&& recipe.getOutput().size() > 0;
	}

	public List<ItemStack> getInputs() {
		ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
		if(getRecipe().getType()){
			inputs.addAll(OreDictionary.getOres(getRecipe().getOredict()));
		}else{
			inputs.add(getRecipe().getInput());
		}
		return inputs;
	}

	@Nonnull
	public ArrayList<Integer> getComminutions() {
		return getRecipe().getComminution();
	}

	@Nonnull
	public List<ItemStack> getOutputs() {
		List<ItemStack> outputs = new ArrayList<ItemStack>();
		outputs.addAll(getRecipe().getOutput());
		return outputs;
	}

	@Nonnull
	public List<ItemStack> getGears() {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for (MineralSizerGearRecipe recipe : MineralSizerGearRecipes.mineral_sizer_gears) {
			stacks.add(recipe.getGear());
		}
		return stacks;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(getInputs(), getGears()));
		ingredients.setOutputs(VanillaTypes.ITEM, getOutputs());
	}

}