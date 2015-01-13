package org.freeforums.geforce.beacon.network;

import org.freeforums.geforce.beacon.main.mod_Beacon;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	
	public void loadConfig(Configuration config){
		config.load();
		mod_Beacon.mcDirectory = config.getString(".minecraft folder path", "options", "default", "What is the path to your .minecraft folder?");		
		if(config.hasChanged()){
			config.save();
        }
	}

}
