package batailleNavale;

public class BatNavaleIA {
	
	/* INTELLIGENCE ARTIFICIELLE
	 * 
	 * Cette classe contient des informations et des méthodes dont se sert l'ordinateur pour adapter sa stratégie de jeu.
	 * 
	 * A chaque tour, il met ces informations à jour puis s'en sert au tour suivant pour savoir comment
	 * il doit jouer.
	 * 
	 * Il y a six informations :
	 * 
	 * - L'étape (j'ai décomposé la stratégie de l'IA en quatre étapes qui sont décrites plus bas)
	 * 
	 * - Des coordonnées style ligne/colonne qu'il utilise lors des étapes 2, 3 et 4 pour
	 * sa recherche de cibles
	 * 
	 * - Le numéro du bateau detecté (étapes 2, 3, 4)
	 * 
	 * - Une variable qui lui indique si le bateau détecté est placé en ligne ou en colonne (étapes 3 et 4)
	 * 
	 * - Et la direction qu'il suit pour faire ses recherches (étapes 3 et 4)
	 * 
	 * - La difficulté choisie par le joueur en début de partie.
	 * 
	 */
	
	public static int[] xy = new int[2];
	public static boolean[] directionsDéjàTestées = new boolean[4];
	
	public static int étape=1;
	public static int ligne=0;
	public static int colonne=0;
	public static char numéroBateauDétecté='0';
	public static boolean bateauACoulerEnLigne=false;
	public static int direction=0;
	public static int difficulte=0;
	
	/*
	 * Les quatre étapes de la stratégie de l'IA
	 */
	
	public static void étape1(char[][] terrainVisé) {
		/*
		 * étape 1 de la stratégie de l'IA : jeu aléatoire
		 */
		
		 do {
			 ligne = (int)(Math.random()*terrainVisé.length);
			 colonne = (int)(Math.random()*terrainVisé[ligne].length);
		 }
		 while(BatNavaleMissiles.caseANePasViser(terrainVisé, true, ligne, colonne));
		 
		 xy[0] = ligne;
		 xy[1] = colonne;
		 
		 if (BatNavaleUtilitaires.estUnChiffre(terrainVisé, ligne, colonne)) {
			 numéroBateauDétecté = terrainVisé[ligne][colonne];
			
			 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
			 
			 if (difficulte>1) {
				 
				 for (int i=0; i<directionsDéjàTestées.length; i++)
					 directionsDéjàTestées[i]=false;
				 
				 étape=2; 
			 }

			 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
			 
		 }
		 else {
			 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
			 
			 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
			
		 }
	}
	
	public static void étape2(char[][] terrainVisé) {
		
		
		 // étape 2 : l'IA sait à présent vers où se trouve un bateau, il va tenter de déterminer
		 // si il est placé en ligne ou en colonne en jouant dans les cases avoisinantes.
		
		// en mode facile, l'IA ne rentre pas ici.
		// en mode moyen, l'IA se contente de trouver une deuxième case puis repasse en étape 1.
		// en mode difficile, l'IA passe à l'étape 3.
		
		if (!BatNavaleCases.possibleDeTirerAutour(terrainVisé, ligne, colonne)) {
			
				étape=1;
				étape1(terrainVisé);
			
		}
		else {
			
			int nbAleatoire;
			 boolean coordonnéesAjustées = false;
			 
			 boolean jouerAGauche = true, jouerADroite = true, jouerEnHaut = true, jouerEnBas = true;
			 boolean étape3Appelée = false;
			 
			 /* ces deux variables ligneInitiale, colonneInitiale serviront à stocker les 
			  * coordonnées reçues en paramètre, on en aura besoin après.
			  */
			 
			 int ligneInitiale = ligne;
			 int colonneInitiale = colonne;
			 
			 xy[0]=ligne;
			 xy[1]=colonne;
			 
			
			 
			 if (difficulte==3) {
				 
				 // recherche avancée réservée à la difficulté HARD
				 
				 // ici, nous devons éliminer les cas particuliers qui entraînent un passage direct
				 // en étape 3 ou 4
				 
				 /***********	RECHERCHE DE CAS PARTICULIERS EN LIGNE    ************/
				 
				 if (colonne<terrainVisé[ligne].length-1&&colonne>0) { // entre les deux limites
					 
					 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne, colonne-1)&&BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne, colonne+1)) {
						 
						 if (!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne-1)&&!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne+1)) {
							 étape=3;
							 bateauACoulerEnLigne = false;
							 étape3Appelée = true;
						 }
						 else {
							 if (!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne-1)) {
								 jouerAGauche=false;
							 }
							 else if (!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne+1)){
								 jouerADroite=false;
							 }
						 }
					 }
					 else {
						 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne, colonne-1)) {
							 if (!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne-1)) {
								 jouerAGauche = false;
							 } 
						 }
						 else if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne, colonne+1)) {
							 if (!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne+1)) {
								 jouerADroite = false;
							 }
						 }
					 }
				 }
				 else if (colonne==0) { // bordure gauche du terrain
					 
					 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne, colonne+1)) {	 
						 if(!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne+1)) {
							 jouerADroite=false;
						 }
					 }
					 else jouerADroite = false;
					 
					 if (!jouerADroite) {
						 étape=3;
						 bateauACoulerEnLigne = false;
						 étape3Appelée = true;
					 }
					 
				 }
				 else { // bordure droite du terrain
					 
					 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne, colonne-1)) {
						 if (!BatNavaleCases.possibilitéPrésenceBateauLigne(terrainVisé, ligne, colonne-1)) {
							 jouerAGauche = false;
						 }
					 }
					 else jouerAGauche = false;
					 
					 if (!jouerAGauche) {
						 étape=3;
						 bateauACoulerEnLigne = false;
						 étape3Appelée = true;
					 }
				 }
				  
				 /***********	RECHERCHE DE CAS PARTICULIERS EN COLONNE  ************/  
				 
				 
				 if (!étape3Appelée) {
					 
					 if (ligne>0&&ligne<terrainVisé.length-1) { // entre les deux bordures
						
						 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne-1, colonne)&&BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne+1, colonne)) {
							 
							 if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne-1, colonne)&&!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne+1, colonne)) {
								 
								 bateauACoulerEnLigne=true;
								 étape=3;
								 étape3Appelée=true;
							 	
							 }
							 else {
								 if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne-1, colonne)) {
									 jouerEnHaut=false;
								 }
								 else if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne-1, colonne)) {
									 jouerEnBas=false;
								 }
							 }
							 
						 }
						 else {
							 if (!BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne-1, colonne)) {
								 if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne-1, colonne)) {
									 jouerEnHaut=false; 
								 }
							 }
							 else {
								 if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne+1, colonne)) {
									 jouerEnBas=false; 
								 }
							 }
						 }
						 
					 }
					 else if (ligne==0) {  // bordure du haut
						 
						 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne+1, colonne)) {
							 if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne+1, colonne)) {
								 jouerEnBas=false;
							 }
						 }
						 else {
							 jouerEnBas=false;
						 }
						 
						 if (!jouerEnBas) {
							 bateauACoulerEnLigne=true;
							 étape=3;
							 étape3Appelée=true;
						 }
						 
					 }
					 else { // bordure du bas
						 
						 if (BatNavaleCases.estUnBateauOuUneCaseVide(terrainVisé, ligne-1, colonne)) {
							 if (!BatNavaleCases.possibilitéPrésenceBateauColonne(terrainVisé, ligne-1, colonne)) {
								 
								jouerEnHaut=false;
								 
							 }
						 }
						 else {
							 jouerEnHaut=false;
						 }
						 
						 if (!jouerEnHaut) {
							 bateauACoulerEnLigne=true;
							 étape=3;
							 étape3Appelée=true;
						 }
						 
					 }
				 }
				 
				 if (étape3Appelée) {
					 ligne=ligneInitiale;
					 colonne=colonneInitiale;
					 étape3(terrainVisé);
				 }
				 
				 
				 // si les cas particuliers n'ont rien donné, alors l'IA effectue une recherche standard
				 
			 }
			 
			
			 if (étape==2) {
			
				 
				 // étape 2-1 : l'IA choisit des coordonnées voisines de celle de la dernière case touchée,
				 // de façon aléatoire.
				 
				 do {
					 
					 coordonnéesAjustées=false;
					 
					 ligne = ligneInitiale;
					 colonne = colonneInitiale;
					 
					 /* 4 directions possibles pour chercher dans une case avoisinante,
					  * donc un nombre aléatoire entre 0 et 3 puis un switch 
					  * pour gérer le changement des coordonnées */
					 
					 do {
						 nbAleatoire = (int)(Math.random()*4);
					 }
					 while(directionsDéjàTestées[nbAleatoire]);
					 
					 switch (nbAleatoire)  {
						case 0:
							if (ligne<terrainVisé.length-1&&jouerEnBas) {
								ligne++;
								coordonnéesAjustées = true;
								directionsDéjàTestées[0]=true;
								break;
							}
						case 1:
							if (ligne>0&&jouerEnHaut) {
								ligne--;
								coordonnéesAjustées = true;
								directionsDéjàTestées[1]=true;
								break;
							}
						case 2:
							if (colonne<terrainVisé[0].length-1&&jouerADroite) {
								colonne++;
								coordonnéesAjustées = true;
								directionsDéjàTestées[2]=true;
								break;
							}
						case 3:
							if (colonne>0&&jouerAGauche) {
								colonne--;
								coordonnéesAjustées = true;
								directionsDéjàTestées[3]=true;
								break;
							}
					}
					 
				 }
				 while(coordonnéesAjustées==false||BatNavaleMissiles.caseANePasViser(terrainVisé, true, ligne, colonne));
				 
				 // préparation des coordonnées du tir
				 
				 xy[0] = ligne;
				 xy[1] = colonne;
				 boolean positionnementDétecté = false;
				 
				 
				 // étape 2-2 : une fois que des coordonnées ont été choisies, l'IA procède au tir
				 
				 if (terrainVisé[ligne][colonne]==numéroBateauDétecté) {
					 
					 // le passage à l'étape 3 se prépare ici car une deuxième case
					 // du bateau a été trouvée, l'IA va maintenant chercher à savoir
					 // si ce bateau est placé en ligne ou en colonne.
					 
					 // l'IA découvre que le bateau est placé en colonne :
					 
					 if (ligne>0&&!positionnementDétecté) {
						 
						 
						 if (terrainVisé[ligne-1][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
							 bateauACoulerEnLigne = false;
							 positionnementDétecté = true;
						 }
					 }
					 
					 if (ligne<terrainVisé.length-1&&!positionnementDétecté) {
						 if (terrainVisé[ligne+1][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
							 bateauACoulerEnLigne = false;
							 positionnementDétecté = true;
						 }
					 }
					 
					 // sinon l'IA découvre que le bateau est placé en ligne :
					 
					 if (colonne>0&&!positionnementDétecté) {
						
						 
						 if (terrainVisé[ligne][colonne-1]==BatNavaleCharacters.VALMISSILETOUCHE) {
							 bateauACoulerEnLigne = true;
							 positionnementDétecté = true;
						 }
					 }
					 
					 if (colonne<terrainVisé[ligne].length-1&&!positionnementDétecté) {
						 
						 if (terrainVisé[ligne][colonne+1]==BatNavaleCharacters.VALMISSILETOUCHE) {
							 bateauACoulerEnLigne = true;
							 positionnementDétecté = true;
						 }
					 }
					 
					 // vérifications terminées, l'IA tire son missile puis termine son tour
					
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 
					 // le passage en étape 3 est réservé à la difficulté HARD
					 
					 if (difficulte==3) {
						 étape=3;
					 }
					 else étape=1;
				
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
				 }
				 else {
					 
					 // l'IA n'a pas trouvé une deuxième case du bateau, il reste donc en étape 2.
					 
					 // tir du missile
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
					 ligne = ligneInitiale;
					 colonne = colonneInitiale;
					 
				 }
			 }
		
			
		}
		
	
		 
		 	 
		 
		 
	
		 
		 
	}
	
	public static void étape3(char[][] terrainVisé) {
		
		 // étape 3
		 
		 // l'IA sait qu'il faut jouer en ligne ou en colonne, mais il veut d'abord s'assurer du positionnement exact
		 // du bateau, c'est à dire qu'il veut savoir dans quelle direction exacte est ce qu'il doit aller pour détruire
		 // le bateau. pour cela, il tente de frapper aléatoirement dans l'une des deux directions: par exemple, quand
		 // le bateau est en ligne, alors il va jouer vers la gauche ou vers la droite. quand il est en colonne, il jouera
		 // vers le haut ou vers le bas.
		 
		 // une fois qu'il tombe sur une case vide, sur une case qui correspond à un bateau coulé ou sur une limite de
		 // terrain, alors il aura compris qu'il faut aller jouer de l'autre côté pour couler le bateau: 
		 // c'est le passage à l'étape 4.
		 
		 if (bateauACoulerEnLigne) {
			 // jeu en ligne
			 
			 
			 if (Math.random()>0.5) {
				 direction = 4;	
				 while(colonne>0&&terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
					colonne--;
				} 
			 }
			 else {
				 direction=6;
				 while(colonne<terrainVisé[ligne].length-1&&terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
					 colonne++;
				} 
			 }
			
			 
			 xy[1]=colonne;
			 
			 if (colonne==0) {
				 
				 étape=4;
				 direction=6;
				 
				 if ((terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF||terrainVisé[ligne][colonne]==numéroBateauDétecté)&&!BatNavaleCases.présenceCarcasseBateauAutour(terrainVisé, ligne, colonne)) {
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
				 }
				 else {
					 
					while (colonne<terrainVisé[ligne].length-1&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
						colonne++;
					}
					 
					 xy[1]=colonne;
					
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
					 if (direction==4) direction=6;
					 else direction=4;
				 }
			 }
			 else if (colonne==terrainVisé[ligne].length-1) {
				 étape=4;
				 direction=4;
				 
				 if ((terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF||terrainVisé[ligne][colonne]==numéroBateauDétecté)&&!BatNavaleCases.présenceCarcasseBateauAutour(terrainVisé, ligne, colonne)) {
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				 }
				 else {
					 
					 while (colonne>0&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
						direction=4;
						colonne--;
					 }
					 
					 xy[1]=colonne;
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
					 if (direction==6) direction=4;
					 else direction=4;
				 }
			 }
			 else {
				 if (!BatNavaleCases.présenceCarcasseBateauAutour(terrainVisé, ligne, colonne)&&terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF) {
					 étape=4;
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 if (direction==4) direction=6;
					 else direction=4;
				 }
				 else if (terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF||terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILEEAU||terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILECOULE) {
					 étape=4;
					 while (terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
						 while(colonne>0&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
							 colonne--;
						 }
						 while (colonne<terrainVisé[ligne].length-1&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
							 colonne++;
						 }
					 }
					 
					 xy[1]=colonne;
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 if (direction==4) direction=6;
					 else direction=4;
				 }
				 else {
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				 }
			 }
		 }
		 else {
			 // jeu en colonne
			 
			if (Math.random()>0.5) {
				direction=2;
				
				while(ligne<terrainVisé.length-1&&terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
					ligne++;
				}
				
			}
			else {
				direction=8;
				
				while (ligne>0&&terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
					ligne--;
					
				}
			}
			
			xy[0] = ligne;
			
			if (ligne==0) {
				
				étape=4;
				direction=2;
				
				if ((terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF||terrainVisé[ligne][colonne]==numéroBateauDétecté)&&!BatNavaleCases.présenceCarcasseBateauAutour(terrainVisé, ligne, colonne)) {
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				}
				else {
					
					while (ligne<terrainVisé.length-1&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
						ligne++;
						direction=2;						
					}
					
					xy[0]=ligne;
					
					BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				}
				
			}
			else if (ligne==terrainVisé.length-1&&!BatNavaleCases.présenceCarcasseBateauAutour(terrainVisé, ligne, colonne)) {
				étape=4;
				direction=8;
				
				if (terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF||terrainVisé[ligne][colonne]==numéroBateauDétecté) {
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
				}
				else {
					
					while (ligne>0&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
						ligne--;
						direction=8;
					}
					
					xy[0]=ligne;
					
					BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				}
			}
			else {
				
				 if (!BatNavaleCases.présenceCarcasseBateauAutour(terrainVisé, ligne, colonne)&&terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF) {
					 étape=4;
					 
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
					 
					 if (direction==8) direction=2;
					 else direction=8;
				 }
				 else if (terrainVisé[ligne][colonne]==BatNavaleCharacters.VALDEF||terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILEEAU||terrainVisé[ligne][colonne]==BatNavaleCharacters.VALMISSILECOULE) {
					 étape=4;
						while (terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
							while (ligne>0&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
								ligne--;
								direction=8;
							}
							while (ligne<terrainVisé.length-1&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
								ligne++;
								direction=2;
							}
							
						}
						xy[0]=ligne;
						BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
						BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				}
				else {
					 BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
					 BatNavaleMissiles.ordinateurATiré(ligne, colonne);
				}
			}
			
			
			 
		 }


		
	}
	
	public static void étape4(char[][] terrainVisé) {

		 if (bateauACoulerEnLigne) {
		 	 
		 	 // jeu en ligne
		 	 
		 	if (direction==4) {
		 		while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&colonne>0) {
		 			colonne--;
		 		}
		 		if (colonne==0&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
		 			direction=6;
		 			while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&colonne<terrainVisé[ligne].length-1) {
			 			colonne++;
			 		}
		 		}
		 	}
		 	else {
		 		while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&colonne<terrainVisé[ligne].length-1) {
		 			colonne++;
		 		}
		 		if (colonne==terrainVisé[ligne].length-1&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
		 			direction=4;
		 			while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&colonne>0) {
			 			colonne--;
			 		}
		 		}
		 	}
		 	
		 	if (direction==4&&colonne==0) direction=6;
		 	
		 	if (direction==6&&colonne==terrainVisé[ligne].length-1) direction=4;
		 	
		 	xy[1]=colonne;
		 	BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
		 	BatNavaleMissiles.ordinateurATiré(ligne, colonne);
		 	 
		 }
		 else {
		 	 
		 	 // jeu en colonne
		 	 
		 	if (direction==2) {
		 		while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&ligne<terrainVisé.length-1) {
		 			ligne++;
		 		}
		 		if (ligne==terrainVisé.length-1&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
		 			direction=8;
		 			while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&ligne>0) {
			 			ligne--;
			 		}
		 		}
		 	}
		 	else {
		 		while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&ligne>0) {
		 			ligne--;
		 		}
		 		if (ligne==0&&terrainVisé[ligne][colonne]!=numéroBateauDétecté) {
		 			direction=2;
		 			while(terrainVisé[ligne][colonne]!=numéroBateauDétecté&&ligne<terrainVisé.length-1) {
		 				ligne++;
		 			}
		 		}
		 	}
		 	
		 	if (direction==2&&ligne==terrainVisé.length-1) direction=4;
		 	
		 	if (direction==8&&ligne==0) direction=2;
		 	 
		 	xy[0]=ligne;
		 	BatNavaleMissiles.tirerMissile(terrainVisé, xy, true);
		 	BatNavaleMissiles.ordinateurATiré(ligne, colonne);
		 	 
		 }
		 
	}

	
	public static void showValues() {
		
		// pour le débogage
		
		System.out.println("INFOS IA");
		System.out.println("Etape : " + étape);
		System.out.println("Ligne : " + (ligne+1));
		System.out.println("Colonne : " + (colonne+1));
		System.out.println("Numéro bateau détecté : " + numéroBateauDétecté);
		System.out.println("Bateau a couler en ligne : " + bateauACoulerEnLigne);
		System.out.println("Direction : " + direction);
		System.out.println("Difficulté: " + difficulte);
		System.out.println("Plus petit bateau joueur: " + BatNavaleSettings.plusPetitBateauJoueur);
	}
	
	
	
	

}
