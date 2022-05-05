package com.generation.gameet.entities;

import java.sql.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.generation.gameet.utility.Utility;

@Component
public class Team extends Entity {
	private String nome = "";
	private Date dataCreazione;
	private int idGioco;
	
	public Team() {
		//costruttore vuoto
	}
	
	public Team(int id, String nome, Date dataCreazione, int idGioco) {
		super(id);
		setNome(nome);
		setDataCreazione(dataCreazione);
		setIdGioco(idGioco);
	}
	
	public Team(String nome, Date dataCreazione, int idGioco) {
		setNome(nome);
		setDataCreazione(dataCreazione);
		setIdGioco(idGioco);
	}
	
	public Team(Map<String, String> m) {
		super(m);
		
		if (m.containsKey("nome"))
			setNome(m.get("nome"));
		if (m.containsKey("data_creazione"))
			setDataCreazione(Date.valueOf(m.get("data_creazione")));
		if (m.containsKey("id_gioco"))
			setIdGioco(Integer.parseInt(m.get("id_gioco")));
	}

	public static Team fromMap(Map<String, String> m) {
		return new Team(m);
	}
	
	public Map<String, String> toMap() {
		Map<String, String> m = super.toMap();
		
		m.put("nome", getNome());
		m.put("data_creazione", getDataCreazione().toString());
		m.put("id_gioco", getIdGioco() + "");
		
		return m;
	}
	
	public static boolean isTeamValid(Map<String, String> m) {
		boolean valid = false;
		
		Team t = new Team(m);
		
		if (!Utility.isStringEmpty(t.getNome()) && t.getIdGioco() > 0)
			valid = true;
		
		return valid;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public int getIdGioco() {
		return idGioco;
	}

	public void setIdGioco(int idGioco) {
		this.idGioco = idGioco;
	}

	@Override
	public String toString() {
		/*
		return 	super.toString() +
				"Nome: " 			+ nome 			+ "\n" +
				"Data creazione: " 	+ dataCreazione + "\n" +
				"ID gioco: " 		+ idGioco 		+ "\n";
		*/
		return super.toString() + Utility.mapToString(toMap());
	}
}
