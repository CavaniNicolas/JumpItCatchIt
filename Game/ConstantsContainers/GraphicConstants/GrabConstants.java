package Game.ConstantsContainers.GraphicConstants;


/** Class GrabConstants <p>
 * Contient les attributs graphiques constants des grabs
 */
public class GrabConstants {

	/**Coordonnees reelles */
	private GrabConstants real;


	/**Largeur des grabs */
	private int grabWidth = 200;
	/**Hauteur des grabs */
	private int grabHeight = 1_000;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public GrabConstants() {
		this. real = null;
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public GrabConstants(GrabConstants real) {
		this.real = real;
	}


	// /** Constructeur pour le stockage des constantes coordonnees Graphiques */
	// public GrabConstants(GrabConstants real, double oneUnityWidth, double oneUnityHeight) {
	// 	this.real = real;
	// 	updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
	// }


	/**Actualise les attributs constants des coordonnees graphiques a partir des coordonnees reelles et de la taille de la fenetre */
	public void updateConstantGraphicAttributes(double oneUnityWidth, double oneUnityHeight) {

		// dimensions des grabs
		grabWidth = (int)(real.grabWidth * oneUnityWidth);
		grabHeight = (int)(real.grabHeight * oneUnityHeight);

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public GrabConstants getReal() {
		return real;
	}

	public int getGrabWidth() {
		return grabWidth;
	}
	public int getGrabHeight() {
		return grabHeight;
	}


}