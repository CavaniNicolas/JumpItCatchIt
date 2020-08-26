package Game.ConstantsContainers.GraphicConstants.EntityConstants;

import Game.ConstantsContainers.GraphicConstants.ConstantGraphicAttributes;


/** Class ItemConstants <p>
 * Contient les attributs graphiques constants des items
 */
public class ItemConstants extends ConstantGraphicAttributes {

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
		super();
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public ItemConstants(ItemConstants realAttributes, int boardJPanelWidth, int boardJPanelHeight) {
		super(realAttributes, boardJPanelWidth, boardJPanelHeight);
		itemFirstX = realAttributes.maxX / 2;
		itemFirstY = realAttributes.maxY + itemHeight / 2;

		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		super.updateConstantGraphicAttributes(boardJPanelNewWidth, boardJPanelNewHeight);

		ItemConstants real = (ItemConstants)realAttributes;

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