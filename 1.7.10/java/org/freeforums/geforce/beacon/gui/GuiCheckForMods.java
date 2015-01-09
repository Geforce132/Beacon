package org.freeforums.geforce.beacon.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.freeforums.geforce.beacon.main.HelpfulMethods;
import org.freeforums.geforce.beacon.main.mod_Beacon;
import org.freeforums.geforce.beacon.network.Links;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiCheckForMods extends GuiScreen {
	
	private GuiScreen prevScreen;
	private ArrayList<String> modsToRemove = new ArrayList<String>();
	private int pageIndex;
	private boolean hasDownloadedMod;
	
	public GuiCheckForMods(GuiScreen par1){
		this.prevScreen = par1;
	}
	
	public void initGui(){
		super.initGui();
		
		this.buttonList.add(new GuiButton(0, 20, this.height - 25, 100, 20, "Download all mods"));
		this.buttonList.add(new GuiButton(1, this.width - 60, this.height - 25, 40, 20, "Back"));
		this.buttonList.add(new GuiButton(2, this.width - 90, 30, 20, 20, "˄"));
		this.buttonList.add(new GuiButton(3, this.width - 90, this.height - 60, 20, 20, "˅"));

		for(int i = 0; i < mod_Beacon.instance.missingMods.size(); i++){
			String buttonChar = "";
			
			if(Links.hasLocalMod(mod_Beacon.instance.missingMods.get(i))){
				buttonChar += "↔"; 
			}else if(Links.hasWebLink(mod_Beacon.instance.missingMods.get(i))){
				buttonChar += "↓";
			}else{
				buttonChar += "?";
			}
			
			this.buttonList.add(new GuiButton((i + 4), (this.width / 2 - this.fontRendererObj.getStringWidth(Links.hasAlias(mod_Beacon.instance.missingMods.get(i)) ? Links.getAlias(mod_Beacon.instance.missingMods.get(i)) : mod_Beacon.instance.missingMods.get(i)) / 2) - 20, 60 + ((i) * 20), 14, 10, buttonChar));
		}
	}
	
	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();

		this.drawCenteredString(fontRendererObj, "Beacon", this.width / 2, 20, 16777215);
		this.drawCenteredString(fontRendererObj, "Missing mods:", this.width / 2, 44, 16777215);

		for(int i = 0; i < mod_Beacon.instance.missingMods.size(); i++){
			this.drawString(fontRendererObj, Links.hasAlias(mod_Beacon.instance.missingMods.get(i)) ? Links.getAlias(mod_Beacon.instance.missingMods.get(i)) : mod_Beacon.instance.missingMods.get(i), this.width / 2 - this.fontRendererObj.getStringWidth(Links.hasAlias(mod_Beacon.instance.missingMods.get(i)) ? Links.getAlias(mod_Beacon.instance.missingMods.get(i)) : mod_Beacon.instance.missingMods.get(i)) / 2, 60 + ((i) * 20), 16777215);
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	public void closeGui()
    {
        for(String mod : modsToRemove){
        	//System.out.println("Removing " + mod);
        	mod_Beacon.instance.missingMods.remove(mod);
        }
        
        Minecraft.getMinecraft().displayGuiScreen(new GuiRestart());
        
    }
	
	protected void actionPerformed(GuiButton par1){
		if(par1.id == 0){
			try{
				HelpfulMethods.downloadMissingMods(mod_Beacon.instance.missingMods);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else if(par1.id == 1){
			if(hasDownloadedMod){
				this.closeGui();
			}else{
				this.mc.displayGuiScreen(prevScreen);
			}
		}else if(par1.id == 2){
			pageIndex++;
		}else if(par1.id == 3){
			pageIndex--;
		}else if(par1.id >= 4){
			try{
				HelpfulMethods.downloadMod(mod_Beacon.instance.missingMods.get(par1.id - 4));
				//System.out.println("Adding " + mod_Beacon.instance.missingMods.get(par1.id - 4) + " to removal queue");
				modsToRemove.add(mod_Beacon.instance.missingMods.get(par1.id - 4));
				mod_Beacon.instance.addedMods.add(mod_Beacon.instance.missingMods.get(par1.id - 4));
				par1.enabled = false;
				hasDownloadedMod = true;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	

}
