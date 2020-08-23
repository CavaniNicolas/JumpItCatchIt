package Game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class ItemBalls {

	/** Tableau contenant les valeurs constantes de chaque Item, ces valeurs sont utiles a la generation des items et a leur initialisation */
	private ArrayList<ItemBall> allExistingBalls = new ArrayList<ItemBall>();


	/** Tableau contenant les items actuellement sur le plateau */
	private ArrayList<ItemBall> itemBalls = new ArrayList<ItemBall>();

	/** Somme des probabilites de tous les items */
	private int sumAllProbas = 0;


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
			System.out.println("LA");
			itemBalls.add(new PlusOneBall(x, y, width, height));

		// Ensuite lors du jeu :
		} else {
			// Calculer les probas et choisir de creer un Item

			// On ajoute l'item a la liste si celui qui le precede est suffisament tombe
			if (true) {

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
			if (ib.y == ib.minY) {
				itemBalls.remove(ib);
				//ib.getClass().setNbItem(ib.getClass().getNbItem() - 1);
				System.out.println(ib.getClass());
			}
		}
	}


	/** Creer en dur la liste de tous les items disponibles du jeu, avec les valeurs necessaires a l'initialisation des items */
	public void createAllExistingBalls() {

		// PlusOneBall
		allExistingBalls.add(new PlusOneBall(4, 30, Color.green, null));
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
