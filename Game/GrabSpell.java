package Game;

import java.awt.Color;
import java.awt.Image;


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


}