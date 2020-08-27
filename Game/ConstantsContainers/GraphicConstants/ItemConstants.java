package Game.ConstantsContainers.GraphicConstants;


/** Class ItemConstants <p>
 * Contient les attributs graphiques constants des items
 */
public class ItemConstants {

	/**Coordonnees reelles */
	private ItemConstants real;


	/**Largeur des Items */
	private int itemWidth = 1_200;
	/**Hauteur des Items */
	private int itemHeight = 1_200;

	/**Position en X initiale des Items */
	private int itemFirstX;
	/**Position en Y initiale des Items */
	private int itemFirstY;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public ItemConstants() {
		this.real = null;
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public ItemConstants(ItemConstants real, int maxXReal, int maxYReal, double oneUnityWidth, double oneUnityHeight) {
		this.real = real;
		itemFirstX = maxXReal / 2;
		itemFirstY = maxYReal + itemHeight / 2;

		updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
	}


	/**Actualise les attributs constants des coordonnees graphiques a partir des coordonnees reelles et de la taille de la fenetre */
	public void updateConstantGraphicAttributes(double oneUnityWidth, double oneUnityHeight) {

		// dimensions des Items
		itemWidth = (int)(real.itemWidth * oneUnityWidth);
		itemHeight = (int)(real.itemHeight * oneUnityHeight);

		// Position initiale des Items
		itemFirstX = (int)(real.itemFirstX * oneUnityWidth);
		itemFirstY = (int)(real.itemFirstY * oneUnityHeight);

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public int getItemWidth() {
		return itemWidth;
	}
	public int getItemHeight() {
		return itemHeight;
	}
	public int getItemFirstX() {
		return itemFirstX;
	}
	public int getItemFirstY() {
		return itemFirstY;
	}


}