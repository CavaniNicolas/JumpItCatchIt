package Game.Gameplay;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

import Game.ConstantsContainers.GraphicConstants.MainConstants;
import Game.ConstantsContainers.GraphicConstants.GrabConstants;


/**Class GrabSpell <p>
 * Le grab du joueur <p>
 * La position (x,y) du GrabSpell est au milieu a gauche du rectangle
 */
public class GrabSpell {

	// Position du grab
	private int x;
	private int y;

	private int width;

	/**Vitesse du grab */
	private int speedGrab;
	/**Range du grab */
	private int rangeGrab;


	/**Couleur du grab */
	private Color colorGrab = new Color(114, 73, 50);
	/**Image du grab */
	private Image imageGrab;


	public GrabSpell() {

	}


	public void initNewGrab() {
		// x de debut
		// y de debut
		// width = initialWidth
		// range = rangeForThisGrab
		// speed = SpeedForThisGrab
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