package Game.Gameplay.Items;

import Game.ConstantsContainers.GraphicConstants.MainConstants;
import Game.ConstantsContainers.GraphicConstants.ItemConstants;
import Game.Gameplay.Items.Item.*;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemBalls implements Serializable {
	private static final long serialVersionUID = 5243564254587031491L;

	/**
	 * Tableau contenant les valeurs constantes de chaque Item, ces valeurs sont
	 * utiles a la generation des items et a leur initialisation
	 */
	private transient ArrayList<ItemBallInit> allExistingBalls = new ArrayList<ItemBallInit>();

	/** Tableau contenant les items actuellement sur le plateau */
	private ArrayList<ItemBall> itemBalls = new ArrayList<ItemBall>();

	/** Somme des probabilites de tous les items */
	private transient int sumAllProbas = 0;

	/** Espace entre deux Items */
	private transient int interItems = 50; // a mettre dans boardGraphism et a initialiser


	public ItemBalls() {
		createAllExistingBalls();
	}


	/** Creer les items qui tombent au milieu du plateau */
	public void createItems(MainConstants MCReal, ItemConstants ICReal) {

		int nbItems = itemBalls.size();

		ItemBall newItem;
		
		// Le premier Item du jeu a tomber
		if (nbItems == 0) {
			
			// Calcule les probas, choisi l'Item correspondant, le cree et fait +1 au nombre en jeu du type d'Item qui est cree
			newItem = createNewItemBall(ICReal);
			// L'ajoute a la liste des Items en jeu
			itemBalls.add(newItem);

		// Ensuite lors du jeu :
		} else {
			
			// On ajoute l'item a la liste si celui qui le precede est suffisament tombe (celui qui precede est le dernier Item de la liste itemsBalls non null)
			ItemBall lastSpawnedItem;

			// On se place sur l'item precedent non null
			int i = 1;
			do {
				lastSpawnedItem = itemBalls.get(nbItems - i);
				i++;
			} while (lastSpawnedItem == null && i <= nbItems);

			// On ajoute l'item a la liste si celui qui le precede est suffisament tombe
			if (lastSpawnedItem.getY() < MCReal.getMaxY() - lastSpawnedItem.getHeight() + interItems) {
				
				// Calcule les probas, choisi l'Item correspondant, le cree et fait +1 au nombre en jeu du type d'Item qui est cree
				newItem = createNewItemBall(ICReal);
				// L'ajoute a la liste des Items en jeu
				itemBalls.add(newItem);
			}

		}

	}


	/** Calcule les probas, choisi l'Item correspondant, le cree et fait +1 au nombre en jeu du type d'Item qui est cree */
	public ItemBall createNewItemBall(ItemConstants ICReal) {
		// Position et dimensions d'une nouvel Item
		int x = ICReal.getItemFirstX();
		int y = ICReal.getItemFirstY();
		int width = ICReal.getItemWidth();
		int height = ICReal.getItemHeight();

		ItemBall newItem = null;
		ItemBallInit newItemInit = null;

		int proba;

		// Recalcul une proba tant qu'on n'a pas cree un Item que l'on est autorise a creer (en fonction du nombre present actuellement en jeu)
		do {

			// Generation dun nombre aleatoire
			proba = (int)(Math.random() * (double)sumAllProbas);
			
			// Recherche quel type d'Item est a creer en fonction du nombre aleatoire obtenu
			int i = 0;
			do {
				newItemInit = allExistingBalls.get(i);
				i++;
			} while (i < allExistingBalls.size() && newItemInit.getAddedPercentItem() <= proba);


		} while (newItemInit.getNbItem() >= newItemInit.getNbMaxItem());

		// Ajoute +1 au nombre present en jeu du type du nouvel Item
		newItemInit.addOneNbItem();

		// Cree le nouvel Item
		if (newItemInit.getName() == "PlusOneBall") {
			newItem = new PlusOneBall(x, y, width, height);

		} else if (newItemInit.getName() == "SpeedProjectileBall") {
			newItem = new SpeedProjectileBall(x, y, width, height);
			
		} else if (newItemInit.getName() == "HealBall") {
			newItem = new HealBall(x, y, width, height);
			
		} else if (newItemInit.getName() == "ShieldBreakBall") {
			newItem = new ShieldBreakBall(x, y, width, height);

		// Si aucun type ne correspond, aucun Item n'est cree, il faut enlever 1 au nombre d'instances en jeu du type d'item
		} else {
			System.out.println("ItemBalls.createNewItemBall() : Erreur, le nom du type de l'Item cree ne correspond a aucune Class d'Item lors de la creation de : '" + newItemInit.getName() + "'");
			newItemInit.removeOneNbItem();
		}

		// Return le nouvel Item cree
		return newItem;
	}


	/** Deplace les items */
	public void moveItems() {

		ItemBall ib = null;

		for (int i=0; i<itemBalls.size(); i++) {
			ib = itemBalls.get(i);

			if (ib != null) {
				// Deplace l'Item
				ib.moveY();

				// Si l'item est tombe en bas de l'ecran, on le supprime
				if (ib.getY() == ib.getMinY()) {

					findDeletedItemAndRemoveOneNbItem(ib);

					// Supprime l'Item
					itemBalls.remove(ib);
				}
			}
		}

	}


	/** Recherche le type de l'item qui sort du jeu et enleve 1 a son nombre d'instances en jeu */
	public void findDeletedItemAndRemoveOneNbItem(ItemBall deletedItem) {
		ItemBallInit deletedItemType;

		if (deletedItem != null) {
			int i = 0;
			do {
				deletedItemType = allExistingBalls.get(i);
				i++;
			} while (deletedItem.getClass().getName().contains(deletedItemType.getName()) == false && i < allExistingBalls.size());

			// Si on a trouve le type de l'Item qui est supprime
			if (deletedItem.getClass().getName().contains(deletedItemType.getName())) {
				// Enleve 1 au nombre d'instances en jeu de ce type d'Item
				deletedItemType.removeOneNbItem();
			} else {
				System.out.println("ItemBalls.findDeletedItemAndRemoveOneNbItem() : Error, deleted Item was not found in allExistingBalls");
			}
		}

	}


	/**Creer en dur la liste de tous les items disponibles du jeu, avec les valeurs necessaires a l'initialisation des items
	 * et Initialise sumAllProbas en sommant toutes les probabilites d'apparition des Items
	 */
	public void createAllExistingBalls() {
		int proba;
		int addedProbas = 0;

		// PlusOneBall
		proba = 30;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("PlusOneBall", 4, proba, addedProbas, null));

		// SpeedProjectileBall
		proba = 20;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("SpeedProjectileBall", 2, proba, addedProbas, null));

		// HealBall
		proba = 60;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("HealBall", 1, proba, addedProbas, null));

		// ShieldBreakBall
		proba = 30;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("ShieldBreakBall", 2, proba, addedProbas, null));

		sumAllProbas = addedProbas;
	}


	/** Affiche les items */
	public void drawItems(Graphics g, MainConstants MC, ItemConstants IC) {
		ItemBall ib = null;
		for (int i=0; i<itemBalls.size(); i++) {
			ib = itemBalls.get(i);
			if (ib != null) {
				ib.drawItem(g, MC, IC);
			}
		}
	}

	@Override
	public String toString() {
		return "ItemBalls [itemBalls=" + itemBalls + "]";
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public ArrayList<ItemBall> getItemBalls() {
		return this.itemBalls;
	}

}
