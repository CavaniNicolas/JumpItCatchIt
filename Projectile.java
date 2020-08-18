
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

public class Projectile extends Entity {

	/**Position initiale en X */
	private int initX;
	/**Range du projetctile */
	private int rangeX;

	/**Degats */
	private int damage;

	/**Personnage cible du projectile */
	private Character aimedCharacter;

	/**Le Personnage qui a tire le projectile */

	/**Le projectile est actif, si il touche sa cible il appliquera des degats et deviendra inactif */
	private boolean isActive;

	/**Couleur et image du projectile */
	private Color colorProjectile;
	private Image imageProjectile = null;


	/**Constructeur du projectile */
	public Projectile(int x, int y, int speedX, int speedY, int accelX, int accelY, BoardGraphism boardGraphism, int rangeX, int damage, Character aimedCharacter, Color colorProjectile) {
		super(x, y, speedX, speedY, accelX, accelY);
		this.initX = x;
		this.rangeX = rangeX;
		this.damage = damage;
		this.aimedCharacter = aimedCharacter;
		this.isActive = true;
		this.colorProjectile = colorProjectile;
		initGraphicAttributes(boardGraphism);
	}


	/**Constructeur pour un projectile horizontal */
	public Projectile(int x, int y, int speedX, BoardGraphism boardGraphism, int range, int damage, Character aimedCharacter, Color colorProjectile) {
		this(x, y, speedX, 0, 0, 0, boardGraphism, range, damage, aimedCharacter, colorProjectile);
	}


	/**Dessine le projectile */
	public void drawProjectile(Graphics g, BoardGraphism boardGraphism) {
		g.setColor(colorProjectile);
		int x = (int)((double)(this.x - this.width / 2) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double)(boardGraphism.getMaxY() - (this.y + this.height / 2)) * boardGraphism.getGraphic().getOneUnityHeight());
		int width = (int)((double)(this.width) * boardGraphism.getGraphic().getOneUnityWidth());
		int height = (int)((double)(this.height) * boardGraphism.getGraphic().getOneUnityHeight());
		g.fillOval(x, y, width, height);
	}


	/**Initialise les champs graphiques */
	public void initGraphicAttributes(BoardGraphism boardGraphism) {
		this.width = boardGraphism.getReal().getProjectileWidth();
		this.height = boardGraphism.getReal().getProjectileHeight();

		// Si le projectile va vers la droite
		if (speedX > 0) {
			this.minX = 0;
			this.maxX = initX + rangeX;
		// Si il va vers la gauche
		} else {
			this.minX = initX - rangeX;
			this.maxX = boardGraphism.getMaxX();
		}
	}

}