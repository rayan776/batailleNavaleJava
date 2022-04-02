package batailleNavale;

public class BatNavaleCases {
	
	public static boolean estUnBateauOuUneCaseVide(char[][] terrain, int ligne, int colonne) {
		
		return (terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne));
		
	}
	
	public static boolean possibleDeTirerAutour(char[][] terrain, int ligne, int colonne) {
		
		// méthode utile uniquement pour la difficulté moyenne de l'IA, pour éviter qu'il ne reste
		// coincé à l'étape 2
		
		
		if (colonne>1) {
			if (terrain[ligne][colonne-1]==BatNavaleCharacters.VALDEF||BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne-1)) {
				if (terrain[ligne][colonne-2]!=BatNavaleCharacters.VALMISSILECOULE)
					return true;
			}
		}
		if (colonne<terrain[ligne].length-2) {
			if (terrain[ligne][colonne+1]==BatNavaleCharacters.VALDEF||BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne+1)) { 
				if (terrain[ligne][colonne+2]!=BatNavaleCharacters.VALMISSILECOULE)
					return true;
			}
		}
		if (ligne>1) {
			if (terrain[ligne-1][colonne]==BatNavaleCharacters.VALDEF||BatNavaleUtilitaires.estUnChiffre(terrain, ligne-1, colonne)) { 
				if (terrain[ligne-2][colonne]!=BatNavaleCharacters.VALMISSILECOULE)
					return true;
			}
		}
		if (ligne<terrain.length-2) {
			if (terrain[ligne+1][colonne]==BatNavaleCharacters.VALDEF||BatNavaleUtilitaires.estUnChiffre(terrain, ligne+1, colonne)) { 
				if (terrain[ligne+2][colonne]!=BatNavaleCharacters.VALMISSILECOULE)
					return true;
			}
		}
		
		return false;
		
	}

	
	public static boolean présenceCarcasseBateauAutour(char[][] terrain, int ligne, int colonne) {
		
		if (colonne<terrain[ligne].length-1) {
			if (terrain[ligne][colonne+1]==BatNavaleCharacters.VALMISSILECOULE) return true;
		}
		
		if (colonne>0) {
			if (terrain[ligne][colonne-1]==BatNavaleCharacters.VALMISSILECOULE) return true;
		}
		
		if (ligne>0) {
			if (terrain[ligne-1][colonne]==BatNavaleCharacters.VALMISSILECOULE) return true;
		}
		
		if (ligne<terrain.length-1) {
			if (terrain[ligne+1][colonne]==BatNavaleCharacters.VALMISSILECOULE) return true;
		}
		
		return false;
		
	}

	public static boolean possibilitéPrésenceBateau (char[][] terrain, int ligne, int colonne) {
		
		int ligneInitiale = ligne;
		int colonneInitiale = colonne;
		
		boolean présencePossibleBateauLigne = false;
		boolean présencePossibleBateauColonne = false;
		
		int casesOuUnBateauPeutSeTrouver = 1;
		
		// début des recherches en ligne
		
		while (colonne<terrain[ligne].length-1) {
			
			// direction droite
			
			colonne++;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if (!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
			
		}
		
		// réinitialisation pour se déplacer dans l'autre direction en ligne
		
		ligne=ligneInitiale;
		colonne=colonneInitiale;
		
		while (colonne>0) {
			
			// direction gauche
			
			colonne--;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if (!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
		}
		
		// fin des recherches en ligne: analyse des résultats
		
		présencePossibleBateauLigne=casesOuUnBateauPeutSeTrouver>=BatNavaleSettings.plusPetitBateauJoueur;
		
		// réinitialisation pour préparer les recherches en colonne
		
		casesOuUnBateauPeutSeTrouver=1;
		
		ligne=ligneInitiale;
		colonne=colonneInitiale;
		
		// début des recherches en colonne
		
		while (ligne<terrain.length-1) {
			
			// direction bas
			
			ligne++;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if(!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
		}
		
		// réinitialisation pour se déplacer dans l'autre direction en colonne
		
		ligne=ligneInitiale;
		colonne=colonneInitiale;
		
		while (ligne>0) {
			
			// direction haut
			
			ligne--;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if (!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
		}
		
		// fin des recherches en colonne: analyse des résultats
		
		présencePossibleBateauColonne=casesOuUnBateauPeutSeTrouver>=BatNavaleSettings.plusPetitBateauJoueur;
		
		// fin des recherches et retour du résultat final
		
		return (présencePossibleBateauLigne||présencePossibleBateauColonne);
		
		
		
	}
	
	public static boolean possibilitéPrésenceBateauColonne (char[][] terrain, int ligne, int colonne) {
		
		int ligneInitiale = ligne;
		int colonneInitiale = colonne;
		
		int casesOuUnBateauPeutSeTrouver=1;
		
		// début des recherches en colonne
		
		while (ligne<terrain.length-1) {
			
			// direction bas
			
			ligne++;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if(!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
		}
		
		// réinitialisation pour se déplacer dans l'autre direction en colonne
		
		ligne=ligneInitiale;
		colonne=colonneInitiale;
		
		while (ligne>0) {
			
			// direction haut
			
			ligne--;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if (!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
		}
		
		// fin des recherches en colonne: analyse des résultats
		
		return casesOuUnBateauPeutSeTrouver>=BatNavaleSettings.plusPetitBateauJoueur;
		
	}
	
	public static boolean possibilitéPrésenceBateauLigne (char[][] terrain, int ligne, int colonne) {
		
		int ligneInitiale = ligne;
		int colonneInitiale = colonne;
		
		int casesOuUnBateauPeutSeTrouver = 1;
		
		// début des recherches en ligne
		
		while (colonne<terrain[ligne].length-1) {
			
			// direction droite
			
			colonne++;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if (!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
			
		}
		
		// réinitialisation pour se déplacer dans l'autre direction en ligne
		
		ligne=ligneInitiale;
		colonne=colonneInitiale;
		
		while (colonne>0) {
			
			// direction gauche
			
			colonne--;
			if (BatNavaleUtilitaires.estUnChiffre(terrain, ligne, colonne)||terrain[ligne][colonne]==BatNavaleCharacters.VALDEF||terrain[ligne][colonne]==BatNavaleCharacters.VALMISSILETOUCHE) {
				if (!présenceCarcasseBateauAutour(terrain, ligne, colonne)) {
					casesOuUnBateauPeutSeTrouver++;
				}
				else break;
			}
			else break;
		}
		
		// fin des recherches en ligne: analyse des résultats
		
		return casesOuUnBateauPeutSeTrouver>=BatNavaleSettings.plusPetitBateauJoueur;
		
		
	}
	

}
