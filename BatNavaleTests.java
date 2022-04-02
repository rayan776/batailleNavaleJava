package batailleNavale;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class BatNavaleTests {
	
	@Test
	final void testListeCoordonnées() {
		
		// test de la fonction qui retourne la liste des coordonnées des bateaux sur un terrain,
		// au format: {numéroBateau, ligne, colonne} pour chaque case où se trouve un bateau.
		
		char[][] terrain = new char[8][8];
		
		terrain[0][2]='3';
		terrain[0][3]='3';
		terrain[0][4]='3';
	
		terrain[3][0]='2';
		terrain[4][0]='2';
		terrain[5][0]='2';
		terrain[6][0]='2';
		terrain[7][0]='2';

		ArrayList<int[]> coord = new ArrayList<>();
		
		coord = BatNavaleMethodesDiverses.listeCoordonneesBateaux(terrain);
		
		int[][] coordTab = new int[8][3];
		
		coordTab = coord.toArray(coordTab);
		
		int[][] coordAttendu = {{3,0,2},{3,0,3},{3,0,4},{2,3,0},{2,4,0},{2,5,0},{2,6,0},{2,7,0}};
		
		assertArrayEquals(coordAttendu, coordTab);
	
	}
	
	@Test
	final void testCoordonnées() {
		
		// test des méthodes censées composer des coordonnées selon ligne et colonne,
		// décomposer des coordonnées au format "A6" en un tableau XY (ligne et colonne)
		// valider des coordonnées saisies par l'utilisateur en fonction de la taille
		// du terrain et du format attendu.
		
		String coordonneesInvalides = "S5";
		String coordonneesInvalides2 = "HH2";
		String coordonneesInvalides3 = "Bataille navale";
		
		assertFalse(BatNavaleCoordonnees.validerCoordonnees(coordonneesInvalides, 18));
		assertFalse(BatNavaleCoordonnees.validerCoordonnees(coordonneesInvalides2, 8));
		assertFalse(BatNavaleCoordonnees.validerCoordonnees(coordonneesInvalides3, 8));
		
		String coordonneesValides = "J10";
		
		assertTrue(BatNavaleCoordonnees.validerCoordonnees(coordonneesValides, 10));
		
		int[] xy = {9,9};
		assertArrayEquals(xy, BatNavaleCoordonnees.décomposerCoordonnees(coordonneesValides));
		
		String coordonneesDecomposees = "H5";
		
		assertEquals(coordonneesDecomposees, BatNavaleCoordonnees.composerCoordonnées(7, 4));
		
		assertEquals("D2", BatNavaleCoordonnees.composerCoordonnées(3, 1));
		
		
	}
	
	@Test
	final void testsVariés() {

		// Tests méthode de placement des bateaux
		
		char[][] terrain = {{'1', '1', '1', '1', '1', '.', 'x', '.'}, {'.', '.', '.', '.', '.', '.', '.', 'x'}};
		
		// on ne peut pas placer, en ligne, un bateau de longueur 6 en coordonnées A1, 
		// la méthode est donc censée retourner false
		
		assertFalse(BatNavaleBateaux.placerBateau(terrain, 2, 6, 0, 0, true));
		
		// Tests méthodes de vérification des cases
		
		assertTrue(BatNavaleCases.estUnBateauOuUneCaseVide(terrain, 0, 0));
		
		assertFalse(BatNavaleCases.possibleDeTirerAutour(terrain, 0, 7));
		
	}
	

}
