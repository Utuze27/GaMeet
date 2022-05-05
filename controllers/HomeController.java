package com.generation.gameet.controllers;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.generation.gameet.dao.DAOUtenti;
import com.generation.gameet.entities.Utente;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private DAOUtenti daoUtenti;
	
	@GetMapping("/") 
	public String home(HttpSession session) {
		//se l'utente non è loggato, fai il redirect al mapping formlogin
		if (session.getAttribute("login") == null)
			return "redirect:/formlogin";
		
		//altrimenti, mostra la home (utente loggato)
		return "home.html";
	}
	
	@GetMapping("/formlogin")
	public String formLogin() {
		return "formlogin.html";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam("email") String e, @RequestParam("password") String p, HttpSession session) {
		if (daoUtenti.isLoginValid(e, p)) {
			session.setAttribute("login", "OK");
			session.setAttribute("email", e);
			session.setAttribute("id", daoUtenti.getUserByEmail(e).getId());
			
			//se loggato, redirect su /
			return "redirect:/";
		} else {
			//altrimenti, redirect su /formlogin.html
			return "redirect:/formlogin";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("login", null);
		session.setAttribute("email", null);
		session.setAttribute("id", null);
		
		return "redirect:/formlogin";
	}
	
	@GetMapping("/registrati")
	public String registrati() {
		return "formnuovoutente.html";
	}
	
	@GetMapping("/nuovoutente")
	public String nuovoUtente(@RequestParam Map<String, String> m) {	
		String ris = "";
		
		Utente u = Utente.fromMap(m);
		
		if (daoUtenti.emailEsistente(u.getEmail()))
			ris = "Email già esistente!";
		else
			ris = daoUtenti.create(m) ? "redirect:/" : "Errore";
		
		return ris;
	}
	
	//localhost:8080/
	@GetMapping("/deviEssereLoggato")
	public String deviEssereLoggato()
	{
		return "deviEssereLoggato.html";
	}
		
		
}
