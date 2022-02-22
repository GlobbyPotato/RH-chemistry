package com.globbypotato.rockhounding_chemistry.machines.recipe.construction;

import net.minecraft.item.ItemStack;

public class SeasoningRackRecipe {

	private ItemStack input, output;
	private String oredict;
	private boolean type;

	public SeasoningRackRecipe(ItemStack input, String oredict, boolean type, ItemStack output){
		this.input = input;
		this.output = output;
		this.oredict = oredict;
		this.type = type;
	}

	public SeasoningRackRecipe(ItemStack input, ItemStack output){
		this(input, "", false, output);
	}

	public SeasoningRackRecipe(String oredict, ItemStack output){
		this(ItemStack.EMPTY, oredict, true, output);
	}

	public String getOredict(){
		return this.oredict;
	}

	public boolean getType(){
		return this.type;
	}

	public ItemStack getInput(){
		if(!this.input.isEmpty()) return this.input.copy();
		return ItemStack.EMPTY;
	}

	public ItemStack getOutput(){
		if(!this.output.isEmpty()) return this.output.copy();
		return ItemStack.EMPTY;
	}

}