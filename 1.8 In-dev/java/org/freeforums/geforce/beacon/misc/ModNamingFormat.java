package org.freeforums.geforce.beacon.misc;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public abstract class ModNamingFormat {
	
	public abstract Object[] formatLink(String modLink, String modName);
	
	public abstract int getStringPartsRequired();
	
	public static Object[] formatCFLink(String link, String modname){
		for(ModNamingFormat format : getNamingFormats()){
			int tokens = new StringTokenizer(link).countTokens();
			
			if(format.getStringPartsRequired() == tokens){
				Object[] info = format.formatLink(link, modname);
				
				if(info == null){
					continue;
				}else{
					return info;
				}
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
	
	public static ModNamingFormat[] getNamingFormats(){
		return new ModNamingFormat[]{new NamingFormat1(), new NamingFormat2()};
	}
	
	//Example link: [1.8] SecurityCraft 1.0.0.jar
	public static class NamingFormat1 extends ModNamingFormat {
		
		public Object[] formatLink(String modLink, String modName) throws NoSuchElementException{
			try{
				String fixedLink = modLink.replace(modName, "modname");

				String mcVersion = getPartOfString(fixedLink, 1, " ");
				mcVersion = mcVersion.replace("[", "");
				mcVersion = mcVersion.replace("]", "");

				String modVersion = getPartOfString(fixedLink, 3, " ");
				return new String[]{ modName, modVersion, mcVersion, fixedLink.contains("alpha") ? "a" : fixedLink.contains("beta") ? "b" : ""};
			}catch(NoSuchElementException e){
				e.printStackTrace();
				return null;
			}
		}

		public int getStringPartsRequired() {
			return 3;
		}
		
	}
	
	//Example link: SecurityCraft v1.6.1 for 1.7.10.jar
	public static class NamingFormat2 extends ModNamingFormat {
		
		public Object[] formatLink(String modLink, String modName) throws NoSuchElementException{
			try{
				String fixedLink = modLink.replace(modName, "modname").replace(".jar", "");
				String mcVersion = getPartOfString(fixedLink, 4, " ");
				mcVersion = mcVersion.replace("[", "");
				mcVersion = mcVersion.replace("]", "");

				String modVersion = getPartOfString(fixedLink, 2, " ").replace("v", "");

				return new String[]{ modName, modVersion, mcVersion, fixedLink.contains("alpha") ? "a" : fixedLink.contains("beta") ? "b" : ""};	
			}catch(NoSuchElementException e){
				e.printStackTrace();
				return null;
			}
		}

		public int getStringPartsRequired() {
			return 4;
		}
	}

}
