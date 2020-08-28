package Game.Gameplay.Items;

import java.awt.Image;

/**Class ItemBallInit <p>
 * sert a la creation et l'initialisation des Items dans le jeu */
public class ItemBallInit {

	/** Quantite de cet Item actif en jeu */
	private int nbItem = 0;

	/** Nom de la classe de l'Item associe aux attributs d'initialisation de cet objet */
	private String name;
	/** Nombre d'apparition maximum de l'Item de ce type */
	private int nbMaxItem;
	/** Chance d'apparition de l'Item de ce type */
	private int percentItem;
	/** borne superieure de l'intervalle de probabilite d'apparition de l'Item de ce type. Dans l'arrayList qui contient les ItemBallInit,
	 * les addedPercentItem sont ranges par ordre croissant pour faciliter la recherche de l'item cree avec sa probabilite */
	private int addedPercentItem;
	/** Image de l'Item de ce type */
	private Image imageItem = null;


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