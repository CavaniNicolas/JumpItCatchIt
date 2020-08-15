import java.awt.Color;
import java.awt.Image;

import java.awt.Graphics;

public class Character extends Entity {

	private KeyBindings keyBindings;

	/**Nombre de vies (en moities de coeur) */
	private int lives = 6;

	// Booleens d'actions
	private ActionBooleans actionBooleans = new ActionBooleans();


	// Booleens de positions
	private boolean isOnLeftSide;

	// Couleur et image du personnage
	private Color colorCharacter;
	private Image imageCharacter = null;



	public Character(int x, int y, Color colorCharacter, Image imageCharacter, KeyBindings keyBindings) {
		super(x, y, 0, 0, 0, 0);
		this.colorCharacter = colorCharacter;
		this.imageCharacter = imageCharacter;
		this.keyBindings = keyBindings;
	}


	public Character(int x, int y, Color colorCharacter, Image imageCharacter) {
		this(x, y, colorCharacter, imageCharacter, null);
	}


	public Character(int x, int y, Color colorCharacter, KeyBindings keyBindings) {
		this(x, y, colorCharacter, null, keyBindings);
	}


	/** Applique une vitesse et une acceleration initiales au personnage pour sauter */
	public void jump() {
		if (actionBooleans.canJump) {
			// Vitesse initiale du saut
			setSpeed(0, 10);
			// Gravite
			setAcceleration(0, -10);

			// On ne peut pas resauter en l'air
			actionBooleans.canJump = false;
			// On peut switch uniquement en l'air
			actionBooleans.canSwitch = true;
		}
	}


	/** Creer une entite Projectile */
	public void shoot() {
		if (actionBooleans.canShoot) {
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
		int x = (int)((double)(this.x - boardGraphism.getReal().getCharacterWidth() / 2) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double)(boardGraphism.getMaxY() - (this.y + boardGraphism.getReal().getCharacterHeight())) * boardGraphism.getGraphic().getOneUnityHeight());
		int width = (int)((double)(boardGraphism.getReal().getCharacterWidth()) * boardGraphism.getGraphic().getOneUnityWidth());
		int height = (int)((double)(boardGraphism.getReal().getCharacterHeight()) * boardGraphism.getGraphic().getOneUnityHeight());
		g.fillRect(x, y, width, height);
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public KeyBindings getKeyBindings() {
		return keyBindings;
	}
	public ActionBooleans getActionBooleans() {
		return actionBooleans;
	}


	public class ActionBooleans {

		// Booleens de pression sur les touches / (de demande d'actions)
		private boolean jumpPressed = false;
		private boolean leftPressed = false;
		private boolean rightPressed = false;
		private boolean grabPressed = false;
		private boolean shieldPressed = false;
		private boolean shootPushPressed = false;
		private boolean switchPressed = false;


		// Booleens d'autorisation d'actions
		private boolean canJump;
		private boolean canLeft;
		private boolean canRight;
		private boolean canGrab;
		private boolean canShield;
		private boolean canShoot;
		private boolean canPush;
		private boolean canSwitch;


		// Booleens d'actions en cours


		// Getters et Setters des Booleens de pression sur les touches / (de demande d'actions)
		public boolean isJumpPressed() {
			return jumpPressed;
		}
		public void setJumpPressed(boolean jumpPressed) {
			this.jumpPressed = jumpPressed;
		}
		public boolean isLeftPressed() {
			return leftPressed;
		}
		public void setLeftPressed(boolean leftPressed) {
			this.leftPressed = leftPressed;
		}
		public boolean isRightPressed() {
			return rightPressed;
		}
		public void setRightPressed(boolean rightPressed) {
			this.rightPressed = rightPressed;
		}
		public boolean isGrabPressed() {
			return grabPressed;
		}
		public void setGrabPressed(boolean grabPressed) {
			this.grabPressed = grabPressed;
		}
		public boolean isShieldPressed() {
			return shieldPressed;
		}
		public void setShieldPressed(boolean shieldPressed) {
			this.shieldPressed = shieldPressed;
		}
		public boolean isShootPushPressed() {
			return shootPushPressed;
		}
		public void setShootPushPressed(boolean shootPushPressed) {
			this.shootPushPressed = shootPushPressed;
		}
		public boolean isSwitchPressed() {
			return switchPressed;
		}
		public void setSwitchPressed(boolean switchPressed) {
			this.switchPressed = switchPressed;
		}

	}
}