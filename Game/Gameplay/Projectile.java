package Game.Gameplay;

import java.awt.Graphics;
import java.awt.Image;

import Game.ConstantsContainers.GraphicConstants.MainConstants;
import Game.ConstantsContainers.GraphicConstants.ProjectileConstants;

import java.awt.Color;

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
	private Color colorProjectile;
	private transient Image imageProjectile = null;


	/**Constructeur du projectile */
	public Projectile(int x, int y, int speedX, int speedY, int accelX, int accelY, MainConstants MCReal, ProjectileConstants PCReal, int rangeX, int damage, Color colorProjectile) {
		super(x, y, speedX, speedY, accelX, accelY);
		this.initX = x;
		this.rangeX = rangeX;
		this.damage = damage;
		this.isActive = true;
		this.colorProjectile = colorProjectile;
		initGraphicAttributes(MCReal, PCReal);
	}


	/**Constructeur pour un projectile horizontal */
	public Projectile(int x, int y, int speedX, MainConstants MCReal, ProjectileConstants PCReal, int range, int damage, Color colorProjectile) {
		this(x, y, speedX, 0, 0, 0, MCReal, PCReal, range, damage, colorProjectile);
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
	public void drawProjectile(Graphics g, MainConstants MC, ProjectileConstants PC) {
		g.setColor(colorProjectile);
		int x = (int)((double)(this.x - this.width / 2) * MC.getOneUnityWidth());
		int y = (int)((double)(MC.getReal().getMaxY() - (this.y + this.height / 2)) * MC.getOneUnityHeight());
		int width = PC.getProjectileWidth();
		int height = PC.getProjectileHeight();
		g.fillOval(x, y, width, height);
	}


	/**Initialise les champs graphiques */
	public void initGraphicAttributes(MainConstants MCReal, ProjectileConstants PCReal) {
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