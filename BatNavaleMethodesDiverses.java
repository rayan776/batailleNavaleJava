package batailleNavale;

import java.util.Scanner;
import java.util.ArrayList;

public class BatNavaleMethodesDiverses {
	
	static Scanner sc = new Scanner(System.in);
	
	public static int initialiserTerrain() {
		
		int tailleTerrain;
		
		tailleTerrain = BatNavaleUtilitaires.saisieInt("\n\nChoisissez une taille pour le terrain (entre " + BatNavaleSettings.TAILLEMINTERRAIN + " et " + BatNavaleSettings.TAILLEMAXTERRAIN + ") :", BatNavaleSettings.TAILLEMINTERRAIN, BatNavaleSettings.TAILLEMAXTERRAIN);
		
		return tailleTerrain;
	}
	
	public static void resetTerrain(char[][] terrain) {
		
		for (int i=0; i<terrain.length; i++)
			for (int j=0; j<terrain[i].length; j++)
				terrain[i][j]=BatNavaleCharacters.VALDEF;
		
	}
	
	public static char[][] initialiserTerrainIA(int tailleTerrain) {
		
		// après avoir initialisé le terrain du joueur, on se base sur sa taille
		// pour initialiser celui de l'IA.
		
		char[][] terrainIA = new char[tailleTerrain][tailleTerrain];
		remplissage(terrainIA, BatNavaleCharacters.VALDEF);
		return terrainIA;
	
	}
	
	public static int bateauxRestants(char[][] terrain) {
		char chiffre='1';
		int bateauxRestants = 0;
		
		for (int i=1; i<=9; i++) {
			if (BatNavaleUtilitaires.estPrésent(terrain, chiffre)) {
				bateauxRestants++;
			}
			chiffre++;
		}
		
		return bateauxRestants;
	}
	
	public static void affichage (char[][] terrain, boolean terrainIA) {
		// affiche le tableau qui modélise le terrain en mer où se déroule la bataille
		
		// le tableau de l'IA sera affiché au joueur sans bateaux, à moins de taper un
		// code de triche, c'est uniquement les cases par défaut et les cases qui ont 
		// été frappés par un missile qui seront affichées.
		
		char lettre = 'A';

		for (int i=1; i<=3; i++)
			System.out.print("\t");
		
		for (int i=1; i<=terrain[0].length; i++)
			System.out.print(i + "\t");
		
		System.out.println();
		
		for (int i=1; i<=3; i++)
			System.out.print("\t");
		
		for (int i=1; i<=terrain[0].length; i++)
			System.out.print("________");
		
		for (int i=1; i<=2; i++)
			System.out.println();
		
		for (int i=0; i<terrain.length; i++) {
			System.out.print("\t");
			System.out.print(lettre + "\t" + "|" + "\t");
			
			for (int j=0; j<terrain[i].length; j++) {
				if (!terrainIA) {
					if (terrain[i][j]!=BatNavaleCharacters.VALBORDURE) {
						System.out.print(terrain[i][j] + "\t");
					}
					else System.out.print(BatNavaleCharacters.VALDEF + "\t");
				}
				else {
					if (BatNavaleCharacters.estUnTirDeMissileOuDeLeau(terrain[i][j])) 
						System.out.print(terrain[i][j] + "\t");
					else System.out.print(BatNavaleCharacters.VALDEF + "\t");
				}
			}
			lettre++;
			for (int j=1; j<=2; j++)
				System.out.println();
		}
	}
	
	public static void remplissage (char[][] terrain, char c) {
		// prépare le tableau qui modélise le terrain en le remplissant avec des
		// valeurs par défaut
		
		for (int i=0; i<terrain.length; i++)
			for (int j=0; j<terrain[i].length; j++)
				terrain[i][j] = c;
		
		
	}
	
	public static void nouvellePartie() {
		
	
		
		System.out.println("Choisissez une difficulté:");
		
		BatNavaleIA.difficulte = BatNavaleUtilitaires.saisieInt("1. Facile\n2. Moyen\n3. Difficile", 1, 3);
		
		if (BatNavaleIA.difficulte==-1) BatNavaleSettings.quitter = true;
		
		if (!BatNavaleSettings.quitter) {
			
			int restart;

			ArrayList<int[]> listeCoordonnéesBateauxJoueur = new ArrayList<>();
			ArrayList<int[]> listeCoordonnéesBateauxIA = new ArrayList<>();
			
			int scoreJoueur = 0, scoreIA = 0;

			boolean tourIA = Math.random()>0.5; // pour savoir si c'est le tour de l'IA ou du joueur
			
			char[][] terrainJoueur = {{}};
			char[][] terrainIA = {{}};
			
			int tailleTerrain = initialiserTerrain();
			
			if (tailleTerrain==-1) BatNavaleSettings.quitter = true;
			else {
				terrainJoueur = new char[tailleTerrain][tailleTerrain];
				remplissage(terrainJoueur, BatNavaleCharacters.VALDEF);
				terrainIA = initialiserTerrainIA(terrainJoueur.length);
			}
			
			if (!BatNavaleSettings.quitter) {
				// préparation du nombre de bateaux
				
				int nombreDeBateaux = BatNavaleBateaux.combienDeBateaux(terrainJoueur.length);
				
				if (nombreDeBateaux==-1) BatNavaleSettings.quitter=true;
				else {
					
					int[] longueurDesBateaux = new int[nombreDeBateaux];
					
					for (int i=0; i<longueurDesBateaux.length; i++)
						longueurDesBateaux[i] = BatNavaleUtilitaires.nbAleatoire(BatNavaleSettings.LONGUEURMINBATEAUX, BatNavaleSettings.LONGUEURMAXBATEAUX);
					
					BatNavaleSettings.plusPetitBateauJoueur = BatNavaleUtilitaires.minPgZero(longueurDesBateaux);
					
					if (nombreDeBateaux==-1) BatNavaleSettings.quitter = true;
					else {
						
						int bateauxIARestantsAuTourPrecedent;
						int bateauxJoueurRestantsAuTourPrecedent;
						
						int bateauxIARestants = nombreDeBateaux, bateauxJoueurRestants = nombreDeBateaux;
						char dernierBateauTouché = '0';
						
						// le joueur place ses bateaux
						
						String libellePlacementBateaux = "\n\nTapez 1 pour placer vous-même vos bateaux.\nTapez 2 pour les placer aléatoirement.\nTapez q pour quitter.";
						
						int joueurPlaceSesBateauxLuiMême = BatNavaleUtilitaires.saisieInt(libellePlacementBateaux, 1, 2);
						
						if (joueurPlaceSesBateauxLuiMême==1) {
							if (nombreDeBateaux>1) {
								System.out.println("Veuillez placer vos " + nombreDeBateaux + " bateaux.");
							}
							else if (nombreDeBateaux==1) {
								System.out.println("Veuillez placer votre bateau.");
							}
							else BatNavaleSettings.quitter = true;
						}
						else if (joueurPlaceSesBateauxLuiMême==2) {
							BatNavaleBateaux.placerBateauxAleatoirement(terrainJoueur, longueurDesBateaux, nombreDeBateaux, terrainJoueur.length);
							System.out.println("\n\nVoici votre terrain : ");
							affichage(terrainJoueur, false);
						}
						else BatNavaleSettings.quitter = true;
							
						if (joueurPlaceSesBateauxLuiMême==1) {
							BatNavaleBateaux.joueurPlaceSesBateaux(terrainJoueur, longueurDesBateaux, nombreDeBateaux, terrainJoueur.length);
								
						}
						
						if (!BatNavaleSettings.quitter) {
							BatNavaleBateaux.nettoyer(terrainJoueur);
							
							// l'IA place ses bateaux aléatoirement
							
							BatNavaleBateaux.placerBateauxAleatoirement(terrainIA, longueurDesBateaux, nombreDeBateaux, terrainJoueur.length);
							
							BatNavaleBateaux.nettoyer(terrainIA);
							
							listeCoordonnéesBateauxJoueur = listeCoordonneesBateaux(terrainJoueur);
							listeCoordonnéesBateauxIA = listeCoordonneesBateaux(terrainIA);
							
							// début de la partie
							
							do {
								
								System.out.println("\nScore : ");
								System.out.println("Joueur - IA");
								System.out.println(scoreJoueur + "-" + scoreIA);
								
								if (!tourIA) {
									
									bateauxIARestantsAuTourPrecedent = bateauxIARestants;
									
									System.out.println("\n\nC'est votre tour.");
									
									dernierBateauTouché = BatNavaleMissiles.joueurTireMissile(terrainIA);
									
									
									if (dernierBateauTouché=='-') {
										BatNavaleSettings.quitter = true;
										break;
									}
									
									bateauxIARestants = bateauxRestants(terrainIA);
									
									tourIA = true;
									
									if (bateauxIARestantsAuTourPrecedent==bateauxIARestants+1) {
										
										scoreJoueur = nombreDeBateaux-bateauxIARestants;
										BatNavaleMissiles.remplacerParCoulé(terrainIA, listeCoordonnéesBateauxIA, dernierBateauTouché);
									}
									
									System.out.println("\nTerrain de l'IA :\n");
									affichage(terrainIA, true);
									
													
									if (BatNavaleMissiles.défaite(terrainIA)) {
										System.out.println("\nVous avez gagné ! ");
										break;
									}
									
									System.out.println("Appuyez sur Entrée quand vous êtes prêt.");
									sc.nextLine();
									
								}
								else {
									
									bateauxJoueurRestantsAuTourPrecedent = bateauxJoueurRestants;
									
									System.out.println("\n\nC'est le tour de l'ordinateur.");
									
									// l'ordinateur joue son tour
									BatNavaleMissiles.ordinateurTireMissile(terrainJoueur);
									
									dernierBateauTouché = BatNavaleIA.numéroBateauDétecté;
									
									bateauxJoueurRestants = bateauxRestants(terrainJoueur);
									
									tourIA = false;
									
									if (bateauxJoueurRestantsAuTourPrecedent==bateauxJoueurRestants+1) {
										
										BatNavaleIA.étape=1;
										
										scoreIA = nombreDeBateaux-bateauxJoueurRestants;
										BatNavaleMissiles.remplacerParCoulé(terrainJoueur, listeCoordonnéesBateauxJoueur, dernierBateauTouché);
										
										if (BatNavaleIA.difficulte==3) {
											longueurDesBateaux[BatNavaleUtilitaires.charToInt(dernierBateauTouché)-1]=0;
											BatNavaleSettings.plusPetitBateauJoueur = BatNavaleUtilitaires.minPgZero(longueurDesBateaux);
										}
										
									}
									
									System.out.println("\nVotre terrain : \n");
									affichage(terrainJoueur, false);
									
									if (BatNavaleMissiles.défaite(terrainJoueur)) {
										System.out.println("\nVous avez perdu ! ");
										break;
									}
								}
								
								
							}
							while(!BatNavaleMissiles.défaite(terrainIA)&&!BatNavaleMissiles.défaite(terrainJoueur));
							
							
							if (!BatNavaleSettings.quitter) {
								System.out.println("\n\nScore final :");
								System.out.println("Joueur - IA");
								System.out.println(scoreJoueur + "-" + scoreIA);
								
								restart = BatNavaleUtilitaires.saisieInt("\n\nSouhaitez-vous revenir au menu principal ?\n1: Oui\n2: Non", 1, 2);
								
								if (restart==2) BatNavaleSettings.quitter = true;
								else BatNavaleSettings.quitter = false;
							}
							
						}
						
						
				
					}
				}
			
			
					
			}

		}
		
		
				
		
	}
	
	
	public static void afficherRèglement() {
		
		for (int i=0; i<15; i++)
			System.out.print("*" + "\t");
		
		for (int i=0; i<1; i++)
			System.out.println();
		
		for (int i=0; i<5; i++)
			System.out.print("\t");
			
		System.out.println("Règles du jeu \"BATAILLE NAVALE\"");
		
		for (int i=0; i<15; i++)
			System.out.print("*" + "\t");
		
		System.out.println();
		System.out.println("1. Vous choisissez une taille pour le terrain de jeu qui représente la mer.\n");
		System.out.println("2. Ensuite, vous choisissez un nombre de bateaux. Vous aurez le même nombre de bateaux que votre adversaire.\n");
		System.out.println("3. Il vous faudra ensuite placer vos bateaux: vous pouvez le faire vous-même en saisissant des coordonnées alphanumériques, du genre A6\n");
		System.out.println("(le terrain est affiché à l'écran pour plus de facilité) ou bien laisser l'ordinateur les placer aléatoirement.\n");
		System.out.println("4. La partie peut alors commencer. Le choix de celui qui commence à jouer en premier est aléatoire. Quand ça sera votre tour, vous");
		System.out.println("devrez saisir des coordonnées et tenter de couler tous les bateaux de votre adversaire. Le premier à avoir coulé tous les bateaux de");
		System.out.println("son adversaire remporte la partie. Bien évidemment, vous ne voyez pas les bateaux de votre adversaire, mais seulement les points");
		System.out.println("indiquant les bateaux touchés ou coulés.\n");
		System.out.println("Vous pouvez néanmoins tricher et voir ses bateaux en tapant \"show\" au moment de saisir les coordonnées pour tirer votre missile.");
		System.out.println("A tout moment, vous pouvez quitter en saisssant q.\n\n\n");
	}
	
	
	public static ArrayList<int[]> listeCoordonneesBateaux(char[][] terrain) {
		
		// génère et retourne la liste des coordonnées de tous les bateaux sur le terrain
		//
		// renvoie une liste de tableaux d'entiers longueur 3 qui contiennent:
		// - le numéro du bateau
		// - coordonnées en ligne
		// - coordonnées en colonne
		//
		// de façon à avoir tous les coordonnées de tous les bateaux présents sur le terrain.
		// ceci nous servira ensuite pour faire apparaître les cases "bateau coulé"
		
		ArrayList<int[]> listCoordonnees = new ArrayList<int[]>();
		
		for (int ligne=0; ligne<terrain.length; ligne++) {
			for (int colonne=0; colonne<terrain[ligne].length; colonne++) {
				if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)) {
					
					int[] coordonnées = new int[3];
					
					coordonnées[0] = BatNavaleUtilitaires.charToInt(terrain[ligne][colonne]);
					coordonnées[1] = ligne;
					coordonnées[2] = colonne;
					
					listCoordonnees.add(coordonnées);
					
				}
			}
		}
		
		return listCoordonnees;
		
	}

}
