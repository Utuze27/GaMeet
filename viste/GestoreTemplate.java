package viste;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.generation.gameet.dao.DAOGiochi;
import com.generation.gameet.dao.DAOUtenti;
import com.generation.gameet.entities.Gioco;
import com.generation.gameet.entities.Messaggio;
import com.generation.gameet.entities.Team;
import com.generation.gameet.entities.Utente;

public class GestoreTemplate {
	//private static final String percorsoCartellaViste = "C:\\Users\\Michele\\eclipse-workspace-progetto-finale\\gameet\\src\\main\\webapp\\viste\\";
	private static final String percorsoCartellaViste = "src/main/webapp/viste/";
	
	@Autowired
	private DAOUtenti daoUtenti;
	
	@Autowired
	private DAOGiochi daoGiochi;
	
	public GestoreTemplate() {
		
	}

	public String leggiHTML(String nomeFile) {
		String ris = "";

		try {
			Scanner file = new Scanner(new File(percorsoCartellaViste + nomeFile));

			while(file.hasNextLine())
				ris += file.nextLine() + "\n";

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ris;
	}

	public String graficaUtente(Utente u, String teamName) {
		return leggiHTML("templateUtente.html")
				.replace("[avatar_url]", u.getAvatarURL())				
				.replace("[id]", 	u.getId() 	+ "")
				.replace("[nome]", 	u.getNome())
				.replace("[cognome]", 	u.getCognome())
				.replace("[nickname]", 	u.getNickname())
				.replace("[nazione]", 	u.getNazione())
				.replace("[pro]", 	u.isPro() ? "si" : "no")
				.replace("[email]", 	u.getEmail())
				.replace("[sesso]", 	u.getSesso() + "")
				.replace("[nome_team]", teamName);
	}

	public String graficaUtenti(List<Utente> utenti, List<String> teamNames) {
		String ris = leggiHTML("elencoUtenti.html");

		String elencoGraficato = "";

		for(int i=0; i < utenti.size(); i++)
			elencoGraficato += graficaUtente(utenti.get(i), teamNames.get(i)) + "<br>";

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	
	public String graficaGioco(Gioco g) {
		return leggiHTML("templateGioco.html")
				.replace("[id]", 				g.getId() 	+ "")
				.replace("[titolo]", 			g.getTitolo())
				.replace("[casa_produttrice]", 	g.getCasaProduttrice())
				.replace("[anno_uscita]", 		g.getAnnoUscita() 	+ "")
				.replace("[genere]", 			g.getGenere())
				.replace("[piattaforme]", 		g.getPiattaforme());
	}
	
	public String graficaGiochi(List<Gioco> giochi) {
		String ris = leggiHTML("elencoGiochi.html");

		String elencoGraficato = "";

		for(Gioco g: giochi)
			elencoGraficato += graficaGioco(g) + "<br>";

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	
	public String graficaTeam(Team t) {
		return leggiHTML("templateTeam.html")
				.replace("[id]", 				t.getId() 	+ "")
				.replace("[nome]", 				t.getNome())
				.replace("[data_creazione]", 	t.getDataCreazione().toString())
				.replace("[id_gioco]", 			daoGiochi.idToTitolo(t.getIdGioco()));
	}
	
	public String graficaTeams(List<Team> teams) {
		String ris = leggiHTML("elencoTeams.html");

		String elencoGraficato = "";

		for(Team t: teams)
			elencoGraficato += graficaTeam(t) + "<br>";

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	
	public String graficaMessaggio(Messaggio m) {
        return leggiHTML("templateMessaggio.html")
                .replace("[id]",                 m.getId()     + "")
                .replace("[from_user_id]",       daoUtenti.IDtoNick(m.getFromUserId()) + "")
                .replace("[to_user_id]",         daoUtenti.IDtoNick(m.getToUserId()) + "")
                .replace("[data_messaggio]",     m.getDataMessaggio().toString())
                .replace("[contenuto]",          m.getContenuto());

    }
	
	public String graficaMessaggi(List<Messaggio> messaggi) {
        String ris = leggiHTML("elencoMessaggi.html");

        String elencoGraficato = "";

        for(Messaggio m: messaggi)
            elencoGraficato += graficaMessaggio(m) + "<br>";

        ris = ris.replace("[elenco]", elencoGraficato);

        return ris;
    }
	
	public String graficaUtenteAmico(Utente u, String teamName) {
		return leggiHTML("templateUtenteAmico.html")
				.replace("[avatar_url]", u.getAvatarURL())
				.replace("[id]", 	u.getId() 	+ "")
				.replace("[nome]", 	u.getNome())
				.replace("[cognome]", 	u.getCognome())
				.replace("[nickname]", 	u.getNickname())
				.replace("[nazione]", 	u.getNazione())
				.replace("[pro]", 	u.isPro() ? "si" : "no")
				.replace("[sesso]", 	u.getSesso() + "")
				.replace("[nome_team]", teamName);
	}
	
	
	public String graficaUtentiAmici(List<Utente> utenti, List<String> teamNames) {
		String ris = leggiHTML("elencoUtentiAmici.html");

		String elencoGraficato = "";

		for(int i=0; i < utenti.size(); i++)
			elencoGraficato += graficaUtente(utenti.get(i), teamNames.get(i));

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	
	public String graficaProfilo(Utente u, String teamName) {
		return leggiHTML("templateProfilo.html")
				.replace("[avatar_url]", u.getAvatarURL())
				.replace("[id]", 	u.getId() 	+ "")
				.replace("[nome]", 	u.getNome())
				.replace("[cognome]", 	u.getCognome())
				.replace("[nickname]", 	u.getNickname())
				.replace("[nazione]", 	u.getNazione())
				.replace("[pro]", 	u.isPro() ? "si" : "no")
				.replace("[sesso]", 	u.getSesso() + "")
				.replace("[nome_team]", teamName)
				.replace("[email]", u.getEmail());
	}
	
	public String graficaRichiesta(Map<String, String> m) {
		return 	leggiHTML("templateRichiestaAmicizia.html")
				.replace("[da]", m.get("nickname"))
				.replace("[data_richiesta]", m.get("data_richiesta"))
				.replace("[nickname]", m.get("nickname"));
	}
	
	public String graficaRichiesteAmicizie(int idUtente) {
		List<Map<String, String>> richieste = daoUtenti.getRichiesteAmicizie(idUtente);
		
		String ris = leggiHTML("elencoRichiesteAmicizie.html");

		String elencoGraficato = "";

		for(Map<String, String> m: richieste)
			elencoGraficato += graficaRichiesta(m) + "<br>";

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	public String graficaMatch(Utente u, String teamName) {
		return leggiHTML("templateMatch.html")
				.replace("[avatar_url]", u.getAvatarURL())
				.replace("[id]", 	u.getId() 	+ "")
				.replace("[nome]", 	u.getNome())
				.replace("[cognome]", 	u.getCognome())
				.replace("[nickname]", 	u.getNickname())
				.replace("[nazione]", 	u.getNazione())
				.replace("[pro]", 	u.isPro() ? "si" : "no")
				.replace("[sesso]", 	u.getSesso() + "")
				.replace("[nome_team]", teamName)
				.replace("[email]", u.getEmail());
	}
	
	public String graficaMatchs(List<Utente> utenti, List<String> teamNames) {
		String ris = leggiHTML("elencoMatch.html");

		String elencoGraficato = "";

		for(int i=0; i < utenti.size(); i++)
			elencoGraficato += graficaMatch(utenti.get(i), teamNames.get(i)) + "<br>";

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	
	public String graficaGiochiMatch(List<Gioco> giochi) {
		String ris = leggiHTML("elencoGiochiMatch.html");

		String elencoGraficato = "";

		for(Gioco g: giochi)
			elencoGraficato += graficaGiocoMatch(g) + "<br>";

		ris = ris.replace("[elenco]", elencoGraficato);

		return ris;
	}
	
	public String graficaGiocoMatch(Gioco g) {
		return leggiHTML("templateGioco.html")
				.replace("[id]", 				g.getId() 	+ "")
				.replace("[titolo]", 			g.getTitolo())
				.replace("[casa_produttrice]", 	g.getCasaProduttrice())
				.replace("[anno_uscita]", 		g.getAnnoUscita() 	+ "")
				.replace("[genere]", 			g.getGenere())
				.replace("[piattaforme]", 		g.getPiattaforme());
	}

}