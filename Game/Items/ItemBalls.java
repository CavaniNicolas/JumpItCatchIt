package Game.Items;

import Game.BoardGraphism;
import Game.Items.Item.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemBalls implements Serializable {

	/** Tableau contenant les valeurs constantes de chaque Item, ces valeurs sont utiles a la generation des items et a leur initialisation */
	private transient ArrayList<ItemBall> allExistingBalls = new ArrayList<ItemBall>();


	/** Tableau contenant les items actuellement sur le plateau */
	private ArrayList<ItemBall> itemBalls = new ArrayList<ItemBall>();

	/** Somme des probabilites de tous les items */
	private transient int sumAllProbas = 0;

	/**Espace entre deux Items */
	private transient int interItems = 50; // a mettre dans boardGraphism et a initialiser


	public ItemBalls() {
		createAllExistingBalls();
		initSumAllProbas();
	}


	/** Creer les items qui tombent au milieu du plateau */
	public void createItems(BoardGraphism boardGraphism) {

		int nbItems = itemBalls.size();

		int x = boardGraphism.getReal().getItemFirstX();
		int y = boardGraphism.getReal().getItemFirstY();
		int width = boardGraphism.getReal().getItemWidth();
		int height = boardGraphism.getReal().getItemHeight();
		
		// Le premier Item du jeu a tomber sera toujours un PlusOneBall
		if (nbItems == 0) {
			itemBalls.add(new PlusOneBall(x, y, width, height));

								ItemBall ib = itemBalls.get(0);
								ItemBall aeb;
								for (int j=0; j<allExistingBalls.size(); j++) {
									aeb = allExistingBalls.get(j);
									if (ib.getClass().getName() == aeb.getClass().getName()) {
										aeb.setNbItem(aeb.getNbItem() + 1);
									}
								}

			// Ensuite lors du jeu :
		} else {
			// Calculer les probas et choisir de creer un Item

			// On ajoute l'item a la liste si celui qui le precede est suffisament tombe
			ItemBall lastSpawnedItem = itemBalls.get(itemBalls.size() - 1);
			if (lastSpawnedItem.getY() < boardGraphism.getMaxY() - lastSpawnedItem.getHeight() + interItems) {
				itemBalls.add(new PlusOneBall(x, y, width, height));
				
								ItemBall ib = itemBalls.get(0);
								ItemBall aeb;
								for (int j=0; j<allExistingBalls.size(); j++) {
									aeb = allExistingBalls.get(j);
									if (ib.getClass().getName() == aeb.getClass().getName()) {
										aeb.setNbItem(aeb.getNbItem() + 1);
									}
								}
			}
				
		}
	}


	/** Deplace les items */
	public void moveItems() {
		ItemBall ib;
		for (int i=0; i<itemBalls.size(); i++) {
			ib = itemBalls.get(i);
			ib.moveY();
			// Si l'item est tombe en bas de l'ecran, on le supprime
			if (ib.getY() == ib.getMinY()) {
				
						ItemBall aeb;
						for (int j=0; j<allExistingBalls.size(); j++) {
							aeb = allExistingBalls.get(j);
							if (ib.getClass().getName() == aeb.getClass().getName()) {
								aeb.setNbItem(aeb.getNbItem() - 1);
							}
						}
				
				itemBalls.remove(ib);
			}
		}
	}


	/** Creer en dur la liste de tous les items disponibles du jeu, avec les valeurs necessaires a l'initialisation des items */
	public void createAllExistingBalls() {

		// PlusOneBall
		allExistingBalls.add(new PlusOneBall(4, 30, Color.green, null));

		// SpeedProjectileBall
		allExistingBalls.add(new SpeedProjectileBall(2, 15, Color.magenta, null));

		// HealBall
		allExistingBalls.add(new HealBall(1, 70, Color.red, null));
	}


	/**Initialise sumAllProbas en sommant toutes les probabilites d'apparition des Items */
	public void initSumAllProbas() {
		for (ItemBall ib : allExistingBalls) {
			sumAllProbas += ib.getPercentItem();
		}
	}


	/** Affiche les items */
	public void drawItems(Graphics g, BoardGraphism boardGraphism) {
		for (int i=0; i<itemBalls.size(); i++) {
			itemBalls.get(i).drawItem(g, boardGraphism);
		}
	}


}
