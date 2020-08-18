
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

public class Projectile extends Entity {

	/**Position initiale en X */
	private int initX;
	/**Range du projetctil */
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


	public Projectile(int x, int y, int speedX, int speedY, int accelX, int accelY, int damage, Character aimedCharacter, Color colorProjectile) {
		super(x, y, speedX, speedY, accelX, accelY);
		this.initX = x;
		this.damage = damage;
		this.aimedCharacter = aimedCharacter;
		this.isActive = true;
		this.colorProjectile = colorProjectile;
	}


	public void drawProjectile(Graphics g, BoardGraphism boardGraphism) {
		g.setColor(colorProjectile);
		int x = (int)((double)(this.x - 40) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double)(boardGraphism.getMaxY() - (this.y + 40)) * boardGraphism.getGraphic().getOneUnityHeight());
		int radius = (int)((double)(/*boardGraphism.getReal().getCharacterWidth()*/ 80) * boardGraphism.getGraphic().getOneUnityWidth());
		g.fillOval(x, y, radius, radius);
	}
}