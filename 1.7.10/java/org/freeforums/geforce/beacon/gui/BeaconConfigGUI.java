package org.freeforums.geforce.beacon.gui;

import org.freeforums.geforce.beacon.main.mod_Beacon;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.GuiConfig;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BeaconConfigGUI extends GuiConfig{
	public BeaconConfigGUI(GuiScreen parent) {
        super(parent, new ConfigElement(mod_Beacon.configFile.getCategory("options")).getChildElements(), "beacon", false, true, GuiConfig.getAbridgedConfigPath(mod_Beacon.configFile.toString())); //TODO
	}

}
