package org.freeforums.geforce.beacon.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRestart extends GuiScreen {
	
	private GuiScreen prevScreen;

	public GuiRestart() {
		
	}
	
	public void initGui(){
		super.initGui();
		
		this.buttonList.add(new GuiButton(1, 20, this.height - 30, 30, 20, "Quit"));
		this.buttonList.add(new GuiButton(2, this.width - 70, this.height - 30, 50, 20, "Not now"));
	}
	
	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();
		
		this.drawCenteredString(fontRendererObj, "Minecraft must restart in order to install the new mods. Quit now?", this.width / 2, 20, 16777215);
		
		super.drawScreen(par1, par2, par3);
	}
	
	protected void actionPerformed(GuiButton par1){
		if(par1.id == 1){
			this.mc.shutdown();
		}else if(par1.id == 2){
			this.mc.displayGuiScreen(prevScreen);
		}
	}

}
