package Game.Gameplay;

import Game.InputActions;
import Game.Gameplay.Items.ItemBall;
import Game.ConstantsContainers.GraphicConstants.MainConstants;
import Game.ConstantsContainers.GraphicConstants.CharacterConstants;
import Game.ConstantsContainers.GraphicConstants.GrabConstants;
import Game.ConstantsContainers.GraphicConstants.ProjectileConstants;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

/** Class Character <p>
 * contient le personnage, toutes ses actions, positions, etats... <p>
 * La position (x,y) du Character est en bas au milieu du rectangle */
public class Character extends Entity {
	private static final long serialVersionUID = 4168430883476917014L;

	/**
	 * Permet de distinguer les deux persos, l'un est celui de gauche au depart et
	 * l'autre celui de droite
	 */
	private transient boolean isLeftCharacter; // Pourrait etre remplace par un ID

	/**Nombre de vies max (en moities de coeur) */
	private int livesMax = 6;
	/**Nombre de vies actuel */
	private int lives = livesMax;

	/** Booleens d'actions */
	private transient ActionBooleans actionBooleans = new ActionBooleans();

	/** Booleens d'Input */
	private transient InputActions inputActions;

	/** HUD du personnage */
	private transient HUDCharacter hudCharacter;

	/** Booleen de position, a gauche ou a droite de son adversaire */
	private transient boolean isOnLeftSide;
	/** Booleen de position, sur la plateforme de gauche */
	private transient boolean isOnLeftPlatform;
	/** Booleen de position, sur la plateforme de droite */
	private transient boolean isOnRightPlatform;
	/**Si les personnages sont a la meme hauteur ou non */
	private transient boolean areOnSameY;
	/**Si les personnages sont l'un dans l'autre en X */
	private transient boolean areOnSameXCollisions;
	/**Vitesse de decollision */
	private transient int decollisionSpeed = 30;


	/**Largeur de la hitbox selon X */
	private transient int hitboxWidth = 800; //this.width / 2;

	/** Booleen, true si on est en train de tomber dans le vide */
	private transient boolean isFalling;
	/** Booleen, true si on est en train de respawn */
	private transient boolean isSpawning;

	// Couleur et image du personnage
	private Color colorCharacter;
	private transient Image imageCharacter = null;


	/** Vitesse Laterale Constante */
	protected transient int speedLateral = 40;
	/** Vitesse Horizontale Constante */
	protected transient int speedVertical = 450;
	/** Vitesse a appliquer a speedX pour le switch */
	protected transient int switchSpeed = 295;


	/**Projectiles du joueur */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	/**Vitesse des projectiles */
	private transient int speedProjectile = 100;
	/**Range des projectiles */
	private transient int rangeProjectile = 12_000; // Peut etre mieux de la calculer en pourcentage par rapport a la taille maxX du board
	/**Degats des projectiles */
	private transient int damageProjectile = 1;
	/**Couleur des projectiles */
	private Color colorProjectile = Color.orange; // Sera a initialiser
	/**Image des projectiles */
	private transient Image imageProjectile = null;

	/**Cool Down pour lancer un projectile (en milli secondes) */
	private transient long coolDownProjectile = 1_500;
	/** Moment auquel on lance un projectile */
	private transient long startTimeProjectile = 0;


	/**Classe contenant le grab du joueur */
	private GrabSpell grabSpell = new GrabSpell();
	/**Vitesse du grab */
	private transient int speedGrab = 400;
	/**Range du grab */
	private transient int rangeGrab = 4_000;
	/**Image du grab */
	private transient Image imageGrab = null;

	/**Cool Down pour le grab (en milli secondes) */
	private transient long coolDownGrab = 1_000;
	/** Moment auquel on effectue un grab */
	private transient long startTimeGrab = 0;



	/**Constructeur Character */
	public Character(int x, int y, boolean isLeftCharacter, Color colorCharacter, Image imageCharacter, InputActions inputActions, CharacterConstants CCReal) {
		super(x, y, 0, 0, 0, 0);
		this.isLeftCharacter = isLeftCharacter;
		this.colorCharacter = colorCharacter;
		this.imageCharacter = imageCharacter;
		this.inputActions = inputActions;
		initGraphicAttributes(CCReal);
		//initHUDCharacter(boardGraphism);
	}


	/**Constructeur Character sans Image */
	public Character(int x, int y, boolean isLeftCharacter, Color colorCharacter, InputActions inputActions, CharacterConstants CCReal) {
		this(x, y, isLeftCharacter, colorCharacter, null, inputActions, CCReal);
	}



	/** Actualise les booleens d'actions */
	public void updateActionBooleans(Character otherCharacter) {
		
		// Verifie le relachement de la touche saut lorsqu'on est en l'air pour autoriser le switch sur un autre appuie de la touche saut
		checkKeyToUpdateCanSwitch();

		// On verifie les coolDown des sorts
		checkCoolDowns();

		// Au sol on peut se deplacer, et on est plus en train de spawner
		if (y == minY && isFalling == false) {			
			actionBooleans.canLeft = true;
			actionBooleans.canRight = true;
			
			isSpawning = false;

			// Permet de supprimer la vitesse et l'acceleration en X recues apres un switch
			speedX = 0;
			accelX = 0;
		}

		// Si on appuie en meme temps sur gauche et sur droite, on ne bouge pas
		if (inputActions.getLeftPressed() && inputActions.getRightPressed()) {
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;
		} else {
			actionBooleans.canLeft = true;
			actionBooleans.canRight = true;
		}

		// Pendant qu'un perso respawn il ne peut pas bouger
		if (isSpawning) {
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;

			actionBooleans.canShoot = false;
			actionBooleans.canGrab = false;
		}


		// Si on est au bord des collisions, on ne peut pas s'enfoncer plus
		/* Peut etre pas tres utile ... on verra */
		if (x <= minX) {
			actionBooleans.canLeft = false;
		}
		if (x >= maxX) {
			actionBooleans.canRight = false;
		}

		// On peut resauter quand on est au sol, et on ne peut pas sauter ou switch
		if (y == minY && isFalling == false) {
			actionBooleans.canJump = true;
			actionBooleans.isJumping = false;
			actionBooleans.canSwitch = false;

			// Pour le switch
			actionBooleans.isJumpFirstReleaseDone = false;
			actionBooleans.canActivateCanSwitch = true;
			actionBooleans.isSwitching = false;
		}

		// Pendant que les persos sont en collision forte, ils ne peuvent pas se deplacer et sauter et switch
		if (areOnSameXCollisions) {
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;
			actionBooleans.canJump = false;
			actionBooleans.canSwitch = false;
		}


		// Si on peut activer le canSwitch (gestion des pressions des touches) et qu'il n'a pas encore ete active, on l'active
		if (actionBooleans.isJumpFirstReleaseDone && actionBooleans.canActivateCanSwitch) {
			actionBooleans.canSwitch = true;
			actionBooleans.canActivateCanSwitch = false;
		}

		// Pendant un switch
		if (actionBooleans.isSwitching) {
			// On ne peut pas se deplacer lateralement
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;
			
			// On ne peut pas grab
			actionBooleans.canGrab = false;

			// On ne peut pas tirer
			actionBooleans.canShoot = false;
		}

		// Pendant un grab
		if (actionBooleans.isGrabing) {
			// On ne peut pas tirer
			actionBooleans.canShoot = false;

			// On ne peut pas switch
			actionBooleans.canSwitch = false;
		}

		// On ne peut pas shoot si les deux joueurs sont sur la meme plateforme (pour les deux joueurs)
		if ((isOnLeftPlatform && otherCharacter.isOnLeftPlatform) ||
			(isOnRightPlatform && otherCharacter.isOnRightPlatform)) {

			actionBooleans.canShoot = false;
		}

		// Si les deux persos sont sur la meme plateforme, celui qui est le plus loin des items ne peut pas grab
		if ((isOnLeftPlatform && otherCharacter.isOnLeftPlatform && isOnLeftSide) ||
			(isOnRightPlatform && otherCharacter.isOnRightPlatform && isOnLeftSide == false)) {

			actionBooleans.canGrab = false;
		}

	}


	/**Verifie les cool downs et active les booleens */
	public void checkCoolDowns() {

		// Pour les projectiles
		if (System.currentTimeMillis() - startTimeProjectile >= coolDownProjectile) {
			actionBooleans.canShoot = true;
		}

		// Pour le grab
		if (System.currentTimeMillis() - startTimeGrab >= coolDownGrab) {
			actionBooleans.canGrab = true;
		}

	}


	/** Actualise les booleens de position */
	public void updatePositionBooleans(MainConstants MCReal, CharacterConstants CCReal, Character otherCharacter) {

		// Si on est a gauche ou a droite de son adversaire
		if (x == otherCharacter.x) {
			if (isLeftCharacter) {
				isOnLeftSide = true;
			} else {
				isOnLeftSide = false;
			}
		} else if (x < otherCharacter.x) {
			isOnLeftSide = true;
		} else {
			isOnLeftSide = false;
		}

		// Si on est sur la plateforme de gauche
		if (x < MCReal.getPlatformWidth() + (CCReal.getCharacterWidth() / 2) ) {
			isOnLeftPlatform = true;
			isOnRightPlatform = false;
			isFalling = false;
		// Si on est sur la plateforme de droite
		} else if (x > MCReal.getMaxX() - MCReal.getPlatformWidth() - CCReal.getCharacterWidth() / 2) {
			isOnLeftPlatform = false;
			isOnRightPlatform = true;
			isFalling = false;
		// Si on est sur aucune plateforme
		} else {
			// On est sur aucune plateforme
			isOnLeftPlatform = false;
			isOnRightPlatform = false;
			// Si on tombe
			if (y <= MCReal.getPlatformHeight()) {
				isFalling = true;
			} else {
				isFalling = false;
			}
		}


		// Si les personnages sont a la meme hauteur ou pas
		if ( (y >= otherCharacter.y && y <= otherCharacter.y + height) || 
			(y + height >= otherCharacter.y && y + height <= otherCharacter.y + height) ) {
				areOnSameY = true;
		} else {
			areOnSameY = false;
		}


		// Si personne ne switch, ou si les deux persos switch en meme temps, on active la collision forte selon X
		if (actionBooleans.isSwitching == otherCharacter.actionBooleans.isSwitching) {
			// Si les persos sont a la meme hauteur et qu'il y a une collision forte entre eux selon X
			// Si le perso est a gauche et est en collision avec l'autre (si on utilise >=, les persos peuvent se repousser en se deplacant)
			if (areOnSameY && isOnLeftSide && x > otherCharacter.x - hitboxWidth) {
				areOnSameXCollisions = true;

			// Si le perso est a droite et est en collision avec l'autre (si on utilise <=, les persos peuvent se repousser en se deplacant)
			} else if (areOnSameY && isOnLeftSide == false && x < otherCharacter.x + hitboxWidth) {
				areOnSameXCollisions = true;

			// Sinon les persos ne sont pas en collision
			} else {
				areOnSameXCollisions = false;
			}

		// Sinon un des persos est en train de switch, on desactive la collision selon X
		} else {
			areOnSameXCollisions = false;
		}


		// Annule la vitesse et l'acceleration en X si les personnages se collisionnent dans les airs (glitch lors des switch)
		if (areOnSameXCollisions) {
			speedX = 0;
			accelX = 0;
		}

	}


	/** Actualise les coordonnees de collisions maximales et minimales */
	public void updateCollisionBorders(MainConstants MCReal, CharacterConstants CCReal, Character otherCharacter) {

		updatePositionBooleans(MCReal, CCReal, otherCharacter);

		int halfCharacterWidth = CCReal.getCharacterWidth() / 2;
		int maxXBoard = MCReal.getMaxX();

		// Si on est sur une plateforme, on determine le sol
		if (isOnLeftPlatform || isOnRightPlatform) {
			minY = MCReal.getPlatformHeight();
		} else {
			// Si on est pas sur une plateforme il faut tomber
			minY = -10_000;
			
			if (isFalling) {
				accelY = GRAVITY;
			}
		}


		// Collisions Borders selon X
		// Les bords principaux sont les murs et le joueur adverse si on est sur la plateforme
		if (isFalling == false) {

			// Si les personnages sont a la meme hauteur il ne peuvant pas se traverser
			if (areOnSameY) {
				// Si le personnage est a gauche de l'autre personnage
				if (isOnLeftSide) {
					// Les limites sont le bord gauche du board et l'autre perso
					minX = halfCharacterWidth;
					maxX = otherCharacter.x - halfCharacterWidth;

				// Sinon le personnage est a droite de l'autre personnage
				} else {
					// Les limites sont le bord droit du board et l'autre perso
					minX = otherCharacter.x + halfCharacterWidth;
					maxX = maxXBoard - halfCharacterWidth;
				}

			// Sinon ils ne sont pas a la meme hauteur, ils peuvent se traverser
			} else {
				// Les limites sont les bords gauche et droit du board
				minX = halfCharacterWidth;
				maxX = maxXBoard - halfCharacterWidth;
			}


			// Supprime la collisions entre les joueurs lors d'un switch d'un des joueurs
			if (actionBooleans.isSwitching || otherCharacter.getActionBooleans().isSwitching) {
				minX = halfCharacterWidth;
				maxX = maxXBoard - halfCharacterWidth;
			}

		// Si on tombe au milieu, les bords sont les plateformes
		} else {
			minX = MCReal.getPlatformWidth() + halfCharacterWidth;
			maxX = maxXBoard - MCReal.getPlatformWidth() - halfCharacterWidth;
		}

	}



	/**Actualise la position du personnage (selon X et Y) puis le deplace (le deplacement gere les collisions grace aux collision borders */
	public void updatePosition(MainConstants MCReal, CharacterConstants CCReal, Character otherCharacter) {
		
		// Si on tombe et qu'on a touché le fond, on replace le personnage sur la plateforme disponible
		if (isFalling && y == minY) {
			replacePlayer(MCReal, CCReal, otherCharacter);
			updateCollisionBorders(MCReal, CCReal, otherCharacter);
		}

		checkMovement(otherCharacter);
		checkJump();
		checkSwitch();

		moveCharacter(otherCharacter);
	}


	/**Repositionne le joueur sur la plateforme disponible si il est tombe dans le vide */
	public void replacePlayer(MainConstants MCReal, CharacterConstants CCReal, Character otherCharacter) {

		// On respawn
		isSpawning = true;
		// On ne tombe plus
		isFalling = false;
		// On perd de la vie
		lives -= 1;

		// Il faudra attendre d'etre sur le sol avant de pouvoir resauter et switch et se deplacer
		actionBooleans.isJumping = false;
		actionBooleans.isSwitching = false;
		actionBooleans.canJump = false;
		actionBooleans.canSwitch = false;
		actionBooleans.canLeft = false;
		actionBooleans.canRight = false;

		// Si la plateforme de gauche est libre
		if (otherCharacter.isOnLeftPlatform == false) {
			x = CCReal.getSecondaryXcoordLeft();

			isOnLeftPlatform = true;
		// Si la plateforme de droite est libre
		} else {
			x = CCReal.getSecondaryXcoordRight();
			isOnRightPlatform = true;
		}

		//  hauteur du spawn
		y = MCReal.getPlatformHeight() * 3;

		// Reset des vitesses accelerations
		speedX = 0;
		speedY = 0;
		accelX = 0;
		accelY = GRAVITY / 6;

	}


	/**Applique la vitesse pour effectuer un deplacement lateral */
	private void checkMovement(Character otherCharacter) {

		// Applique une vitesse initiale au personnage pour se deplacer lateralement
		if (inputActions.getLeftPressed() && actionBooleans.canLeft) {
			this.speedX = - this.speedLateral;

		} else if (inputActions.getRightPressed() && actionBooleans.canRight) {
			this.speedX = this.speedLateral;

		} else {
			this.speedX = 0;
		}


		// Permet deviter le tremblement des personnages si ils essayent de se deplacer alors quils sont cote a cote mais que leur position ne touche pas leur minX ou maxX
		if (x + speedX > maxX || x + speedX < minX) {
			speedX = 0;
		}
	}


	/**Applique les vitesses et accelerations pour effectuer un saut */
	private void checkJump() {

		// Applique une vitesse et une acceleration initiales au personnage pour sauter
		if (inputActions.getJumpPressed() && actionBooleans.canJump && isFalling == false) {
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
		if (actionBooleans.isJumping && inputActions.getJumpPressed() && actionBooleans.canSwitch && isFalling == false) {

			// Gravite
			this.accelY = GRAVITY;
			// Propulsion
			this.speedY = this.speedVertical / 2;
			if (isOnLeftPlatform) {
				this.accelX = this.switchSpeed;
			}
			if (isOnRightPlatform) {
				this.accelX = -this.switchSpeed;
			}

			// On est en train de switch, on ne peux plus effectuer un switch
			actionBooleans.isSwitching = true;
			actionBooleans.canSwitch = false;
		}
	}


	/**Verifie et Lance les actions a effectuer (grab shield shoot push) */
	public void checkActions(MainConstants MCReal, ProjectileConstants PCReal, GrabConstants GCReal, CharacterConstants CCReal) {
		checkShoot(MCReal, PCReal);
		checkGrab(MCReal, GCReal, CCReal);
	}


	/** Creer une entite Projectile */
	public void checkShoot(MainConstants MCReal, ProjectileConstants PCReal) {

		/**Si on appuie sur Shoot et qu'on peut shoot */
		if (inputActions.getShootPushPressed() && actionBooleans.canShoot) {

			// Tire vers la droite
			if (isOnLeftSide) {
				projectiles.add(new Projectile(x + (this.width / 2), y + (this.height / 2), speedProjectile,
									MCReal, PCReal, rangeProjectile, damageProjectile, colorProjectile) );
			// Tire vers la gauche
			} else {
				projectiles.add(new Projectile(x - (this.width) / 2, y + (this.height / 2), - speedProjectile,
									MCReal, PCReal, rangeProjectile, damageProjectile, colorProjectile) );
			}

			// On ne peut plus shoot tout de suite
			actionBooleans.canShoot = false;
			startTimeProjectile = System.currentTimeMillis();
		}	
	}


	/**Lance un Grab pour attraper un objet */
	public void checkGrab(MainConstants MCReal, GrabConstants GCReal, CharacterConstants CCReal) {

		boolean launchGrabDir = true;

		// Si on appuie sur grab et qu'on peut grab
		if (inputActions.getGrabPressed() && actionBooleans.canGrab) {

			// Si on est sur la plateforme de gauche
			if (isOnLeftPlatform) {
				// Lance un grab vers la droite
				launchGrabDir = true;

			// Si on est sur la plateforme de droite
			} else if (isOnRightPlatform) {
				// Lance un grab vers la gauche
				launchGrabDir = false;

			// Si on est au dessus du vide
			} else {
				// Tomber de la meme maniere qu'un switch au dessus du vide
			}

			// ERREUR SUR LAUNCHGRABDIR : mieux vaut utiliser des int code pour les different cas, pour le moment on lance vers la droite par defaut
			grabSpell.initNewGrab(x, y, launchGrabDir, rangeGrab, speedGrab, GCReal, CCReal);

			actionBooleans.isGrabing = true;

			// On ne peut plus grab tout de suite
			actionBooleans.canGrab = false;
			startTimeGrab = System.currentTimeMillis();

		}

	}


	/**Deplace le personnage */
	public void moveCharacter(Character otherCharacter) {

		// Si il y a collisions trop importante entre les perso, il faut les ecarter lentement chacun l'un de l'autre
		if (areOnSameXCollisions) {
			//Gestion des X
			if (isOnLeftSide) {
				x -= decollisionSpeed;
			} else {
				x += decollisionSpeed;
			}

			//Gestion des Y
			this.moveY();

		// Si les deux perso ne sont pas en collisions, on se deplace normalement
		} else {
			this.moveXY();
		}

	}


	/**Deplace le grab avec son joueur */
	public void moveGrab(CharacterConstants CCReal) {
		grabSpell.moveGrabWithCharacter(x, y, CCReal);
	}


	/** Deplace les projectiles */
	public void moveProjectiles() {
		Projectile proj;
		for (int i=0; i<projectiles.size(); i++) {
			proj = projectiles.get(i);

			proj.moveX();
			if (proj.x == proj.minX || proj.x == proj.maxX) {
				projectiles.remove(i);
			}
		}
	}


	/** Verifie la collision des projectiles et inflige des degats et les fait disparaitre */
	public void checkProjectilesCollision(Character otherCharacter) {
		Projectile proj;

		for (int i=0; i<projectiles.size(); i++) {
			proj = projectiles.get(i);

			// Si les projectiles de ce personnage touchent l'adversaire
			if (proj.checkCharacterCollision(otherCharacter)) {
				// Il faut supprimer ce projectile
				projectiles.remove(i);
			}
		}
	}


	/** Etend le grab si le personnage est en train de grab */
	public void stretchGrab() {

		// Si on est en train de grab
		if (actionBooleans.isGrabing) {
			grabSpell.stretchGrab();
		}

		// Verifie si on a finit de grab
		if (grabSpell.isGrabFinished()) {
			actionBooleans.isGrabing = false;
		}

	}


	/** Verifie les collisions du grab avec les items */
	public void checkGrabCollision(ArrayList<ItemBall> itemBalls) {
		ItemBall itemBall = null;

		for (int i=0; i<itemBalls.size(); i++) {
			itemBall = itemBalls.get(i);

			if (itemBall != null) {

				// renvoie true si le grab touche un item
				if (grabSpell.checkItemCollision(itemBall.x, itemBall.y, itemBall.width)) {
					itemBall.effects(this);
					itemBalls.remove(i);
				}
			}

		}

	}


	/**Dessine le personnage */
	public void drawCharacter(Graphics g, MainConstants MC, CharacterConstants CC) {
		g.setColor(colorCharacter);
		int x = (int)((double)(this.x - this.width / 2) * MC.getOneUnityWidth());
		int y = (int)((double)(MC.getReal().getMaxY() - (this.y + this.height)) * MC.getOneUnityHeight());
		int width = CC.getCharacterWidth();
		int height = CC.getCharacterHeight();
		g.fillRect(x, y, width, height);
	}


	/**Dessine les projectiles de ce personnage */
	public void drawProjectiles(Graphics g, MainConstants MC, ProjectileConstants PC) {
		for (int i=0; i<projectiles.size(); i++) {
			projectiles.get(i).drawProjectile(g, MC, PC);
		}
	}


	/**Dessine le grab du ce personnage */
	public void drawGrab(Graphics g, MainConstants MC, GrabConstants GC) {
		grabSpell.drawGrab(g, MC, GC);
	}


	/**Initialise les champs graphiques */
	public void initGraphicAttributes(CharacterConstants CCReal) {
		this.width = CCReal.getCharacterWidth();
		this.height = CCReal.getCharacterHeight();
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public ActionBooleans getActionBooleans() {
		return actionBooleans;
	}
	public int getLives() {
		return lives;
	}
	public int getLivesMax() {
		return livesMax;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}

	public InputActions getInputActions() {
		return inputActions;
	}

	public void setInputActions(InputActions inputActions) {
		this.inputActions = inputActions;
	}


	/** Verifie le relachement de la touche saut lorsqu'on est en l'air pour autoriser le switch sur un autre appuie de la touche saut */
	public void checkKeyToUpdateCanSwitch() {
		// Si bouton sauter est relache
		if (inputActions.getJumpPressed() == false) {
			// Active le booleens qui permet dactiver le canSwitch si on est dans les airs et qu'on
			// relache le bouton sauter (pour pouvoir rappuyer dessus dans les airs pour switch)
			if (actionBooleans.isJumping == true && actionBooleans.isJumpFirstReleaseDone == false) {
				actionBooleans.isJumpFirstReleaseDone = true;
			}
		}
	}


	public class ActionBooleans {

		// Booleens de pression sur les touches / (de demande d'actions)
		private boolean switchPressed = false;

		// Booleens d'autorisation d'actions
		private boolean canJump = true;
		private boolean canLeft = true;
		private boolean canRight = true;
		private boolean canGrab = true;
		private boolean canShield = true;
		private boolean canShoot = true;
		private boolean canPush = false;
		private boolean canSwitch = false;

		/**Booleen permettant dactiver canSwitch (en fonction des pression sur Jump) */
		private boolean canActivateCanSwitch = false;
		private boolean isJumpFirstReleaseDone = false;

		// Booleens d'actions en cours
		private boolean isJumping = false;
		private boolean isSwitching = false;
		private boolean isGrabing = false;


		// Getters et Setters des Booleens de pression sur les touches / (de demande d'actions)
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

	@Override
	public String toString() {
		return "Character [isSpawning=" + isSpawning + ", lives=" + lives
				+ ", livesMax=" + livesMax + ", grabSpell" + grabSpell + ", projectiles=" + projectiles + " " + super.toString() + "]";
	}

}