package batailleNavale;

import java.util.Scanner;


public class BatNavaleBateaux {
	
	static Scanner sc = new Scanner(System.in);	
	
	public static int combienDeBateaux(int taille) {

		int maxBateaux;
		
		if (taille>12) maxBateaux=7;
		else if (taille>10) maxBateaux=6;
		else if (taille>8) maxBateaux=5;
		else maxBateaux=3;
		
		int nbBateaux;
		String libelleSaisieNbBateaux = "\n\nCombien de bateaux souhaitez-vous pour la bataille? (" + BatNavaleSettings.MINBATEAUX + " minimum, " + maxBateaux + " maximum)";
		
		nbBateaux = BatNavaleUtilitaires.saisieInt(libelleSaisieNbBateaux, 1, maxBateaux);
		
		return nbBateaux;
	}
	
	public static int[] saisiePlacementBateau(int numBateau, int[] longueurDesBateaux, int tailleTerrain) {
		
		int[] infosBateau = new int[3];
		String coordonnees;
		int enLigneSaisie=0;
		int ligne=0;
		int colonne=0;
		
		do {
			System.out.println("Longueur : ");
			for (int i=1; i<=longueurDesBateaux[numBateau-1]; i++)
				System.out.print(numBateau + "\t");
			
			System.out.println("\n\nSaisissez des coordonnées : ");
			coordonnees = sc.nextLine();
			if (coordonnees.equalsIgnoreCase("q")) BatNavaleSettings.quitter = true;
		}
		while(!BatNavaleSettings.quitter&&!BatNavaleCoordonnees.validerCoordonnees(coordonnees, tailleTerrain));
		
		if (!BatNavaleSettings.quitter) {
			int[] xy = BatNavaleCoordonnees.décomposerCoordonnees(coordonnees);
			
			ligne = xy[0];
			colonne = xy[1];
			
			String texteSaisieLigneOuColonne = "\n\nSouhaitez vous le placer en ligne ou en colonne?\n" + "1. Ligne\n" + "2. Colonne";
		
			enLigneSaisie = BatNavaleUtilitaires.saisieInt(texteSaisieLigneOuColonne, 1, 2);
			
			if (enLigneSaisie==-1) {
				BatNavaleSettings.quitter = true;
			}
			
		}
		
		infosBateau[0] = ligne;
		infosBateau[1] = colonne;
		infosBateau[2] = enLigneSaisie;
		
		return infosBateau;
		
	}
	
	public static void joueurPlaceSesBateaux(char[][] terrainJoueur, int[] longueurDesBateaux, int nombreDeBateaux, int tailleTerrain) {
		// laisse le joueur placer des bateaux sur le terrain
		
		
		int longueurBateau, numBateau=0, ligne=0, colonne=0, bateauRestant=0, replacerBateau=3;
		boolean enLigne = false;
		int bateauxPlacés=0;
		int[] infosBateaux = new int[3];
		boolean restart=true;
		boolean restartMenuFinal=true;
		
		int[][] coordonneesBateauxPlacés = new int[nombreDeBateaux][3];
		
		int[] numeros = new int[nombreDeBateaux];

		do {
			
			for (int j=0; j<numeros.length; j++)
				numeros[j]=(j+1);
			
			numBateau=0;
			ligne=0;
			colonne=0;
			bateauRestant=0;
			replacerBateau=3;
			
			bateauxPlacés=0;
			
			for (int i=0; i<infosBateaux.length; i++)
				infosBateaux[i]=0;
			
			
			BatNavaleMethodesDiverses.affichage(terrainJoueur, false);
			
			do {
				
				if (nombreDeBateaux>1&&bateauxPlacés<nombreDeBateaux-1) {
					
						
					numBateau = BatNavaleUtilitaires.saisieInt("\n\nNuméro du bateau à placer", 1, nombreDeBateaux);
						
					if (numBateau==-1) { 
						BatNavaleSettings.quitter = true;
						restart = false;
					}
					
				}
				else if (nombreDeBateaux==1) {
					numBateau=1;
				}
				else if (bateauxPlacés==nombreDeBateaux-1) {
					for (int j=0; j<numeros.length; j++)
						if (numeros[j]>0) bateauRestant=numeros[j];
				
					System.out.println("Veuillez placer le bateau n°" + bateauRestant);
					numBateau = bateauRestant;
				}
						
				if (!BatNavaleSettings.quitter&&BatNavaleUtilitaires.estPrésent(terrainJoueur, BatNavaleUtilitaires.intToChar(numBateau))) {
							
							
						System.out.println("\n\nVous avez déjà placé le bateau numéro " + numBateau);
						System.out.println("Souhaitez-vous le replacer?");
						System.out.println("1. Oui");
						System.out.println("2. Non");
						
						replacerBateau = BatNavaleUtilitaires.saisieInt("Tapez : ", 1, 2);
							
						if (replacerBateau==1) {
							numeros[numBateau-1]=numBateau;
						}
							
						if (replacerBateau==-1) BatNavaleSettings.quitter=true;
					
				}
				else {
					replacerBateau=3;
				}
				
				
				if (!BatNavaleSettings.quitter&&replacerBateau!=2) {
					infosBateaux = saisiePlacementBateau(numBateau, longueurDesBateaux, tailleTerrain);
					
					if (!BatNavaleSettings.quitter) {
						longueurBateau = longueurDesBateaux[numBateau-1];
						ligne = infosBateaux[0];
						colonne = infosBateaux[1];
						enLigne = (infosBateaux[2]==1);
						
						if (replacerBateau==1) {
							supprimerBateau(terrainJoueur, numBateau);
							bateauxPlacés--;
							numeros[numBateau-1]=numBateau;
						}
						
						if (placerBateau(terrainJoueur, numBateau, longueurBateau, ligne, colonne, enLigne)) {
								BatNavaleMethodesDiverses.affichage(terrainJoueur, false);
								bateauxPlacés++;
								
								coordonneesBateauxPlacés[numBateau-1][0] = ligne;
								coordonneesBateauxPlacés[numBateau-1][1] = colonne;
								coordonneesBateauxPlacés[numBateau-1][2] = infosBateaux[2];
								
								numeros[numBateau-1]=0;
						}
						else {
							
							if (replacerBateau==1) {
								bateauxPlacés++;
								numeros[numBateau-1]=numBateau;
								placerBateau(terrainJoueur, numBateau, longueurBateau, coordonneesBateauxPlacés[numBateau-1][0], coordonneesBateauxPlacés[numBateau-1][1], (coordonneesBateauxPlacés[numBateau-1][2]==1));
							}
								System.out.println("\n\nLe bateau n'a pas pu être placé aux coordonnées précisées, faute de place. Veuillez réessayer.");
						}
					}
					else restart=false;
					
					

					
				}
				else restart=false;
				
			
			}
			while(bateauxPlacés<nombreDeBateaux&&!BatNavaleSettings.quitter);
			
			if (!BatNavaleSettings.quitter) {
				do {
					
					System.out.println("Vous voulez maintenant: ");
					System.out.println("1. Confirmer votre placement de bateaux");
					System.out.println("2. Replacer un bateau");
					System.out.println("3. Replacer tous les bateaux");
					
					replacerBateau = BatNavaleUtilitaires.saisieInt("Tapez: ", 1, 3);
					
					switch (replacerBateau) {
					case 1:
						if (bateauxPlacés==nombreDeBateaux) {
							restart=false;
							restartMenuFinal=false;
						}
						else {
							System.out.println("Vous n'avez pas encore placé tous vos bateaux!");
							restart=false;
							restartMenuFinal=true;
						}
						break;
					case 2:
						
						numBateau = BatNavaleUtilitaires.saisieInt("Quel bateau replacer?", 1, nombreDeBateaux);
						
						if (numBateau==-1) BatNavaleSettings.quitter=true;
						
						if (!BatNavaleSettings.quitter) {
							supprimerBateau(terrainJoueur, numBateau);
							bateauxPlacés--;
							
							infosBateaux = saisiePlacementBateau(numBateau, longueurDesBateaux, tailleTerrain);
							
						}
						else {
							
							restartMenuFinal=false;
						}
						
						if (!BatNavaleSettings.quitter) {
							longueurBateau = longueurDesBateaux[numBateau-1];
							ligne = infosBateaux[0];
							colonne = infosBateaux[1];
							enLigne = (infosBateaux[2]==1);
							
							if (placerBateau(terrainJoueur, numBateau, longueurBateau, ligne, colonne, enLigne)) {
								BatNavaleMethodesDiverses.affichage(terrainJoueur, false);
								bateauxPlacés++;
								
								coordonneesBateauxPlacés[numBateau-1][0] = ligne;
								coordonneesBateauxPlacés[numBateau-1][1] = colonne;
								coordonneesBateauxPlacés[numBateau-1][2] = infosBateaux[2];
							}
							else {
								
								placerBateau(terrainJoueur, numBateau, longueurBateau, coordonneesBateauxPlacés[numBateau-1][0], coordonneesBateauxPlacés[numBateau-1][1], (coordonneesBateauxPlacés[numBateau-1][2]==1));
								bateauxPlacés++;
								System.out.println("\n\nLe bateau n'a pas pu être placé aux coordonnées précisées, faute de place. Veuillez réessayer.");
							}
							
						}
						else restartMenuFinal=false;
						
						if (!BatNavaleSettings.quitter) restartMenuFinal=true;
						restart=false;
						break;
						
					case 3:
						BatNavaleMethodesDiverses.resetTerrain(terrainJoueur);
						System.out.println("Votre terrain a été réinitialisé.");
						restart=true;
						restartMenuFinal=false;
						break;
					case -1:
						restart=false;
						restartMenuFinal=false;
						BatNavaleSettings.quitter=true;
					}
				}
				while(restartMenuFinal);
			}
			
		
			
			
		}
		while(restart);
		
		
	}

		
	public static void placerBateauxAleatoirement(char[][] terrain, int[] longueurDesBateaux, int nombreDeBateaux, int tailleTerrain) {
		
		int numBateau = 1;
		int longueurBateau;
		
		int ligne;
		int colonne;
		
		boolean enLigne;
		
		int i=1;
		
		while(i<=nombreDeBateaux) {
		
			longueurBateau = longueurDesBateaux[numBateau-1];
			ligne = (int)(Math.random()*tailleTerrain);
			colonne = (int)(Math.random()*tailleTerrain);
			enLigne = Math.random()>0.5;
			
			
			if (placerBateau(terrain, numBateau, longueurBateau, ligne, colonne, enLigne)) {
				numBateau++;
				i++;
			}
		}
		
	}
	
	public static void ajouterBordures(char[][] terrain, char numBateau, boolean enLigne) {
		for (int i=0; i<terrain.length; i++) {
			for (int j=0; j<terrain[i].length; j++) {
				
				
				if (terrain[i][j]==numBateau) {
					
					if (enLigne) {
						
						if (i>0) {
							if (terrain[i-1][j]==BatNavaleCharacters.VALDEF) {
								terrain[i-1][j]=BatNavaleCharacters.VALBORDURE;
							}
						}
						
						if (i<terrain.length-1) {
							if (terrain[i+1][j]==BatNavaleCharacters.VALDEF) {
								terrain[i+1][j]=BatNavaleCharacters.VALBORDURE;
							}
						}
						
						if (j>0) {
							if (terrain[i][j-1]==BatNavaleCharacters.VALDEF) {
								terrain[i][j-1]=BatNavaleCharacters.VALBORDURE;
							}
						}
						
						if (j<terrain[i].length-1) {
							if (terrain[i][j+1]==BatNavaleCharacters.VALDEF) {
								terrain[i][j+1]=BatNavaleCharacters.VALBORDURE;
							}
						}
					}
					else {
						if (i>0) {
							if (terrain[i-1][j]==BatNavaleCharacters.VALDEF) {
								terrain[i-1][j]=BatNavaleCharacters.VALBORDURE;
							}
						}
						
						if (i<terrain.length-1) {
							if (terrain[i+1][j]==BatNavaleCharacters.VALDEF) {
								terrain[i+1][j]=BatNavaleCharacters.VALBORDURE;
							}
						}
						
						if (j>0) {
							if (terrain[i][j-1]==BatNavaleCharacters.VALDEF) {
								terrain[i][j-1]=BatNavaleCharacters.VALBORDURE;
							}
						}
						
						if (j<terrain[i].length-1) {
							if (terrain[i][j+1]==BatNavaleCharacters.VALDEF) {
								terrain[i][j+1]=BatNavaleCharacters.VALBORDURE;
							}
						}
					}
					
					
				}
				
				
			}
		}
	}
	
	public static void nettoyer(char[][] terrain) {
		
		for (int i=0; i<terrain.length; i++)
			for (int j=0; j<terrain[i].length; j++)
				if (terrain[i][j]==BatNavaleCharacters.VALBORDURE) terrain[i][j] = BatNavaleCharacters.VALDEF;
		
		
	}
	
	public static void supprimerBateau(char[][] terrain, int numBateau) {
		
		char chiffreBateau = BatNavaleUtilitaires.intToChar(numBateau);	
		
		for (int i=0; i<terrain.length; i++) {
			for (int j=0; j<terrain[i].length; j++) {
				if (terrain[i][j]==chiffreBateau) {
					terrain[i][j]=BatNavaleCharacters.VALDEF;
					
					if (i>0) {
						if (terrain[i-1][j]==BatNavaleCharacters.VALBORDURE) {
							terrain[i-1][j]=BatNavaleCharacters.VALDEF;
						}
					}
					if (i<terrain.length-1) {
						if (terrain[i+1][j]==BatNavaleCharacters.VALBORDURE) {
							terrain[i+1][j]=BatNavaleCharacters.VALDEF;
						}
					}
					if (j>0) {
						if (terrain[i][j-1]==BatNavaleCharacters.VALBORDURE) {
							terrain[i][j-1]=BatNavaleCharacters.VALDEF;
						}
					}
					if (j<terrain[i].length-1) {
						if (terrain[i][j+1]==BatNavaleCharacters.VALBORDURE) {
							terrain[i][j+1]=BatNavaleCharacters.VALDEF;
						}
					}
					
				}
			}
		}
	
	}
	
	public static boolean placerBateau(char[][] terrain, int numBateau, int longueurBateau, int ligne, int colonne, boolean enLigne) {
		
		// la méthode renvoie un booléen pour indiquer si le bateau a pu être placé.
		// si oui, elle renvoie true, sinon false.
		
		char numAPlacer = BatNavaleUtilitaires.intToChar(numBateau);
		
		int nombreDeCaracteresPlacés = 0;
		
		if (terrain[ligne][colonne]==BatNavaleCharacters.VALDEF) {
			
			int casesDisponibles = 1;
			int i;
			
			if (enLigne) {
				i=colonne;
				
				i--;
				while (i>=0) {
					if (terrain[ligne][i]==BatNavaleCharacters.VALDEF) casesDisponibles++;
					else break;
					i--;
				}
				
				i = colonne;
				
				i++;
				while (i<terrain[0].length) {
					
					if (terrain[ligne][i]==BatNavaleCharacters.VALDEF) casesDisponibles++;
					else break;
					i++;			
				}
				
				if (casesDisponibles>=longueurBateau) {
					
					terrain[ligne][colonne] = numAPlacer;
					nombreDeCaracteresPlacés++;
					
					i = colonne;
					
					i++;
					while (i<terrain[0].length) {
						
						if (terrain[ligne][i]==BatNavaleCharacters.VALDEF) {
							
							terrain[ligne][i] = numAPlacer;
							nombreDeCaracteresPlacés++;
						}
						else break;
						i++;
						
						if (nombreDeCaracteresPlacés==longueurBateau) {
							ajouterBordures(terrain, numAPlacer, enLigne);
							return true;
						}
						
					}
					
					i = colonne;
					
					i--;
					while (i>=0) {
						
						
						if (terrain[ligne][i]==BatNavaleCharacters.VALDEF) {
							
							terrain[ligne][i] = numAPlacer;
							nombreDeCaracteresPlacés++;
						}
						else break;
						i--;
						
						if (nombreDeCaracteresPlacés==longueurBateau) {
							ajouterBordures(terrain, numAPlacer, enLigne);
							return true;
						}
						
					}
				}
				else return false;
				
			}
			else {
				
				i=ligne;
				
				i--;
				while (i>=0) {
					if (terrain[i][colonne]==BatNavaleCharacters.VALDEF) casesDisponibles++;
					else break;
					i--;
				}
				
				i = ligne;
				
				i++;
				while (i<terrain.length) {
					
					if (terrain[i][colonne]==BatNavaleCharacters.VALDEF) casesDisponibles++;
					else break;
					i++;			
				}
				
				if (casesDisponibles>=longueurBateau) {
					
					terrain[ligne][colonne] = numAPlacer;
					nombreDeCaracteresPlacés++;
					
					i = ligne;
					
					i++;
					while (i<terrain.length) {
						
						if (terrain[i][colonne]==BatNavaleCharacters.VALDEF) {
							
							terrain[i][colonne] = numAPlacer;
							nombreDeCaracteresPlacés++;
						}
						else break;
						i++;
						
						if (nombreDeCaracteresPlacés==longueurBateau) {
							ajouterBordures(terrain, numAPlacer, enLigne);
							return true;
						}
						
					}
					
					i = ligne;
					
					i--;
					while (i>=0) {
						
						
						if (terrain[i][colonne]==BatNavaleCharacters.VALDEF) {
							
							terrain[i][colonne] = numAPlacer;
							nombreDeCaracteresPlacés++;
						}
						else break;
						i--;
						
						if (nombreDeCaracteresPlacés==longueurBateau) {
							ajouterBordures(terrain, numAPlacer, enLigne);
							return true;
						}
						
					}
				}
				else return false;
	
				
			}
			
		}
		else return false;
		
		return true;
		
		
	}

}
