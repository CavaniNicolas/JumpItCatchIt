package Game.Items;

import java.awt.Color;
import java.awt.Image;

public class ItemBallInit {  //Item Ball Proba


	/** Quantite de cet Item actif en jeu */
	private int nbItem = 0;

	// Attributs d'initialisation des Items
	private int nbMaxItem;
	private int percentItem;
	private Color colorItem;
	private Image imageItem = null;

	private String name;

	/** Constructeur pour la liste d'initialisation des items */
	public ItemBallInit(int nbMaxItem, int percentItem, Color colorItem, Image imageItem, String name) {
		this.nbMaxItem = nbMaxItem;
		this.percentItem = percentItem;
		this.colorItem = colorItem;
		this.imageItem = imageItem;
		this.name = name;
	}


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


	public int getNbItem() {
		return nbItem;
	}
	public void setNbItem(int newNbItem) {
		nbItem = newNbItem;
	}

}