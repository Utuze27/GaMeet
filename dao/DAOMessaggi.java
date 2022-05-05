package com.generation.gameet.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.generation.gameet.entities.Messaggio;
import com.generation.gameet.entities.Utente;
import com.generation.gameet.interfaces.ICRUD;

public class DAOMessaggi implements ICRUD {
	@Autowired
	private Database db;

	@Autowired
	private DAOUtenti daoUtenti;

	@Override
	public boolean create(Map<String, String> m) {
		if (Messaggio.isMessaggioValid(m))
			return db.update(
				"INSERT INTO messaggi (from_user_id, to_user_id, contenuto) " +
				"VALUES (?, ?, ?)",

				m.get("from_user_id"), m.get("to_user_id"), m.get("contenuto")
			);
		else
			return false;
	}

	@Override
	public List<Map<String, String>> read() {
		return db.read("SELECT * FROM messaggi");
	}

	@Override
	public Map<String, String> read(int id) {
		List<Map<String, String>> listaMessaggi = db.read("SELECT * FROM messaggi WHERE id = ?", id + "");

		return listaMessaggi.isEmpty() ? new LinkedHashMap<String, String>() : listaMessaggi.get(0);
	}

	@Override
	public boolean update(Map<String, String> m) {
		if (Messaggio.isMessaggioValid(m))
			return db.update(
				"UPDATE messaggi SET from_user_id = ?, to_user_id = ?, contenuto = ? " +
				"WHERE id = ?",

				m.get("from_user_id"), m.get("to_user_id"), m.get("contenuto"),
				m.get("id")
			);
		else
			return false;
	}

	@Override
	public boolean delete(int id) {
		return db.update("DELETE FROM messaggi WHERE id = ?", id + "");
	}

	public String leggiMessaggio(int idMessaggio) {
		String ris = "Messaggio non trovato!";
		
		List<Map<String, String>> lista = db.read("SELECT contenuto FROM messaggi WHERE id = ?", idMessaggio + "");
		
		if (!lista.isEmpty())
			ris = lista.get(0).get("contenuto");
		
		return ris;
	}
	
	public boolean scriviMessaggio(int fromIdUtente, int toIdUtente, String contenuto) {
		boolean ris = false;
		
		System.out.println(fromIdUtente);
		System.out.println(toIdUtente);
		System.out.println(contenuto);
		
		if (daoUtenti.sonoAmici(fromIdUtente, toIdUtente)) {
			String query = 	"INSERT INTO messaggi (from_user_id, to_user_id, contenuto) " +
							"VALUES (?, ?, ?)";
			
			ris = db.update(query, fromIdUtente + "", toIdUtente + "", contenuto);
		}
		
		return ris;
	}
	
	public List<Map<String, String>> leggiMessaggiPerUtente(int from_user_id)
    {

        String query ="select * from Messaggi where from_user_id = ? or to_user_id = ?";

        List<Map<String, String>> elenco = db.read(query, from_user_id +"", from_user_id +"");


        return elenco;
    }
}
