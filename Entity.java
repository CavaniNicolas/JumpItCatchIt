
/**Class Entity<p>
 * Tous les objets qui vont pouvoir avoir un mouvement, (vitesse et acceleration)
 */
public class Entity {

	// Coordonnees du l'entite
	protected int x;
	protected int y;
	// Vitesses du l'entite
	protected int speedX;
	protected int speedY;
	// Accelerations du l'entite
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
		x += speedX;
		y += speedY;
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