package com.generation.gameet.entities;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Entity {
	private int id;

	public Entity() {
		//costruttore vuoto
	}
	
	public Entity(int id) {
		setId(id);
	}
	
	public Entity(Map<String, String> m) {
		if (m.containsKey("id"))
			setId(Integer.parseInt(m.get("id")));
	}
	
	public Map<String, String> toMap() {
		Map<String, String> m = new LinkedHashMap<>();
		
		m.put("id", getId() + "");
		
		return m;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ID: " + id + "\n";
	}
}
