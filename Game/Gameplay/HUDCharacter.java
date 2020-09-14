package Game.Gameplay;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

import Game.ConstantsContainers.GraphicConstants.GraphicHUDConstants;
import Game.ConstantsContainers.GraphicConstants.GraphicMainConstants;
import Game.Gameplay.Items.ItemBall;


public class HUDCharacter {

	// les differentes images


	/** Affiche le HUD */
	public void displayHUD(Graphics g, GraphicMainConstants MC, GraphicHUDConstants HC, Character characterRed, Character characterBlue) {
		
		// Affiche les coeurs
		displayHUDHearts(g, MC, HC, true, characterRed.getLives(), characterRed.getLivesMax());
		displayHUDHearts(g, MC, HC, false,  characterBlue.getLives(), characterBlue.getLivesMax());

		// Affiche les effets (items attrapes)
		displayHUDCaughtItems(g, MC, HC, characterRed.getCaughtItemBalls(), true);
		displayHUDCaughtItems(g, MC, HC, characterBlue.getCaughtItemBalls(), false);
	}


	/** Affiche les effets qu'un joueur possede (items qu'il a attrape) */
	private void displayHUDCaughtItems(Graphics g, GraphicMainConstants MC, GraphicHUDConstants HC, ArrayList<ItemBall> caughtItemBalls, boolean isLeft) {
		int x;
		int y;
		int width;
		int height;

		// Pour les calculs de positions en X des caughtItems selon le joueur
		int inv;
		if (isLeft) {
			inv = 1;
			x = HC.getCaughtItemXLeft();
		} else {
			inv = -1;
			x = HC.getCaughtItemXRight();
		}

		y = MC.getMaxY() - HC.getCaughtItemY(); System.out.println(MC.getMaxY() + " " + HC.getCaughtItemY() + " " + y);
		width = HC.getCaughtItemWidth();
		height = HC.getCaughtItemHeight();

		ItemBall cib; //caughtItemBall
		for (int i=0; i<caughtItemBalls.size(); i++) {
			cib = caughtItemBalls.get(i);

			g.setColor(cib.getColorItem());

			// Si ce n'est pas le tout premier a afficher
			if (i != 0) {
				// On calcul la position du prochain caughtItem
				x += inv * (width + HC.getInterCaughtItems());
			}

			// On dessine le caughtItem
			g.fillOval(x, y, width, height);
		}

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
			x = HC.getHeartsXLeft();
		} else {
			inv = -1;
			colorHearts = Color.blue;
			x = HC.getHeartsXRight();
		}

		y = HC.getHeartsY();
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
					x += inv * HC.getInterHearts();
				}
			}
			g.fillRect(x, y, width, height);
			i++;
		}
	}

}