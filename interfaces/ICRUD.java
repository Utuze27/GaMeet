package com.generation.gameet.interfaces;

import java.util.List;
import java.util.Map;

import com.generation.gameet.utility.Utility;

public interface ICRUD {
	boolean create(Map<String, String> m);
	List<Map<String, String>> read();
	Map<String, String> read(int id);
	boolean update(Map<String, String> m);
	boolean delete(int id);
	
	default String readString() {
		String ris = "";
		
		for (Map<String, String> m: read())
			ris += Utility.mapToString(m) + "\n";
		
		return ris;
	}
	
	default String readString(int id) {
		return Utility.mapToString(read(id));
	}
}
