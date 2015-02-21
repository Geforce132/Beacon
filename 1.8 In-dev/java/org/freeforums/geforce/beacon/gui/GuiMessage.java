package org.freeforums.geforce.beacon.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiMessage extends GuiScreen {
	
	private GuiScreen prevScreen;
	private String message;
	private String backButtonText;
	
	public GuiMessage(GuiScreen prevScreen, String message, String backButtonText){
		this.prevScreen = prevScreen;
		this.message = message;
		this.backButtonText = backButtonText;
	}
	
	public void initGui(){
		super.initGui();
		
		this.buttonList.add(new GuiButton(1, (this.width / 2) - this.fontRendererObj.getStringWidth(backButtonText) + 3, this.height - 30, this.fontRendererObj.getStringWidth(backButtonText) + 20, 20, backButtonText));
	}
	
	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();
		
		this.drawString(fontRendererObj, message, this.width / 2 - this.fontRendererObj.getStringWidth(message) / 2, 40, 16777215);
		
		super.drawScreen(par1, par2, par3);
	}
	
	protected void actionPerformed(GuiButton par1){
		if(par1.id == 1){
			this.mc.displayGuiScreen(prevScreen);
		}
	}

}
