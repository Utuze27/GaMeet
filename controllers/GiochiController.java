package com.generation.gameet.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.generation.gameet.dao.DAOGiochi;
import com.generation.gameet.entities.Gioco;
import viste.GestoreTemplate;

@Controller
@RequestMapping("/gioco")
public class GiochiController {
	@Autowired
	private DAOGiochi daoGiochi;
	
	@Autowired
	private GestoreTemplate gestoreTemplate;
	
	//localhost:8080/gioco/leggiGiochi
	@GetMapping("/leggiGiochi")
	@ResponseBody
	public String graficaGiochi() {
		List<Map<String, String>> listaGiochi= daoGiochi.read();
		List<Gioco> giochi = new ArrayList<>();
		
		for (Map<String, String> m: listaGiochi)
			giochi.add(Gioco.fromMap(m));
			
		return gestoreTemplate.graficaGiochi(giochi);
	}
	
	//localhost:8080/gioco/leggiGioco?id=1
	@GetMapping("/leggiGioco")
	@ResponseBody
	public String leggiGioco(@RequestParam("id") int id) {
		Gioco g = Gioco.fromMap(daoGiochi.read(id));
		
		return gestoreTemplate.graficaGioco(g);
	}
	
	//localhost:8080/gioco/aggiungiGioco?titolo=batman&casa_produttrice=lucasrl&anno_uscita=2022&genere=action&piattaforme=pc,ps4
	@GetMapping("/aggiungiGioco")
	@ResponseBody
	public String aggiungiGioco(@RequestParam Map<String,String> m) {
		return daoGiochi.create(m) ? "Gioco aggiunto!" : "Errore";
	}
	
	//localhost:8080/gioco/modificaGioco?id=1&titolo=batmanIlRitorno&casa_produttrice=lucasrl&anno_uscita=2022&genere=action&piattaforme=pc,ps4
	@GetMapping("/modificaGioco")
	@ResponseBody
	public String modificaGioco(@RequestParam Map<String,String> m) {
		return daoGiochi.update(m) ? "Gioco modificato!" : "Errore";
	}
	
	//localhost:8080/gioco/eliminaGioco?id=2
	@GetMapping("/eliminaGioco")
	@ResponseBody
	public String eliminaGioco(@RequestParam("id") int id) {
		return daoGiochi.delete(id) ? "Gioco eliminato!" : "Errore";
	}
}