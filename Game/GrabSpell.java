package Game;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;


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
	public void drawGrab(Graphics g, BoardGraphism boardGraphism) {
		g.setColor(colorGrab);
		int x = (int)((double) (this.x) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double) (boardGraphism.getMaxY() - (this.y + boardGraphism.getReal().getGrabHeight() / 2)) * boardGraphism.getGraphic().getOneUnityHeight());
		int width = (int)((double) (this.width * boardGraphism.getGraphic().getOneUnityWidth()));
		int height = boardGraphism.getGraphic().getGrabHeight();
		g.fillRect(x, y, width, height);
	}

}