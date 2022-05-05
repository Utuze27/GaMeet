package com.generation.gameet.utility;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

public abstract class JSON {
	public static Map<String, String> JSONToMap(String jsonString) {
		Map<String, String> m = new LinkedHashMap<>();
		
		if (!Utility.isStringEmpty(jsonString)) {			
			//jsonString: {"chiave1": "valore1", "chiave2": "valore2", ...}
			JSONObject json = new JSONObject(jsonString);
			
			for (String key: json.keySet())
				m.put(key, json.getString(key));
		}
			
		return m;
	}
	
	public static JSONObject MapToJSON(Map<String, String> m) {
		JSONObject json = new JSONObject();
		
		for (String key: m.keySet())
			json.put(key, m.get(key));
		
		return json;
	}
}
