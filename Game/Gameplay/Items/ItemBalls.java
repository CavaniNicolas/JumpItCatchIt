package Game.Gameplay.Items;

import Game.ConstantsContainers.GraphicConstants.GraphicMainConstants;
import Game.ConstantsContainers.GraphicConstants.GraphicItemConstants;
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
	private ArrayList<ItemBall> itemBallList = new ArrayList<ItemBall>();

	/** Somme des probabilites de tous les items */
	private transient int sumAllProbas = 0;

	/** Espace entre deux Items */
	private transient int interItems = 50; // a mettre dans boardGraphism et a initialiser


	public ItemBalls() {
		createAllExistingBalls();
	}


	/** Creer les items qui tombent au milieu du plateau */
	public void createItems(GraphicMainConstants MCReal, GraphicItemConstants ICReal) {

		int nbItems = itemBallList.size();

		ItemBall newItem;
		
		// Le premier Item du jeu a tomber
		if (nbItems == 0) {
			
			// Calcule les probas, choisi l'Item correspondant, le cree et fait +1 au nombre en jeu du type d'Item qui est cree
			newItem = createNewItemBall(ICReal);
			// L'ajoute a la liste des Items en jeu
			itemBallList.add(newItem);

		// Ensuite lors du jeu :
		} else {
			
			// On ajoute l'item a la liste si celui qui le precede est suffisament tombe (celui qui precede est le dernier Item de la liste itemsBalls non null)
			ItemBall lastSpawnedItem;

			// On se place sur l'item precedent non null
			int i = 1;
			do {
				lastSpawnedItem = itemBallList.get(nbItems - i);
				i++;
			} while (lastSpawnedItem == null && i <= nbItems);

			// On ajoute l'item a la liste si celui qui le precede est suffisament tombe
			if (lastSpawnedItem.getY() < MCReal.getMaxY() - lastSpawnedItem.getHeight() + interItems) {
				
				// Calcule les probas, choisi l'Item correspondant, le cree et fait +1 au nombre en jeu du type d'Item qui est cree
				newItem = createNewItemBall(ICReal);
				// L'ajoute a la liste des Items en jeu
				itemBallList.add(newItem);
			}

		}

	}


	/** Calcule les probas, choisi l'Item correspondant, le cree et fait +1 au nombre en jeu du type d'Item qui est cree */
	public ItemBall createNewItemBall(GraphicItemConstants ICReal) {
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

		} else if (newItemInit.getName() == "RadiusProjectileBall") {
			newItem = new RadiusProjectileBall(x, y, width, height);

		} else if (newItemInit.getName() == "CoolDownProjectileBall") {
			newItem = new CoolDownProjectileBall(x, y, width, height);

		} else if (newItemInit.getName() == "HealBall") {
			newItem = new HealBall(x, y, width, height);
			
		} else if (newItemInit.getName() == "ShieldBreakBall") {
			newItem = new ShieldBreakBall(x, y, width, height);
		
		} else if (newItemInit.getName() == "ImmuneBall") {
			newItem = new ImmuneBall(x, y, width, height);
		}

		// Si aucun type ne correspond, aucun Item n'est cree, il faut enlever 1 au nombre d'instances en jeu du type d'item
		else {
			System.out.println("ItemBalls.createNewItemBall() : Erreur, le nom du type de l'Item cree ne correspond a aucune Class d'Item lors de la creation de : '" + newItemInit.getName() + "'");
			newItemInit.removeOneNbItem();
		}

		// Return le nouvel Item cree
		return newItem;
	}


	/** Deplace les items */
	public void moveItems() {

		ItemBall ib = null;

		for (int i=0; i<itemBallList.size(); i++) {
			ib = itemBallList.get(i);

			if (ib != null) {
				// Deplace l'Item
				ib.moveY();

				// Si l'item est tombe en bas de l'ecran, on le supprime
				if (ib.getY() == ib.getMinY()) {

					// Supprime l'Item
					removeBall(ib);
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

		// RadiusProjectileBall
		proba = 30;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("RadiusProjectileBall", 2, proba, addedProbas, null));

		// CoolDownProjectileBall
		proba = 40;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("CoolDownProjectileBall", 2, proba, addedProbas, null));

		// HealBall
		proba = 60;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("HealBall", 1, proba, addedProbas, null));

		// ShieldBreakBall
		proba = 30;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("ShieldBreakBall", 2, proba, addedProbas, null));

		// ImmuneBall
		proba = 20;
		addedProbas += proba;
		allExistingBalls.add(new ItemBallInit("ImmuneBall", 1, proba, addedProbas, null));

		sumAllProbas = addedProbas;
	}


	/** Supprime un item de la liste des items en jeu */
	public void removeBall(ItemBall deletedItem) {
		findDeletedItemAndRemoveOneNbItem(deletedItem);
		itemBallList.remove(deletedItem);
	}


	/** Affiche les items */
	public void drawItems(Graphics g, GraphicMainConstants MC, GraphicItemConstants IC) {
		ItemBall ib = null;
		for (int i=0; i<itemBallList.size(); i++) {
			ib = itemBallList.get(i);
			if (ib != null) {
				ib.drawItem(g, MC, IC);
			}
		}
	}

	@Override
	public String toString() {
		return "ItemBalls [itemBallList=" + itemBallList + "]";
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public ArrayList<ItemBall> getItemBallList() {
		return this.itemBallList;
	}

}
