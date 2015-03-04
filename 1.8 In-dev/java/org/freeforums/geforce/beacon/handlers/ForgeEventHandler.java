package org.freeforums.geforce.beacon.handlers;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.freeforums.geforce.beacon.gui.GuiCheckForMods;
import org.freeforums.geforce.beacon.gui.GuiMessage;
import org.freeforums.geforce.beacon.main.mod_Beacon;

public class ForgeEventHandler {
		
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiInitialized(InitGuiEvent.Post event){
		if(event.gui instanceof GuiMultiplayer){
			event.buttonList.add(new GuiButton(9, event.gui.width / 2 - 200, event.gui.height - 52, 45, 20, "Beacon"));
		}
		
		if(event.gui instanceof GuiMainMenu){
			event.buttonList.add(new GuiButton(7, event.gui.width / 2 + 105, (event.gui.height / 4 + 48) + 24 * 2, 70, 20, "Update Mods"));
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onScreenDrawn(DrawScreenEvent.Post event){
		if(event.gui instanceof GuiMultiplayer && !mod_Beacon.instance.missingMods.isEmpty()){
			event.gui.drawString(event.gui.mc.fontRendererObj, "(" + String.valueOf(mod_Beacon.instance.missingMods.size()) + " mod" + (mod_Beacon.instance.missingMods.size() >= 2 ? "s" : "") + " missing!)", (event.gui.width / 2) + 45, 20, 0xFF3377);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onButtonClicked(ActionPerformedEvent.Pre event){
		if(event.gui instanceof GuiMainMenu && event.button.id == 7){
			event.setCanceled(true);
			//if(!mod_Beacon.instance.missingMods.isEmpty()){
			event.gui.mc.displayGuiScreen(new GuiCheckForMods(event.gui));
			//}else{
			//	event.gui.mc.displayGuiScreen(new GuiMessage(event.gui, "Beacon has found no outdated or missing mods in your Minecraft installation.", "Back"));
			//}
			
			event.gui.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		}
	}
	
	@SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if(event.modID.matches("beacon")){
        	mod_Beacon.configFile.save();
        	mod_Beacon.configHandler.loadConfig(mod_Beacon.configFile);
        }
    }

}
