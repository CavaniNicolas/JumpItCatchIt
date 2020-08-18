import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
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
	protected int speedVertical = 45;


	/**Projectiles du joueur */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	/**Couleur des projectiles */
	private Color colorProjectile = Color.orange; // Sera a initialiser
	private Image imageProjectile = null;


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

		// On peut resauter et se deplacer quand on est au sol, et on ne peut pas sauter ou switch
		if (y == minY && isFalling == false) {
			actionBooleans.canJump = true;
			actionBooleans.isJumping = false;
			actionBooleans.canSwitch = false;

			// Au sol on peut se deplacer
			actionBooleans.canLeft = true;
			actionBooleans.canRight = true;

			// Pour le switch
			actionBooleans.isJumpFirstReleaseDone = false;
			actionBooleans.canActivateCanSwitch = true;
			actionBooleans.isSwitching = false;
		}

		// Si on peut activer le canSwitch (gestion des pressions des touches) et qu'il n'a pas encore ete active, on l'active
		if (actionBooleans.isJumpFirstReleaseDone && actionBooleans.canActivateCanSwitch) {
			actionBooleans.canSwitch = true;
			actionBooleans.canActivateCanSwitch = false;
		}

		// On ne peut pas se deplacer lateralement lors d'un switch
		if (actionBooleans.isSwitching) {
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;
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
		if (x < boardGraphism.getReal().getPlatformWidth() + (boardGraphism.getReal().getCharacterWidth() / 2) ) {
			isOnLeftPlatform = true;
			isOnRightPlatform = false;
			isFalling = false;
		// Si on est sur la plateforme de droite
		} else if (x > boardGraphism.getMaxX() - boardGraphism.getReal().getPlatformWidth() - boardGraphism.getReal().getCharacterWidth() / 2) {
			isOnLeftPlatform = false;
			isOnRightPlatform = true;
			isFalling = false;
		// Si on est sur aucune plateforme
		} else {
			// On est sur aucune plateforme
			isOnLeftPlatform = false;
			isOnRightPlatform = false;
			// Si on tombe
			if (y <= boardGraphism.getReal().getPlatformHeight()) {
				isFalling = true;
			} else {
				isFalling = false;
			}
		}

	}


	/** Actualise les coordonnees de collisions maximales et minimales */
	public void updateCollisionBorders(BoardGraphism boardGraphism, Character otherCharacter) {

		updatePositionBooleans(boardGraphism, otherCharacter);

		int halfCharacterWidth = boardGraphism.getReal().getCharacterWidth() / 2;

		// Si on est sur une plateforme, on determine le sol
		if (isOnLeftPlatform || isOnRightPlatform) {
			minY = boardGraphism.getReal().getGroundLevelYCoord();
		} else {
			// Si on est pas sur une plateforme il faut tomber
			minY = -140;
			
			if (isFalling) {
				accelY = GRAVITY;
			}
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

			// Supprime la collisions entre les joueurs lors d'un switch d'un des joueurs
			if (actionBooleans.isSwitching || otherCharacter.getActionBooleans().isSwitching) {
				minX = halfCharacterWidth;
				maxX = boardGraphism.getMaxX() - halfCharacterWidth;
			}

		// Si on tombe au milieu, les bords sont les plateformes
		} else {
			minX = boardGraphism.getReal().getPlatformWidth() + halfCharacterWidth;
			maxX = boardGraphism.getMaxX() - boardGraphism.getReal().getPlatformWidth() - halfCharacterWidth;
		}

	}



	/**Actualise la position du personnage (selon X et Y) puis le deplace (le deplacement gere les collisions grace aux collision borders */
	public void updatePosition(BoardGraphism boardGraphism, Character otherCharacter) {
		
		// Si on tombe et qu'on a touché le fond, on replace le personnage sur la plateforme disponible
		if (isFalling && y == minY) {
			replacePlayer(boardGraphism, otherCharacter);
			updateCollisionBorders(boardGraphism, otherCharacter);
		}

		checkMovement();
		checkJump();
		checkSwitch();

		this.move();
	}


	/**Repositionne le joueur sur la plateforme disponible si il est tombe dans le vide */
	public void replacePlayer(BoardGraphism boardGraphism, Character otherCharacter) {

		// On ne tombe plus
		isFalling = false;

		// Il faudra attendre d'etre sur le sol avant de pouvoir resauter et switch
		actionBooleans.isJumping = false;
		actionBooleans.isSwitching = false;
		actionBooleans.canJump = false;
		actionBooleans.canSwitch = false;

		// Si la plateforme de gauche est libre
		if (otherCharacter.isOnLeftPlatform == false) {
			x = boardGraphism.getReal().getSecondaryXcoordLeft();

			isOnLeftPlatform = true;
		// Si la plateforme de droite est libre
		} else {
			x = boardGraphism.getReal().getSecondaryXcoordRight();
			isOnRightPlatform = true;
		}

		//  hauteur du spawn
		y = boardGraphism.getReal().getGroundLevelYCoord() * 3;

		// Reset des vitesses accelerations
		speedX = 0;
		speedY = 0;
		accelX = 0;
		accelY = GRAVITY / 2;

	}


	/**Applique la vitesse pour effectuer un deplacement lateral */
	private void checkMovement() {

		// Applique une vitesse initiale au personnage pour se deplacer lateralement
		if (actionBooleans.leftPressed && actionBooleans.canLeft) {
			this.speedX = - this.speedLateral;

		} else if (actionBooleans.rightPressed && actionBooleans.canRight) {
			this.speedX = this.speedLateral;

		} else {
			this.speedX = 0;
		}
	}


	/**Applique les vitesses et accelerations pour effectuer un saut */
	private void checkJump() {

		// Applique une vitesse et une acceleration initiales au personnage pour sauter
		if (actionBooleans.jumpPressed && actionBooleans.canJump && isFalling == false) {
			// Vitesse initiale du saut
			this.speedY = this.speedVertical;
			// Gravite
			this.accelY = GRAVITY;

			// On est en train de sauter
			actionBooleans.isJumping = true;
			// On ne peut pas resauter en l'air
			actionBooleans.canJump = false;
		}

	}


	/**Applique les vitesses et accelerations pour effectuer un switch */
	private void checkSwitch() {

		// Si on appuie sur sauter pendant qu'on est en l'air, on switch
		if (actionBooleans.isJumping && actionBooleans.jumpPressed && actionBooleans.canSwitch && isFalling == false) {

			// Gravite
			this.accelY = GRAVITY;
			// Propulsion
			this.speedY = this.speedVertical / 2;
			if (isOnLeftPlatform) {
				this.accelX = 30;
			}
			if (isOnRightPlatform) {
				this.accelX = -30;
			}

			// On est en train de switch, on ne peux plus effectuer un switch
			actionBooleans.isSwitching = true;
			actionBooleans.canSwitch = false;
		}
	}


	/**Verifie et Lance les actions a effectuer (grab shield shoot push) */
	public void checkActions(BoardGraphism boardGraphism) {
		checkShoot(boardGraphism);
	}


	/** Creer une entite Projectile */
	public void checkShoot(BoardGraphism boardGraphism) {

		/**Si on appuie sur Shoot et qu'on peut shoot */
		if (actionBooleans.shootPushPressed && actionBooleans.canShoot) {

			System.out.println("FIRE");
			// Tire vers la droite
			if (isOnLeftSide) {
				projectiles.add(new Projectile(x, y + boardGraphism.getReal().getCharacterHeight() / 2, 1, 0, 1, 0, 1, this, colorProjectile) ); // Attention a revoir !
			// Tire vers la gauche
			} else {
				projectiles.add(new Projectile(x, y + boardGraphism.getReal().getCharacterHeight() / 2, -1, 0, -1, 0, 1, this, colorProjectile) );
			}

			// On ne peut plus shoot tout de suite
			actionBooleans.canShoot = false; // Add un timer
		}	
	}


	/** Deplace les projectiles */
	public void moveProjectiles() {
		for (int i=0; i<projectiles.size(); i++) {
			projectiles.get(i).move();
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


	/**Dessine les projectiles de ce personnage */
	public void drawProjectiles(Graphics g, BoardGraphism boardGraphism) {
		for (int i=0; i<projectiles.size(); i++) {
			projectiles.get(i).drawProjectile(g, boardGraphism);
		}
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
		private boolean canShoot = true;
		private boolean canPush;
		private boolean canSwitch;

		/**Booleen permettant dactiver canSwitch (en fonction des pression sur Jump) */
		private boolean canActivateCanSwitch;
		private boolean isJumpFirstReleaseDone;

		// Booleens d'actions en cours
		private boolean isJumping;
		private boolean isSwitching;


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
		public boolean isCanActivateCanSwitch() {
			return canActivateCanSwitch;
		}
		public void setCanActivateCanSwitch(boolean canActivateCanSwitch) {
			this.canActivateCanSwitch = canActivateCanSwitch;
		}
		public boolean isJumping() {
			return isJumping;
		}
		public void setIsJumping(boolean isJumping) {
			this.isJumping = isJumping;
		}
		public boolean isCanSwitch() {
			return canSwitch;
		}
		public void setCanSwitch(boolean canSwitch) {
			this.canSwitch = canSwitch;
		}
		public boolean isJumpFirstReleaseDone() {
			return isJumpFirstReleaseDone;
		}
		public void setIsJumpFirstReleaseDone(boolean isJumpFirstReleaseDone) {
			this.isJumpFirstReleaseDone = isJumpFirstReleaseDone;
		}

	}
}