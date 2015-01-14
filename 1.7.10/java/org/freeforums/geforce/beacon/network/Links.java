package org.freeforums.geforce.beacon.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.freeforums.geforce.beacon.main.mod_Beacon;

public class Links {
	
	public static HashMap<String, String> webLinks = new HashMap<String, String>();
	public static HashMap<String, String> localMods = new HashMap<String, String>();
	public static HashMap<String, String> modAliases = new HashMap<String, String>();
	
	public static void setupLinks() throws IOException{
		URL modList = new URL("https://www.github.com/Geforce132/Beacon/raw/master/modList.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(modList.openStream()));
		
		String line;
		while((line = in.readLine()) != null){
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter("~");
			
			List<String> modInfo = new ArrayList<String>();
			
			while(scanner.hasNext()){
				modInfo.add(scanner.next());
			}
			
			if(modInfo.size() == 3 && !modInfo.get(1).matches(mod_Beacon.MCVERSION)){
				continue;
			}else if(modInfo.size() == 4 && !modInfo.get(2).matches(mod_Beacon.MCVERSION)){
				continue;
			}
			
			if(modInfo.size() == 3){ 
				webLinks.put(modInfo.get(0), modInfo.get(2));
			}else if(modInfo.size() == 4){ 
				webLinks.put(modInfo.get(0), modInfo.get(3));
				modAliases.put(modInfo.get(0), modInfo.get(3));
			}
		}
    }
	
	public static void setupLocalMods(){}
	
	public static void setupAliases(){}
	
	public static String getLink(String mod){
		return webLinks.get(mod);
	}
	
	public static String getLocalModPath(String mod){
		return localMods.get(mod);
	}
	
	public static String getAlias(String mod){
		return modAliases.get(mod);
	}
	
	public static boolean hasWebLink(String mod){
		return webLinks.containsKey(mod);
	}
	
	public static boolean hasLocalMod(String mod){
		return localMods.containsKey(mod);
	}
	
	public static boolean hasAlias(String mod){
		return modAliases.containsKey(mod);
	}

	
}
