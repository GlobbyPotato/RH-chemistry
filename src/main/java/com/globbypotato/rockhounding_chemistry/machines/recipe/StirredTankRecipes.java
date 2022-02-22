package com.globbypotato.rockhounding_chemistry.machines.recipe;

import java.util.ArrayList;

import com.globbypotato.rockhounding_chemistry.enums.materials.EnumFluid;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.StirredTankRecipe;
import com.globbypotato.rockhounding_chemistry.utils.BaseRecipes;
import com.globbypotato.rockhounding_chemistry.utils.ModUtils;
import com.globbypotato.rockhounding_core.utils.CoreBasics;
import com.globbypotato.rockhounding_core.utils.CoreUtils;

import net.minecraftforge.fml.common.Loader;

public class StirredTankRecipes extends BaseRecipes{
	public static ArrayList<StirredTankRecipe> stirred_tank_recipes = new ArrayList<StirredTankRecipe>();

	public static void machineRecipes() {
		stirred_tank_recipes.add(new StirredTankRecipe(	getFluid(EnumFluid.HYDROCHLORIC_ACID, 100),							
														getFluid(EnumFluid.METHANOL, 100),											
														getFluid(EnumFluid.CHLOROMETHANE, 150), 			
														getFluid(EnumFluid.RAW_FLUE_GAS, 50), 			
														0
		));

		stirred_tank_recipes.add(new StirredTankRecipe(	getFluid(EnumFluid.SALT_BRINE, 80),							
														CoreBasics.waterStack(100), 											
														getFluid(EnumFluid.SODIUM_HYDROXIDE, 170), 			
														null,
														4
		));

		if(Loader.isModLoaded(ModUtils.mekanism_id)){
			if(CoreUtils.fluidExists(ModUtils.mek_brine)){
				stirred_tank_recipes.add(new StirredTankRecipe(	CoreUtils.getFluid(ModUtils.mek_brine, 80),							
																CoreBasics.waterStack(100), 											
																getFluid(EnumFluid.SODIUM_HYDROXIDE, 170), 			
																null,
																4
				));
			}
		}

	}

}