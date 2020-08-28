package Game.ConstantsContainers.GraphicConstants;


/** Class MainConstants <p>
 * Contient les attributs graphiques constants divers
 * Les attributs de MainConstants sont initialises en coordonnees reels.
 * Il est necessaire de creer un objet MainConstants pour stocker les attributs coordonnees reels,
 * cet objet sera un attribut d'un deuxieme objet MainConstants dont les attributs seront des coordonnees graphiques.
 */
public class MainConstants {

	/**Coordonnees reelles */
	private MainConstants real;


	/**Largeur max du board */
	private int maxX = 16_000;
	/**Hauteur max du board */
	private int maxY = 10_000;

	/**Largeur d'une unite graphique */
	private double oneUnityWidth = 1.0;
	/**Hauteur d'une unite graphique */
	private double oneUnityHeight = 1.0;

	/**Largeur des plateformes */
	private int platformWidth = 5_000;
	/**Hauteur des plateformes (et en consequent hauteur du sol) */
	private int platformHeight = 1_500;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public MainConstants() {
		this.real = null;
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public MainConstants(MainConstants real) {
		this.real = real;
	}


	// /** Constructeur pour le stockage des constantes coordonnees Graphiques */
	// public MainConstants(MainConstants real, int boardJPanelWidth, int boardJPanelHeight) {
	// 	this.real = real;
	// 	updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	// }


	/**Actualise les attributs constants des coordonnees graphiques a partir des coordonnees reelles et de la taille de la fenetre */
	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		// dimensions d'une unite
		this.oneUnityWidth = (double)boardJPanelNewWidth / (double)real.maxX;
		this.oneUnityHeight = (double)boardJPanelNewHeight / (double)real.maxY;

		// dimensions du JPanel
		this.maxX = boardJPanelNewWidth;
		this.maxY = boardJPanelNewHeight;

		// dimensions des plateformes
		platformWidth = (int)(real.platformWidth * oneUnityWidth);
		platformHeight = (int)(real.platformHeight * oneUnityHeight);
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public MainConstants getReal() {
		return real;
	}

	public int getMaxX() {
		return maxX;
	}
	public int getMaxY() {
		return maxY;
	}

	public double getOneUnityWidth() {
		return oneUnityWidth;
	}
	public double getOneUnityHeight() {
		return oneUnityHeight;
	}

	public int getPlatformWidth() {
		return platformWidth;
	}
	public int getPlatformHeight() {
		return platformHeight;
	}


}