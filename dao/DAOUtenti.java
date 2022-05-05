package com.generation.gameet.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.generation.gameet.entities.Team;
import com.generation.gameet.entities.Utente;
import com.generation.gameet.interfaces.ICRUD;

@Component
public class DAOUtenti implements ICRUD {
	@Autowired
	private Database db;
	
	@Autowired
	ApplicationContext context;
	
	@Override
	public boolean create(Map<String, String> m) {
		if (Utente.isUtenteValid(m))
			return db.update(
				"INSERT INTO utenti (nome, cognome, nickname, sesso, nazione, id_team, pro, email, pass, " +
				"adm, preferenze, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				
				m.get("nome"), m.get("cognome"), m.get("nickname"), m.get("sesso"),
				m.get("nazione"), m.get("id_team"), m.get("pro"), m.get("email"),
				m.get("pass"),
				m.containsKey("adm") ? m.get("adm") : null,
				m.containsKey("preferenze") ? m.get("preferenze") : null,
				m.containsKey("avatar_url")	? m.get("avatar_url") : null
			);
		else
			return false;
			
	}
	
	

	@Override
	public List<Map<String, String>> read() {
		return db.read("SELECT * FROM utenti");
	}

	@Override
	public Map<String, String> read(int id) {
		List<Map<String, String>> listaUtenti = db.read("SELECT * FROM utenti WHERE id = ?", id + "");
		
		return listaUtenti.isEmpty() ? new LinkedHashMap<String, String>() : listaUtenti.get(0);
	}

	@Override
	public boolean update(Map<String, String> m) {
		if (Utente.isUtenteValid(m))
			return db.update(
				"UPDATE utenti SET nome = ?, cognome = ?, nickname = ?, sesso = ?, nazione = ?, " +
				"id_team = ?, pro = ?, email = ?, pass = ?, data_registrazione = ?, " +
				"adm = ?, preferenze = ?, avatar_url = ? WHERE id = ?",
				
				m.get("nome"), m.get("cognome"), m.get("nickname"), m.get("sesso"),
				m.get("nazione"), m.get("id_team"), m.get("pro"), m.get("email"),
				m.get("pass"), m.get("data_registrazione"), m.get("adm"),
				m.get("preferenze"), m.get("avatar_url"), m.get("id")
			);
		else
			return false;
	}

	@Override
	public boolean delete(int id) {
		return db.update("DELETE FROM utenti WHERE id = ?", id + "");
	}
	
	public Team getTeam(Utente u) {
		Team team = (Team) context.getBean("teamOggetto");
		
		if (u.getIdTeam() != 0) {
			Map<String, String> m = db.read("SELECT * FROM teams WHERE id = ?", u.getIdTeam() + "").get(0);
			
			team = Team.fromMap(m);
		}
		
		return team;
	}
	
	public int nickToID(String nickname) {
		int userID = 0;
		System.out.println(nickname);
		List<Map<String, String>> lista = db.read("SELECT id FROM utenti WHERE nickname = ?", nickname);
		
		System.out.println(lista);
		
		if(!lista.isEmpty()) {
			userID = Integer.parseInt(lista.get(0).get("id"));
		}
		System.out.println(userID);
		
		return userID;
	}
	
	public String getTeamName(Utente u) {
		String ris = "libero";
		
		Team team = getTeam(u);
		
		if (team.getId() != 0) //se non c'è nel db
			ris = team.getNome();
			
		return ris;
	}
	
	public List<String> getTeamNames(List<Utente> utenti) {
		List<String> teamNames = new ArrayList<>();
		
		for (Utente u: utenti)
			teamNames.add(getTeamName(u));
		
		return teamNames;
	}
	
	public boolean isLoginValid(String email, String password) {
		boolean valid = false;
		
		List<Map<String, String>> lista = db.read("SELECT pass FROM utenti WHERE email = ?", email);
		
		if (!lista.isEmpty())
			valid = Utente.fromMap(lista.get(0)).getPassword().equals(password);
		
		return valid;
	}
	
	public boolean emailEsistente(String email) {
		return db.read("SELECT * FROM utenti WHERE email = ?", email).isEmpty() ? false : true;
	}
	
	public int getIDUtente(String nickname) {
		int idUtente = 0;
		
		List<Map<String, String>> lista = db.read("SELECT id FROM utenti WHERE nickname = ?", nickname);
		
		if (!lista.isEmpty()) {
			Utente utente = Utente.fromMap(lista.get(0));
			
			idUtente = utente.getId();
		}
		
		return idUtente;
	}
	
	public boolean richiestaEsistente(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		String query = "SELECT * FROM amicizia "
				+ "		WHERE ((id_utente_1 = ? AND id_utente_2 = ?) OR"
				+ "		(id_utente_1 = ? AND id_utente_2 = ?)) AND data_accettata IS NULL";
		
		List<Map<String, String>> lista = db.read(query, idUtente1 + "", idUtente2 + "", idUtente2 + "", idUtente1 + "");
		
		//ris = true se la lista non è vuota (richiesta esistente)
		ris = lista.isEmpty() ? false : true;
		
		return ris;
	}
	
	public boolean amiciziaEsistente(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		String query = "SELECT * FROM amicizia "
				+ "		WHERE ((id_utente_1 = ? AND id_utente_2 = ?) OR"
				+ "		(id_utente_1 = ? AND id_utente_2 = ?)) AND data_accettata IS NOT NULL";
		
		List<Map<String, String>> lista = db.read(query, idUtente1 + "", idUtente2 + "", idUtente2 + "", idUtente1 + "");
		
		//ris = true se la lista non è vuota (richiesta esistente)
		ris = lista.isEmpty() ? false : true;
		
		return ris;
	}
	
	//controlla se idUtente1 ha inviato una richiesta a idUtente2 (ma non viceversa!)
	public boolean richiestaEsistente1a2(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		String query = "SELECT * FROM amicizia "
				+ "		WHERE (id_utente_1 = ? AND id_utente_2 = ?) "
				+ "		AND data_accettata IS NULL";
		
		List<Map<String, String>> lista = db.read(query, idUtente1 + "", idUtente2 + "");
		
		//ris = true se la lista non è vuota (richiesta esistente)
		ris = lista.isEmpty() ? false : true;
		
		return ris;
	}
	
	public boolean inviaAmicizia(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		if (idUtente1 != idUtente2 && !richiestaEsistente(idUtente1, idUtente2)) 
			ris = db.update("INSERT INTO amicizia (id_utente_1, id_utente_2) VALUES (?, ?)", idUtente1 + "", idUtente2 + "");
		
		return ris;
	}
	
	//idUtente1 ha inviato la richiesta a idUtente2
	//idUtente2 accetta la richiesta di idUtente1
	public boolean accettaAmicizia(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		//se 1 ha inviato la richiesta a 2 e non è ancora stata accettata
		if (richiestaEsistente1a2(idUtente1, idUtente2)) {
			String query = 	"UPDATE amicizia SET data_accettata = (curdate()) WHERE " +
							"id_utente_1 = ? AND id_utente_2 = ?";
			
			ris = db.update(query, idUtente1 + "", idUtente2 + "");
		}
		
		return ris;
	}
	
	//idUtente1 ha inviato la richiesta a idUtente2
	//idUtente2 rifiuta la richiesta di idUtente1
	public boolean rifiutaAmicizia(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		//se c'è una richiesta da 1 a 2 e non è ancora stata accettata
		if (richiestaEsistente1a2(idUtente1, idUtente2)) {
			String query = 	"DELETE FROM amicizia WHERE " +
							"id_utente_1 = ? AND id_utente_2 = ?";
			
			ris = db.update(query, idUtente1 + "", idUtente2 + "");
		}
		
		return ris;
	}
	
	//elimina un'amicizia tra 1 e 2 (o viceversa)
	public boolean eliminaAmicizia(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		if (amiciziaEsistente(idUtente1, idUtente2)) {
			String query = "DELETE FROM amicizia "
					+ "		WHERE ((id_utente_1 = ? AND id_utente_2 = ?) OR"
					+ "		(id_utente_1 = ? AND id_utente_2 = ?)) AND data_accettata IS NOT NULL";
			
			ris = db.update(query, idUtente1 + "", idUtente2 + "", idUtente2 + "", idUtente1 + "");
		}
		
		return ris;
	}
	
	//ritorna i nickname degli amici di idUtente
	public List<Map<String, String>> listaAmici(int idUtente) {
		List<Map<String, String>> lista;
		
		String query = "select nickname "
				+ "from amicizia join utenti on id_utente_1=utenti.id or id_utente_2=utenti.id "
				+ "where (id_utente_1=? or id_utente_2=?) and utenti.id != ? and data_accettata is not null";
		
		lista = db.read(query, idUtente + "", idUtente + "", idUtente + "");
		
		return lista;
	}
	
	public boolean sonoAmici(int idUtente1, int idUtente2) {
		boolean ris = false;
		
		String query = 	"select * from amicizia " +
						"where ((id_utente_1=? and id_utente_2=?) OR (id_utente_1=? and id_utente_2=?))" +
						"and data_accettata is not null";
		
		ris = db.read(query, idUtente1 + "", idUtente2 + "", idUtente2 + "", idUtente1 + "").isEmpty() ? false : true;
		
		return ris;
	}
	
	
	
	//ritorna la lista di giochi di un certo utente
	public List<Map<String, String>> listaGiochi(int idUtente) {
		String query = 	"SELECT giochi.* " +
						"FROM giochi INNER JOIN utenti_giochi ON giochi.id=utenti_giochi.id_gioco " +
						"WHERE id_utente = ?";
		
		return db.read(query, idUtente + "");
	}
	
	//ritorna, per ogni amico di idUtente, la lista dei giochi ai quali giocano
	//ritorna una lista di mappe.
	//ogni mappa ha le chiavi "nickname" e "titolo"
	public List<Map<String, String>> listaGiochiAmici(int idUtente) {
		String query =
				"select nickname, titolo "
			+ 	"from giochi inner join utenti_giochi on id_gioco=giochi.id "
			+ 	"	inner join utenti on id_utente=utenti.id "
			+ 	"where utenti.id in ( "
			+ 	"	select id_utente_1 from amicizia where id_utente_2=? and data_accettata is not null "
			+ 	"	union "
			+ 	"	select id_utente_2 from amicizia where id_utente_1=? and data_accettata is not null "
			+ 	") and utenti.id != ?";
		
		return db.read(query, idUtente + "", idUtente + "", idUtente + "");
	}
	
	public Utente getUserByEmail(String email) {
		Utente utente = (Utente) context.getBean("utenteOggetto");
		
		List<Map<String, String>> lista = db.read("SELECT * FROM utenti WHERE email = ?", email);
		
		if (!lista.isEmpty())
			utente = Utente.fromMap(lista.get(0));
		
		return utente;
	}
	
	public String IDtoNick(int id) {
        String n = "";
        String nick = "";

        n = String.valueOf(id);
        
        List<Map<String, String>> lista = db.read("SELECT nickname FROM utenti WHERE id = ?", n);

        if(!lista.isEmpty()) {
            nick = (lista.get(0).get("nickname"));
        }
        

        return nick;
    }
	
	public Utente leggiDaNickname(String nickname) {
		Utente u = null;
		
        List<Map<String, String>> lista = db.read("SELECT * FROM utenti WHERE nickname = ?", nickname);

        if(!lista.isEmpty()) {
            u = Utente.fromMap(lista.get(0));
        }
        

        return u;
    }
	
	//ritorna tutte le richieste di amicizia verso idUtente
	public List<Map<String, String>> getRichiesteAmicizie(int idUtente) {
		List<Map<String, String>> richieste = new ArrayList<>();
		Map<String, String> richiesta;
		
		String query = "select id_utente_1, data_richiesta "
				+ "from amicizia inner join utenti on utenti.id=amicizia.id_utente_2 "
				+ "where id_utente_2 = ? and data_accettata is null;";
		
		List<Map<String, String>> lista = db.read(query, idUtente + "");
		
		if (!lista.isEmpty()) {
			for (Map<String, String> m: lista) {
				richiesta = new LinkedHashMap<>();
				richiesta.put("nickname", IDtoNick(Integer.parseInt(m.get("id_utente_1")))); 
				richiesta.put("data_richiesta", m.get("data_richiesta"));
				
				richieste.add(richiesta);
			}
		}
		
		return richieste;
	}
}
