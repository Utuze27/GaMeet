package com.generation.gameet.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.generation.gameet.entities.Gioco;
import com.generation.gameet.entities.Utente;


@Component
public class DAO {
	@Autowired
	DAOUtenti daoUtenti;
	
	@Autowired
	DAOGiochi daoGiochi;
	
	@Autowired
	DAOTeam daoTeam;
	
	@Autowired
	private Database db;
	
	public boolean aggiungiGioco(int idUtente, String titolo) {
		boolean aggiunto = false;
		
		Map<String, String> m = daoGiochi.read(titolo);
		
		if (!m.isEmpty()) {
			Gioco g = Gioco.fromMap(m);
			
			String query = 	"insert into Utenti_Giochi (id_utente, id_gioco) " +
							"values (?, (select id from Giochi where titolo = ?))";
			
			aggiunto = db.update(query, idUtente + "", titolo);
		}
		
		return aggiunto;
	}
	
	public boolean rimuoviGioco(int idUtente, String titolo) {
		boolean rimosso = false;
		
		Map<String, String> m = daoGiochi.read(titolo);
		
		if (!m.isEmpty()) {
			Gioco g = Gioco.fromMap(m);
			
			String query = 	"delete from Utenti_Giochi where id_gioco = " +
							"(select id from Giochi where titolo = ?) AND id_utente = ?";

			rimosso = db.update(query, titolo, idUtente + "");
		}
		
		return rimosso;
	}
	
	//prende tutti gli  utenti (tranne idUtente) che giocano a uno qualsiasi dei miei giochi
	//ma con almeno MINGIOCHIINCOMUNE giochi in comune
	public List<Utente> matchUtenti(int idUtente) {
		List<Utente> listaUtenti = new ArrayList<>();
		
		final int MINGIOCHIINCOMUNE = 3;
		
		String query = 
					"select distinct utenti.* from utenti_giochi as u inner join utenti on utenti.id=u.id_utente "
				+ 	"where id_gioco in (select id_gioco from utenti_giochi where id_utente = ?) and id_utente != ? "
				+ 	"and ( "
				+ 	"	select count(*) from utenti_giochi where id_utente = u.id_utente "
				+ 	"	and id_gioco in (select id_gioco from utenti_giochi where id_utente = ?) "
				+ 	") >= ?;";
		
		List<Map<String, String>> lista = db.read(query, idUtente + "", idUtente + "", idUtente + "", MINGIOCHIINCOMUNE + "");
		
		for (Map<String, String> m: lista)
			listaUtenti.add(Utente.fromMap(m));
		
		return listaUtenti;
	}
	
	public List<Gioco> listaGiochiMatch(int idUtente) {
		List<Gioco> listaGiochi = new ArrayList<>();
		
		final int MINGIOCHIINCOMUNE = 3;
		
		String query = "select distinct giochi.* from utenti_giochi\r\n"
				+ "	inner join utenti on utenti_giochi.id_utente=utenti.id\r\n"
				+ "	inner join giochi on utenti_giochi.id_gioco=giochi.id \r\n"
				+ "	where utenti_giochi.id_gioco in (\r\n"
				+ "		select id_gioco from utenti_giochi where id_utente = ?\r\n"
				+ "	) and utenti_giochi.id_utente in (\r\n"
				+ "		select id_utente from (\r\n"
				+ "			select\r\n"
				+ "			u.id as id_utente,\r\n"
				+ "			(\r\n"
				+ "				select count(*) from utenti_giochi where id_utente=? and id_gioco in (\r\n"
				+ "					select id_gioco from utenti_giochi where id_utente=u.id\r\n"
				+ "				)\r\n"
				+ "			) as gc\r\n"
				+ "			from utenti as u where u.id != ?\r\n"
				+ "		) as t where gc >= ?\r\n"
				+ "	);";
		
		List<Map<String, String>> lista = db.read(query, idUtente + "", idUtente + "", idUtente + "", MINGIOCHIINCOMUNE + "");
		
		for (Map<String, String> m: lista)
			listaGiochi.add(Gioco.fromMap(m));
		
		return listaGiochi;
	}
}
