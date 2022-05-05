package com.generation.gameet.entities;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.generation.gameet.utility.JSON;
import com.generation.gameet.utility.Utility;
import com.generation.gameet.utility.UtilityUtente;

@Component
public class Utente extends Entity {
	private String nome, cognome, nickname, nazione, email, password;
	String avatarURL = "";
	private Map<String, String> preferenze;	
	private char sesso;
	private int idTeam;
	private boolean pro, admin;
	private Date dataRegistrazione;
	
	public Utente() {
		//costruttore vuoto
	}
	
	public Utente(int id, String nome,
			String cognome, String nickname, String nazione, String email, String password,
			String preferenze, String avatarURL, char sesso, int idTeam, boolean pro, boolean admin,
			Date dataRegistrazione) {
		super(id);
		setNome(nome);
		setCognome(cognome);
		setNickname(nickname);
		setNazione(nazione);
		setEmail(email);
		setPassword(password);
		setPreferenze(preferenze);
		setAvatarURL(avatarURL);
		setSesso(sesso);
		setIdTeam(idTeam);
		setPro(pro);
		setAdmin(admin);
		setDataRegistrazione(dataRegistrazione);
	}
	
	public Utente(String nome,
			String cognome, String nickname, String nazione, String email, String password,
			String preferenze, String avatarURL, char sesso, int idTeam, boolean pro, boolean admin,
			Date dataRegistrazione) {
		setNome(nome);
		setCognome(cognome);
		setNickname(nickname);
		setNazione(nazione);
		setEmail(email);
		setPassword(password);
		setPreferenze(preferenze);
		setAvatarURL(avatarURL);
		setSesso(sesso);
		setIdTeam(idTeam);
		setPro(pro);
		setAdmin(admin);
		setDataRegistrazione(dataRegistrazione);
	}
	
	public Utente(Map<String, String> m) {
		super(m);
		
		if (m.containsKey("nome"))
			setNome(m.get("nome"));
		if (m.containsKey("cognome"))
			setCognome(m.get("cognome"));
		if (m.containsKey("nickname"))
			setNickname(m.get("nickname"));
		if (m.containsKey("nazione"))
			setNazione(m.get("nazione"));
		if (m.containsKey("email"))
			setEmail(m.get("email"));
		if (m.containsKey("pass"))
			setPassword(m.get("pass"));
		if (m.containsKey("preferenze"))
			setPreferenze(m.get("preferenze"));
		if (m.containsKey("avatar_url"))
			setAvatarURL(m.get("avatar_url"));
		if (m.containsKey("sesso"))
			setSesso(m.get("sesso").charAt(0));
		if (m.containsKey("id_team"))
			setIdTeam(m.get("id_team") == null ? 0 : Integer.parseInt(m.get("id_team")));
		if (m.containsKey("pro"))
			setPro(m.get("pro").equalsIgnoreCase("y") ? true : false);
		if (m.containsKey("adm"))
			setAdmin(m.get("adm") != null && m.get("adm").equalsIgnoreCase("y") ? true : false);
		if (m.containsKey("data_registrazione"))
			setDataRegistrazione(Date.valueOf(m.get("data_registrazione")));
	}
	
	public Map<String, String> toMap() {
		Map<String, String> m = super.toMap();
		
		m.put("nome", getNome());
		m.put("cognome", getCognome());
		m.put("nickname", getNickname());
		m.put("nazione", getNazione());
		m.put("email", getEmail());
		m.put("pass", getPassword());
		m.put("preferenze", getPreferenzeString());
		m.put("avatar_url", getAvatarURL());
		m.put("sesso", getSesso() + "");
		m.put("id_team", getIdTeam() + "");
		m.put("pro", isPro() ? "y" : "n");
		m.put("adm", isAdmin() ? "y" : "n");
		m.put("data_registrazione", getDataRegistrazione().toString());
		
		return m;
	}
	
	public static Utente fromMap(Map<String, String> m) {
		return new Utente(m);
	}
	
	public static boolean isUtenteValid(Map<String, String> m) {
		boolean valid = false;
		
		Utente u = Utente.fromMap(m);
		
		if (
				!Utility.isStringEmpty(u.getNome()) && !Utility.isStringEmpty(u.getCognome()) &&
				!Utility.isStringEmpty(u.getNickname()) && !Utility.isStringEmpty(u.getNazione()) &&
				UtilityUtente.isEmailValid(u.getEmail()) && !Utility.isStringEmpty(u.getPassword()) &&
				UtilityUtente.isSessoValid(u.getSesso())
			)
			valid = true;
		
		return valid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getPreferenze() {
		return preferenze;
	}
	
	public String getPreferenzeString() {
		return JSON.MapToJSON(preferenze).toString();
	}

	public void setPreferenze(String preferenze) {
		if (Utility.isStringEmpty(preferenze))
			this.preferenze = new LinkedHashMap<String, String>();
		else {			
			Map<String, String> p = JSON.JSONToMap(preferenze);
			
			setPreferenze(p);
		}
	}
	
	public void setPreferenze(Map<String, String> preferenze) {
		this.preferenze = preferenze;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public char getSesso() {
		return sesso;
	}

	public void setSesso(char sesso) {
		this.sesso = sesso;
	}

	public int getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}

	public boolean isPro() {
		return pro;
	}

	public void setPro(boolean pro) {
		this.pro = pro;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	@Override
	public String toString() {
		String ris = 	super.toString() +
						"Nome: " 		+ nome 								+ "\n" +
						"Cognome: " 	+ cognome 							+ "\n" +
						"Nickname: " 	+ nickname 							+ "\n" +
						"Nazione: " 	+ nazione							+ "\n" +
						"Email: " 		+ email 							+ "\n";
		
		if (preferenze != null && !preferenze.isEmpty())
			ris += "Preferenze: " + Utility.mapToString(preferenze) + "\n";
		
		if (!Utility.isStringEmpty(avatarURL))
			ris += "Avatar URL: " + avatarURL + "\n";
		
		if (!Utility.isStringEmpty(Character.toString(sesso)))
			ris += "Sesso: " + sesso + "\n";
			
		ris +=	"ID Team: " 				+ idTeam 			+ "\n" +
				"Pro: " 					+ pro 				+ "\n" +
				"Admin: " 					+ admin				+ "\n" +
				"Data di registrazione: "	+ dataRegistrazione + "\n";
		
		return ris;
	}
}
