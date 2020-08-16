
/**Class Entity<p>
 * Tous les objets qui vont pouvoir avoir un mouvement, (vitesse et acceleration)
 */
public class Entity {

	/**Gravite */
	protected final int GRAVITY = -2;

	// Coordonnees de collisions minimales et maximales
	protected int minX = 0; // Init a enlever
	protected int maxX = 1600;
	protected int minY = 0;

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


	/** Deplace l'entite */
	public void move() {
		speedX += accelX;
		speedY += accelY;

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