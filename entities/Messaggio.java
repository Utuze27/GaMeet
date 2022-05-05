package com.generation.gameet.entities;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.generation.gameet.utility.Utility;

public class Messaggio extends Entity {
	private int fromUserId, toUserId;
	private String contenuto;
	private Date dataMessaggio;
	
	public Messaggio() {
		//costruttore vuoto
	}
	
	public Messaggio(int id, int fromUserId, int toUserId, String contenuto, Date dataMessaggio) {
		super(id);
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.contenuto = contenuto;
		this.dataMessaggio = dataMessaggio;
	}
	
	public Messaggio(int fromUserId, int toUserId, String contenuto, Date dataMessaggio) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.contenuto = contenuto;
		this.dataMessaggio = dataMessaggio;
	}
	
	public Messaggio(Map<String, String> m) {
		super(m);
		
		if(m.containsKey("from_user_id"))
			setFromUserId(Integer.parseInt(m.get("from_user_id")));
		if(m.containsKey("to_user_id"))
			setToUserId(Integer.parseInt(m.get("to_user_id")));
		if(m.containsKey("contenuto"))
			setContenuto(m.get("contenuto"));
		if(m.containsKey("data_messaggio"))
			setDataMessaggio(Date.valueOf(m.get("data_messaggio")));
	}
	
	public static Messaggio fromMap(Map<String, String> m) {
		return new Messaggio(m);
	}
	
	public Map<String, String> toMap() {
		Map<String, String> m = super.toMap();
		
		m.put("from_user_id", getFromUserId() + "");
		m.put("to_user_id", getToUserId() + "");
		m.put("contenuto", getContenuto());
		m.put("data_messaggio", getDataMessaggio().toString());
		
		return m;
	}
	
	public static boolean isMessaggioValid(Map<String, String> map) {
		boolean ris = false;
		
		Messaggio m = Messaggio.fromMap(map);
		
		if (!Utility.isStringEmpty(m.getContenuto()) && m.getFromUserId() > 0 && m.getToUserId() > 0)
			ris = true;
		
		return ris;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}

	public Date getDataMessaggio() {
		return dataMessaggio;
	}

	public void setDataMessaggio(Date dataMessaggio) {
		this.dataMessaggio = dataMessaggio;
	}

	@Override
	public String toString() {
		return 	super.toString() +
				"From User ID: " 	+ fromUserId 	+ "\n" +
				"To User ID: " 		+ toUserId 		+ "\n" + 
				"Contenuto: " 		+ contenuto		+ "\n" +
				"Data: "			+ dataMessaggio + "\n";
	}
	
	
}
