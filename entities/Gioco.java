package com.generation.gameet.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.generation.gameet.utility.Utility;

@Component
public class Gioco extends Entity {
	private String titolo, casaProduttrice, genere, piattaforme;
	private int annoUscita;
	
	public Gioco() {
		//costruttore vuoto
	}
	
	public Gioco(int id, String titolo, String casaProduttrice, String genere, String piattaforme, int annoUscita) {
		super(id);
		setTitolo(titolo);
		setCasaProduttrice(casaProduttrice);
		setGenere(genere);
		setPiattaforme(piattaforme);
		setAnnoUscita(annoUscita);
	}
	
	public Gioco(String titolo, String casaProduttrice, String genere, String piattaforme, int annoUscita) {
		setTitolo(titolo);
		setCasaProduttrice(casaProduttrice);
		setGenere(genere);
		setPiattaforme(piattaforme);
		setAnnoUscita(annoUscita);
	}
	
	public Gioco(Map<String, String> m) {
		super(m);
		
		if (m.containsKey("titolo"))
			setTitolo(m.get("titolo"));
		if (m.containsKey("casa_produttrice"))
			setCasaProduttrice(m.get("casa_produttrice"));
		if (m.containsKey("genere"))
			setGenere(m.get("genere"));
		if (m.containsKey("piattaforme"))
			setPiattaforme(m.get("piattaforme"));
		if (m.containsKey("anno_uscita"))
			setAnnoUscita(Integer.parseInt(m.get("anno_uscita")));
	}
	
	public static Gioco fromMap(Map<String, String> m) {
		return new Gioco(m);
	}

	public Map<String, String> toMap() {
		Map<String, String> m = super.toMap();
		
		m.put("titolo", getTitolo());
		m.put("casa_produttrice", getCasaProduttrice());
		m.put("genere", getGenere());
		m.put("piattaforme", getPiattaforme());
		m.put("anno_uscita", getAnnoUscita() + "");
		
		return m;
	}
	
	public static boolean isGiocoValid(Map<String, String> m) {
		boolean valid = false;
		
		Gioco g = Gioco.fromMap(m);
		
		if (
				!Utility.isStringEmpty(g.getTitolo()) && !Utility.isStringEmpty(g.getCasaProduttrice()) &&
				!Utility.isStringEmpty(g.getGenere()) && !Utility.isStringEmpty(g.getPiattaforme()) &&
				g.getAnnoUscita() >= 1940 && g.getAnnoUscita() <= 2022
			)
			valid = true;
		
		return valid;
	}
	
	public String getTitolo() {
		return titolo;
	}
	
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public String getCasaProduttrice() {
		return casaProduttrice;
	}
	
	public void setCasaProduttrice(String casaProduttrice) {
		this.casaProduttrice = casaProduttrice;
	}
	
	public String getGenere() {
		return genere;
	}
	
	public void setGenere(String genere) {
		this.genere = genere;
	}
	
	public String getPiattaforme() {
		return piattaforme;
	}
	
	public String[] getPiattaformeArray() {
		return piattaforme.split(",");
	}
	
	public void setPiattaforme(String piattaforme) {
		this.piattaforme = piattaforme;
	}
	
	public int getAnnoUscita() {
		return annoUscita;
	}
	
	public void setAnnoUscita(int annoUscita) {
		this.annoUscita = annoUscita;
	}

	@Override
	public String toString() {
		return 	super.toString() +
				"Titolo: " 				+ titolo 			+ "\n" +
				"Casa produttrice: " 	+ casaProduttrice 	+ "\n" +
				"Genere: " 				+ genere			+ "\n" +
				"Piattaforme: " 		+ piattaforme 		+ "\n" +
				"Anno d'uscita: " 		+ annoUscita 		+ "\n";
	}
}
