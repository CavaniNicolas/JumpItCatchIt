package Game.Gameplay;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.awt.Graphics;

import Game.ConstantsContainers.GraphicConstants.MainConstants;
import Game.ConstantsContainers.GraphicConstants.CharacterConstants;
import Game.ConstantsContainers.GraphicConstants.GrabConstants;


/**Class GrabSpell <p>
 * Le grab du joueur <p>
 * La position (x,y) du GrabSpell est au milieu a gauche du rectangle
 */
public class GrabSpell implements Serializable {
	private static final long serialVersionUID = 270878635786477201L;

	// Position du grab
	private int x;
	private int y;

	/**Largeur du grab (longueur) */
	private int width;

	/**Grab vers la droite ou vers la gauche */
	private boolean launchGrabDir;

	/**True si le grab a atteint une cible ou si il est arrive a max range, pour commencer le retour */
	private boolean hasReached;

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
	public void initNewGrab(int xChara, int yChara, boolean launchGrabDir, int rangeGrab, int speedGrab, GrabConstants GCReal, CharacterConstants CCReal) {
		width = GCReal.getGrabWidth();
		this.launchGrabDir = launchGrabDir;
		this.hasReached = false;
		this.rangeGrab = rangeGrab;
		this.speedGrab = speedGrab;
		moveGrabWithCharacter(xChara, yChara, CCReal);
	}


	/**Deplace le grab en suivant le personnage */
	public void moveGrabWithCharacter(int xChara, int yChara, CharacterConstants CCReal) {

		int marge = CCReal.getCharacterWidth() / 6;

		y = yChara + CCReal.getCharacterHeight() / 2;

		// Si on grab vers la droite
		if (launchGrabDir) {
			x = xChara + CCReal.getCharacterWidth() / 2 - marge;

		// Sinon on grab vers la gauche
		} else {
			x = xChara - CCReal.getCharacterWidth() / 2 + marge;

		}
	}


	/**Etire le grab */
	public void stretchGrab() {

		/**Coeff pour adapter les calculs a la direction du grab */
		int coeff = 1;
		// Si le grab a atteint quelque chose ou la max Range, on repli le grab
		if (hasReached) {
			coeff = -1;
		}

		// Etire le grab
		width += coeff * speedGrab;

		// Si le grab a atteint sa range Max
		if (width >= rangeGrab) {
			hasReached = true;
		}

	}


	/** Renvoie true si le grab est termine */
	public boolean isGrabFinished() {
		boolean isFinished = false;
		if (width <= 0) {
			isFinished = true;
		}
		return isFinished;
	}


	/** Verifie les collisions avec les items */
	public boolean checkItemCollision(int itemX, int itemY, int itemWidth) {
		boolean isGrabed = false;

		/**Coeff pour adapter les calculs a la direction du grab */
		int coeff = 1;
		if (launchGrabDir == false) {
			coeff = -1;
		}

		// La gestion des collisions entre le grab et les items utilise une hitbox ronde pour les items et un point pour le bout du grab
		if ( Math.pow((double)(x + coeff * width - itemX), 2) + Math.pow((double)(y - itemY), 2) < Math.pow((double)(itemWidth), 2) ) {
			isGrabed = true;

			// Un item est attrape, le grab doit se replier
			hasReached = true;
		}

		return isGrabed;
	}


	/**Dessine le grab */
	public void drawGrab(Graphics g, MainConstants MC, GrabConstants GC) {
		g.setColor(colorGrab);
		int x;
		int y = (int)((double) (MC.getReal().getMaxY() - (this.y + GC.getReal().getGrabHeight() / 2)) * MC.getOneUnityHeight());
		int width = (int)((double) (this.width * MC.getOneUnityWidth()));
		int height = GC.getGrabHeight();
		
		if (launchGrabDir) {
			x = (int)((double) (this.x) * MC.getOneUnityWidth());
		} else {
			x = (int)((double) (this.x - this.width) * MC.getOneUnityWidth());
		}

		g.fillRect(x, y, width, height);
	}


	/* ======= */
	/* Setters */
	/* ======= */

	public void setHasReached(boolean hasReached) {
		this.hasReached = hasReached;
	}

}