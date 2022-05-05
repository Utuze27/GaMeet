package com.generation.gameet;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.generation.gameet.dao.DAO;
import com.generation.gameet.dao.DAOGiochi;
import com.generation.gameet.dao.DAOMessaggi;
import com.generation.gameet.dao.DAOTeam;
import com.generation.gameet.dao.DAOUtenti;
import com.generation.gameet.dao.Database;
import com.generation.gameet.entities.Gioco;
import com.generation.gameet.entities.Messaggio;
import com.generation.gameet.entities.Team;
import com.generation.gameet.entities.Utente;

import viste.GestoreTemplate;

@Configuration
public class Context {
	@Bean
	@Scope("singleton")
	public Database db(){
		return new Database();
	}
	
	@Bean
	@Scope("singleton")
	@Primary
	public DAOUtenti daoUtenti() {
		return new DAOUtenti();
	}
	
	@Bean
	@Scope("singleton")
	public DAOGiochi daoGiochi() {
		return new DAOGiochi();
	}
	
	@Bean
	@Scope("singleton")
	public DAOTeam daoTeam() {
		return new DAOTeam();
	}
	
	@Bean
	@Scope("singleton")
	public DAOMessaggi daoMessaggi() {
		return new DAOMessaggi();
	}
	
	@Bean
	@Scope("singleton")
	public DAO dao() {
		return new DAO();
	}
	
	@Bean
	@Scope("prototype")
	@Primary
	public Utente utenteMappa(Map<String,String> m) {
		return new Utente(m);
	}
	
	@Bean
	@Scope("prototype")
	//@Primary
	public Utente utenteOggetto() {
		return new Utente();
	}

	@Bean
	@Scope("prototype")
	public Gioco giocoMappa(Map<String,String> m) {
		return new Gioco(m);
	}
	
	@Bean
	@Scope("prototype")
	@Primary
	public Gioco giocoOggetto() {
		return new Gioco();
	}
	
	@Bean
	@Scope("prototype")
	public Team teamMappa(Map<String,String> m) {
		return new Team(m);
	}
	
	@Bean
	@Scope("prototype")
	@Primary
	public Team teamOggetto() {
		return new Team();
	}
	
	@Bean
	@Scope("prototype")
	public Messaggio messaggioMappa(Map<String,String> m) {
		return new Messaggio(m);
	}
	
	@Bean
	@Scope("prototype")
	@Primary
	public Messaggio messaggioOggetto() {
		return new Messaggio();
	}
	
	@Bean
	@Scope("singleton")
	public GestoreTemplate gestoreTemplate() {
		return new GestoreTemplate();
	} 
}
