package Game.Gameplay;

import Game.InputActions;
import Game.Gameplay.Items.ItemBall;
import Game.Gameplay.Items.ItemBalls;
import Game.ConstantsContainers.GraphicConstants.GraphicMainConstants;
import Game.ConstantsContainers.GameplayConstants.GameplayCharacterConstants;
import Game.ConstantsContainers.GraphicConstants.GraphicCharacterConstants;
import Game.ConstantsContainers.GraphicConstants.GraphicGrabConstants;

import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics;

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

	/** Constantes de Gameplay de Character */
	private GameplayCharacterConstants GameCC = new GameplayCharacterConstants();

	/** Booleens d'actions */
	private transient ActionBooleans actionBooleans = new ActionBooleans();

	/** Booleens d'Input */
	private transient InputActions inputActions;


	// Couleur et image du personnage // A SUPPRIMER DE CETTE CLASSE
	private Color colorCharacter;


	/** Nombre de vies actuel */
	private int lives;
	/** Vitesse Laterale actuelle du joueur */
	protected transient int speedLateral;
	/** Vitesse Verticale actuelle du joueur */
	protected transient int speedVertical;



	/** Booleen de position, a gauche ou a droite de son adversaire */
	private transient boolean isOnLeftSide;
	/** Booleen de position, sur la plateforme de gauche */
	private transient boolean isOnLeftPlatform;
	/** Booleen de position, sur la plateforme de droite */
	private transient boolean isOnRightPlatform;
	/** Si les personnages sont a la meme hauteur ou non */
	private transient boolean areOnSameY;
	/** Si les personnages sont l'un dans l'autre en X */
	private transient boolean areOnSameXCollisions;

	/** Booleen, true si on est en train de tomber dans le vide */
	private transient boolean isFalling;
	/** Booleen, true si on est en train de respawn */
	private transient boolean isSpawning;
	/** Booleen, true si on est en train detre pousse vers la gauche, false vers la droite
	 * Attention, a remplacer par un int code ou une enum, (peut etre le meme de direction que ceux des projectiles) */
	private transient boolean isBeingPushedLeft = true; // A REVOIR
	/** Booleen, true si on est invulnerable (au Shoot, au Push) */
	private transient boolean isImmuned = false;

	/** Projectiles du joueur */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	/** Vitesse des projectiles */
	private transient int speedProjectile;
	/** Range des projectiles */
	private transient int rangeProjectile;
	/** Degats des projectiles */
	private transient int damageProjectile;
	/** Rayon des projectiles*/
	private transient int radiusProjectile;


	/** Cool Down pour lancer un projectile (en milli secondes) */
	private transient long coolDownProjectile;
	/** Moment auquel on lance un projectile */
	private transient long startTimeProjectile = 0;


	/** Force pour pousser l'adversaire */
	private transient int pushStrength;
	/** Cool Down pour pousser (en milli secondes) */
	private transient long coolDownPush;
	/** Moment auquel on a pousser l'adversaire */
	private transient long startTimePush = 0;


	/** Classe contenant le grab du joueur */
	private GrabSpell grabSpell = new GrabSpell();
	/** Vitesse du grab */
	private transient int speedGrab;
	/** Range du grab */
	private transient int rangeGrab;

	/** Cool Down pour le grab (en milli secondes) */
	private transient long coolDownGrab;
	/** Moment auquel on effectue un grab */
	private transient long startTimeGrab = 0;

	/** Items attrapes par le personnage */
	private ArrayList<ItemBall> caughtItemBalls = new ArrayList<ItemBall>();


	/**Constructeur Character */
	public Character(int x, int y, boolean isLeftCharacter, Color colorCharacter, InputActions inputActions, GameplayCharacterConstants GameCC, GraphicCharacterConstants CCReal) {
		super(x, y, 0, 0, 0, 0);
		this.isLeftCharacter = isLeftCharacter;
		this.colorCharacter = colorCharacter;
		this.inputActions = inputActions;
		this.GameCC = GameCC;
		initGraphicAttributes(CCReal);
		initGameplayAttributes();
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


		// A l'atterrissage d'un switch
		if (actionBooleans.isSwitching && y == minY && isFalling == false) {
			// Permet de supprimer la vitesse et l'acceleration en X recues apres un switch
			speedX = 0;
			accelX = 0;
		}

		// A un atterrissage quelconque
		if (y == minY && isFalling == false) {
			// On peut resauter, on ne peut pas switch
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
		// Si on n'est pas en train de Grab, on l'indique au GrabSpell
		else {
			grabSpell.setLaunchGrabDir(GrabSpell.NO_GRAB);
		}

		// Si les deux joueurs sont sur la meme plateforme
		if ((isOnLeftPlatform && otherCharacter.isOnLeftPlatform) ||
			(isOnRightPlatform && otherCharacter.isOnRightPlatform)) {

			// Aucun ne peut Shoot
			actionBooleans.canShoot = false;

			// Si on est derriere son adversaire
			if ((isOnLeftPlatform && isOnLeftSide) || 
				(isOnRightPlatform && isOnLeftSide == false)) {

				checkCanPush(otherCharacter);

				// On ne peut pas grab d'items
				actionBooleans.canGrab = false;

			} else {
				actionBooleans.canPush = false;
			}

		} else {
			actionBooleans.canPush = false;
		}


		// Si on est pousse
		if (actionBooleans.isBeingPushed) {
			// On ne peut pas se deplacer
			actionBooleans.canLeft = false;
			actionBooleans.canRight = false;
			// On ne peut pas sauter ni grab
			actionBooleans.canJump = false;
			actionBooleans.canGrab = false;
			actionBooleans.canShield = false;
		}


	}


	/**Verifie les cool downs et active les booleens */
	public void checkCoolDowns() {

		// Pour les projectiles
		if (System.currentTimeMillis() - startTimeProjectile >= coolDownProjectile) {
			actionBooleans.canShoot = true;
		}

		// Pour le push
		if (System.currentTimeMillis() - startTimePush >= coolDownPush) {
			actionBooleans.canCanPush = true;
		}

		// Pour le grab
		if (System.currentTimeMillis() - startTimeGrab >= coolDownGrab) {
			actionBooleans.canGrab = true;
		}

	}


	/** Verifie la distance entre les deux persos pour autoriser le Push 
	 * Les persos sont deja supposes sur la meme plateforme, et this Character est derriere otherCharacter */
	public void checkCanPush(Character otherCharacter) {

		// On peut le pousser (si le coolDown et la distance entre les persos l'autorisent et si l'adversaire n'est pas immunise)
		if (actionBooleans.canCanPush &&
			(Math.abs(x - otherCharacter.x) <= GameCC.getRangePush()) && areOnSameY &&
			otherCharacter.isImmuned == false) {
			actionBooleans.canPush = true;
		} else {
			actionBooleans.canPush = false;
		}

	}


	/** Actualise les booleens de position */
	public void updatePositionBooleans(GraphicMainConstants MCReal, GraphicCharacterConstants CCReal, Character otherCharacter) {

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
			if (areOnSameY && isOnLeftSide && x > otherCharacter.x - GameCC.getHitboxWidth()) {
				areOnSameXCollisions = true;

			// Si le perso est a droite et est en collision avec l'autre (si on utilise <=, les persos peuvent se repousser en se deplacant)
			} else if (areOnSameY && isOnLeftSide == false && x < otherCharacter.x + GameCC.getHitboxWidth()) {
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
	public void updateCollisionBorders(GraphicMainConstants MCReal, GraphicCharacterConstants CCReal, Character otherCharacter) {

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
	public void updatePosition(GraphicMainConstants MCReal, GraphicCharacterConstants CCReal, Character otherCharacter) {
		
		// Si on tombe et qu'on a touché le fond, on replace le personnage sur la plateforme disponible
		if (isFalling && y == minY) {
			replacePlayer(MCReal, CCReal, otherCharacter);
			updateCollisionBorders(MCReal, CCReal, otherCharacter);
		}

		checkMovement(otherCharacter);
		checkJump();
		checkSwitch();

		checkBeingPushed();

		moveCharacter(otherCharacter);
	}


	/**Repositionne le joueur sur la plateforme disponible si il est tombe dans le vide */
	public void replacePlayer(GraphicMainConstants MCReal, GraphicCharacterConstants CCReal, Character otherCharacter) {

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
		}

		// Si on appuie pas sur les touches de deplacements mais quon a le droit de se deplacer on ne bouge plus
		if (inputActions.getLeftPressed() == false && inputActions.getRightPressed() == false && actionBooleans.canLeft && actionBooleans.canRight) {
			speedX = 0;
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
				this.speedX = GameCC.getSwitchSpeed();
			}
			if (isOnRightPlatform) {
				this.speedX = -GameCC.getSwitchSpeed();
			}

			// On est en train de switch, on ne peux plus effectuer un switch
			actionBooleans.isSwitching = true;
			actionBooleans.canSwitch = false;
		}
	}


	/**Verifie si on est toujours en train d'etre pousse */
	public void checkBeingPushed() {

		int nextSpeedX = speedX += accelX;
		// Si on est en train de se faire pousser
		if (actionBooleans.isBeingPushed) {

			// Et qu'on a finit de se faire pousser
			if ((isBeingPushedLeft && nextSpeedX >= 0 ) ||
				(isBeingPushedLeft == false && nextSpeedX <= 0)) {
				// on est plus en train de se faire pousser, (on pourra se deplacer a nouveau)
				actionBooleans.isBeingPushed = false;
				accelX = 0;
				speedX = 0;
			}
		}
	}


	/**Deplace le personnage */
	public void moveCharacter(Character otherCharacter) {

		// Si il y a collisions trop importante entre les perso, il faut les ecarter lentement chacun l'un de l'autre
		if (areOnSameXCollisions) {
			//Gestion des X
			if (isOnLeftSide) {
				x -= GameCC.getDecollisionSpeed();
			} else {
				x += GameCC.getDecollisionSpeed();
			}

			//Gestion des Y
			this.moveY();

		// Si les deux perso ne sont pas en collisions, on se deplace normalement
		} else {
			this.moveXY();
		}

	}


	/**Verifie et Lance les actions a effectuer (grab shield shoot push) */
	public void checkActions(Character otherCharacter, GraphicMainConstants MCReal, GraphicGrabConstants GCReal, GraphicCharacterConstants CCReal) {
		checkShoot(MCReal);
		checkPush(otherCharacter);
		checkGrab(MCReal, GCReal, CCReal);
	}


	/** Creer une entite Projectile */
	public void checkShoot(GraphicMainConstants MCReal) {

		/**Si on appuie sur Shoot et qu'on peut shoot */
		if (inputActions.getShootPushPressed() && actionBooleans.canShoot) {

			// Tire vers la droite
			if (isOnLeftSide) {
				projectiles.add(new Projectile(x + (this.width / 2), y + (this.height / 2), speedProjectile,
									MCReal, radiusProjectile, rangeProjectile, damageProjectile) );
			// Tire vers la gauche
			} else {
				projectiles.add(new Projectile(x - (this.width) / 2, y + (this.height / 2), - speedProjectile,
									MCReal, radiusProjectile, rangeProjectile, damageProjectile) );
			}

			// On ne peut plus shoot tout de suite
			actionBooleans.canShoot = false;
			startTimeProjectile = System.currentTimeMillis();
		}	
	}


	/**Pousse l'adversaire hors de sa plateforme */
	public void checkPush(Character otherCharacter) {

		// Si on est autorise a pousser et quon appuie sur pousser
		if (actionBooleans.canPush && inputActions.getShootPushPressed()) {

			// Pousse vers la droite
			if (isOnLeftSide) {
				otherCharacter.beingPushed(pushStrength);
				otherCharacter.isBeingPushedLeft = false;
			// Pousse vers la gauche
			} else {
				otherCharacter.beingPushed(-pushStrength);
				otherCharacter.isBeingPushedLeft = true;
			}

			otherCharacter.getActionBooleans().isBeingPushed = true;

			// On ne peut plus pousser tout de suite
			actionBooleans.canPush = false;
			actionBooleans.canCanPush = false;
			startTimePush = System.currentTimeMillis();
		}

	}


	/**Pousse le personnage cible */
	public void beingPushed(int opponentPushStrength) {
		this.speedX = opponentPushStrength;
		this.accelX = -opponentPushStrength / 60;
	}


	/**Lance un Grab pour attraper un objet */
	public void checkGrab(GraphicMainConstants MCReal, GraphicGrabConstants GCReal, GraphicCharacterConstants CCReal) {

		int launchGrabDir = GrabSpell.NO_GRAB;

		// Si on appuie sur grab et qu'on peut grab
		if (inputActions.getGrabPressed() && actionBooleans.canGrab) {

			// Si on est sur la plateforme de gauche
			if (isOnLeftPlatform) {
				// Lance un grab vers la droite
				launchGrabDir = GrabSpell.GRAB_RIGHT;

			// Si on est sur la plateforme de droite
			} else if (isOnRightPlatform) {
				// Lance un grab vers la gauche
				launchGrabDir = GrabSpell.GRAB_LEFT;

			// Si on est au dessus du vide
			} else {
				// // Propulsion vertical et on tombe
				// this.accelY = GRAVITY;
				// this.speedY = this.speedVertical / 2;
				// Attention a faire en sorte de ne plus pouvoir agir a partir de la, au risque de s'envoler vers d'autres cieux
			}

			if (launchGrabDir != GrabSpell.NO_GRAB) {
				// Lance le nouveau grab
				grabSpell.initNewGrab(x, y, launchGrabDir, rangeGrab, speedGrab, GCReal, CCReal);

				// Le perso est en train de grab
				actionBooleans.isGrabing = true;
				
				// On ne peut plus grab tout de suite
				actionBooleans.canGrab = false;
				startTimeGrab = System.currentTimeMillis();
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
				// Si on est pas immunise, on est blesse
				if (isImmuned == false) {
					lives -= proj.getDamage();
				}
				// Il faut supprimer ce projectile
				projectiles.remove(i);
			}
		}
	}


	/**Deplace le grab avec son joueur */
	public void moveGrab(GraphicCharacterConstants CCReal) {
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
	public void checkGrabCollision(ItemBalls itemBalls) {

		if (actionBooleans.isGrabing) {
			ArrayList<ItemBall> itemBallList = itemBalls.getItemBallList();
			ItemBall itemBall = null;

			for (int i=0; i<itemBallList.size(); i++) {
				itemBall = itemBallList.get(i);

				if (itemBall != null) {

					// renvoie true si le grab touche un item
					if (grabSpell.checkItemCollision(itemBall.x, itemBall.y, itemBall.width)) {
						itemBall.effects(this);

						// Si l'effet n'est pas instantane, si il est long, on l'ajoute a la liste des items attrapes
						if (itemBall.getCoolDownEffect() != 0) {

							// Ajoute l'effet a la liste
							addEffectToList(itemBall);
						}
						itemBalls.removeBall(itemBall);
					}
				}

			}
		}

	}


	/**Ajoute l'effet attrape a la liste ou ralonge l'effet si celui ci est deja present */
	public void addEffectToList(ItemBall itemBall) {
		int i=0;
		boolean isFound = false;
		ItemBall cib; // caughtItemBall

		while (i < caughtItemBalls.size() && isFound == false) {
			cib = caughtItemBalls.get(i);

			// le perso a deja cet effet
			if (cib.getClass() == itemBall.getClass()) {
				isFound = true;
				itemBall = cib; // important pour modifier le cooldown de l'effet deja en cours et non d'un item que l'on ajoute pas en double a la liste
			}

			i++;
		}

		// On ajoute l'effet a la liste si on ne lavait pas deja
		if (isFound == false) {
			caughtItemBalls.add(itemBall);
		}
		// On reinitialise le coolDown de cet effet
		itemBall.setStartTimeEffect(System.currentTimeMillis());

	}


	/** Verifie les effets que le personnage possede pour les arreter */
	public void checkItemsEffects() {
		ItemBall cib; //caughtItemBall
		for (int i=0; i<caughtItemBalls.size(); i++) {
			cib = caughtItemBalls.get(i);
			//Si le cooldown de l'effet est termine, on supprime l'effet
			if (cib.isCoolDownfinished()) {
				cib.resetEffects(this);
				caughtItemBalls.remove(i);
			}
		}
	}


	/* ==== */
	/* Init */
	/* ==== */

	/**Initialise les champs graphiques */
	public void initGraphicAttributes(GraphicCharacterConstants CCReal) {
		this.width = CCReal.getCharacterWidth();
		this.height = CCReal.getCharacterHeight();
	}


	/**Initialise les attributs lies au gameplay */
	public void initGameplayAttributes() {
		// GameplayConstants pour les peros
		this.lives = GameCC.getLivesMax();
		this.speedLateral = GameCC.getSpeedLateral();
		this.speedVertical = GameCC.getSpeedVertical();

		// GameplayConstants pour les projectiles
		this.speedProjectile = GameCC.getSpeedProjectile();
		this.rangeProjectile = GameCC.getRangeProjectile();
		this.damageProjectile = GameCC.getDamageProjectile();
		this.coolDownProjectile = GameCC.getCoolDownProjectile();
		this.radiusProjectile = GameCC.getRadiusProjectile();

		// GameplayConstants pour le Push
		this.pushStrength = GameCC.getPushStrength();
		this.coolDownPush = GameCC.getCoolDownPush();

		// GameplayConstants pour le grab
		this.speedGrab = GameCC.getSpeedGrab();
		this.rangeGrab = GameCC.getRangeGrab();
		this.coolDownGrab = GameCC.getCoolDownGrab();
	}


	/* ==== */
	/* Draw */
	/* ==== */

	/**Dessine le personnage */
	public void drawCharacter(Graphics g, GraphicMainConstants MC, GraphicCharacterConstants CC) {
		g.setColor(colorCharacter);
		int x = (int)((double)(this.x - this.width / 2) * MC.getOneUnityWidth());
		int y = (int)((double)(MC.getReal().getMaxY() - (this.y + this.height)) * MC.getOneUnityHeight());
		int width = CC.getCharacterWidth();
		int height = CC.getCharacterHeight();
		g.fillRect(x, y, width, height);
	}


	/**Dessine les projectiles de ce personnage */
	public void drawProjectiles(Graphics g, GraphicMainConstants MC) {
		for (int i=0; i<projectiles.size(); i++) {
			projectiles.get(i).drawProjectile(g, MC);
		}
	}


	/**Dessine le grab du ce personnage */
	public void drawGrab(Graphics g, GraphicMainConstants MC, GraphicGrabConstants GC) {
		grabSpell.drawGrab(g, MC, GC);
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
		return GameCC.getLivesMax();
	}
	public GameplayCharacterConstants getGameCC() {
		return GameCC;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	public int getSpeedProjectile() {
		return speedProjectile;
	}
	public void setSpeedProjectile(int speedProjectile) {
		this.speedProjectile = speedProjectile;
	}
	public int getRadiusProjectile() {
		return radiusProjectile;
	}
	public void setRadiusProjectile(int radiusProjectile) {
		this.radiusProjectile = radiusProjectile;
	}
	public long getCoolDownProjectile() {
		return coolDownProjectile;
	}
	public void setCoolDownProjectile(long coolDownProjectile) {
		this.coolDownProjectile = coolDownProjectile;
	}
	public void setImmune(Boolean isImmuned) {
		this.isImmuned = isImmuned;
	}
	public ArrayList<ItemBall> getCaughtItemBalls() {
		return caughtItemBalls;
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

		private boolean canCanPush = true;
		private boolean isBeingPushed = false;

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
				+ ", grabSpell" + grabSpell + ", projectiles=" + projectiles + " " + super.toString() + "]";
	}

}