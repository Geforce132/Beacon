package org.freeforums.geforce.beacon.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.freeforums.geforce.beacon.main.mod_Beacon;

public class Links {
		
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
	
			if(modInfo.size() == 2){ 
				mod_Beacon.instance.beacon.modUrls.put(modInfo.get(0), modInfo.get(1));
				mod_Beacon.log("Read URL " + modInfo.get(1) + " for mod" + modInfo.get(0));
			}
		}
	}
	
}
