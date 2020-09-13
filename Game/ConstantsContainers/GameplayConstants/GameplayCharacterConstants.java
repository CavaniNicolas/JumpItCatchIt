package Game.ConstantsContainers.GameplayConstants;

/** Constantes de gameplay */
public class GameplayCharacterConstants {

	/* =========================================================== */
	/* Constantes qui ne peuvent pas changer au cours de la partie */
	/* =========================================================== */

	/* ========= */
	/* Character */
	/* ========= */

	/** Nombre de vies max (en moities de coeur) */
	private int livesMax = 6;
	/** Vitesse de decollision entre les deux persos */
	private int decollisionSpeed = 30;
	/** Largeur de la hitbox selon X pour les collisions entre les deux persos */
	private int hitboxWidth = 800; //Character.width / 2;
	/** Vitesse a appliquer a speedX pour le switch */
	private int switchSpeed = 295;


	/* =========================================================== */
	/*       Constantes qui peuvent changer pendant la partie      */
	/* =========================================================== */

	/* ========= */
	/* Character */
	/* ========= */

	/** Vitesse Laterale du personnage */
	private int speedLateral = 40;
	/** Vitesse Verticale du personnage */
	private int speedVertical = 450;


	/* ========== */
	/* Projectile */
	/* ========== */

	/** Vitesse des projectiles */
	private int speedProjectile = 100;
	/** Range des projectiles */
	private int rangeProjectile = 12_000;
	/** Degats des projectiles */
	private int damageProjectile = 1;
	/** Cool Down pour lancer un projectile (en milli secondes) */
	private transient long coolDownProjectile = 1_500;


	/* ==== */
	/* Push */
	/* ==== */

	/** Force pour pousser l'adversaire */
	private transient int pushStrength = 100;
	/** Cool Down pour pousser (en milli secondes) */
	private transient long coolDownPush = 1_000;


	/* ==== */
	/* Grab */
	/* ==== */

	/** Vitesse du grab */
	private transient int speedGrab = 400;
	/** Range du grab */
	private transient int rangeGrab = 3_000;
	/** Cool Down pour le grab (en milli secondes) */
	private transient long coolDownGrab = 1_000;


	/* ======= */
	/* Getters */
	/* ======= */

	/* ========= */
	/* Character */
	/* ========= */

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

	/* ========== */
	/* Projectile */
	/* ========== */

	public int getSpeedProjectile() {
		return speedProjectile;
	}
	public int getRangeProjectile() {
		return rangeProjectile;
	}
	public int getDamageProjectile() {
		return damageProjectile;
	}
	public long getCoolDownProjectile() {
		return coolDownProjectile;
	}

	/* ==== */
	/* Push */
	/* ==== */

	public int getPushStrength() {
		return pushStrength;
	}
	public long getCoolDownPush() {
		return coolDownPush;
	}

	/* ==== */
	/* Grab */
	/* ==== */

	public int getSpeedGrab() {
		return speedGrab;
	}
	public int getRangeGrab() {
		return rangeGrab;
	}
	public long getCoolDownGrab() {
		return coolDownGrab;
	}


}
