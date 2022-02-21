package com.globbypotato.rockhounding_chemistry.machines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.Rhchemistry;
import com.globbypotato.rockhounding_chemistry.enums.machines.EnumMachinesF;
import com.globbypotato.rockhounding_chemistry.handlers.GuiHandler;
import com.globbypotato.rockhounding_chemistry.machines.io.MachineIO;
import com.globbypotato.rockhounding_chemistry.machines.tile.TETubularBedBase;
import com.globbypotato.rockhounding_chemistry.machines.tile.TETubularBedLow;
import com.globbypotato.rockhounding_chemistry.machines.tile.TETubularBedMid;
import com.globbypotato.rockhounding_chemistry.machines.tile.TETubularBedController;
import com.globbypotato.rockhounding_chemistry.machines.tile.TETubularBedTank;
import com.globbypotato.rockhounding_chemistry.machines.tile.TETubularBedTop;
import com.globbypotato.rockhounding_chemistry.machines.tile.TileTank;
import com.globbypotato.rockhounding_core.machines.tileentity.IFluidHandlingTile;
import com.globbypotato.rockhounding_core.machines.tileentity.TileEntityInv;
import com.globbypotato.rockhounding_core.utils.CoreUtils;
import com.globbypotato.rockhounding_core.utils.MachinesUtils;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class MachinesF extends MachineIO {
	public static PropertyEnum VARIANT = PropertyEnum.create("variant", EnumMachinesF.class);

	public MachinesF(String name) {
		super(name, Material.IRON, EnumMachinesF.getNames(), 3.0F, 5.0F, SoundType.METAL);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumMachinesF.values()[0]).withProperty(FACING, EnumFacing.NORTH));
	}



	//---------- VARIANT HANDLER ----------
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumMachinesF.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumMachinesF)state.getValue(VARIANT)).ordinal();
	}

	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	TileEntityInv tile = (TileEntityInv)world.getTileEntity(pos);
    	return state.withProperty(FACING, EnumFacing.getFront(tile.facing));
    }

	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[] { VARIANT, FACING });
	}



	//---------- BLOCK HANDLER ----------
    @Override
	public boolean hiddenParts(int meta) {
		return meta == EnumMachinesF.TUBULAR_BED_LOW.ordinal()
			|| meta == EnumMachinesF.TUBULAR_BED_TOP.ordinal()
			|| meta == EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal();
	}

    @Override
	public boolean baseParts(int meta) {
		return meta == EnumMachinesF.TUBULAR_BED_BASE.ordinal()
			|| meta == EnumMachinesF.TUBULAR_BED_MID.ordinal()
			|| meta == EnumMachinesF.TUBULAR_BED_TANK.ordinal();
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
    	super.onBlockPlacedBy(world, pos, state, placer, stack);

    	int meta = stack.getItemDamage();
    	EnumFacing isFacing = EnumFacing.getFront(2);
        world.setBlockState(pos, state.withProperty(VARIANT, EnumMachinesF.values()[meta]).withProperty(FACING, isFacing), 2);

        TileEntity te = world.getTileEntity(pos);
        if(world.getTileEntity(pos) != null){

	        if(te instanceof TETubularBedBase){
	        	TETubularBedBase reactor = (TETubularBedBase) world.getTileEntity(pos);
	        	setOrDropBlock(world, state, pos, reactor.getFacing(), placer, EnumMachinesF.TUBULAR_BED_LOW);
	        }

	        if(te instanceof TETubularBedMid){
	        	TETubularBedMid reactor = (TETubularBedMid) world.getTileEntity(pos);
	        	setOrDropBlock(world, state, pos, reactor.getFacing(), placer, EnumMachinesF.TUBULAR_BED_TOP);
	        }

	        if(te instanceof TETubularBedTank){
	        	TETubularBedTank reactor = (TETubularBedTank) world.getTileEntity(pos);
	        	setOrDropBlock(world, state, pos, reactor.getFacing(), placer, EnumMachinesF.TUBULAR_BED_CONTROLLER);
	        }

        }
    }

	@Override
	public void checkFullBlocks(World world, BlockPos pos, IBlockState state) {
		int meta = state.getBlock().getMetaFromState(state);
		
		if(meta == EnumMachinesF.TUBULAR_BED_BASE.ordinal()){
			checkTopBlocks(world, world.getBlockState(pos), world.getBlockState(pos.up()), pos);
		}
		if(meta == EnumMachinesF.TUBULAR_BED_LOW.ordinal()){
			checkBaseBlocks(world, world.getBlockState(pos.down()), pos);
		}
		if(meta == EnumMachinesF.TUBULAR_BED_MID.ordinal()){
			checkTopBlocks(world, world.getBlockState(pos), world.getBlockState(pos.up()), pos);
		}
		if(meta == EnumMachinesF.TUBULAR_BED_TOP.ordinal()){
			checkBaseBlocks(world, world.getBlockState(pos.down()), pos);
		}
		if(meta == EnumMachinesF.TUBULAR_BED_TANK.ordinal()){
			checkTopBlocks(world, world.getBlockState(pos), world.getBlockState(pos.up()), pos);
		}
		if(meta == EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal()){
			checkBaseBlocks(world, world.getBlockState(pos.down()), pos);
		}

	}

	private void setOrDropBlock(World world, IBlockState state, BlockPos pos, EnumFacing facing, EntityLivingBase placer, EnumMachinesF prop) {
		if(world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos)){
			world.setBlockState(pos.up(), this.getDefaultState().withProperty(VARIANT, prop).withProperty(FACING, facing), 2);
			TileEntityInv top = (TileEntityInv)world.getTileEntity(pos.up());
			top.facing = facing.ordinal();
		}else{
			int meta = state.getBlock().getMetaFromState(state);
			if(meta == EnumMachinesF.TUBULAR_BED_BASE.ordinal()
			|| meta == EnumMachinesF.TUBULAR_BED_MID.ordinal()
			|| meta == EnumMachinesF.TUBULAR_BED_TANK.ordinal()){
				TileEntity te = world.getTileEntity(pos);
				ItemStack itemstack = this.getSilkTouchDrop(state);
				handleTileNBT(te, itemstack);
		        spawnAsEntity(world, pos, itemstack);
			}else{
	            this.dropBlockAsItem(world, pos, state, 0);
			}
            world.setBlockToAir(pos);
		}
	}

    private void checkTopBlocks(World world, IBlockState state, IBlockState stateUp, BlockPos pos) {
		int meta = state.getBlock().getMetaFromState(state);
		TileEntity te = world.getTileEntity(pos);
		TileEntity teUp = world.getTileEntity(pos.up());
		if(teUp == null || 
				(
 				    (te instanceof TETubularBedBase && !(teUp instanceof TETubularBedLow))
			     || (te instanceof TETubularBedMid && !(teUp instanceof TETubularBedTop))
			     || (te instanceof TETubularBedTank && !(teUp instanceof TETubularBedController))
				)
		){
			ItemStack itemstack = this.getSilkTouchDrop(state);
			if(		   meta == EnumMachinesF.TUBULAR_BED_BASE.ordinal()
					|| meta == EnumMachinesF.TUBULAR_BED_MID.ordinal()
					|| meta == EnumMachinesF.TUBULAR_BED_TANK.ordinal()
			){
				handleTileNBT(te, itemstack);
			}
	        spawnAsEntity(world, pos, itemstack);
	        world.setBlockToAir(pos);
		}
	}

	private static void checkBaseBlocks(World world, IBlockState state, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		TileEntity teDown = world.getTileEntity(pos.down());
		if(teDown == null || 
				(
					   (te instanceof TETubularBedLow && !(teDown instanceof TETubularBedBase))
				   ||  (te instanceof TETubularBedTop && !(teDown instanceof TETubularBedMid))
				   ||  (te instanceof TETubularBedController && !(teDown instanceof TETubularBedTank))
				)
		){
			world.setBlockToAir(pos);
		}
	}

	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		int meta = state.getBlock().getMetaFromState(state);
        return FULL_BLOCK_AABB;
    }

	public boolean canEmitSignal(IBlockState state){
		int meta = state.getBlock().getMetaFromState(state);
        return false/*meta == EnumMachinesF.SLURRY_DRUM.ordinal()*/;
	}

	@Override
    public boolean canProvidePower(IBlockState state){
        return canEmitSignal(state);
    }

	@Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
        return canEmitSignal(state);
    }

	@Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
        return blockState.getWeakPower(blockAccess, pos, side);
    }

	@Override
    public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side){
		int currentPower = 0;
        TileEntity te = world.getTileEntity(pos);
        if(te != null){
        	if(te instanceof TileTank){
	        	TileTank tank = (TileTank)te;
	        	currentPower = tank.emittedPower();
        	}
        }
        return currentPower;
    }



	//---------- TILE HANDLER ----------
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		int meta = state.getBlock().getMetaFromState(state);
		if(meta == EnumMachinesF.TUBULAR_BED_BASE.ordinal()){
			return new TETubularBedBase();
		}
		if(meta == EnumMachinesF.TUBULAR_BED_LOW.ordinal()){
			return new TETubularBedLow();
		}
		if(meta == EnumMachinesF.TUBULAR_BED_MID.ordinal()){
			return new TETubularBedMid();
		}
		if(meta == EnumMachinesF.TUBULAR_BED_TOP.ordinal()){
			return new TETubularBedTop();
		}
		if(meta == EnumMachinesF.TUBULAR_BED_TANK.ordinal()){
			return new TETubularBedTank();
		}
		if(meta == EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal()){
			return new TETubularBedController();
		}
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if (!world.isRemote) {
			int meta = state.getBlock().getMetaFromState(state);
			if(world.getTileEntity(pos) != null){
				if(world.getTileEntity(pos) instanceof IFluidHandlingTile){
					if(!player.getHeldItemMainhand().isEmpty()){
						if (CoreUtils.isBucketType(player.getHeldItemMainhand())){
							((IFluidHandlingTile)world.getTileEntity(pos)).interactWithFluidHandler(player, hand, world, pos, facing);
							return true;
						}
					}
				}

				if(hasNullifier(player, hand)){
					handleNullifier(world, pos, player, hand, state.getBlock(), meta);
					return false;
				}
				if(meta == EnumMachinesF.TUBULAR_BED_LOW.ordinal()){
		    		player.openGui(Rhchemistry.instance, GuiHandler.tubular_bed_mid_id, world, pos.getX(), pos.getY(), pos.getZ());
				}
				if(meta == EnumMachinesF.TUBULAR_BED_BASE.ordinal()){
		    		player.openGui(Rhchemistry.instance, GuiHandler.tubular_bed_mid_id, world, pos.getX(), pos.getY() + 1, pos.getZ());
				}
				if(meta == EnumMachinesF.TUBULAR_BED_MID.ordinal()){
					if(world.getTileEntity(pos.down(1)) != null && world.getTileEntity(pos.down(1)) instanceof TETubularBedLow){
			    		player.openGui(Rhchemistry.instance, GuiHandler.tubular_bed_mid_id, world, pos.getX(), pos.getY() - 1, pos.getZ());
					}
				}
				if(meta == EnumMachinesF.TUBULAR_BED_TOP.ordinal()){
					if(world.getTileEntity(pos.down(2)) != null && world.getTileEntity(pos.down(2)) instanceof TETubularBedLow){
			    		player.openGui(Rhchemistry.instance, GuiHandler.tubular_bed_mid_id, world, pos.getX(), pos.getY() - 2, pos.getZ());
					}
				}
				if(meta == EnumMachinesF.TUBULAR_BED_TANK.ordinal()){
		    		player.openGui(Rhchemistry.instance, GuiHandler.tubular_bed_monitor_id, world, pos.getX(), pos.getY() + 1, pos.getZ());
				}
				if(meta == EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal()){
		    		player.openGui(Rhchemistry.instance, GuiHandler.tubular_bed_monitor_id, world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
		return true;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		IBlockState state = world.getBlockState(pos);
		int meta = state.getBlock().getMetaFromState(state);

		if(CoreUtils.hasWrench(player)){
			handleRotation(world, pos, player, meta);
		}
	}



	//---------- DROP HANDLER ----------
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		int meta = state.getBlock().getMetaFromState(state);
		return meta != EnumMachinesF.TUBULAR_BED_LOW.ordinal()
			&& meta != EnumMachinesF.TUBULAR_BED_TOP.ordinal()
			&& meta != EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal()
			? Item.getItemFromBlock(this) : null;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		int meta = state.getBlock().getMetaFromState(state);
		return meta != EnumMachinesF.TUBULAR_BED_LOW.ordinal()
			&& meta != EnumMachinesF.TUBULAR_BED_TOP.ordinal()
			&& meta != EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal();
	}

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		int meta = state.getBlock().getMetaFromState(state);
		if(meta == EnumMachinesF.TUBULAR_BED_LOW.ordinal()){
			return new ItemStack(Item.getItemFromBlock(this), 1, EnumMachinesF.TUBULAR_BED_BASE.ordinal());
		}
		if(meta == EnumMachinesF.TUBULAR_BED_TOP.ordinal()){
			return new ItemStack(Item.getItemFromBlock(this), 1, EnumMachinesF.TUBULAR_BED_MID.ordinal());
		}
		if(meta == EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal()){
			return new ItemStack(Item.getItemFromBlock(this), 1, EnumMachinesF.TUBULAR_BED_TANK.ordinal());
		}
		return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack){
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);
        List<ItemStack> items = new ArrayList<ItemStack>();
        int meta = this.getMetaFromState(state);
        ItemStack itemstack = ItemStack.EMPTY;
        if(meta != EnumMachinesF.TUBULAR_BED_LOW.ordinal()
        && meta != EnumMachinesF.TUBULAR_BED_TOP.ordinal()
        && meta != EnumMachinesF.TUBULAR_BED_CONTROLLER.ordinal()
        ){
        	itemstack = new ItemStack(this, 1, meta);
        }
        handleTileNBT(te, itemstack);
        if (!itemstack.isEmpty()){
        	items.add(itemstack);
        }
        ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
        for (ItemStack item : items){
        	spawnAsEntity(worldIn, pos, item);
        }
    }

	public void handleTileNBT(TileEntity te, ItemStack itemstack) {
        if(te != null){

    		MachinesUtils.addMachineNbt(itemstack, te);

        }
	}

}