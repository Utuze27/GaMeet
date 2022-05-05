package com.generation.gameet.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.generation.gameet.dao.DAO;
import com.generation.gameet.dao.DAOUtenti;
import com.generation.gameet.entities.Gioco;
import com.generation.gameet.entities.Team;
import com.generation.gameet.entities.Utente;

import viste.GestoreTemplate;

@Controller
@RequestMapping("/utente")
public class UtentiController {
	@Autowired
	private DAOUtenti daoUtenti;
	
	@Autowired
	private DAO dao;
	
	@Autowired
	private ApplicationContext context;
	
	//il nome deve coincidere con il nome del metodo (Bean)
	@Autowired
	private GestoreTemplate gestoreTemplate;
	
	//localhost:8080/utente/leggiUtenti
	@GetMapping("/leggiUtenti")
	@ResponseBody
	public String graficaUtenti(HttpSession session) {
		if (session.getAttribute("login") == null)
			return "redirect:/deviEssereLoggato";
		else {			
			List<Map<String, String>> listaUtenti = daoUtenti.read();
			List<Utente> utenti = new ArrayList<>();
			
			for (Map<String, String> m: listaUtenti)
				utenti.add(Utente.fromMap(m));
			
			return gestoreTemplate.graficaUtenti(utenti, daoUtenti.getTeamNames(utenti));
		}
	}
	
	
	//localhost:8080/utente/leggiUtente?id=3
	@GetMapping("/leggiUtente")
	@ResponseBody
	public String leggiUtente( @RequestParam("nickname") String nickname) {
		Utente u = Utente.fromMap(daoUtenti.read(daoUtenti.nickToID(nickname)));
		
		return gestoreTemplate.graficaUtente(u, daoUtenti.getTeamName(u));
	}
	
	//localhost:8080/utente/aggiungiUtente?nome=Carlo&cognome=Rossi&nickname=beppe02&nazione=germany&email=beppe@gmail.com&pass=beppevess&sesso=m&pro=n&adm=n&data_registrazione=2022-04-10
	@GetMapping("/aggiungiUtente")
	@ResponseBody
	public String aggiungiUtente(@RequestParam Map<String,String> m) {
		return daoUtenti.create(m) ? "Utente creato!" : "Errore";
	}
	
	//localhost:8080/utente/modificaUtente?id=5&nome=Carlo&cognome=Rossi&nickname=beppe02&nazione=germany&email=beppe@gmail.com&pass=beppevess&sesso=m&pro=n&adm=n&data_registrazione=2022-04-10
	@GetMapping("/modificaUtente")
	@ResponseBody
	public String modificaUtente(@RequestParam Map<String,String> m) {
		return daoUtenti.update(m) ? "Utente modificato!" : "Errore";
	}
	
	//localhost:8080/utente/eliminaUtente?id=4
	@GetMapping("/eliminaUtente")
	@ResponseBody
	public String eliminaUtente(@RequestParam("id") int id) {
		return daoUtenti.delete(id) ? "Utente eliminato!" : "Errore";
	}
	
	//localhost:8080/utente/aggiungiGioco?titolo=Horizon Zero Dawn
	@GetMapping("/aggiungiGioco")
	@ResponseBody
	public String aggiungiGioco(HttpSession session, @RequestParam("titolo") String titolo) {
		return dao.aggiungiGioco((int) session.getAttribute("id"), titolo) ? "Gioco aggiunto!" : "Errore";
	}
	
	//localhost:8080/utente/rimuoviGioco?idUtente=3&titolo=Batman
	@GetMapping("/rimuoviGioco")
	@ResponseBody
	public String rimuoviGioco(HttpSession session, @RequestParam("titolo") String titolo) {
		return dao.rimuoviGioco((int) session.getAttribute("id"), titolo) ? "Gioco rimosso!" : "Errore";
	}
	
	//localhost:8080/utente/matchUtenti
		@GetMapping("/matchUtenti")
		@ResponseBody
		public String matchUtenti(HttpSession session) {
			
			List<Utente> utenti = dao.matchUtenti((int) session.getAttribute("id"));
			
			if (utenti.isEmpty())
				return "Il match non ha trovato nessun utente!";
			
			
			return gestoreTemplate.graficaMatchs(utenti, daoUtenti.getTeamNames(utenti));
		}

	
	
	//localhost:8080/utente/inviaAmicizia?nickname=Jack2
	@GetMapping("/inviaAmicizia")
	public String inviaAmicizia(HttpSession session, @RequestParam("nickname") String nickname) {
		boolean ris = false;
		
		int idDest = daoUtenti.nickToID(nickname);
		
		if (idDest != 0)
			ris = daoUtenti.inviaAmicizia((int) session.getAttribute("id"), idDest);
		
		return ris ? "redirect:/utente/provaInvioAmicizia" : "redirect:/utente/erroreInvioAmicizia";
	}

	@GetMapping("/provaInvioAmicizia")
	public String provaInvioAmicizia()
	{
		return "/provaInvioAmicizia.html";
	}
	@GetMapping("/erroreInvioAmicizia")
	public String erroreInvioAmicizia()
	{
		return "/erroreInvioAmicizia.html";
	}
	
	//localhost:8080/utente/accettaAmicizia?nickname=ErCuoco
	@GetMapping("/accettaAmicizia")
	@ResponseBody
	public String accettaAmicizia(HttpSession session, @RequestParam("nickname") String nickname) {
		if (daoUtenti.accettaAmicizia(daoUtenti.nickToID(nickname), (int) session.getAttribute("id")))
			return gestoreTemplate.leggiHTML("richiestaAmiciziaAccettata.html");
		else
			return gestoreTemplate.leggiHTML("erroreAccettaAmicizia.html");
		
	}
	
	//localhost:8080/utente/rifiutaAmicizia?idUtente2=3
	@GetMapping("/rifiutaAmicizia")
	@ResponseBody
	public String rifiutaAmicizia(HttpSession session,  @RequestParam("nickname") String nickname) {
		if (daoUtenti.rifiutaAmicizia(daoUtenti.nickToID(nickname), (int) session.getAttribute("id")))
			return gestoreTemplate.leggiHTML("richiestaAmiciziaRifiutata.html");
		else
			return gestoreTemplate.leggiHTML("erroreRifiutaAmicizia.html");
	}
	
	//localhost:8080/utente/eliminaAmicizia?nickname=...
	@GetMapping("/eliminaAmicizia")
	@ResponseBody
	public String eliminaAmicizia(HttpSession session,@RequestParam("nickname") String nickname) {
		if (daoUtenti.eliminaAmicizia(daoUtenti.nickToID(nickname), (int) session.getAttribute("id")))
			return gestoreTemplate.leggiHTML("richiestaAmiciziaEliminata.html");
		else
			return gestoreTemplate.leggiHTML("erroreEliminaAmicizia.html");
		
	}
	
	//localhost:8080/utente/listaAmici
	@GetMapping("/listaAmici")
	@ResponseBody
	public String listaAmici(HttpSession session) {
		String ris = "";
		
		List<Utente> utenti = new ArrayList<>();
		List<String> teams = new ArrayList<>();
		List<Map<String, String>> lista = daoUtenti.listaAmici((int) session.getAttribute("id"));
		
		if (lista.isEmpty())
			ris = gestoreTemplate.leggiHTML("noAmici.html");
		else {	
			for (Map<String, String> m: lista) {
				String nickname = m.get("nickname");
				Utente u = daoUtenti.leggiDaNickname(nickname);
				
				if (u != null) {					
					utenti.add(u);
					
					teams.add(daoUtenti.getTeam(u).getNome());
				}
			}
			
			ris = gestoreTemplate.graficaUtentiAmici(utenti, teams);
		}
		
		return ris;
	}

	
	//localhost:8080/utente/listaGiochi
	@GetMapping("/listaGiochi")
	@ResponseBody
	public String listaGiochi(HttpSession session)
	{

		List<Gioco> giochi = new ArrayList<>();
		
		List<Map<String, String>> elenco = daoUtenti.listaGiochi((int) session.getAttribute("id"));
		
		for(Map<String,String> m : elenco) {
			giochi.add(Gioco.fromMap(m));
		}
		
		return gestoreTemplate.graficaGiochi(giochi);
	}
	
	//localhost:8080/utente/listaGiochiAmici?idUtente=3
	@GetMapping("/listaGiochiAmici")
	@ResponseBody
	public String listaGiochiAmici(HttpSession session) {
		String ris = "";
		
		List<Map<String, String>> lista = daoUtenti.listaGiochiAmici((int) session.getAttribute("id"));
		
		if (lista.isEmpty())
			ris = "Nessun risultato trovato!";
		else
			for (Map<String, String> m: lista) {				
				for (String key: m.keySet())
					ris += key + ": " + m.get(key) + "<br>";
				
				ris += "<br>";
			}
		
		return ris;
	}
	
	//localhost:8080/utente/leggiMe
	@GetMapping("/leggiMe")
    @ResponseBody
    public String leggiMe(HttpSession session) {
        Utente u = Utente.fromMap(daoUtenti.read((int) session.getAttribute("id")));

        return gestoreTemplate.graficaProfilo(u, daoUtenti.getTeamName(u));
    }
	
	//localhost:8080/utente/formAmicizia
	@GetMapping("/formAmicizia")
    public String formNewFriend() {

         return "/formamicizia.html";
    }
	
	//localhost:8080/utente/formEliminaAmicizia
		@GetMapping("/formEliminaAmicizia")
	    public String formEliminaAmicizia() {

	         return "/formEliminaAmicizia.html";
	    }


	//localhost:8080/utente/richiesteAmicizia
	@GetMapping("/richiesteAmicizia")
	@ResponseBody
    public String richiesteAmicizia(HttpSession session) {
		return gestoreTemplate.graficaRichiesteAmicizie((int) session.getAttribute("id"));
    }
	
	//localhost:8080/utente/listaGiochiMatch
	@GetMapping("/listaGiochiMatch")
	@ResponseBody
	public String listaGiochiMatch(HttpSession session) {
		String ris = "";
		
		List<Gioco> lista = dao.listaGiochiMatch((int) session.getAttribute("id"));
		
		if (lista.isEmpty())
			ris = gestoreTemplate.leggiHTML("noGiochiMatch.html");
		else
			ris = gestoreTemplate.graficaGiochiMatch(lista);
		
		return ris;
	}
}
