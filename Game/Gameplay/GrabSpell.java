package Game.Gameplay;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

import Game.ConstantsContainers.GraphicConstants.MainConstants;
import Game.ConstantsContainers.GraphicConstants.CharacterConstants;
import Game.ConstantsContainers.GraphicConstants.GrabConstants;


/**Class GrabSpell <p>
 * Le grab du joueur <p>
 * La position (x,y) du GrabSpell est au milieu a gauche du rectangle
 */
public class GrabSpell {

	// Position du grab
	private int x;
	private int y;

	/**Largeur du grab (longueur) */
	private int width;

	/**Vitesse du grab */
	private int speedGrab;
	/**Range du grab */
	private int rangeGrab;


	/**Couleur du grab */
	private Color colorGrab = new Color(114, 73, 50);
	/**Image du grab */
	private Image imageGrab;


	/** Un seul GrabSpell est cree par Character, mais ils sont re-initialises a chaque utilisation */
	public GrabSpell() {
	}


	/**Initialise un nouveau grab lors du lancement */
	public void initNewGrab(int xChara, int yChara, boolean grabRight, int rangeGrab, int speedGrab, GrabConstants GCReal, CharacterConstants CCReal) {
		moveGrabWithCharacter(xChara, yChara, grabRight, CCReal);
		width = GCReal.getGrabWidth();
		this.rangeGrab = rangeGrab;
		this.speedGrab = speedGrab;
	}


	/**Deplace le grab en suivant le personnage */
	public void moveGrabWithCharacter(int xChara, int yChara, Boolean grabRight, CharacterConstants CCReal) {

		int marge = CCReal.getCharacterWidth() / 6;

		y = yChara + CCReal.getCharacterHeight() / 2;

		// Si on grab vers la droite
		if (grabRight) {
			x = xChara + CCReal.getCharacterWidth() / 2 - marge;

		// Sinon on grab vers la gauche
		} else {
			x = xChara - CCReal.getCharacterWidth() / 2 + marge;

		}
	}


	/**Etire le grab jusqu'a sa range max */
	public void stretchGrab() {

	}


	/**Dessine le grab */
	public void drawGrab(Graphics g, MainConstants MC, GrabConstants GC) {
		g.setColor(colorGrab);
		int x = (int)((double) (this.x) * MC.getOneUnityWidth());
		int y = (int)((double) (MC.getReal().getMaxY() - (this.y + GC.getReal().getGrabHeight() / 2)) * MC.getOneUnityHeight());
		int width = (int)((double) (this.width * MC.getOneUnityWidth()));
		int height = GC.getGrabHeight();
		g.fillRect(x, y, width, height);
	}

}