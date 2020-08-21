package Game;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class ItemBalls {

	/** Tableau contenant les valeurs constantes de chaque Item, ces valeurs sont utiles a la generation des items et a leur initialisation */
	private ArrayList<ItemBall> allExistingBalls = new ArrayList<ItemBall>();


	/** Nombre max d'items sur le plateau */
	private final int nbMaxBall = 7;
	/** Tableau contenant les items actuellement sur le plateau */
	private ArrayList<ItemBall> itemBalls = new ArrayList<ItemBall>();

	/** Somme des probabilites de tous les items */
	private int sumAllProbas = 0;


	public ItemBalls() {
		createAllExistingBalls();
		initSumAllProbas();
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

}
