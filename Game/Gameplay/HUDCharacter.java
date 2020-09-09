package Game.Gameplay;

import java.awt.Graphics;
import java.awt.Color;

import Game.ConstantsContainers.GraphicConstants.GraphicHUDConstants;
import Game.ConstantsContainers.GraphicConstants.GraphicMainConstants;


public class HUDCharacter {

	// les differentes images


	/** Affiche le HUD */
	public void displayHUD(Graphics g, GraphicMainConstants MC, GraphicHUDConstants HC, Character characterRed, Character characterBlue) {
		
		displayHUDHearts(g, MC, HC, true, characterRed.getLives(), characterRed.getLivesMax());
		displayHUDHearts(g, MC, HC, false,  characterBlue.getLives(), characterBlue.getLivesMax());
	}


	/**Affiche les coeurs du HUD d'un joueur */
	private void displayHUDHearts(Graphics g, GraphicMainConstants MC, GraphicHUDConstants HC, boolean isLeft, int lives, int livesMax) {
		Color colorHearts;
		int x;
		int y;
		int width;
		int height;

		// On ne peut pas afficher un rectangle avec un width negatif; il faut l'inverser si c'est le cas
		int inv;
		if (isLeft) {
			inv = 1;
			colorHearts = Color.red;
			x = (int) ((double) HC.getHeartsXLeft());
		} else {
			inv = -1;
			colorHearts = Color.blue;
			x = (int) ((double) HC.getHeartsXRight());
		}

		y = (int) ((double) HC.getHeartsY());
		width = HC.getHeartWidth();
		height = HC.getHeartHeight();

		int i=1;
		while (i <= livesMax) {
			// On affiche les coeurs remplis (colores)
			if (i <= lives) {
				g.setColor(colorHearts);
			// On affiche les coeurs vides (noirs)
			} else {
				g.setColor(Color.black);
			}

			// Si ce n'est pas la toute premiere moitie a afficher
			if (i != 1) {
				x += inv * width;
				// Si cest la premiere moitie dun coeur il faut rajouter l'espace entre les coeurs, on fait i-1 car le while a debuter a i=1
				if ((i-1) % 2 == 0) {
					x += (int) ((double) inv * HC.getInterHearts());
				}
			}
			g.fillRect(x, y, width, height);
			i++;
		}
	}

}