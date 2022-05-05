package com.generation.gameet.dao;

import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class Database {
	private String percorso;
	private String username = "root";
	private String password = "root";
	private Connection c;
	
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	public Database() {
		try {
			Class.forName(DRIVER);
			setPercorso("gameet");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setPercorso(String nomeDB) {
		String timeZone = "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&amp";
        this.percorso = "jdbc:mysql://localhost:3306/"+ nomeDB + "?" + timeZone;
	}

	public Connection getC() {
		return c;
	}
	
	public void apriConnessione() {
		try {
        	this.c = DriverManager.getConnection(percorso,username,password);
        } catch(Exception e) {
        	System.out.println(	"Catch del costruttore nella classe Database\n" +
        						"Verifica build path, username e password"		);
        	e.printStackTrace();
        }
	}
	
	public void chiudiConnessione() {
		try {
			c.close();
		} catch(Exception e) {
			System.out.println("Non riesco a chiudere la connessione!");
			e.printStackTrace();
		}
	}
	
	public List<Map<String, String>> read(String query, String... params) {
		List<Map<String, String>> ris = new ArrayList<Map<String, String>>();
		
		try {
			apriConnessione();
			PreparedStatement ps = c.prepareStatement(query);
			
			if(params.length > 0)
				for(int i = 0; i < params.length; i++)
					ps.setString(i + 1, params[i]);
			
			ResultSet tabella = ps.executeQuery();
			int nColonne = tabella.getMetaData().getColumnCount();
			Map<String, String> mappa;
			String chiave;
			String valore;
			
			while(tabella.next()) {
				mappa = new HashMap<String, String>();
				
				for(int i = 1; i <= nColonne; i++) {
					chiave = tabella.getMetaData().getColumnLabel(i);
					valore = tabella.getString(i);
					mappa.put(chiave, valore);
				}
				
				ris.add(mappa);
			}
		} catch(Exception e) {
			System.out.println("Catch nel metodo read di Database");
			
			e.printStackTrace();
		} finally {
			chiudiConnessione();
		}
		
		return ris;
	}
	
	public boolean update(String query, String... params) {
		boolean ris = false;
		
		try {
			apriConnessione();
			PreparedStatement ps = c.prepareStatement(query);
			
			for(int i = 0; i < params.length; i++)
				ps.setString(i + 1, params[i]);
			
			ps.executeUpdate();
			
			ris = true;
		} catch(Exception e) {
			System.out.println("Catch nel metodo update di Database");
			e.printStackTrace();
		} finally {
			chiudiConnessione();
		}
		
		return ris;
	}
}
