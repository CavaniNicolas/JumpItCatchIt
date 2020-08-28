package Game;

import java.awt.Graphics;
import java.awt.Color;


public class HUDCharacter {
	
	// attributs des coeurs
	private int firstHeartX;
	private int heartsY;
	private int interHearts;
	private Color colorHearts = Color.red;
	// les differentes images


	/**Initialise les attributs des coeurs du HUD */
	public void initHUDHearts(int firstHeartX, int heartsY, int interHearts, Color colorHearts) {
		this.firstHeartX = firstHeartX;
		this.heartsY = heartsY;
		this.interHearts = interHearts;
		this.colorHearts = colorHearts;
	}


	// /**Affiche le HUD */
	// public void displayHUDHearts(Graphics g, BoardGraphism boardGraphism, int lives, int livesMax) {

	// 	// On ne peut pas afficher un rectangle avec un width negatif; il faut l'inverser si c'est le cas
	// 	int inv;
	// 	if (colorHearts == Color.red) {
	// 		inv = 1;
	// 	} else {
	// 		inv = -1;
	// 	}

	// 	int x = (int) ((double) firstHeartX * boardGraphism.getGraphic().getOneUnityWidth());
	// 	int y = (int) ((double) heartsY * boardGraphism.getGraphic().getOneUnityHeight());
	// 	int width = boardGraphism.getGraphic().getHeartWidth();
	// 	int height = boardGraphism.getGraphic().getHeartHeight();

	// 	int i=1;
	// 	while (i <= livesMax) {
	// 		// On affiche les coeurs remplis (colores)
	// 		if (i <= lives) {
	// 			g.setColor(colorHearts);
	// 		// On affiche les coeurs vides (noirs)
	// 		} else {
	// 			g.setColor(Color.black);
	// 		}

	// 		// Si ce n'est pas la toute premiere moitie a afficher
	// 		if (i != 1) {
	// 			x += inv * width;
	// 			// Si cest la premiere moitie dun coeur il faut rajouter l'espace entre les coeurs, on fait i-1 car le while a debuter a i=1
	// 			if ((i-1) % 2 == 0) {
	// 				x += (int) ((double) inv * interHearts * boardGraphism.getGraphic().getOneUnityWidth());
	// 			}
	// 		}
	// 		g.fillRect(x, y, width, height);
	// 		i++;
	// 	}
	// }

}