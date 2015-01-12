package org.freeforums.geforce.beacon.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class BeaconGuiFactory implements IModGuiFactory{

	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement arg0) {
		return null;
	}

	public void initialize(Minecraft arg0) {
		
	}

	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return BeaconConfigGUI.class;
	}

	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}
