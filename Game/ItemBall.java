package Game;

import java.awt.Color;
import java.awt.Image;

public class ItemBall extends Entity {

	// Attributs d'initialisation des Items
	private int nbMaxItem;
	private int percentItem;
	private Color colorItem;
	private Image imageItem = null;



	public ItemBall(int nbMaxItem, int percentItem, Color colorItem, Image imageItem) {
		super();
		this.nbMaxItem = nbMaxItem;
		this.percentItem = percentItem;
		this.colorItem = colorItem;
		this.imageItem = imageItem;
	}


	/** Effets qu'applique cet Item, cette methode sera Override pour chaque Item */
	public void effects(Character character) {}


	/* ======= */
	/* Getters */
	/* ======= */

	public int getNbMaxItem() {
		return nbMaxItem;
	}
	public int getPercentItem() {
		return percentItem;
	}
	public Color getColorItem() {
		return colorItem;
	}
	public Image getImageItem() {
		return imageItem;
	}

}
