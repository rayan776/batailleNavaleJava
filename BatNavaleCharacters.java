package batailleNavale;

public class BatNavaleCharacters {
	
	final static char VALDEF = '.';
	final static char VALMISSILEEAU = 'x';
	final static char VALMISSILETOUCHE = '@';
	final static char VALMISSILECOULE = '#';
	final static char VALBORDURE = '*';
	
	public static boolean estUnTirDeMissile(char c) {
		if (c==VALMISSILECOULE||c==VALMISSILEEAU||c==VALMISSILETOUCHE) return true;
		else return false;
	}
	
	public static boolean estUnTirDeMissileOuDeLeau(char c) {
		if (c==VALDEF||c==VALMISSILECOULE||c==VALMISSILEEAU||c==VALMISSILETOUCHE) return true;
		else return false;
	}

}
