package org.freeforums.geforce.beacon.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;

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
