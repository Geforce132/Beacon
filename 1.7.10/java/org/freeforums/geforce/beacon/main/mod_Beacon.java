package org.freeforums.geforce.beacon.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.freeforums.geforce.beacon.handlers.ForgeEventHandler;
import org.freeforums.geforce.beacon.network.Links;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="beacon", name="Beacon", version=mod_Beacon.VERSION)
public class mod_Beacon {
	
	public static final String MODID = "beacon";
	public static final String NAME = "Beacon";
	public static final String VERSION = "v1.0.0";
	
	@Instance("beacon")
	public static mod_Beacon instance = new mod_Beacon();
	
	public static ForgeEventHandler eventHandler = new ForgeEventHandler();
			
	public ArrayList<String> missingMods = new ArrayList<String>();
	public ArrayList<String> addedMods = new ArrayList<String>();

	public static Configuration configFile;
	public static String mcDirectory;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		mod_Beacon.configFile = new Configuration(event.getSuggestedConfigurationFile());
		mod_Beacon.configFile.load();
		
		String tempMcDirectory = mod_Beacon.configFile.getString("Minecraft directory path", "options", "C:/Users/User/AppData/Roaming/.minecraft", "Path to your Minecraft installation?");
		if(tempMcDirectory.contains("\"")){ 
			mcDirectory = tempMcDirectory.replaceAll("\"", "/");
		}else{
			mcDirectory = tempMcDirectory;
		}
		
		if(mod_Beacon.configFile.hasChanged()){
			mod_Beacon.configFile.save();
        }
		
		Links.setupLinks();	
		Links.setupLocalMods();
		Links.setupAliases();
	}
		
	
	@EventHandler
	public void init(FMLInitializationEvent event){}
		
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(mod_Beacon.eventHandler);
	}
	
	@NetworkCheckHandler
	public boolean onConnectionReceived(Map<String,String> modList, Side side){

		for(int i = 0; i < modList.size(); i++){
			String modid = (String) modList.keySet().toArray()[i];
			String version = (String) modList.values().toArray()[i];
			
			if(version.toLowerCase().startsWith("v")){ version = version.replaceFirst("v", ""); }
			if(modid.matches("mcp") || modid.matches("FML") || modid.matches("Forge")){ continue; }
			if(instance.missingMods.contains(modid + " v" + version)){ continue; }
			if(instance.addedMods.contains(modid + " v" + version)){ continue; }
			
			instance.missingMods.add((modid + " v" + version));
		}
		
		return true;
	}

}
