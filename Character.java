import java.awt.Color;
import java.awt.Image;

import java.awt.Graphics;

public class Character extends Entity {

	private KeyBindings keyBindings;

	/**Nombre de vies (en moities de coeur) */
	private int lives = 6;

	/** Booleens d'actions */
	private ActionBooleans actionBooleans = new ActionBooleans();


	/** Booleen de position, a gauche ou a droite de son adversaire */
	private boolean isOnLeftSide;
	/** Booleen de position, sur la plateforme de gauche */
	private boolean isOnLeftPlatform;
	/** Booleen de position, sur la plateforme de droite */
	private boolean isOnRightPlatform;

	/** Booleen, true si on est en train de tomber dans le vide */
	private boolean isFalling;

	// Couleur et image du personnage
	private Color colorCharacter;
	private Image imageCharacter = null;


	/** Vitesse Laterale Constante */
	protected int speedLateral = 4; //temporaire, a mettre dans Entity plus tard
	/** Vitesse Horizontale Constante */
	protected int speedVertical = 50;



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


	/** Actualise les coordonnees de collisions maximales et minimales */
	public void updateCollisionBorders(BoardGraphism boardGraphism, Character otherCharacter) {

		updatePositionBooleans(boardGraphism, otherCharacter);

		int halfCharacterWidth = boardGraphism.getReal().getCharacterWidth() / 2;

		// Si on est sur une plateforme, on determine le sol
		if (isOnLeftPlatform || isOnRightPlatform) {
			minY = boardGraphism.getReal().getPlatformHeight();
		} else {
			minY = 0;
			accelY = GRAVITY;
		}

		// Collisions Borders selon X
		// Les bords principaux sont les murs et le joueur adverse si on est sur la plateforme
		if (isFalling == false) {
			if (isOnLeftSide) { // Il faudra peut etre traiter ces cas aussi par rapport a si l'adversaire est sur sa plateforme ou pas
				minX = halfCharacterWidth;
				maxX = otherCharacter.x - halfCharacterWidth;

			} else {
				minX = otherCharacter.x + halfCharacterWidth;
				maxX = boardGraphism.getMaxX() - halfCharacterWidth;
			}

		// Si on tombe au milieu, les bords sont les plateformes
		} else {
			minX = boardGraphism.getReal().getPlatformWidth() + boardGraphism.getReal().getCharacterWidth() / 2;
			maxX = boardGraphism.getMaxX() - boardGraphism.getReal().getPlatformWidth() - boardGraphism.getReal().getCharacterWidth() / 2;
		}

	}


	/** Actualise les booleens de position */
	public void updatePositionBooleans(BoardGraphism boardGraphism, Character otherCharacter) {

		// Si on est a gauche ou a droite de son adversaire
		if (x < otherCharacter.x) {
			isOnLeftSide = true;
		} else {
			isOnLeftSide = false;
		}

		// Si on est sur la plateforme de gauche
		if (x < boardGraphism.getReal().getPlatformWidth() + boardGraphism.getReal().getCharacterWidth() / 2) {
			isOnLeftPlatform = true;
			isOnRightPlatform = false;
			isFalling = false;
		// Si on est sur la plateforme de droite
		} else if (x > boardGraphism.getMaxX() - boardGraphism.getReal().getPlatformWidth() - boardGraphism.getReal().getCharacterWidth() / 2) {
			isOnLeftPlatform = false;
			isOnRightPlatform = true;
			isFalling = false;
		} else {
			// Si on est sur aucune plateforme et Si on tombe
			if (y <= boardGraphism.getReal().getPlatformHeight()) {
				isFalling = true;
				isOnLeftPlatform = false;
				isOnRightPlatform = false;
			} else {
				isFalling = false;
			}
		}


		// ICI : Si "falling == true" (cf updateCollisionBorders()) pensez a mettre canJump a false
		// (peut etre aussi canLeft et canRight si on effectue une projection vers le vide),
		// ICI : Si "falling == true" et qu'on touche le minY,
		// alors on repositionne le personnage sur la plateforme et on lui fait perdre de la vie

	}


	/** Actualise les booleens d'actions */
	public void updateActionBooleans() {

		// Si on appuie en meme temps sur gauche et sur droite, on ne bouge pas
		if (actionBooleans.leftPressed && actionBooleans.rightPressed) {
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;
		} else {
			actionBooleans.canLeft = true;
			actionBooleans.canRight = true;
		}

		// Si on est au bord des collisions, on ne peut pas s'enfoncer plus
		// Peut etre pas tres utile ... on verra (c'etait pour essayer de supprimer le tremblement quand les joueurs se foncent dedans)
		if (x <= minX) {
			actionBooleans.canLeft = false;
		}
		if (x >= maxX) {
			actionBooleans.canRight = false;
		}

		// On peut resauter une fois quon a atterit, et on ne peut plus switch
		if (y == minY) {
			actionBooleans.canJump = true;
			actionBooleans.canSwitch = false;
		}

	}


	/**Actualise la position du personnage (selon X et Y) puis le deplace (le deplacement gere les collisions grace aux collision borders */
	public void updatePosition() {
		updateXPosition();
		updateYPosition();
		// switch();

		move();
	}


	/**Actualise la position du personnage (selon X) (quand on est sur la plateforme) */
	private void updateXPosition() {

		// Applique une vitesse initiale au personnage pour se deplacer lateralement
		if (actionBooleans.leftPressed && actionBooleans.canLeft) {
			this.speedX = - this.speedLateral;

		} else if (actionBooleans.rightPressed && actionBooleans.canRight) {
			this.speedX = this.speedLateral;

		} else {
			this.speedX = 0;
		}
	}


	/**Actualise la position du personnage (selon Y) */
	private void updateYPosition() {

		// Applique une vitesse et une acceleration initiales au personnage pour sauter
		if (actionBooleans.jumpPressed && actionBooleans.canJump) {
			// Vitesse initiale du saut
			this.speedY = this.speedVertical;
			// Gravite
			this.accelY = GRAVITY;

			// On ne peut pas resauter en l'air
			actionBooleans.canJump = false;
			// On peut switch uniquement en l'air
			actionBooleans.canSwitch = true; // sera a modifer d'emplacement et potentielement de gameplay
		}


		// ICI : Appel des fonctions qui calculeront la force a appliquer au personnage pour effectuer le switch et application de ces forces au personnage

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
		private boolean canJump = true;
		private boolean canLeft = true;
		private boolean canRight = true;
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