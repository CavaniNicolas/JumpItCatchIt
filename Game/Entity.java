package Game;

/**Class Entity<p>
 * Tous les objets qui vont pouvoir avoir un mouvement, (vitesse et acceleration)
 */
public class Entity {

	/**Gravite */
	protected final int GRAVITY = -20;

	// Coordonnees de collisions minimales et maximales
	protected int minX;
	protected int maxX;
	protected int minY;

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


	public Entity(int x, int y, int speedX, int speedY, int accelX, int accelY) {
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.accelX = accelX;
		this.accelY = accelY;
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
}