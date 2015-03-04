package org.freeforums.geforce.beacon.network;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.freeforums.geforce.beacon.gui.ModUpdateListEntry;

public class ThreadDownloadFileFromURL implements Runnable {
	
	private ModUpdateListEntry entry;
	
	public ThreadDownloadFileFromURL(ModUpdateListEntry entry){
		this.entry = entry;
	}

	public void run() {
		File file = new File(("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/mods/1.8/") + entry.getEntryName());
		
		try{
			FileUtils.copyURLToFile(new URL(entry.getDownloadLink()), file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
