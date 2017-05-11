package com.globbypotato.rockhounding_chemistry.machines.gui;

import java.util.Arrays;
import java.util.List;

import com.globbypotato.rockhounding_chemistry.handlers.Reference;
import com.globbypotato.rockhounding_chemistry.machines.container.ContainerOwcController;
import com.globbypotato.rockhounding_chemistry.machines.tileentity.TileEntityOwcController;
import com.globbypotato.rockhounding_chemistry.utils.Translator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOwcController extends GuiBase {
	
	 public static ResourceLocation TEXTURE_REF = 
			 new ResourceLocation(Reference.MODID + ":textures/gui/guiowccontroller.png");
    
    private final InventoryPlayer playerInventory;
    private final TileEntityOwcController owcController;
	public static final int WIDTH = 176;
	public static final int HEIGHT = 183;

    public GuiOwcController(InventoryPlayer playerInv, TileEntityOwcController tile){
        super(tile, new ContainerOwcController(playerInv,tile));
        this.TEXTURE = TEXTURE_REF;
        this.owcController = tile;
        this.playerInventory = playerInv;
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
    }
   
    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
       super.drawScreen(mouseX, mouseY, f);
	   int x = (this.width - this.xSize) / 2;
	   int y = (this.height - this.ySize) / 2;

	   //RF label
	   String rfString = TextFormatting.DARK_GRAY + "Storage: " + TextFormatting.WHITE + this.owcController.getRedstone() + "/" + this.owcController.getChargeMax() + " RF";
	   //Tide label
	   String waterString = TextFormatting.DARK_GRAY + "Water: " + TextFormatting.AQUA + this.owcController.actualWater() + "/" + this.owcController.totalWater() + " tiles";
	   //Yeld label
	   String yeldString = TextFormatting.DARK_GRAY + "Yeld: " + TextFormatting.YELLOW + this.owcController.getYeld() + "/" + this.owcController.getYeldMax() + " RF/tide";
	   //interval label
	   int tideInterval = this.owcController.tideChance() * this.owcController.conveyorMultiplier();
	   String intervalString = TextFormatting.DARK_GRAY + "Tides: " + TextFormatting.GRAY + tideInterval + " ticks";

	   //main string
	   String multiString[] = new String[]{rfString, "", waterString, yeldString, intervalString};

	   //RF area
	   if((mouseX >= 93+x && mouseX <= 117+x && mouseY >= 16+y && mouseY <= 77+y)){
		   List<String> tooltip = Arrays.asList(multiString);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }
	   //Tide area
	   if((mouseX >= 144+x && mouseX <= 150+x && mouseY >= 16+y && mouseY <= 77+y)){
		   List<String> tooltip = Arrays.asList(waterString);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }
	   //Yeld area
	   if((mouseX >= 160+x && mouseX <= 166+x && mouseY >= 16+y && mouseY <= 77+y)){
		   List<String> tooltip = Arrays.asList(yeldString);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }

	   //Conveyor
	   if(mouseX >= 121+x && mouseX <= 138+x && mouseY >= 38+y && mouseY <= 55+y){
		   int cf = 3 - this.owcController.conveyorMultiplier();
		   String text = "Conveyor Upgrade x" + cf;
		   List<String> tooltip = Arrays.asList(text);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }
	   //Efficiency
	   if(mouseX >= 121+x && mouseX <= 138+x && mouseY >= 60+y && mouseY <= 77+y){
		   String text = "Efficiency Upgrade x" + (this.owcController.efficiencyMultiplier()-1);
		   List<String> tooltip = Arrays.asList(text);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }
	   //Duality
	   if(mouseX >= 121+x && mouseX <= 138+x && mouseY >= 16+y && mouseY <= 33+y){
		   String text = "Duality Upgrade";
		   List<String> tooltip = Arrays.asList(text);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }

	   //Activation tooltip
	   if(mouseX >= 73+x && mouseX <= 91+x && mouseY >= 16+y && mouseY <= 34+y){
		   String text = "System Activation";
		   List<String> tooltip = Arrays.asList(text);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }

	   //Extraction tooltip
	   if(mouseX >= 73+x && mouseX <= 91+x && mouseY >= 38+y && mouseY <= 56+y){
		   String text = "Extract RF";
		   List<String> tooltip = Arrays.asList(text);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }
	   
	   //Sanity check
	   if(mouseX >= 7+x && mouseX <= 70+x && mouseY >= 16+y && mouseY <= 77+y){
		   String text = "Sanity Check";
		   List<String> tooltip = Arrays.asList(text);
		   drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	   }

    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
    	super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
        String device = Translator.translateToLocal("container.owcController");
        this.fontRendererObj.drawString(device, this.xSize / 2 - this.fontRendererObj.getStringWidth(device) / 2, 6, 4210752);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
    	super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        //power bar
        if (this.owcController.hasRedstone()){
            int k = this.getBarScaled(60, this.owcController.getRedstone(), this.owcController.getChargeMax());
            this.drawTexturedModalRect(i + 94, j + 17 + (60 - k), 176, 59, 23, k);
        }
        if(this.owcController.activationKey){
	        //tide bar
	        if (this.owcController.actualWater() > 0){
	            int k = this.getBarScaled(60, this.owcController.actualWater(), this.owcController.totalWater());
	            this.drawTexturedModalRect(i + 145, j + 17 + (60 - k), 183, 119, 5, k);
	        }
	        //yeld bar
	        if (this.owcController.getYeld() > 0){
	            int k = this.getBarScaled(60, this.owcController.getYeld(), this.owcController.getYeldMax());
	            this.drawTexturedModalRect(i + 161, j + 17 + (60 - k), 176, 119, 5, k);
	        }

	        //sanity check
	        if(this.owcController.checkChamber()){
	            this.drawTexturedModalRect(i + 10, j + 22, 176, 0, 7, 5);
	        }
	        if(this.owcController.checkTower()){
	            this.drawTexturedModalRect(i + 10, j + 31, 176, 0, 7, 5);
	        }
	        if(this.owcController.checkDevices()){
	            this.drawTexturedModalRect(i + 10, j + 40, 176, 0, 7, 5);
	        }
	        if(this.owcController.checkConduit()){
	            this.drawTexturedModalRect(i + 10, j + 49, 176, 0, 7, 5);
	        }
	        if(this.owcController.checkVolume()){
	            this.drawTexturedModalRect(i + 10, j + 58, 176, 0, 7, 5);
	        }
	        if(this.owcController.checkTide()){
	            this.drawTexturedModalRect(i + 10, j + 67, 176, 0, 7, 5);
	        }

	        //upgrades
	        if(this.owcController.checkDuality()){
	            this.drawTexturedModalRect(i + 121, j + 16, 176, 5, 18, 18);
	        }
	        if(this.owcController.checkConveyor()){
	            this.drawTexturedModalRect(i + 121, j + 38, 176, 23, 18, 18);
	        }
	        if(this.owcController.checkEfficiency()){
	            this.drawTexturedModalRect(i + 121, j + 60, 176, 41, 18, 18);
	        }

	        //acquire
            this.drawTexturedModalRect(i + 73, j + 16, 194, 5, 18, 18);
        }

        //extract
        if(this.owcController.extractionKey){
            this.drawTexturedModalRect(i + 73, j + 38, 194, 23, 18, 18);
        }

    }
}