package com.generation.gameet.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.generation.gameet.dao.DAO;
import com.generation.gameet.dao.DAOMessaggi;
import com.generation.gameet.dao.DAOUtenti;
import com.generation.gameet.entities.Messaggio;
import com.generation.gameet.utility.Utility;

import viste.GestoreTemplate;

@Controller
@RequestMapping("/messaggio")
public class MessaggiController {
	@Autowired
	private DAOUtenti daoUtente;
	
	@Autowired
	private DAOMessaggi daoMessaggi;
	
	@Autowired
	private DAO dao;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private GestoreTemplate gestoreTemplate;
	
	//localhost:8080/messaggio/leggiMessaggio
	@GetMapping("/leggiMessaggio")
	@ResponseBody
	public String leggiMessaggio(HttpSession session) {
		return daoMessaggi.leggiMessaggio((int) session.getAttribute("id"));
	}
	
	//localhost:8080/messaggio/scriviMessaggio?nickname=ErCuoco&contenuto=Hello
	@GetMapping("/scriviMessaggio")
	@ResponseBody
	public String scriviMessaggio(HttpSession session, @RequestParam String nickname, @RequestParam String contenuto) {
		boolean inviato = false;
		
		if(!Utility.isStringEmpty(contenuto))
			inviato = daoMessaggi.scriviMessaggio(
					(int) session.getAttribute("id"),
					 daoUtente.nickToID(nickname),
				     contenuto);
		
		System.out.println(inviato);
		
		return gestoreTemplate.leggiHTML(inviato ? "invioMessaggioSuccesso.html" : "invioMessaggioErrore.html");
	}
	
//	//localhost:8080/messaggio/listaMessaggi
//	@GetMapping("/listaMessaggi")
//	@ResponseBody
//	public String listaMessaggi(HttpSession session)
//	{
//		int from_user_id = (int) session.getAttribute("id");
//		return daoMessaggi.leggiMessaggiPerUtente(from_user_id);
//	}
	//localhost:8080/messaggio/leggiMessaggi
	@GetMapping("/leggiMessaggi")
    @ResponseBody
    public String graficaMessaggi(HttpSession session) {
        if (session.getAttribute("login") == null)
            return "Devi essere loggato!";
        else {
            List<Map<String, String>> listaMessaggi = daoMessaggi.leggiMessaggiPerUtente((int) session.getAttribute("id"));
            List<Messaggio> messaggi = new ArrayList<>();
            if(listaMessaggi.isEmpty())
            	return gestoreTemplate.leggiHTML("noMessaggi.html");
            
            for (Map<String, String> m: listaMessaggi) {
                messaggi.add(Messaggio.fromMap(m));
            }
            return gestoreTemplate.graficaMessaggi(messaggi);
        }

    }
	//localhost:8080/messaggio/formMessaggio
	@GetMapping("/formMessaggio")
    public String formNewMex() {

         return "/formnuovomessaggio.html";
    }
}
