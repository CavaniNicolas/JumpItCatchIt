package Game.ConstantsContainers.GraphicConstants;


/** Class HUDConstants <p>
 * Contient les attributs graphiques constants du HUD
 */
public class HUDConstants extends ConstantGraphicAttributes {

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
	public HUDConstants() {
		super();
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public HUDConstants(HUDConstants realAttributes, int boardJPanelWidth, int boardJPanelHeight) {
		super(realAttributes, boardJPanelWidth, boardJPanelHeight);
		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	@Override
	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		super.updateConstantGraphicAttributes(boardJPanelNewWidth, boardJPanelNewHeight);

		HUDConstants real = (HUDConstants)realAttributes;

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