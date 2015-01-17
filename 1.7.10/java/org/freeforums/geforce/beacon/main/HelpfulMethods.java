package org.freeforums.geforce.beacon.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.freeforums.geforce.beacon.gui.GuiCheckForMods;
import org.freeforums.geforce.beacon.network.Links;
import org.freeforums.geforce.beacon.network.ThreadDownloadFile;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class HelpfulMethods {
	
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
	
	public static boolean hasMod(String modid, String version) {
		for(int i = 0; i < Loader.instance().getActiveModList().size(); i++){
			ModContainer mod = Loader.instance().getActiveModList().get(i);
						
			if((mod.getModId() + mod.getVersion().replaceFirst("v", "")).matches(modid + version)){
				return true;
			}
		}
		
		return false;
	}
    
    public static String getVersionOfForge(String version) {
		if(version.startsWith("9.11")){
			return "1.6.2/1.6.4";
		}else if(version.startsWith("10.12")){
			return "1.7.2";
		}else if(version.startsWith("10.13")){
			return "1.7.10";
		}else if(version.startsWith("10.14")){
			return "1.8";
		}else{
			return "unknown";
		}
	}
	
	public static void transferFile(String modName, String transferFrom, String transferTo) throws IOException{
		FileInputStream from = new FileInputStream(transferFrom);
		
		File fileDirectory = new File(transferTo);
		File fileToCreate = new File(transferTo + modName + (transferFrom.toLowerCase().endsWith(".jar") ? ".jar" : ".zip"));

		//System.out.println("Creating dir: " + transferTo);
		//System.out.println("Creating mod: " + transferTo + modName + (transferFrom.toLowerCase().endsWith(".jar") ? ".jar" : ".zip"));
		if(!fileDirectory.exists()){
			fileDirectory.mkdir();	
		}
		
		if(!fileToCreate.exists()){
			fileToCreate.createNewFile();
			fileToCreate.setWritable(true);
		}
		
		FileOutputStream to = new FileOutputStream(transferTo + modName + (transferFrom.toLowerCase().endsWith(".jar") ? ".jar" : ".zip"));
		
		byte[] buffer = new byte[4094];
		int byteRead;
		
		while((byteRead = from.read(buffer)) != -1){
			to.write(buffer, 0, byteRead);
		}
		
		System.out.println("[Beacon] Transfered mod from " + transferFrom + " to " + transferTo + modName);
		
		from.close();
		to.close();		
	}
	
	public static void downloadFile(String modid, String url, String path, GuiCheckForMods screen) throws IOException{
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
            File fileToCreate = new File(path + modid + ".jar");

		    if(!fileDirectory.exists()){
			    fileDirectory.mkdir();	
		    }
		
		    if(!fileToCreate.exists()){
			    fileToCreate.createNewFile();
			    fileToCreate.setWritable(true);
		    }
			
			in = new BufferedInputStream(website.openStream());
			out = new FileOutputStream(path + modid + ".jar");
			
			new ThreadDownloadFile(in, out, size, modid, screen).run();
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
	
	public static boolean downloadMod(String mod, GuiCheckForMods screen) throws IOException {
		if(Links.hasLocalMod(mod)){
			transferFile(mod, Links.getLocalModPath(mod), mod_Beacon.mcDirectory.matches("default") ? ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/1.7.10/") : mod_Beacon.mcDirectory);
			return true;
		}else if(Links.hasWebLink(mod)){
			downloadFile(mod, Links.getLink(mod), mod_Beacon.mcDirectory.matches("default") ? ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/1.7.10/") : mod_Beacon.mcDirectory, screen);
			return true;
		}	
		
		return false;
	}

	public static List<String> downloadMissingMods(ArrayList<String> missingMods, GuiCheckForMods screen) {
		List<String> downloadedMods = new ArrayList<String>();
		
		for(String mod : missingMods){		
			try{
				if(Links.hasLocalMod(mod)){
					transferFile(Links.hasAlias(mod) ? Links.getAlias(mod) : mod, Links.getLocalModPath(mod), mod_Beacon.mcDirectory.matches("default") ? ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/1.7.10/") : mod_Beacon.mcDirectory);
					downloadedMods.add(mod);
				}else if(Links.hasWebLink(mod) && hasInternetConnection()){
					downloadFile(Links.hasAlias(mod) ? Links.getAlias(mod) : mod, Links.getLink(mod), mod_Beacon.mcDirectory.matches("default") ? ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/1.7.10/") : mod_Beacon.mcDirectory, screen);
					downloadedMods.add(mod);
				}else if(!Links.hasLocalMod(mod) && !Links.hasWebLink(mod)){
					continue;
				}	
			}catch(IOException e){
				e.printStackTrace();
				System.out.println("[Beacon] Catching exception while downloading the '" + mod + "' mod.");
				continue;
			}	
			
		}
		
		return downloadedMods;
	}

}
