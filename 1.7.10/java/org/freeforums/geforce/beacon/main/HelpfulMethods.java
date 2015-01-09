package org.freeforums.geforce.beacon.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.freeforums.geforce.beacon.network.Links;

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
			e.printStackTrace();
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
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
		
		from.close();
		to.close();		
	}
	
	public static void downloadFile(String modid, String url, String path) throws IOException{
//		URL website = new URL(url);
//		
//		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
//		FileOutputStream fos = new FileOutputStream(path + (url.toLowerCase().endsWith(".jar") ? ".jar" : ".zip"));
//		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		
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
			byte[] data = new byte[4094];
			int count;
			double sumCount = 0.0D;
			
			while((count = in.read(data, 0, 4094)) != -1){
				out.write(data, 0, count);
				sumCount += count;
				
				if(size > 0){
					System.out.println("Percentage: " + (sumCount / size * 100.0) + "%");
				}
			}
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
	
	public static void downloadMod(String mod) throws IOException {
		if(Links.hasLocalMod(mod)){
			transferFile(mod, Links.getLocalModPath(mod), mod_Beacon.mcDirectory + "/mods/1.7.10/" );
		}else if(Links.hasWebLink(mod)){
			downloadFile(mod, Links.getLink(mod), mod_Beacon.mcDirectory + "/mods/1.7.10/");
		}			
	}

	public static void downloadMissingMods(ArrayList<String> missingMods) throws IOException {
		for(String mod : missingMods){
			if(Links.hasLocalMod(mod)){
				transferFile(mod, Links.getLocalModPath(mod), mod_Beacon.mcDirectory + "/mods/1.7.10/" );
			}else if(Links.hasWebLink(mod)){
				downloadFile(mod, Links.getLink(mod), mod_Beacon.mcDirectory + "/mods/1.7.10/");

			}		
		}
	}
	
}
