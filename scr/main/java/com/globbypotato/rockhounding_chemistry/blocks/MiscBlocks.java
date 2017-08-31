package com.globbypotato.rockhounding_chemistry.blocks;

import java.util.Random;

import com.globbypotato.rockhounding_chemistry.enums.EnumMiscBlocks;
import com.globbypotato.rockhounding_core.blocks.itemblocks.BaseMetaIB;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MiscBlocks extends BlockIO {
	public static final PropertyEnum VARIANT = PropertyEnum.create("type", EnumMiscBlocks.class);
	Random rand = new Random();

    public MiscBlocks(Material material, String[] array, float hardness, float resistance, String name, SoundType stepSound){
        super(material, array, hardness, resistance, name, stepSound);
		GameRegistry.register(new BaseMetaIB(this, EnumMiscBlocks.getNames()).setRegistryName(name));
		setHarvestLevel("pickaxe", 1);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumMiscBlocks.values()[0]));
    }

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumMiscBlocks.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumMiscBlocks)state.getValue(VARIANT)).ordinal();
	}

	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

}