package org.freeforums.geforce.beacon.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

import org.freeforums.geforce.beacon.main.mod_Beacon;

public class BeaconConfigGUI extends GuiConfig{
	public BeaconConfigGUI(GuiScreen parent) {
        super(parent, new ConfigElement(mod_Beacon.configFile.getCategory("options")).getChildElements(), "beacon", false, false, GuiConfig.getAbridgedConfigPath(mod_Beacon.configFile.toString())); 
	}

}
