package org.freeforums.geforce.beacon;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThreadDownloadFile implements Runnable {
	
	private BufferedInputStream in;
	private FileOutputStream out;
	private int fileSize;
	private String modid;
	private String modVersion;
	
	public ThreadDownloadFile(BufferedInputStream in, FileOutputStream out, int fileSize, String modid, String modVersion){
		this.in = in;
		this.out = out;
		this.fileSize = fileSize;
		this.modid = modid;
		this.modVersion = modVersion;
	}

	public void run() {
		try{
			byte[] data = new byte[4094];
			int count;
			double sumCount = 0.0D;
			
			while((count = in.read(data, 0, 4094)) != -1){
				out.write(data, 0, count);
				sumCount += count;
				
				System.out.println("[Beacon] Downloading '" + modid + " " + modVersion + "'. " + (sumCount / fileSize * 100.0) + "% complete.");
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
