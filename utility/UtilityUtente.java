package com.generation.gameet.utility;

public abstract class UtilityUtente {
	public static boolean isSessoValid(char s) {
		boolean valid = false;
		
		String sesso = Character.toString(s);
		
		if (sesso.equalsIgnoreCase("m") || sesso.equalsIgnoreCase("f") || sesso == null)
			valid = true;
			
		return valid;
	}
	
	public static boolean isEmailValid(String email) {
		boolean valid = false;
		
		if (email.contains("@") && email.contains(".") && email.length() > 6)
			valid = true;
		
		return valid;
	}
}
