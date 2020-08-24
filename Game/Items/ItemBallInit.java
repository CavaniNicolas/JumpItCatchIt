package Game.Items;

import java.awt.Color;
import java.awt.Image;

public class ItemBallInit {  //Item Ball Proba


	/** Quantite de cet Item actif en jeu */
	private int nbItem = 0;

	// Attributs d'initialisation des Items
	private int nbMaxItem;
	private int percentItem;
	private int addedPercentItem;
	private Image imageItem = null;

	private String name;

	/** Constructeur pour la liste d'initialisation des items */
	public ItemBallInit(String name, int nbMaxItem, int percentItem, int addedPercentItem, Image imageItem) {
		this.name = name;
		this.nbMaxItem = nbMaxItem;
		this.percentItem = percentItem;
		this.addedPercentItem = addedPercentItem;
		this.imageItem = imageItem;
	}


	/**Ajoute 1 au nombre d'instances en jeu de cet Item */
	public void addOneNbItem() {
		this.nbItem += 1;
	}

	/**Enleve 1 au nombre d'instances en jeu de cet Item */
	public void removeOneNbItem() {
		this.nbItem -= 1;
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public String getName() {
		return name;
	}
	public int getNbMaxItem() {
		return nbMaxItem;
	}
	public int getPercentItem() {
		return percentItem;
	}
	public int getAddedPercentItem() {
		return addedPercentItem;
	}
	public Image getImageItem() {
		return imageItem;
	}
	public int getNbItem() {
		return nbItem;
	}

}