package com.generation.gameet.utility;

import java.util.Map;

public abstract class Utility {
	public static boolean isStringEmpty(String s) {
		//ritorna true se:
		//s è null
		//s è stringa vuota ("")
		//s contiene soltanto spazi ("     ") isBlank
		
		return s == null || s.isBlank();
	}
	
	public static String capitalize(String s) {
		//esempio:
		//input: ciao
		//output: Ciao
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public static String mapToString(Map<String, String> m) {
		String ris = "";
		
		for (String key: m.keySet())
			ris += capitalize(key.replace("_", " ")) + ": " + m.get(key) + "\n";
		
		return ris;
	}
}
