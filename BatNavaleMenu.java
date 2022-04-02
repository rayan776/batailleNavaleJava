package batailleNavale;

public class BatNavaleMenu {
	
	public static void batailleNavalleMenu() {
		
		do {
			System.out.println("BATAILLE NAVALE\n");
			
			System.out.println("1. Nouvelle partie");
			System.out.println("2. Afficher les règles du jeu");
			System.out.println("q. Quitter\n");
			
			int choix = BatNavaleUtilitaires.saisieInt("Choisissez: ", 1, 2);
			
			switch (choix) {
				case 1:
					BatNavaleMethodesDiverses.nouvellePartie();
					break;
				case 2:
					BatNavaleMethodesDiverses.afficherRèglement();
					break;
				case -1:
					BatNavaleSettings.quitter=true;
			}
			
			
		}
		while(!BatNavaleSettings.quitter);
		
		
	}
	

}
