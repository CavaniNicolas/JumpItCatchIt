package Game.Gameplay;

import java.awt.Graphics;
import java.awt.Color;

import Game.ConstantsContainers.GraphicConstants.HUDConstants;
import Game.ConstantsContainers.GraphicConstants.MainConstants;


public class HUDCharacter {

	// les differentes images


	/**Initialise les attributs des coeurs du HUD */
	public void initHUDHearts() {
	}


	/** Affiche le HUD */
	public void displayHUD(Graphics g, MainConstants MC, HUDConstants HC, Character characterRed, Character characterBlue) {
		
		displayHUDHearts(g, MC, HC, lives, livesMax);
		displayHUDHearts(g, Mc, HC, lives, livesMax);
	}


	/**Affiche les coeurs du HUD d'un joueur */
	public void displayHUDHearts(Graphics g, MainConstants MC, HUDConstants HC, int lives, int livesMax) {

		// On ne peut pas afficher un rectangle avec un width negatif; il faut l'inverser si c'est le cas
		int inv;
		if (colorHearts == Color.red) {
			inv = 1;
		} else {
			inv = -1;
		}

		int x = (int) ((double) HC.getFirstHeartX() * MC.getOneUnityWidth()); //heartsXLeft ou heartsXRight
		int y = (int) ((double) HC.getHeartsY() * MC.getOneUnityHeight());
		int width = HC.getHeartWidth();
		int height = HC.getHeartHeight();

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
					x += (int) ((double) inv * HC.getInterHearts() * MC.getOneUnityWidth());
				}
			}
			g.fillRect(x, y, width, height);
			i++;
		}
	}

}