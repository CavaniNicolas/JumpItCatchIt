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

	/* =========== */
	/* CaughtItems */
	/* =========== */
	/**Position en X du premier caughtItems de gauche */
	private int caughtItemXLeft = 300;
	/**Position en X du premier caughtItems de droite */
	private int caughtItemXRight = 15_400; // (temporaire cf issue #29)
	/**Position en Y des caughtItems */
	private int caughtItemY = 1_200;
	/**Largeur des caughtItems */
	private int caughtItemWidth = 300;
	/**Hauteur des caughtItems */
	private int caughtItemHeight = 300;
	/**Distance entre les caughtItems d'un meme personnage */
	private int interCaughtItems = 100;


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

		// position des caughtItems
		caughtItemXLeft = (int)(real.caughtItemXLeft * oneUnityWidth);
		caughtItemXRight = (int)(real.caughtItemXRight * oneUnityWidth);
		caughtItemY = (int)(real.caughtItemY * oneUnityHeight);

		// dimension des caughtItems
		caughtItemWidth = (int)(real.caughtItemWidth * oneUnityWidth);
		caughtItemHeight = (int)(real.caughtItemHeight * oneUnityHeight);
		interCaughtItems = (int)(real.interCaughtItems * oneUnityWidth);

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public GraphicHUDConstants getReal() {
		return real;
	}

	/* ====== */
	/* Hearts */
	/* ====== */

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

	/* =========== */
	/* CaughtItems */
	/* =========== */

	public int getCaughtItemXLeft() {
		return caughtItemXLeft;
	}
	public int getCaughtItemXRight() {
		return caughtItemXRight;
	}
	public int getCaughtItemY() {
		return caughtItemY;
	}
	public int getCaughtItemWidth() {
		return caughtItemWidth;
	}
	public int getCaughtItemHeight() {
		return caughtItemHeight;
	}
	public int getInterCaughtItems() {
		return interCaughtItems;
	}

}