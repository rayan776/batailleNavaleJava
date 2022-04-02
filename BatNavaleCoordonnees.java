package batailleNavale;

public class BatNavaleCoordonnees {
	
	public static String composerCoordonnées(int ligne, int colonne) {
		
		// transforme un couple de coordonnées (x,y) en coordonnées au format "[A-Z][1-9]"
		
		char lettre = 'A';
		
		for (int i=0; i<ligne; i++) {
			lettre++;
		}
		
		colonne++;
		
		String coordonnéesComposées = "" + lettre;
		coordonnéesComposées += colonne;
		
		return coordonnéesComposées;
		
	}
	
	public static int[] décomposerCoordonnees(String coordonnees) {
		// décompose des coordonnées de la forme [A-Z][1-9]
		// par exemple, pour E7, on renvoie un tableau de int contenant
		// 5 pour la ligne, et 7 pour la colonne.
		
		coordonnees = coordonnees.toUpperCase();
		
		int[] coordonneesExactes = new int[2];
		
		char[] alphabet = new char[26];
		
		char lettre = 'A';
		
		for (int i=0; i<alphabet.length; i++) {
			alphabet[i] = lettre;
			lettre++;
		}
		
		int posLigne = 0;
		
		for (int i=0; i<alphabet.length; i++) {
			if (coordonnees.charAt(0)==alphabet[i]) {
				posLigne = (i+1);
				break;
			}
		}
		
		String nombreAConvertir = "" + coordonnees.charAt(1);
		if (coordonnees.length()==3) nombreAConvertir += coordonnees.charAt(2);
		int posColonne;
		
		nombreAConvertir = nombreAConvertir.trim();
		
		posColonne = Integer.parseInt(nombreAConvertir);
		
		coordonneesExactes[0] = posLigne-1;
		coordonneesExactes[1] = posColonne-1;
		
		return coordonneesExactes;
	}
	
	
	
	public static boolean validerCoordonnees(String coordonnees, int taille) {
		// vérification de la validité des coordonnées
		boolean valide = false;
		
		coordonnees = coordonnees.toUpperCase();
		
		// vérification de la longueur
		if (coordonnees.length()<2||coordonnees.length()>3) return false;
		
		// vérification de la première lettre
		char[] lettresValides = new char[taille];
		
		char charMin = 'A';
		
		for (int i=0; i<lettresValides.length; i++) {
			lettresValides[i] = charMin;
			charMin++;
		}
		
		for (int i=0; i<lettresValides.length; i++) {
			
			if (coordonnees.charAt(0)==lettresValides[i]) {
				
				valide = true;
				break;
			}
		}
		
		if (!valide) return false;
		
		// vérification des chiffres
		
		int chiffre;
		String chiffreAConvertir = "";
		
		switch (coordonnees.length()) {
			case 2:
				try {
					chiffreAConvertir += coordonnees.charAt(1);
					chiffreAConvertir = chiffreAConvertir.trim();
					chiffre = Integer.parseInt(chiffreAConvertir);
				
					if (chiffre>0&&chiffre<=taille) valide = true;
					else valide = false;
				
				}
				catch (Exception e) {
					valide = false;
				}
				break;
			case 3:
				try {
					chiffreAConvertir += coordonnees.charAt(1);
					chiffreAConvertir += coordonnees.charAt(2);
					chiffreAConvertir = chiffreAConvertir.trim();
					chiffre = Integer.parseInt(chiffreAConvertir);
					
					if (chiffre>0&&chiffre<=taille) valide = true;
					else valide=false;
					
				}
				catch (Exception e) {
					valide = false;
				}
		}
		
		return valide;
		
	}

}
