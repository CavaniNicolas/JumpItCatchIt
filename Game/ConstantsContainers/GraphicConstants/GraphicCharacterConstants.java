package Game.ConstantsContainers.GraphicConstants;


/** Class CharacterConstants <p>
 * Contient les attributs graphiques constants des personnages
 */
public class GraphicCharacterConstants {

	/**Coordonnees reelles */
	private GraphicCharacterConstants real;


	/**Largeur des personnages */
	private int characterWidth = 1_600;
	/**Hauteur des personnages*/
	private int characterHeight = 2_000;

	/**Coordonnee principale en X du personnage a gauche (position sur la plateforme au debut) */
	private int primaryXcoordLeft = 3_800;
	/**Coordonnee secondaire en X du personnage a gauche (position lors d'un respawn) */
	private int secondaryXcoordLeft = 1_800;

	/**Coordonnee principale en X du personnage a droite (position sur la plateforme au debut) */
	private int primaryXcoordRight = 12_200;
	/**Coordonnee secondaire en X du personnage a droite (position lors d'un respawn) */
	private int secondaryXcoordRight = 14_200;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public GraphicCharacterConstants() {
		this.real = null;
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public GraphicCharacterConstants(GraphicCharacterConstants real) {
		this.real = real;
	}


	// /** Constructeur pour le stockage des constantes coordonnees Graphiques */
	// public CharacterConstants(CharacterConstants real, double oneUnityWidth, double oneUnityHeight) {
	// 	this.real = real;
	// 	updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
	// }


	/**Actualise les attributs constants des coordonnees graphiques a partir des coordonnees reelles et de la taille de la fenetre */
	public void updateConstantGraphicAttributes(double oneUnityWidth, double oneUnityHeight) {

		// dimensions des personnages
		characterWidth = (int)(real.characterWidth * oneUnityWidth);
		characterHeight = (int)(real.characterHeight * oneUnityHeight);

		// positions des personnages en X sur les plateforme
		primaryXcoordLeft = (int)(real.primaryXcoordLeft * oneUnityWidth);
		secondaryXcoordLeft = (int)(real.secondaryXcoordLeft * oneUnityWidth);
		primaryXcoordRight = (int)(real.primaryXcoordRight * oneUnityWidth);
		secondaryXcoordRight = (int)(real.secondaryXcoordRight * oneUnityWidth);
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public GraphicCharacterConstants getReal() {
		return real;
	}

	public int getCharacterWidth() {
		return characterWidth;
	}
	public int getCharacterHeight() {
		return characterHeight;
	}
	public int getPrimaryXcoordLeft() {
		return primaryXcoordLeft;
	}
	public int getSecondaryXcoordLeft() {
		return secondaryXcoordLeft;
	}
	public int getPrimaryXcoordRight() {
		return primaryXcoordRight;
	}
	public int getSecondaryXcoordRight() {
		return secondaryXcoordRight;
	}


}