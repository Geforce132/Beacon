package org.freeforums.geforce.beacon.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

import org.freeforums.geforce.beacon.main.HelpfulMethods;
import org.freeforums.geforce.beacon.main.mod_Beacon;
import org.freeforums.geforce.beacon.network.Links;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiCheckForMods extends GuiScreen {
	
	private GuiCheckForMods.List missingModsList;
	private GuiScreen prevScreen;
	private ArrayList<String> modsToRemove = new ArrayList<String>();
	private boolean hasDownloadedMod;
	private boolean downloadedAllMods = false;
	private boolean hasInternetConnection = true;
	
	public HashMap<String, Integer> downloads = new HashMap<String, Integer>();
	
	public GuiCheckForMods(GuiScreen par1){
		this.prevScreen = par1;
	}
	
	public void initGui(){
		super.initGui();
		
		this.hasInternetConnection = HelpfulMethods.hasInternetConnection();
		
		this.missingModsList = new GuiCheckForMods.List();
        this.missingModsList.registerScrollButtons(7, 8);
        
		this.buttonList.add(new GuiUnicodeGlyphButton(0, 20, this.height - 25, 130, 20, "  Download all mods", "\u21A1\u21A1", 2.0F));
		this.buttonList.add(new GuiUnicodeGlyphButton(1, this.width - 60, this.height - 25, 40, 20, "Back", GuiUtils.UNDO_CHAR, 2.0F));
		this.buttonList.add(new GuiUnicodeGlyphButton(2, 20, this.height - 55, 100, 20, "  Download mod", "\u21D9", 2.5F));
		
		((GuiButton) GuiCheckForMods.this.buttonList.get(2)).enabled = false;
	}
	
	public void drawScreen(int par1, int par2, float par3){
		this.drawDefaultBackground();

		this.missingModsList.drawScreen(par1, par2, par3);
		
		this.drawCenteredString(fontRendererObj, "Beacon", this.width / 2, 20, 16777215);
		
		if(!hasInternetConnection){
			this.drawString(fontRendererObj, "No internet connection detected!", 12, 5, 0xFF3377);
			this.drawString(fontRendererObj, "Downloads are disabled.", 37, 20, 0xFF3377);
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
			
			java.util.List<String> downloadedMods = HelpfulMethods.downloadMissingMods(mod_Beacon.instance.missingMods, this);
			
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
			try{
				if(HelpfulMethods.downloadMod(mod_Beacon.instance.missingMods.get(missingModsList.selectedEntry), this)){
					modsToRemove.add(mod_Beacon.instance.missingMods.get(missingModsList.selectedEntry));
					mod_Beacon.instance.addedMods.add(mod_Beacon.instance.missingMods.get(missingModsList.selectedEntry));
					par1.enabled = false;
					hasDownloadedMod = true;
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public void setModDownloading(String modid, double percentage){
		this.downloads.put(modid, (int) percentage);
		
		if(this.downloads.get(modid) >= 100){
			this.downloads.remove(modid);
		}
	}
	
	@SideOnly(Side.CLIENT)
    class List extends GuiSlot
    {
        private final java.util.List list = Lists.newArrayList();
        public final Map<Integer, String> numberToLink = new HashMap<Integer, String>();
        private final Map<Integer, String> downloadType = new HashMap<Integer, String>();

        protected int selectedEntry = -1;

        public List()
        {
            super(GuiCheckForMods.this.mc, GuiCheckForMods.this.width, GuiCheckForMods.this.height, 32, GuiCheckForMods.this.height - 65 + 4, 18);
            Iterator iterator = mod_Beacon.instance.missingMods.iterator();
            int counter = -1;
            
            while (iterator.hasNext())
            {
            	counter++;
                String mod = (String)iterator.next();
                
                if(Links.hasLocalMod(mod)){
                	this.numberToLink.put(counter, Links.getLocalModPath(mod));
                	this.downloadType.put(counter, "local");
                }else if(Links.hasWebLink(mod)){
                	this.numberToLink.put(counter, Links.getLink(mod));
                	this.downloadType.put(counter, "web");
                }else{
                	this.downloadType.put(counter, "none");
                }
                
                this.list.add(Links.hasAlias(mod) ? Links.getAlias(mod) : mod);
            }
        }

        protected int getSize()
        {
            return this.list.size();
        }

        /**
         * The element in the slot that was clicked, boolean for whether it was double clicked or not
         */
        protected void elementClicked(int par1, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
        	this.selectedEntry = par1;
        	
        	if(this.downloadType.get(par1).matches("local")){
        		((GuiUnicodeGlyphButton) GuiCheckForMods.this.buttonList.get(2)).displayString = " Transfer mod";
        		((GuiUnicodeGlyphButton) GuiCheckForMods.this.buttonList.get(2)).glyph = "\u21C4";
        		((GuiUnicodeGlyphButton) GuiCheckForMods.this.buttonList.get(2)).glyphScale = 2.0F;
        	}else if(this.downloadType.get(par1).matches("web")){
        		((GuiUnicodeGlyphButton) GuiCheckForMods.this.buttonList.get(2)).displayString = "Download mod";
        		((GuiUnicodeGlyphButton) GuiCheckForMods.this.buttonList.get(2)).glyph = "\u21D9";
        		((GuiUnicodeGlyphButton) GuiCheckForMods.this.buttonList.get(2)).glyphScale = 2.5F;
        	}
            
            if(this.downloadType.get(par1).matches("none")){
            	((GuiButton) GuiCheckForMods.this.buttonList.get(2)).enabled = false;
            }else{
            	((GuiButton) GuiCheckForMods.this.buttonList.get(2)).enabled = true;
            }
        }

        /**
         * Returns true if the element passed in is currently selected
         */
        protected boolean isSelected(int par1)
        {
        	return par1 == selectedEntry;
        }

        /**
         * Return the height of the content being scrolled
         */
        protected int getContentHeight()
        {
            return this.getSize() * 18;
        }

        protected void drawBackground()
        {
        	GuiCheckForMods.this.drawDefaultBackground();
        }

        protected void drawSlot(int par1, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
        	if(this.downloadType.get(par1).matches("none")){
        		GuiCheckForMods.this.drawCenteredString(GuiCheckForMods.this.fontRendererObj, this.list.get(par1).toString(), this.width / 2, p_148126_3_ + 1, 0xFF3377); 
        	}else if(downloads.containsKey(this.list.get(par1))){
        		GuiCheckForMods.this.drawCenteredString(GuiCheckForMods.this.fontRendererObj, (this.list.get(par1).toString() + " (" + downloads.get(this.list.get(par1)) + "%)"), this.width / 2, p_148126_3_ + 1, 16777215);
        	}else{
        		GuiCheckForMods.this.drawCenteredString(GuiCheckForMods.this.fontRendererObj, this.list.get(par1).toString(), this.width / 2, p_148126_3_ + 1, 16777215);
        	}
        }
    }

}
