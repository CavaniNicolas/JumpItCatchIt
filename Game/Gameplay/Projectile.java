package Game.Gameplay;

import java.awt.Color;
import java.awt.Graphics;

import Game.ConstantsContainers.GraphicConstants.GraphicMainConstants;
import Game.ConstantsContainers.GraphicConstants.GraphicProjectileConstants;


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

	/**Le projectile est actif, si il touche sa cible il appliquera des degats et deviendra inactif */
	private transient boolean isActive;

	/**Couleur et image du projectile */
	private Color colorProjectile = Color.orange;


	/**Constructeur du projectile */
	public Projectile(int x, int y, int speedX, int speedY, int accelX, int accelY, GraphicMainConstants MCReal, GraphicProjectileConstants PCReal, int rangeX, int damage) {
		super(x, y, speedX, speedY, accelX, accelY);
		this.initX = x;
		this.rangeX = rangeX;
		this.damage = damage;
		this.isActive = true;
		initGraphicAttributes(MCReal, PCReal);
	}


	/**Constructeur pour un projectile horizontal */
	public Projectile(int x, int y, int speedX, GraphicMainConstants MCReal, GraphicProjectileConstants PCReal, int range, int damage) {
		this(x, y, speedX, 0, 0, 0, MCReal, PCReal, range, damage);
	}


	/** Verifie la collision avec un personnage */
	public boolean checkCharacterCollision(Character character) {
		boolean hasTouched = false;
		// La gestion des collisions entre les projectiles et les personnages utilise des hitbox rondes pour les deux entites
		if ( Math.pow((double)(x - character.x), 2) + Math.pow((double)(y - (character.y + character.height/2)), 2) < Math.pow((double)(width/2 + character.width/2), 2) ) {
			character.setLives(character.getLives() - damage);
			hasTouched = true;
		}
		return hasTouched;
	}


	/**Dessine le projectile */
	public void drawProjectile(Graphics g, GraphicMainConstants MC, GraphicProjectileConstants PC) {
		g.setColor(colorProjectile);
		int x = (int)((double)(this.x - this.width / 2) * MC.getOneUnityWidth());
		int y = (int)((double)(MC.getReal().getMaxY() - (this.y + this.height / 2)) * MC.getOneUnityHeight());
		int width = PC.getProjectileWidth();
		int height = PC.getProjectileHeight();
		g.fillOval(x, y, width, height);
	}


	/**Initialise les champs graphiques */
	public void initGraphicAttributes(GraphicMainConstants MCReal, GraphicProjectileConstants PCReal) {
		this.width = PCReal.getProjectileWidth();
		this.height = PCReal.getProjectileHeight();

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

}