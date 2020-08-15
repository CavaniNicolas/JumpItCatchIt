import java.awt.Color;
import java.awt.Image;

import java.awt.Graphics;

public class Character extends Entity {

	KeyBindings keyBindings;

	/**Nombre de vies (en moities de coeur) */
	private int lives = 6;

	// Booleens d'autorisation d'actions
	private boolean canJump;
	private boolean canGrab;
	private boolean canShield;
	private boolean canShoot;
	private boolean canPush;

	// Booleens de positions
	private boolean isOnLeftSide;

	private Color colorCharacter;
	private Image imageCharacter = null;


	public Character(int x, int y, Color colorCharacter, Image imageCharacter) {
    super(x, y, 0, 0, 0, 0);
		this.colorCharacter = colorCharacter;
		this.imageCharacter = imageCharacter;
	}

	public Character(int x, int y, Color colorCharacter) {
		this(x, y, colorCharacter, null);
	}

	/** gives initial speed and acceleration to the character when jumping */
	public void jump() {
		if (canJump) {
			setSpeed(0, -10);//initial jump speed
			setAcceleration(0, 10);//gravity
			canJump = false; //can't jump if already in the air
		}
	}

	/** creates an entity projectile */
	public void shoot() {
		if (canShoot) {
			Entity shot;
			if (isOnLeftSide) {
				shot = new Entity(x, y, 10, 0, 0, 0);
			} else {
				shot = new Entity(x, y, -10, 0, 0, 0);
			}
		}	
	}

	/**Dessine le personnage */
	public void drawCharacter(Graphics g, BoardGraphism boardGraphism) {
		g.setColor(colorCharacter);
		int x = (int)((double)(this.x) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double)(this.y) * boardGraphism.getGraphic().getOneUnityHeight());
		int width = (int)((double)(boardGraphism.getReal().getCharacterWidth()) * boardGraphism.getGraphic().getOneUnityWidth());
		int height = (int)((double)(boardGraphism.getReal().getCharacterHeight()) * boardGraphism.getGraphic().getOneUnityHeight());
		g.fillRect(x, y, width, height);
	}
}