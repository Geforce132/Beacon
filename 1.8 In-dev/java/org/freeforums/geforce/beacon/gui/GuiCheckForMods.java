package org.freeforums.geforce.beacon.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiUnicodeGlyphButton;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import org.freeforums.geforce.beacon.jsoup.Document;
import org.freeforums.geforce.beacon.jsoup.Element;
import org.freeforums.geforce.beacon.jsoup.Jsoup;
import org.freeforums.geforce.beacon.main.HelpfulMethods;
import org.freeforums.geforce.beacon.main.mod_Beacon;
import org.freeforums.geforce.beacon.misc.DownloadInfo;
import org.freeforums.geforce.beacon.misc.ModNamingFormat;

import com.google.common.collect.Lists;

public class GuiCheckForMods extends GuiScreen {
	
	private GuiScreen prevScreen;
	private GuiUpdatesAvailableList guiUpdatesAvaliableList;
	private GuiUpdatesSelectedList guiUpdatesSelectedList;
	
	private List<ModUpdateListEntry> updatesAvaliableList = Lists.newArrayList();
	private List<ModUpdateListEntry> updatesSelectedList = Lists.newArrayList();
    
	private boolean field_175289_s = false;
	
	public GuiCheckForMods(GuiScreen par1){
		this.prevScreen = par1;
	}
	
	public void initGui(){
		super.initGui();
		
    	List<DownloadInfo> modDownloadLinks = new ArrayList<DownloadInfo>();
        for(ModContainer modInstalled : mod_Beacon.instance.beacon.getModsCompatibleWithBeacon()){

        	try{
	        	File file = new File(mod_Beacon.instance.beacon.getURLForMod(modInstalled));
	    		Document doc = Jsoup.parse(file, null);
	    		List<Element> resultLinks = doc.select("a");
	    		

	    	    for(Element link : resultLinks){
	    	    	String href = link.attr("href");
	    	    	if(href.endsWith("/download")){
	    	    		Element element = HelpfulMethods.getDownloadLinkFromElement(resultLinks, link.attr("href"));
	    	    		if(element != null){
		    	    		modDownloadLinks.add(new DownloadInfo(modInstalled, element.text(), href));
	    	    		}
	    	    	}else{
	    	    		continue;
	    	    	}
	    	    }
        	}catch(IOException e){
        		e.printStackTrace();
        	}       		
        }
        
        for(DownloadInfo link : modDownloadLinks){
	    	Object[] info = ModNamingFormat.formatCFLink(link.modLinkName, link.mod.getName()); //TODO ModName
	    	if(info != null && ((String) info[2]).matches(Loader.MC_VERSION)){
	    		this.updatesAvaliableList.add(new ModUpdateListEntry(link.mod.getModId(), (String) info[0], link.mod.getVersion() + " -> " + "v" + info[1] + info[3], link.downloadLink, ((String) info[2]).matches("a") ? ModUpdateListEntry.ModTypes.ALPHA : ((String) info[2]).matches("b") ? ModUpdateListEntry.ModTypes.BETA : ModUpdateListEntry.ModTypes.RELEASE, this, mc));
	    	}
	    }
    	
    	this.updatesAvaliableList.add(new ModUpdateListEntry("beacon", "Beacon", "v1.0.5", "test", ModUpdateListEntry.ModTypes.BETA, this, mc));
        
        this.guiUpdatesAvaliableList = new GuiUpdatesAvailableList(this.mc, 200, this.height, this.updatesAvaliableList);
        this.guiUpdatesAvaliableList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.guiUpdatesAvaliableList.registerScrollButtons(7, 8);
        this.guiUpdatesSelectedList = new GuiUpdatesSelectedList(this.mc, 200, this.height, this.updatesSelectedList);
        this.guiUpdatesSelectedList.setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.guiUpdatesSelectedList.registerScrollButtons(9, 10);
        
        this.buttonList.add(new GuiUnicodeGlyphButton(0, 20, this.height - 25, 130, 20, "  Download mods", "\u21A1\u21A1", 2.0F)); //21A7
		this.buttonList.add(new GuiUnicodeGlyphButton(1, this.width - 60, this.height - 25, 40, 20, "Back", GuiUtils.UNDO_CHAR, 2.0F));
		
	}
	
	public void drawScreen(int par1, int par2, float par3){
		this.drawBackground(0);

		this.guiUpdatesAvaliableList.drawScreen(par1, par2, par3);
		this.guiUpdatesSelectedList.drawScreen(par1, par2, par3);
		
		this.drawCenteredString(fontRendererObj, "Beacon", this.width / 2, 20, 16777215);
		
		super.drawScreen(par1, par2, par3);
	}
	
	public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.guiUpdatesAvaliableList.handleMouseInput();
        this.guiUpdatesSelectedList.handleMouseInput();
    }
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.guiUpdatesAvaliableList.mouseClicked(mouseX, mouseY, mouseButton);
        this.guiUpdatesSelectedList.mouseClicked(mouseX, mouseY, mouseButton);
    }
	
	protected void actionPerformed(GuiButton par1){
		if(par1.id == 0){		
			for(ModUpdateListEntry entry : updatesSelectedList){
				System.out.println(entry.getEntryName() + " | " + entry.getEntryMetadata() + " | " + entry.getDownloadLink());
				HelpfulMethods.downloadMod(entry);
			}
		}else if(par1.id == 1){
			this.mc.displayGuiScreen(prevScreen);
		}
	}
	
	public boolean hasResourcePackEntry(ModUpdateListEntry par1)
    {
        return this.updatesSelectedList.contains(par1);
    }

    public List getListForEntry(ModUpdateListEntry par1)
    {
        return this.hasResourcePackEntry(par1) ? this.updatesSelectedList : this.updatesAvaliableList;
    }
    
    public List getSelectedUpdatesList()
    {
        return this.updatesSelectedList;
    }
    
    public List getAvaliableUpdatesList()
    {
        return this.updatesAvaliableList;
    }
    
    public void func_175288_g()
    {
        this.field_175289_s = true;
    }

}
