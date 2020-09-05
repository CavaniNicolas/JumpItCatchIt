package Game.Gameplay;

import java.io.Serializable;

/**
 * Class Entity <p>
 * Tous les objets qui vont pouvoir avoir un mouvement, (vitesse et acceleration)
 */
public class Entity implements Serializable {
	private static final long serialVersionUID = 5724559282425349586L;

	/** Gravite */
	protected transient final int GRAVITY = -20;

	// Coordonnees de collisions minimales et maximales
	protected transient int minX;
	protected transient int maxX;
	protected transient int minY;

	// Largeur et Hauteur de l'entite	
	protected int width;
	protected int height;

	// Coordonnees de l'entite
	protected int x;
	protected int y;
	// Vitesses de l'entite
	protected int speedX;
	protected int speedY;
	// Accelerations de l'entite
	protected int accelX;
	protected int accelY;


	public Entity(int x, int y, int speedX, int speedY, int accelX, int accelY, int width, int height) {
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.accelX = accelX;
		this.accelY = accelY;
		this.width = width;
		this.height = height;
	}


	public Entity(int x, int y, int speedX, int speedY, int accelX, int accelY) {
		this(x, y, speedX, speedY, accelX, accelY, 0, 0);
	}


	public Entity() {
		this(0, 0, 0, 0, 0, 0, 0, 0);
	}


	/**Deplace l'entite */
	public void moveXY() {
		moveX();
		moveY();
	}


	/** Deplace l'entite selon X*/
	public void moveX() {
		speedX += accelX;

		// Collision a gauche, on conserve le max
		if (minX > x + speedX) {
			x = minX;
		} else {
			x = x + speedX;
		}

		// Collision a droite, on conserve le min
		if (maxX < x) {
			x = maxX;
		}

	}


	/** Deplace l'entite selon Y*/
	public void moveY() {
		speedY += accelY;

		// Collision au sol, on conserve le max (on est au sol)
		if (minY > y + speedY) {
			y = minY;
			speedY = 0;
			accelY = 0;

		// Sinon on tombe, on conserve le min
		} else {
			y = y + speedY;
		}

	}


	/* ======= */
	/* Setters */
	/* ======= */

	public void setAcceleration(int accelX, int accelY) {
		this.accelX = accelX;
		this.accelY = accelY;
	}

	public void setSpeed(int speedX, int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}


	public int getMinX() {
		return minX;
	}
	public void setMinX(int minX) {
		this.minX = minX;
	}
	public int getMaxX() {
		return maxX;
	}
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	public int getMinY() {
		return minY;
	}
	public void setMinY(int minY) {
		this.minY = minY;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Entity [x=" + x + ", y=" + y + ", speedX=" + speedX + ", speedY=" + speedY + ", accelX=" + accelX + ", accelY=" + accelY + ", height=" + height + ", width=" + width + "]";
	}

}