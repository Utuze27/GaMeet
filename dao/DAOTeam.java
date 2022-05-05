package com.generation.gameet.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.generation.gameet.entities.Team;
import com.generation.gameet.entities.Utente;
import com.generation.gameet.interfaces.ICRUD;

public class DAOTeam implements ICRUD {
	@Autowired
	private Database db;

	@Autowired
	private DAOUtenti daoUtenti;

	@Override
	public boolean create(Map<String, String> m) {
		if (Team.isTeamValid(m))
			return db.update(
					"INSERT INTO teams (nome, data_creazione, id_gioco) " +
							"VALUES (?, ?, ?)",

							m.get("nome"), m.get("data_creazione"), m.get("id_gioco")
					);
		else
			return false;
	}

	@Override
	public List<Map<String, String>> read() {
		return db.read("SELECT * FROM teams");
	}

	@Override
	public Map<String, String> read(int id) {
		List<Map<String, String>> listaTeams = db.read("SELECT * FROM teams WHERE id = ?", id + "");

		return listaTeams.isEmpty() ? new LinkedHashMap<String, String>() : listaTeams.get(0);
	}

	@Override
	public boolean update(Map<String, String> m) {
		if (Team.isTeamValid(m))
			return db.update(
					"UPDATE teams SET nome = ?, data_creazione = ?, id_gioco = ? WHERE id = ?",

					m.get("nome"), m.get("data_creazione"), m.get("id_gioco"), m.get("id")
					);
		else
			return false;
	}

	@Override
	public boolean delete(int id) {
		return db.update("DELETE FROM teams WHERE id = ?", id + "");
	}

	public boolean aggiungiAlTeam(int idUtente, int idTeam) {
		boolean ris = false;

		Utente u = Utente.fromMap(daoUtenti.read(idUtente));
		if(u.isPro() && u.getIdTeam() != idTeam)
		{
			String query = "update Utenti set id_team = ? where id = ? and pro = 'y'";

			ris = db.update(query, idTeam + "", idUtente + "");
		}

		return ris;
	}

	public boolean esciDalTeam(int idUtente, int idTeam) {
		boolean ris = false;

		Utente u = Utente.fromMap(daoUtenti.read(idUtente));

		if(u.getIdTeam() == idTeam)
		{
			String query = "update Utenti set id_team = null where id = ? and pro = 'y'";

			ris = db.update(query, idUtente + "");
		}

		return ris;
	}

	public String listaUtentiTeam(int idTeam) {
		List<Map<String, String>> elenco;
		String ris = "";

		elenco = db.read("select Utenti.nickname from Utenti inner join Teams on Utenti.id_team = Teams.ID where id_team = ?", idTeam + "");

		if(elenco.isEmpty())
			ris = "Non ci sono utenti nel Team selezionato";
		else
		{
			for(Map<String, String> m : elenco)
				ris += m.get("nickname") + "<br>";
		}

		return ris;

	}

	public boolean eliminaTeam(int idTeam, int idUtente) {
		boolean ris = false;

		Utente u = Utente.fromMap(daoUtenti.read(idUtente));

		if(u.isAdmin())
			ris = delete(idTeam);

		return ris;
	}
	
	public int nameToID(String name) {
		int teamID = 0;
		
		List<Map<String, String>> lista = db.read("SELECT id FROM teams WHERE nome = ?", name);
		
		if(!lista.isEmpty()) {
			teamID = Integer.parseInt(lista.get(0).get("id"));
		}
		
		return teamID;
	}
	
	public String idToName(int id) {
		String name = null;
		
		List<Map<String, String>> lista = db.read("SELECT nome FROM teams WHERE id = ?", id + "");
		
		if(!lista.isEmpty()) {
			name = lista.get(0).get("nome");
		}
		
		return name;
	}


}
