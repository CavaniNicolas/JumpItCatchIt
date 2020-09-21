package Game.Gameplay;

import java.awt.Color;
import java.awt.Graphics;

import Game.ConstantsContainers.GraphicConstants.GraphicMainConstants;


/**Class Projectile <p>
 * La position (x,y) du Projectile est au centre du cercle */
public class Projectile extends Entity {
	private static final long serialVersionUID = 827208985306185938L;

	/** Position initiale en X */
	private transient int initX;
	/**Range du projetctile */
	private transient int rangeX;

	/**Degats */
	private transient int damage;


	/**Couleur et image du projectile */
	private Color colorProjectile = Color.orange;


	/**Constructeur du projectile */
	public Projectile(int x, int y, int speedX, int speedY, int accelX, int accelY, GraphicMainConstants MCReal, int radius, int rangeX, int damage) {
		super(x, y, speedX, speedY, accelX, accelY, radius, radius);
		this.initX = x;
		this.rangeX = rangeX;
		this.damage = damage;
		initCollisionBorders(MCReal);
	}


	/**Constructeur pour un projectile horizontal */
	public Projectile(int x, int y, int speedX, GraphicMainConstants MCReal, int radius, int range, int damage) {
		this(x, y, speedX, 0, 0, 0, MCReal, radius, range, damage);
	}


	/** Verifie la collision avec un personnage */
	public boolean checkCharacterCollision(Character character) {
		boolean hasTouched = false;
		// La gestion des collisions entre les projectiles et les personnages utilise des hitbox rondes pour les deux entites
		if ( Math.pow((double)(x - character.x), 2) + Math.pow((double)(y - (character.y + character.height/2)), 2) < Math.pow((double)(width/2 + character.width/2), 2) ) {
			hasTouched = true;
		}
		return hasTouched;
	}


	/**Dessine le projectile */
	public void drawProjectile(Graphics g, GraphicMainConstants MC) {
		g.setColor(colorProjectile);
		int x = (int)((double)(this.x - this.width / 2) * MC.getOneUnityWidth());
		int y = (int)((double)(MC.getReal().getMaxY() - (this.y + this.height / 2)) * MC.getOneUnityHeight());
		int width = (int)((double)this.width * MC.getOneUnityWidth());
		int height = (int)((double)this.height * MC.getOneUnityHeight());
		g.fillOval(x, y, width, height);
	}


	/**Initialise les bordures max du projectile */
	public void initCollisionBorders(GraphicMainConstants MCReal) {

		// Si le projectile va vers la droite
		if (speedX > 0) {
			this.minX = 0;
			this.maxX = initX + rangeX;
		// Si il va vers la gauche
		} else {
			this.minX = initX - rangeX;
			this.maxX = MCReal.getMaxX();
		}
	}


	public int getDamage() {
		return damage;
	}
}