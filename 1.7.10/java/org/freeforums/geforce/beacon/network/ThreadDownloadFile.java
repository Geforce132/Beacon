package org.freeforums.geforce.beacon.network;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.freeforums.geforce.beacon.gui.GuiCheckForMods;

public class ThreadDownloadFile implements Runnable {
	
	private BufferedInputStream in;
	private FileOutputStream out;
	private int fileSize;
	private String modid;
	private GuiCheckForMods screen;
	
	public ThreadDownloadFile(BufferedInputStream in, FileOutputStream out, int fileSize, String modid, GuiCheckForMods screen){
		this.in = in;
		this.out = out;
		this.fileSize = fileSize;
		this.modid = modid;
		this.screen = screen;
	}

	public void run() {
		try{
			byte[] data = new byte[4094];
			int count;
			double sumCount = 0.0D;
			
			while((count = in.read(data, 0, 4094)) != -1){
				out.write(data, 0, count);
				sumCount += count;
				
				if(fileSize > 0 && screen != null){
					screen.setModDownloading(modid, (sumCount / fileSize * 100.0));
				}else if(fileSize > 0 && screen == null){
					System.out.println("[Beacon] Downloading '" + modid + "'. " + (sumCount / fileSize * 100.0) + "% complete.");
				}
			}
			
			if(screen.downloads.containsKey(modid)){
				screen.downloads.remove(modid);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(in != null){
				try{
					in.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
			
			if(out != null){
				try{
					out.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

}
