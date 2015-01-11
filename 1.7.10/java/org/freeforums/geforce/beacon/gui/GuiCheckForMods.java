package org.freeforums.geforce.beacon.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private boolean downloadedAllMods = false;
	
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
			
			if(Links.hasWebLink(mod_Beacon.instance.missingMods.get(i)) && !HelpfulMethods.hasInternetConnection()){
				((GuiButton) this.buttonList.get((i + 4))).enabled = false;
			}
			
			if(!Links.hasWebLink(mod_Beacon.instance.missingMods.get(i)) && !Links.hasLocalMod(mod_Beacon.instance.missingMods.get(i))){
				((GuiButton) this.buttonList.get((i + 4))).visible = false;
			}
		}
	}
	
	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();

		this.drawCenteredString(fontRendererObj, "Beacon", this.width / 2, 20, 16777215);
		this.drawCenteredString(fontRendererObj, "Missing mods:", this.width / 2, 44, 16777215);

		if(!HelpfulMethods.hasInternetConnection()){
			this.drawString(fontRendererObj, "No internet connection detected!", 12, 15, 0xFF3377);
			this.drawString(fontRendererObj, "Downloads are disabled.", 37, 30, 0xFF3377);
		}
		
		for(int i = 0; i < mod_Beacon.instance.missingMods.size(); i++){
			this.drawString(fontRendererObj, Links.hasAlias(mod_Beacon.instance.missingMods.get(i)) ? Links.getAlias(mod_Beacon.instance.missingMods.get(i)) : mod_Beacon.instance.missingMods.get(i), this.width / 2 - this.fontRendererObj.getStringWidth(Links.hasAlias(mod_Beacon.instance.missingMods.get(i)) ? Links.getAlias(mod_Beacon.instance.missingMods.get(i)) : mod_Beacon.instance.missingMods.get(i)) / 2, 60 + ((i) * 20), 16777215);
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	public void closeGui()
    {
		if(!downloadedAllMods){
			for(String mod : modsToRemove){
				mod_Beacon.instance.missingMods.remove(mod);
			}
		}else{
			mod_Beacon.instance.missingMods.clear();
		}
        
        Minecraft.getMinecraft().displayGuiScreen(new GuiRestart());
        
    }
	
	protected void actionPerformed(GuiButton par1){
		if(par1.id == 0){
			
			List<String> downloadedMods = HelpfulMethods.downloadMissingMods(mod_Beacon.instance.missingMods, this);
			
			for(int i = 0; i < downloadedMods.size(); i++){
				mod_Beacon.instance.addedMods.add(downloadedMods.get(i));
				modsToRemove.add(downloadedMods.get(i));
			}

			for(int i = 4; i < this.buttonList.size(); i++){
				if(((GuiButton) this.buttonList.get(i)).enabled){
					((GuiButton) this.buttonList.get(i)).enabled = false;
				}
			}
			
			downloadedAllMods = true;
			par1.enabled = false;
		}else if(par1.id == 1){
			if(hasDownloadedMod || downloadedAllMods){
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
				if(HelpfulMethods.downloadMod(mod_Beacon.instance.missingMods.get(par1.id - 4), this)){
					modsToRemove.add(mod_Beacon.instance.missingMods.get(par1.id - 4));
					mod_Beacon.instance.addedMods.add(mod_Beacon.instance.missingMods.get(par1.id - 4));
					par1.enabled = false;
					hasDownloadedMod = true;
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	

}
