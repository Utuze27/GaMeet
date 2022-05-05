package com.generation.gameet.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.generation.gameet.entities.Gioco;
import com.generation.gameet.entities.Team;
import com.generation.gameet.interfaces.ICRUD;

@Component
public class DAOGiochi implements ICRUD {
	@Autowired
	private Database db;
	
	@Override
	public boolean create(Map<String, String> m) {
		if (Gioco.isGiocoValid(m))
			return db.update(
				"INSERT INTO giochi (titolo, casa_produttrice, anno_uscita, genere, piattaforme) " +
				"VALUES (?, ?, ?, ?, ?)",
				
				m.get("titolo"), m.get("casa_produttrice"), m.get("anno_uscita"),
				m.get("genere"), m.get("piattaforme")
			);
		else
			return false;
	}

	@Override
	public List<Map<String, String>> read() {
		return db.read("SELECT * FROM giochi");
	}

	@Override
	public Map<String, String> read(int id) {
		List<Map<String, String>> listaGiochi = db.read("SELECT * FROM giochi WHERE id = ?", id + "");
		
		return listaGiochi.isEmpty() ? new LinkedHashMap<String, String>() : listaGiochi.get(0);
	}
	
	public Map<String, String> read(String titolo) {
		List<Map<String, String>> listaGiochi = db.read("SELECT * FROM giochi WHERE titolo = ?", titolo + "");
		
		return listaGiochi.isEmpty() ? new LinkedHashMap<String, String>() : listaGiochi.get(0);
	}

	@Override
	public boolean update(Map<String, String> m) {
		if (Gioco.isGiocoValid(m))
			return db.update(
				"UPDATE giochi SET titolo = ?, casa_produttrice = ?, anno_uscita = ?, " +
				"genere = ?, piattaforme = ? WHERE id = ?",
				
				m.get("titolo"), m.get("casa_produttrice"), m.get("anno_uscita"),
				m.get("genere"), m.get("piattaforme"), m.get("id")
			);
		else
			return false;
	}

	@Override
	public boolean delete(int id) {
		return db.update("DELETE FROM giochi WHERE id = ?", id + "");
	}
	
	//ritorna tutti i team che giocano a idGioco
	public List<Team> getTeam(int idGioco) {
		List<Team> teams = new ArrayList<Team>();
		
		List<Map<String, String>> l = db.read("SELECT * FROM teams WHERE id_gioco = ?", idGioco + "");
		
		if (!l.isEmpty())
			for (Map<String, String> m: l)
				teams.add(Team.fromMap(m));
		
		return teams;
	}
	
	public String idToTitolo(int id) {
		String titolo = null;
		
		List<Map<String, String>> lista = db.read("SELECT titolo FROM giochi WHERE id = ?", id + "");
		
		if(!lista.isEmpty()) {
			titolo = lista.get(0).get("titolo");
		}
		
		return titolo;
	}
}
