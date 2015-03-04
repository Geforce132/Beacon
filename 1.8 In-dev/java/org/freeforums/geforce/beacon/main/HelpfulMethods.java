package org.freeforums.geforce.beacon.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import net.minecraft.command.WrongUsageException;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import org.freeforums.geforce.beacon.gui.GuiCheckForMods;
import org.freeforums.geforce.beacon.jsoup.Element;
import org.freeforums.geforce.beacon.misc.ModNamingFormat;
import org.freeforums.geforce.beacon.network.Links;
import org.freeforums.geforce.beacon.network.ThreadDownloadFile;

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
	
	public static boolean isModInstalled(String modid, String version) {
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
	
	public static URL getCurseForgeDLLink(String modid) throws MalformedURLException{
		return new URL("http://minecraft.curseforge.com/mc-mods/" + modid + "/files");
	}
	
	public static Object[] formatCFLink(String link, String modname){
		for(ModNamingFormat format : ModNamingFormat.getNamingFormats()){
			Object[] info = format.formatLink(link, modname);
			if(info != null){
				return info;
			}
		}
		
		return null;
	}
	
	public static Element getDownloadLinkFromElement(List<Element> elements, String rawDLLink){
		for(int i = 0; i < elements.size(); i++){
			if(((Element) elements.toArray()[i]).attr("href").matches(rawDLLink.replace("/download", ""))){
				return (Element) elements.toArray()[i];
			}
		}
		
		return null;
	}
	
	public static String getPartOfString(String string, int index, String delimiter){
		Scanner scanner = new Scanner(string).useDelimiter(delimiter);
		
		for(int i = 1; i < index; i++){
			scanner.next();
		}
		
		return scanner.next();
	}
	
	/**
	 * Compares the two versions given. 
	 * 
	 * @return -1 If str1 is less than str2. <br>
	 *          0 If str1 matches str2. <br>
	 *          1 If str1 is greater than str2.
	 */
	
	@SuppressWarnings("unused")
	public static Integer compareVersions(String str1, String str2) {
		String[] vals1 = str1.split("\\.");
		String[] vals2 = str2.split("\\.");
		
		int i = 0;
		
		while(i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])){
			i++;
		}
		
		if(i < vals1.length && i < vals2.length){
			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}else{
			return Integer.signum(vals1.length - vals2.length);
		}
	}
	
	public static void downloadMod(String url, String path, String modid, String modVersion, GuiCheckForMods screen) throws IOException{
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
            File fileToCreate = new File(path + "[" + Loader.MC_VERSION + "] " + modid + " " + modVersion + ".jar");

		    if(!fileDirectory.exists()){
			    fileDirectory.mkdir();	
		    }
		
		    if(!fileToCreate.exists()){
			    fileToCreate.createNewFile();
			    fileToCreate.setWritable(true);
		    }
			
			in = new BufferedInputStream(website.openStream());
			out = new FileOutputStream(path + "[" + Loader.MC_VERSION + "] " + modid + " " + modVersion + ".jar");
			
			new Thread(new ThreadDownloadFile(in, out, size, modid, screen)).start();
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
		if(Links.hasWebLink(mod)){
			//downloadFile(mod, Links.getLink(mod), mod_Beacon.mcDirectory.matches("default") ? ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/1.8/") : mod_Beacon.mcDirectory, screen);
			return true;
		}	
		
		return false;
	}

	public static List<String> downloadMissingMods(ArrayList<String> missingMods, GuiCheckForMods screen) {
		List<String> downloadedMods = new ArrayList<String>();
		
//		for(String mod : missingMods){		
//			try{
//				if(Links.hasWebLink(mod) && hasInternetConnection()){
//					downloadMod(Links.hasAlias(mod) ? Links.getAlias(mod) : mod, Links.getLink(mod), mod_Beacon.mcDirectory.matches("default") ? ("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/" + Loader.MC_VERSION + "/") : mod_Beacon.mcDirectory + "mods/" + Loader.MC_VERSION + "/", screen);
//					downloadedMods.add(mod);
//				}else if(!Links.hasWebLink(mod)){
//					continue;
//				}	
//			}catch(IOException e){
//				e.printStackTrace();
//				System.out.println("[Beacon] Catching exception while downloading the '" + mod + "' mod.");
//				continue;
//			}	
//			
//		}
		
		return downloadedMods;
	}

}
