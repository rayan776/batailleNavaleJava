package batailleNavale;

import java.util.Scanner;

public class BatNavaleUtilitaires {
	
	static Scanner sc = new Scanner(System.in);
	
	public static boolean estPrésent(char[] tab, char val) {
		for (int i=0; i<tab.length; i++)
			if (tab[i]==val) return true;
		
		return false;
	}
	
	public static int nbAleatoire(int min, int max) {
		int nbAlea;
		nbAlea = (int)((Math.random()*(max-min+1))+min);
		return nbAlea;
	}
	
	public static char intToChar(int nb) {
		char c;
		String nb_String;
		nb_String = "" + nb;
		c = nb_String.charAt(0);
		return c;
	}
	
	public static int charToInt(char c) {
		int nb;
		String c_String;
		c_String = "" + c;
		nb = Integer.parseInt(c_String);
		return nb;
	}
	
	public static boolean estPrésent(char[][] tab, char val) {
		for (int i=0; i<tab.length; i++)
			for (int j=0; j<tab[i].length; j++)
				if (tab[i][j]==val) return true;
		
		return false;
	}
	
	public static boolean estUnChiffre(char[][] tab, int ligne, int colonne) {
		// retourne vrai si un caractère dans un tableau de char à deux dimensions
		// est un chiffre [0-9]
		
		char chiffre = '0';
		
		for (int i=0; i<10; i++) {
			if (tab[ligne][colonne]==chiffre) return true;
			chiffre++;
		}
		
		return false;
		
	}
	
	public static int minPgZero(int[] t) {
		// retourne la plus petite valeur strictement supérieure à zéro d'un tableau de int
		int val=t[0];
		
		for (int i=0; i<t.length; i++) {
			if (t[i]>0) {
				val=t[i];
				break;
			}
		}
			
		
		for (int i=0; i<t.length; i++) {
			if (t[i]>0&&t[i]<val) val=t[i];
		}
		
		return val;
		
	}
	
	public static int saisieInt(String libelle, int min, int max) {
		
		boolean error = false;
		String saisie;
		int nbSaisi = 0;
		
		do {
			try {
				System.out.println(libelle);
				saisie = sc.nextLine();
				if (saisie.equalsIgnoreCase("q")) {
					return -1;
				}
				nbSaisi = Integer.parseInt(saisie);
				error = false;
			}
			catch (Exception e) {
				error = true;
			}
		}
		while(nbSaisi<min||nbSaisi>max||error);
		
		return nbSaisi;

	}

}
