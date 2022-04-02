package batailleNavale;

import java.util.Scanner;
import java.util.ArrayList;

public class BatNavaleMissiles {
	
	static Scanner sc = new Scanner(System.in);
	
	public static void remplacerParCoulé(char[][] terrain, ArrayList<int[]> coordonnees, char chiffre) {
		// remplace tous les caractères présents à certaines coordonnées par une valeur
		// dans un tableau de char.
		
		// cette méthode nous servira à remplacer les cases d'un bateau qui a été coulé par un caractère
		// spécial, ce qui nous permettra de distinguer les bateaux coulés des bateaux touchés mais pas coulés.
		
		int numBateau = BatNavaleUtilitaires.charToInt(chiffre);
	
			for (int[] nxy : coordonnees) {
				if (nxy[0]==numBateau) {
					terrain[nxy[1]][nxy[2]]=BatNavaleCharacters.VALMISSILECOULE;
				}
			}
	}
	
	public static boolean caseANePasViser(char[][] terrain, boolean tirIA, int ligne, int colonne) {
		/* retourne vrai si une case donnée du terrain, envoyé en paramtères, est une case où le tir
		* n'est pas possible.
		* le résultat varie en fonction du fait que ça soit le joueur ou l'IA qui souhaite tirer. */
		
		/*
		 * l'IA étend ses recherches selon la difficulté
		 */
	
		
		if (tirIA) {
			// quand c'est l'IA qui recherche
			
			if (!BatNavaleCharacters.estUnTirDeMissile(terrain[ligne][colonne])) {
				
				// l'IA sait que la case n'est pas un tir de missile...
				
				if (BatNavaleIA.difficulte==1) return false;
				
				if (!BatNavaleCases.présenceCarcasseBateauAutour(terrain, ligne, colonne)) {

					/* 
					Ici, l'IA sait que la case est vide, et qu'elle n'est pas entourée par des carcasses.
					 
					 */
					
					if (BatNavaleIA.difficulte==2) return false;
					
					/*
					 *  uniquement en difficiulté DIFFICILE : recherche avancée pour l'IA
					 *
					 * - Il doit s'assurer qu'il y a assez de cases où la présence d'un bateau est possible 
					 * (case vide ou case missile touché) pour qu'un bateau soit présent à cet endroit, 
					 * que ça soit en ligne ou en colonne, dans les quatre directions.
					 * 
					 * - Si il trouve assez de cases vides, alors il doit s'assurer qu'elles ne soient pas entourées
					 * par des carcasses.
					 * 
					 * 
					 */
					
					return !BatNavaleCases.possibilitéPrésenceBateau(terrain, ligne, colonne);
					
				
				}
				else return true;

			}
			else return true;

		}
		else return BatNavaleCharacters.estUnTirDeMissile(terrain[ligne][colonne]);
		
	
	
	}
	
	public static int[] saisieTirerMissile(char[][] terrainVisé) {
		// laisse le joueur saisir des coordonnées pour tirer un missile sur le terrain de l'IA
		
		String coordonnees = "";
		boolean déjàVisé = false;
		boolean cheatCode = false;
		int[] xy = new int[2];
		int ligne;
		int colonne;
		
		do {
			if (déjàVisé) {
				System.out.println("\nVous avez déjà visé cette case!\n");
				déjàVisé = false;
			}
			
			if (cheatCode) { 
				System.out.println("\nCheat code activé\n\n");
				BatNavaleMethodesDiverses.affichage(terrainVisé, false);
				cheatCode = false;
			}
			
			System.out.println("\nSaisissez des coordonnées pour tirer le missile : ");
			coordonnees = sc.nextLine();
			
			if (coordonnees.equalsIgnoreCase("show")) cheatCode = true;
			else if (coordonnees.equalsIgnoreCase("q")) {
				xy[0]=-1;
				
				return xy;
			}
			
			if (BatNavaleCoordonnees.validerCoordonnees(coordonnees, terrainVisé.length)) {
				xy = BatNavaleCoordonnees.décomposerCoordonnees(coordonnees);
				ligne = xy[0];
				colonne = xy[1];
				if (caseANePasViser(terrainVisé, false, ligne, colonne)) déjàVisé = true;
			}
		}
		while(!BatNavaleCoordonnees.validerCoordonnees(coordonnees, terrainVisé.length)||déjàVisé);
		
		return xy;
		
	}
	
	public static char tirerMissile(char[][] terrainVisé, int[] xy, boolean tirIA) {

		// cette méthode tire un missile, puis indique si un bateau a été touché ou non.
		// elle peut être utilisée à la fois par l'IA et par le joueur.
		// si oui, elle indique le numéro du bateau, puis nous dit si il a été coulé ou pas.
		// le numéro du bateau qui a été touché est retourné sous forme d'un char en fin de méthode, 0 est retourné si aucun bateau n'a été touché
		
		int ligne, colonne;
		char dernierBateauTouché;
		
		ligne = xy[0];
		colonne = xy[1];
		
		if (terrainVisé[ligne][colonne]!=BatNavaleCharacters.VALDEF) {
			if (tirIA) {
				System.out.println("\n\nVotre bateau n°" + terrainVisé[ligne][colonne] + " a été touché!");
			}
			else {
				System.out.println("\n\nVous avez touché le bateau n°" + terrainVisé[ligne][colonne] + " de l'IA!");
			}
			
			dernierBateauTouché = terrainVisé[ligne][colonne];
			
			terrainVisé[ligne][colonne] = BatNavaleCharacters.VALMISSILETOUCHE;
			
			if (!BatNavaleUtilitaires.estPrésent(terrainVisé, dernierBateauTouché)) {
				if (tirIA) {
					
				
					System.out.println("\n\nVotre bateau n°" + dernierBateauTouché + " a été coulé !");
				}
				else {
				
					System.out.println("\n\nVous avez coulé le bateau n°" + dernierBateauTouché + " de l'IA !");
				}
				
			}
			
		}
		else {
			System.out.println("\n\nLe missile a touché l'eau.");

			terrainVisé[ligne][colonne] = BatNavaleCharacters.VALMISSILEEAU;
			dernierBateauTouché='0';
		}
		
		return dernierBateauTouché;
	}
	
	public static char joueurTireMissile(char[][] terrainVisé) {
		int[] xy = saisieTirerMissile(terrainVisé);
		
		if (xy[0]==-1) return '-';
		
		else return tirerMissile(terrainVisé, xy, false);
	}
	
	public static void ordinateurATiré(int ligne, int colonne) {
		System.out.println("\n\nL'ordinateur a tiré en : " + BatNavaleCoordonnees.composerCoordonnées(ligne, colonne));
	}
	
	public static void ordinateurTireMissile(char[][] terrainVisé) {
		 
		 if (BatNavaleIA.étape==1) {
			 
			BatNavaleIA.étape1(terrainVisé);
			 
		 }
		 else if (BatNavaleIA.étape==2) {
			 
			BatNavaleIA.étape2(terrainVisé);
			 
		 }
		 else if (BatNavaleIA.étape==3) {
			
			BatNavaleIA.étape3(terrainVisé);
				
		}
		else {
			BatNavaleIA.étape4(terrainVisé);

		}
			
	}
			
	public static boolean défaite(char[][] terrain) {
		
		// retourne vrai si il ne reste aucun chiffre dans un terrain, son joueur a donc perdu 
		// et c'est son adversaire qui a gagné.
		
		char chiffre = '1';
	
		for (int k=0; k<9; k++) {
			if (BatNavaleUtilitaires.estPrésent(terrain, chiffre)) {
				return false;
			}
			chiffre++;
		}
		
		return true;
		
	}

}
