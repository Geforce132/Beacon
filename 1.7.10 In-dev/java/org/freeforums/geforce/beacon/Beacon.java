package org.freeforums.geforce.beacon;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import org.freeforums.geforce.beacon.jsoup.Document;
import org.freeforums.geforce.beacon.jsoup.Element;
import org.freeforums.geforce.beacon.jsoup.Elements;
import org.freeforums.geforce.beacon.jsoup.Jsoup;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	private BeaconEventHandler eventHandler;
	
	//Mod info.
	private String modid;
	private String modVersion;
	private String cfLink;
	private String mcVersion;
	//
	
	private boolean hasDownloadMod = false;
	
	/**
	 * <b> Be sure to read the {@link Beacon} documentation! </b>
	 * @param modid Your mod's ID, found in your @Mod annotation.
	 * @param modVersion The version of the mod, also found in your @Mod annotation.
	 * @param cflink The ID of your mod on minecraft.curseforge.com. 
	 * <p/>
	 * e.g: If the link to a mod was "http://minecraft.curseforge.com/mc-mods/000000-examplemod", the ID would be "000000-examplemod".
	 *
	 * @param mcVersion version that your mod is running on. (e.g "1.7.10")
	 */
	
	public Beacon(String modid, String modVersion, String cfLink, String mcVersion){
		this.modid = modid;
		this.modVersion = modVersion;
		this.cfLink = cfLink;
		this.mcVersion = mcVersion;
		this.eventHandler = new BeaconEventHandler(this);
		checkModVersion();
	}

	public boolean onConnectionReceived(Map<String,String> modList, Side side){			
		
		for(int i = 0; i < modList.size(); i++){
			String modID = (String) modList.keySet().toArray()[i];
			String version = (String) modList.values().toArray()[i];

			if(version.toLowerCase().startsWith("v")){ version = version.replaceFirst("v", ""); }

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
	
	private void checkModVersion() {
		
	}

	private void downloadModRequiredForServer(String modID, String version) throws MalformedURLException, IOException {
		Document doc = Jsoup.parse(HelpfulMethods.getCurseForgeDLLink(cfLink), 2000);
		Elements resultLinks = doc.select("a");
		
		for (Element link : resultLinks) {
	        String href = link.attr("href");
	        
	        if(link.text().toLowerCase().matches("[" + mcVersion + "] " + modID + " " + version)){
	        	HelpfulMethods.downloadMod(href, ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/" + mcVersion + "/"), modID, version, mcVersion);
	        }
	    }
	}
	
	public void registerBeaconEventHandler(){
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}
	
	public BeaconEventHandler getEventHandler(){
		return this.eventHandler;
	}
	
public class BeaconEventHandler{
	
	private Beacon beaconInstance;
	
	public BeaconEventHandler(Beacon beacon) {
		beaconInstance = beacon;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onScreenDrawn(DrawScreenEvent.Pre event){
		if(event.gui != null && event.gui instanceof GuiMainMenu){
			event.gui.drawString(event.gui.mc.fontRenderer, "hello", 5, 5, 0xFF3377);
		}
	}
}

public static class HelpfulMethods{
	 
	public static boolean isModInstalled(String modid, String version) {
		for(int i = 0; i < Loader.instance().getActiveModList().size(); i++){
			ModContainer mod = Loader.instance().getActiveModList().get(i);

			if((mod.getModId() + mod.getVersion().replaceFirst("v", "")).matches(modid + version)){
				return true;
			}
		}

		return false;
	}
	
	public static void downloadMod(String url, String path, String modid, String modVersion, String mcVersion) throws IOException{
		BufferedInputStream in = null;
		FileOutputStream out = null;
		
		try{
			URL website = new URL(url);
			URLConnection conn = website.openConnection();
			int size = conn.getContentLength();
			
			if(size < 0){
				System.out.println("Could not get file size for mod: " + modid);
			}
            
            File fileDirectory = new File(path);
            File fileToCreate = new File(path + "[" + mcVersion + "] " + modid + " " + modVersion + ".jar");

		    if(!fileDirectory.exists()){
			    fileDirectory.mkdir();	
		    }
		
		    if(!fileToCreate.exists()){
			    fileToCreate.createNewFile();
			    fileToCreate.setWritable(true);
		    }
			
			in = new BufferedInputStream(website.openStream());
			out = new FileOutputStream(path + "[" + mcVersion + "] " + modid + " " + modVersion + ".jar");
			
			new Thread(new ThreadDownloadFile(in, out, size, modid, modVersion)).start();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(in != null){
				in.close();
			}
			
			if(out != null){
				out.close();
			}
		}
	}
	
	public static URL getCurseForgeDLLink(String modid) throws MalformedURLException{
		return new URL("http://minecraft.curseforge.com/mc-mods/" + modid + "/files");
	}
	
	/**
	 * Is there an existing Internet connection?
	 */	
	public static boolean hasInternetConnection(){
		try{
			URL url = new URL("http://www.google.com");
			
			HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
			Object objData = urlConnect.getContent();
		}catch(UnknownHostException e){
			return false;
		}catch(IOException e){
			return false;
		}
		
		return true;
	}
	 
 }

}
