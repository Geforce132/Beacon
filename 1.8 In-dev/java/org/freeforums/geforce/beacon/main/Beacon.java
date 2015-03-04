package org.freeforums.geforce.beacon.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.freeforums.geforce.beacon.jsoup.Document;
import org.freeforums.geforce.beacon.jsoup.Element;
import org.freeforums.geforce.beacon.jsoup.Elements;
import org.freeforums.geforce.beacon.jsoup.Jsoup;

/**
 * <b>The main Beacon class.</b>
 * <p/>
 * Implementing this class in your mod will allow players to automatically download the newest version of
 * your mod from minecraft.curseforge.com when the game is started.
 * <p/>
 * Also, if you're joining a modded server, 
 * and you don't have the required mods to join, 
 * it will download the required version of the mod(s) for you. (if the mod(s)
 * has it's CurseForge extension posted in the Beacon repository. 
 * (http://www.github.com/Geforce132/Beacon) )
 * 
 * <p/>
 * <b>How to add this into your mod: </b> <p/>
 * Note: In the following paragraph, "main class" = class in your mod with the @Mod annotation. <p/>
 * 1: Add <pre> private Beacon beacon; 
 * 
 * {@literal @}EventHandler
 * public void postInit({@link FMLPostInitializationEvent} event){
 *    beacon = new Beacon(*see constructor for details on parameters.*);
 *    beacon.registerBeaconEventHandler();
 * }
 * 
 * </pre> in your main class. <p/>
 * 
 * @author Geforce
 * @version 1.0.0
 * 
 */
public class Beacon {
		
	//Mod info.
	private String modid;
	private String modVersion;
	private String cfLink;
	private String mcVersion;
	//
	
	private boolean hasDownloadMod = false;
	
	public ArrayList<String> modIncompatibilities = new ArrayList<String>();
	public HashMap<String, String> modUrls = new HashMap<String, String>();
	
	public List<String> validKeys = new ArrayList<String>();
	
	public Beacon(){	
		this.validKeys.add("add-incompatibility");
		this.validKeys.add("add-url");
	}
	
	private void checkModVersion() {
		
	}

	private void downloadModRequiredForServer(String modID, String version) throws MalformedURLException, IOException {
		Document doc = Jsoup.parse(HelpfulMethods.getCurseForgeDLLink(cfLink), 2000);
		Elements resultLinks = doc.select("a");
		
		for (Element link : resultLinks) {
	        String href = link.attr("href");
	        
	        if(link.text().toLowerCase().matches("[" + mcVersion + "] " + modID + " " + version)){
	        	//HelpfulMethods.downloadMod(href, ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/" + mcVersion + "/"), modID, version, mcVersion);
	        }
	    }
	}

	public boolean onConnectionReceived(Map<String,String> modList, Side side){			
		for(int i = 0; i < modList.size(); i++){
			String modID = (String) modList.keySet().toArray()[i];
			String version = (String) modList.values().toArray()[i];

			if(version.toLowerCase().startsWith("v")){ version = version.replaceFirst("v", ""); }
			if(modList.containsKey("Forge") && !HelpfulMethods.getVersionOfForge(modList.get("Forge")).matches(Loader.MC_VERSION)){ continue; }
			if(modid.matches("mcp") || modid.matches("FML") || modid.matches("Forge")){ continue; }
			
			if(modID.matches(modid) && !modVersion.matches(version)){ 
				try{
					downloadModRequiredForServer(modid, version);
				}catch(IOException e){
					e.printStackTrace();
				}
			}	
			
		}
		
		return true;
	}

	public void handleMessage(String sender, String key, String message) {
		if(key.matches("add-incompatibility")){
			modIncompatibilities.add(message);
			mod_Beacon.log(sender + " added a mod incompatibility: " + message);
		}else if(key.matches("add-url")){
			Scanner scanner = new Scanner(message).useDelimiter(" ");
			String modID = scanner.next();
			String url = scanner.next();
			modUrls.put(modID, url);
			mod_Beacon.log(sender + " added a mod URL (" + url + ") with the mod ID: " + modID);
		}
	}
	
	public List<ModContainer> getModsCompatibleWithBeacon(){
		List<ModContainer> list = new ArrayList<ModContainer>();
		for(ModContainer modInstalled : Loader.instance().getActiveModList()){
        	if(modInstalled.getModId().matches("mcp") || modInstalled.getModId().matches("FML") || modInstalled.getModId().matches("Forge")){ continue; }
			
			if(modUrls.containsKey(modInstalled.getModId())){
				list.add(modInstalled);
			}
		}
		
		return list;
	}
	
	public String getURLForMod(ModContainer mod){
		return modUrls.get(mod.getModId());
	}

}
