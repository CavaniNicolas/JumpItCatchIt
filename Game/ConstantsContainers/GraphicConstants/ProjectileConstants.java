package Game.ConstantsContainers.GraphicConstants;


/** Class ProjectileConstants <p>
 * Contient les attributs graphiques constants des projectiles
 */
public class ProjectileConstants {

	/**Coordonnees reelles */
	private ProjectileConstants real;


	/**Largeur des projectiles */
	private int projectileWidth = 1_000;
	/**Hauteur des projectiles */
	private int projectileHeight = 1_000;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public ProjectileConstants() {
		this. real = null;
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public ProjectileConstants(ProjectileConstants real, double oneUnityWidth, double oneUnityHeight) {
		this.real = real;
		updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
	}


	/**Actualise les attributs constants des coordonnees graphiques a partir des coordonnees reelles et de la taille de la fenetre */
	public void updateConstantGraphicAttributes(double oneUnityWidth, double oneUnityHeight) {

		// dimensions des projectiles
		projectileWidth = (int)(real.projectileWidth * oneUnityWidth);
		projectileHeight = (int)(real.projectileHeight * oneUnityHeight);

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public ProjectileConstants getReal() {
		return real;
	}

	public int getProjectileWidth() {
		return projectileWidth;
	}
	public int getProjectileHeight() {
		return projectileHeight;
	}


}