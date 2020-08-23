package Game;

import Menu.KeyBindings;
import Menu.FileFunctions;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * class Board extends JPanel
 * <p>
 * Gere le jeu
 */
public class Board extends JPanel {
	private static final long serialVersionUID = 2L;

	/**
	 * Les attributs graphiques et les fonctions d'affichage (les attributs sont
	 * initialises au premier appel de paintComponent()
	 */
	private BoardGraphism boardGraphism = new BoardGraphism();

	/** La classe KeyListener */
	private PlayerKeyListener playerKeyListener = new PlayerKeyListener();

	/**Booleen, true si le jeu est en cours */
	private boolean isPlaying;

	/** Personnage rouge (initialement a gauche) */
	private Character characterRed;
	/** Personnage bleu (initialement a droite) */
	private Character characterBlue;

	/** Items du jeu, tombant entre les deux plateformes lors du jeu */
	private ItemBalls itemBalls;

	/**Timer du jeu */
	private Timer gamePlayTimer;
	/**Timer d'affichage du jeu */
	private Timer gameDisplayTimer;


	public class GamePlayTimerListener implements ActionListener {

		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			if (isPlaying) {

				// Characters
				updateAllCollisionBorders();
				updateActionBooleans();
				updatePositionAndMoveAll();
				checkActions();

				// Projectiles
				moveProjectiles();
				checkProjectilesCollision();

				// Items
				createItems();
				moveItems();
			}
		}
	}


	public void togglePause() {
		isPlaying = !isPlaying;
		if (!isPlaying) {
			gamePlayTimer.stop();
		} else {
			gamePlayTimer = new Timer(12, new GamePlayTimerListener());
			gamePlayTimer.start();
			gameDisplayTimer.start();
		}
	}


	/**
	 * Actualise les coordonnees de collision minimale et maximale de tous les
	 * objets
	 */
	public void updateAllCollisionBorders() {

		// Les personnages
		characterRed.updateCollisionBorders(boardGraphism, characterBlue);
		characterBlue.updateCollisionBorders(boardGraphism, characterRed);

	}


	/** Actualise les booleens d'actions de personnages */
	public void updateActionBooleans() {
		// Les personnages
		characterRed.updateActionBooleans(characterBlue);
		characterBlue.updateActionBooleans(characterRed);
	}


	/**
	 * Actualise la position de tous les objets, (les collisions sont gerees lors du
	 * deplacement des objets grace aux collision borders)
	 */
	public void updatePositionAndMoveAll() {

		// Les personnages
		characterRed.updatePosition(boardGraphism, characterBlue);
		characterBlue.updatePosition(boardGraphism, characterRed);
	}

	/** Verifie et Lance les actions a effectuer (grab shield shoot push) */
	public void checkActions() {

		// Les personnages
		characterRed.checkActions(boardGraphism);
		characterBlue.checkActions(boardGraphism);
	}


	/** Deplace les projectiles */
	public void moveProjectiles() {
		characterRed.moveProjectiles();
		characterBlue.moveProjectiles();
	}


	/** Verifie la collision des projectiles et inflige des degats en faisant disparaitre le projectile */
	public void checkProjectilesCollision() {
		characterRed.checkProjectilesCollision(characterBlue);
		characterBlue.checkProjectilesCollision(characterRed);
	}


	/** Creer les items qui tombent au milieu du plateau */
	public void createItems() {
		itemBalls.createItems(boardGraphism);
	}


	public void moveItems() {
		itemBalls.moveItems();
	}


	/**
	 * Fonction d'affichage principale
	 * <p>
	 * Appelee a l'aide de repaint().
	 * <p>
	 * Les fonctions drawTruc sont pour les objets en mouvement Lesfonctions
	 * displayTruc sont pour les objets fixes
	 */
	public void paintComponent(Graphics g) {
		// Initialisation des attributs graphiques, effectuee a chaque
		// redimensionnement de la fenetre
		boardGraphism.updateGraphicCoordsAttributes(boardGraphism.getMaxX(), boardGraphism.getMaxY(), this.getWidth(),
				this.getHeight());

		// Affiche les plateformes
		boardGraphism.displayPlatforms(g);

		// Affiche les items
		itemBalls.drawItems(g, boardGraphism);

		// Affiche les personnages
		characterRed.drawCharacter(g, boardGraphism);
		characterBlue.drawCharacter(g, boardGraphism);

		// Affiche les projectiles
		characterRed.drawProjectiles(g, boardGraphism);
		characterBlue.drawProjectiles(g, boardGraphism);

		// Affiche la vie des joueurs
		characterRed.displayCharacterHUD(g, boardGraphism);
		characterBlue.displayCharacterHUD(g, boardGraphism);
	}


	/**
	 * Initialise le jeu, creer les deux joueurs avec leurs touches claviers
	 * associees serialisees, la ArrayList d'objets
	 */
	public void initGame() {
		isPlaying = false;

		gameDisplayTimer = new Timer(12, new GameDisplayTimerListener());


		// Initialise les coordonnees reelles des objets
		boardGraphism.initRealCoordsAttributes();

		// On charge les objets (sans image) tout doit etre fonctionnel
		// Les fonctions d'affichage s'occuperont d'afficher des images si elles
		// existent, des carres sinon

		// On récupère les keyBindings des joueurs
		KeyBindings redKeyBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("red"));
		KeyBindings blueKeyBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("blue"));


		// Creation des deux persos
		characterRed = new Character(boardGraphism.getReal().getPrimaryXcoordLeft(),
				boardGraphism.getReal().getGroundLevelYCoord(), true, Color.red, redKeyBindings, boardGraphism);
		characterBlue = new Character(boardGraphism.getReal().getPrimaryXcoordRight(),
				boardGraphism.getReal().getGroundLevelYCoord(), false, Color.blue, blueKeyBindings, boardGraphism);

		// On charge les images, et on les met dans les objets (null si elles n'ont pas
		// reussi)
		loadAndSetAllImages();

		// init ItemBalls
		itemBalls = new ItemBalls();
	}


	/** Charge toutes les images du jeu et les ajoute aux objets */
	public void loadAndSetAllImages() {

	}


	/**Actualise l'affichage graphique */
	public void updateWindow() {
		repaint();
	}


	/**Delay */
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}

	public void setIsPlaying(Boolean bool) {
		isPlaying = bool;
	}

	public BoardGraphism getBoardGraphism() {
		return boardGraphism;
	}

	public boolean getIsPlaying() {
		return isPlaying;
	}

	public Character getCharacterRed() {
		return characterRed;
	}

	public Character getCharacterBlue() {
		return characterBlue;
	}

	public PlayerKeyListener getPlayerKeyListener() {
		return playerKeyListener;
	}

	/**
	 * class PlayerKeyListener implements KeyListener
	 * <p>
	 * Gere les saisies clavier
	 */
	public class PlayerKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {

			int code = event.getKeyChar();
			//System.out.print("Code clavier "+ code + "\n ");

			//escape
			if (isPlaying) {
				togglePressedKeys(code, characterRed, true);
				togglePressedKeys(code, characterBlue, true);
			}
		}

		@Override
		public void keyReleased(KeyEvent event) {
			int code = event.getKeyChar();
			// System.out.print("Code clavier "+ code + "\n ");

			if (isPlaying) {
				togglePressedKeys(code, characterRed, false);
				togglePressedKeys(code, characterBlue, false);
			}

		}

		@Override
		public void keyTyped(KeyEvent event) {
		}

		/** Toggle les booleens de KeyPressed */
		public void togglePressedKeys(int code, Character character, boolean toggle) {
			KeyBindings characterKeys = character.getKeyBindings();

			// Pour le personnage bleu
			// Sauter (2)
			if (code == characterKeys.getKeyBindings().get(2).getKeyValue()) {
				character.getActionBooleans().setJumpPressed(toggle);

				// Si on relache le bouton sauter
				if (toggle == false) {
					// Active le booleens qui permet dactiver le canSwitch si on est dans les airs
					// et qu'on
					// relache le bouton sauter (pour pouvoir rappuyer dessus dans les airs pour
					// switch)
					if (character.getActionBooleans().isJumping() == true
							&& character.getActionBooleans().isJumpFirstReleaseDone() == false) {
						character.getActionBooleans().setIsJumpFirstReleaseDone(true);
					}
				}

      		}
			// Gauche (0)
			if (code == characterKeys.getKeyBindings().get(0).getKeyValue()) {
				character.getActionBooleans().setLeftPressed(toggle);
			}
			// Droite (1)
			if (code == characterKeys.getKeyBindings().get(1).getKeyValue()) {
				character.getActionBooleans().setRightPressed(toggle);
			}
			// Grab (3)
			if (code == characterKeys.getKeyBindings().get(3).getKeyValue()) {
				character.getActionBooleans().setGrabPressed(toggle);
			}
			// Shield (4)
			if (code == characterKeys.getKeyBindings().get(4).getKeyValue()) {
				character.getActionBooleans().setShieldPressed(toggle);
			}
			// Shoot Push (5)
			if (code == characterKeys.getKeyBindings().get(5).getKeyValue()) {
				character.getActionBooleans().setShootPushPressed(toggle);
			}
		}

	}


	public class GameDisplayTimerListener implements ActionListener {

		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {

			updateWindow();

		}
	}

}