package org.freeforums.geforce.beacon.main;

import java.util.Arrays;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import org.freeforums.geforce.beacon.Beacon;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="beacon", name="Beacon", version=mod_Beacon.VERSION, guiFactory = "org.freeforums.geforce.beacon.gui.BeaconGuiFactory", dependencies = mod_Beacon.FORGEVERSION)
public class mod_Beacon {
	
	public static final String MODID = "beacon";
	public static final String NAME = "Beacon";
	private static final String MOTU = "First!";
    
    //TODO ********************************* This is v1.0.6 for MC 1.7.10!
	protected static final String VERSION = "v1.0.6";
	protected static final String FORGEVERSION = "required-after:Forge@[10.13.0.1180,)";
	public static final String MCVERSION = "1.7.10";
	
	@Instance("beacon")
	public static mod_Beacon instance = new mod_Beacon();
	
	public static Beacon beacon;
		
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		beacon = new Beacon(MODID, VERSION, "", MCVERSION);
		
		ModMetadata modMeta = event.getModMetadata();
        modMeta.authorList = Arrays.asList(new String[] {
            "Geforce"
        });
        modMeta.autogenerated = false;
        modMeta.credits = "Thanks to Stack Overflow for help with code."; 
        modMeta.description = "Beacon adds the ability to download mods in-game. \nJoin your favorite servers without having to download every mod in your browser! \n \nThe list of downloadable mods can be found at the above URL.\n \nMessage of the update: \n" + MOTU;
        modMeta.url = "http://www.github.com/Geforce132/Beacon";
	}
		
	
	@EventHandler
	public void init(FMLInitializationEvent event){}
		
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		beacon.registerBeaconEventHandler();
	}
	
	@NetworkCheckHandler
	public boolean onConnectionReceived(Map<String,String> modList, Side side){
		return beacon.onConnectionReceived(modList, side);
	}

}
