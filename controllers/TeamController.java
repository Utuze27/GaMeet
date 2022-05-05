package com.generation.gameet.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.generation.gameet.dao.DAOTeam;
import com.generation.gameet.entities.Team;
import viste.GestoreTemplate;

@Controller
@RequestMapping("/team")
public class TeamController {
	@Autowired
	private DAOTeam daoTeam;
	
	@Autowired
	private GestoreTemplate gestoreTemplate;
	
	//localhost:8080/team/leggiTeams
	@GetMapping("/leggiTeams")
	@ResponseBody
	public String graficaTeams() {
		List<Map<String, String>> listaTeams= daoTeam.read();
		List<Team> giochi = new ArrayList<>();
		
		for (Map<String, String> m: listaTeams)
			giochi.add(Team.fromMap(m));
			
		return gestoreTemplate.graficaTeams(giochi);
	}
	
	//localhost:8080/team/leggiTeam?nome=Kobra Kai
	@GetMapping("/leggiTeam")
	@ResponseBody
	public String leggiTeam(@RequestParam("nome") String nome) {
		Team g = Team.fromMap(daoTeam.read(daoTeam.nameToID(nome)));
		
		return gestoreTemplate.graficaTeam(g);
	}
	
	//localhost:8080/team/aggiungiTeam?nome=team2&data_creazione=2022-03-01&id_gioco=1
	@GetMapping("/aggiungiTeam")
	@ResponseBody
	public String aggiungiTeam(@RequestParam Map<String,String> m) {
		return daoTeam.create(m) ? "Team aggiunto!" : "Errore";
	}
	
	//localhost:8080/team/modificaTeam?id=2&nome=NuovoTeam&data_creazione=2022-03-01&id_gioco=1
	@GetMapping("/modificaTeam")
	@ResponseBody
	public String modificaTeam(@RequestParam Map<String,String> m) {
		return daoTeam.update(m) ? "Team modificato!" : "Errore";
	}
	
	//localhost:8080/team/aggiungiAlTeam?nome=Kobra Kai
	@GetMapping("/aggiungiAlTeam")
	@ResponseBody
	public String aggiungiAlTeam(HttpSession session, @RequestParam("nome") String nome) {
		return daoTeam.aggiungiAlTeam((int) session.getAttribute("id"),daoTeam.nameToID(nome)) ? "Utente aggiunto al Team" : "Utente non pro oppure sei già nel team";
	}

	//localhost:8080/team/esciDalTeam?nome=Kobra Kai
	@GetMapping("/esciDalTeam")
	@ResponseBody
	public String esciDalTeam(HttpSession session, @RequestParam("nome") String nome) {
		return daoTeam.esciDalTeam((int) session.getAttribute("id"), daoTeam.nameToID(nome)) ? "Utente uscito dal Team" : "Utente non presente nel team";
	}

	//localhost:8080/team/listaUtentiTeam?nome=Kobra Kai
	@GetMapping("/listaUtentiTeam")
	@ResponseBody
	public String listaUtentiTeam(@RequestParam("nome") String nome) {
		return daoTeam.listaUtentiTeam(daoTeam.nameToID(nome));
	}
	
	//localhost:8080/team/eliminaTeam?id_utente=1&id_team=1
	@GetMapping("/eliminaTeam")
	@ResponseBody
	public String eliminaTeam(@RequestParam("id_utente") int idUtente, @RequestParam("id_team") int idTeam) {
		return daoTeam.eliminaTeam(idTeam, idUtente) ? "Team eliminato" : "Errore/Non sei un admin";
	}
	@GetMapping("/formteam")
    public String formComeTeam() {

         return "/FormTeam.html";
    }
	@GetMapping("/formUscitaTeam")
    public String formUscitaTeam() {

         return "/FormUscitaTeam.html";
    }
}