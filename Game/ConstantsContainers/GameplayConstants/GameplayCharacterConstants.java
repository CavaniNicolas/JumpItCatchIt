package Game.ConstantsContainers.GameplayConstants;

/** Constantes de gameplay */
public class GameplayCharacterConstants {

	/* Constantes qui ne peuvent pas changer au cours de la partie */
	/* =========================================================== */

	/**Nombre de vies max (en moities de coeur) */
	private int livesMax = 6;

	/**Vitesse de decollision entre les deux persos */
	private int decollisionSpeed = 30;

	/**Largeur de la hitbox selon X pour les collisions entre les deux persos */
	private int hitboxWidth = 800; //Character.width / 2;

	/** Vitesse a appliquer a speedX pour le switch */
	private int switchSpeed = 295;


	/* Constantes qui peuvent changer pendant la partie */
	/* ================================================ */

	/** Vitesse Laterale Constante */
	private int speedLateral = 40;

	/** Vitesse Verticale Constante */
	private int speedVertical = 450;


	/* ======= */
	/* Getters */
	/* ======= */

	public int getLivesMax() {
		return livesMax;
	}
	public int getDecollisionSpeed() {
		return decollisionSpeed;
	}
	public int getHitboxWidth() {
		return hitboxWidth;
	}
	public int getSwitchSpeed() {
		return switchSpeed;
	}
	public int getSpeedLateral() {
		return speedLateral;
	}
	public int getSpeedVertical() {
		return speedVertical;
	}

}
