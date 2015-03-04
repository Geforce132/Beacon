package org.freeforums.geforce.beacon.misc;

import net.minecraftforge.fml.common.ModContainer;

public class DownloadInfo {
	
	public ModContainer mod;
	public String modLinkName;
	public String downloadLink;
	
	public DownloadInfo(ModContainer mod, String modLinkName, String downloadLink){
		this.mod = mod;
		this.modLinkName = modLinkName;
		this.downloadLink = downloadLink;
	}

}
