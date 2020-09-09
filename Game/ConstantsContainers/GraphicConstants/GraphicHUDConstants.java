package Game.ConstantsContainers.GraphicConstants;


/** Class HUDConstants <p>
 * Contient les attributs graphiques constants du HUD
 */
public class GraphicHUDConstants {

	/**Coordonnees reelles */
	private GraphicHUDConstants real;


	/* ====== */
	/* Coeurs */
	/* ====== */
	/**Position en X du coeur de gauche HUD de gauche */
	private int heartsXLeft = 500;
	/**Position en X du coeur de droite du HUD de droite */
	private int heartsXRight = 15_100; //16_000 - 500 - 400 : maxX - memeDistanceDuBord - largeur (temporaire cf issue #29)
	/**Position en Y des coeurs des HUD */
	private int heartsY = 400; // ERREUR ICI, devrait etre 10_000 - 400 = 9_600 adapter la fonction displayHUDHearts()
	/**Largeur des coeurs du HUD */
	private int heartWidth = 400;
	/**Hauteur des coeurs du HUD */
	private int heartHeight = 800;
	/**Distance entre les coeurs d'un meme personnage */
	private int interHearts = 200;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public GraphicHUDConstants() {
		this.real = null;
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public GraphicHUDConstants(GraphicHUDConstants real) {
		this.real = real;
	}


	// /** Constructeur pour le stockage des constantes coordonnees Graphiques */
	// public HUDConstants(HUDConstants real, double oneUnityWidth, double oneUnityHeight) {
	// 	this.real = real;
	// 	updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
	// }


	/**Actualise les attributs constants des coordonnees graphiques a partir des coordonnees reelles et de la taille de la fenetre */
	public void updateConstantGraphicAttributes(double oneUnityWidth, double oneUnityHeight) {

		// positions des coeurs
		heartsXLeft = (int)(real.heartsXLeft * oneUnityWidth);
		heartsXRight = (int)(real.heartsXRight * oneUnityWidth);
		heartsY = (int)(real.heartsY * oneUnityHeight);

		// dimension des coeurs
		heartWidth = (int)(real.heartWidth * oneUnityWidth);
		heartHeight = (int)(real.heartHeight * oneUnityHeight);
		interHearts = (int)(real.interHearts * oneUnityWidth);

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public GraphicHUDConstants getReal() {
		return real;
	}

	public int getHeartsXLeft() {
		return heartsXLeft;
	}
	public int getHeartsXRight() {
		return heartsXRight;
	}
	public int getHeartsY() {
		return heartsY;
	}
	public int getHeartWidth() {
		return heartWidth;
	}
	public int getHeartHeight() {
		return heartHeight;
	}
	public int getInterHearts() {
		return interHearts;
	}


}