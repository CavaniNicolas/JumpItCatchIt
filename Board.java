import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

	/** Booleen, true si le jeu est en cours */
	private boolean isPlaying = false;

	/** Personnage rouge (initialement a gauche) */
	private Character characterRed;
	/** Personnage bleu (initialement a droite) */
	private Character characterBlue;

	/**Timer du jeu */
	private Timer gamePlayTimer;

	/** each player's key bindings */
	KeyBindings redKeyBindings;
	KeyBindings blueKeyBindings; // A supprimer pour en faire des variables locales des methodes


	public Board() {
		gamePlayTimer = new Timer(12, new GamePlayTimerListener());
	}


	/** Le jeu lui meme (la boucle while true) */
	public void startGame() {
		this.isPlaying = true;

		gamePlayTimer.start();
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
		characterRed.updateActionBooleans();
		characterBlue.updateActionBooleans();
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


	/** Actualise l'affichage graphique */
	public void updateWindow() {
		repaint();
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
		// Initialisation des attributs graphiques, effectuees a chaque
		// redimensionnement de la fenetre
		boardGraphism.updateGraphicCoordsAttributes(boardGraphism.getMaxX(), boardGraphism.getMaxY(), this.getWidth(),
				this.getHeight());

		boardGraphism.displayPlatforms(g);
		boardGraphism.drawCharacters(g, characterRed, characterBlue);
		boardGraphism.drawProjectiles(g, characterRed, characterBlue);
	}


	/** sets the players bindings to their correct value */
	public void setBindings() {
		// check if non default key settings exist
		String pathRedKeyBindings = "redKeyBindings.txt";
		String pathBlueKeyBindings = "blueKeyBindings.txt";

		File f = new File(pathRedKeyBindings);
		if (!f.exists() || f.isDirectory()) {
			pathRedKeyBindings = "redKeyBindingsDefault.txt";
		}
		f = new File(pathBlueKeyBindings);
		if (!f.exists() || f.isDirectory()) {
			pathBlueKeyBindings = "blueKeyBindingsDefault.txt";
		}
		redKeyBindings = getBindings(pathRedKeyBindings);
		blueKeyBindings = getBindings(pathBlueKeyBindings);
	}


	/** return a KeyBinding object from a String path to a file */
	public KeyBindings getBindings(String path) {
		KeyBindings keyBindings = null;
		ObjectInputStream ois;
		// create an input flux to read an object from a file
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
			try {
				// create the object from the file
				keyBindings = (KeyBindings) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			// close the flux
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyBindings;
	}


	/**
	 * Initialise le jeu, creer les deux joueurs avec leurs touches claviers
	 * associees serialisees, la ArrayList d'objets
	 */
	public void initGame() {

		// Initialise les coordonnees reelles des objets
		boardGraphism.initRealCoordsAttributes();

		// On charge les objets (sans image) tout doit etre fonctionnel
		// Les fonctions d'affichage s'occuperont d'afficher des images si elles
		// existent, des carres sinon

		// On récupère les keyBindings des joueurs
		setBindings(); // faire deux appels a cette fonction, elle devra retourner les KeyBindings, et
						// prendre en parametres le nom du fichier a aller chercher + le nom du fichier
						// par defaut

		// Creation des deux persos
		characterRed = new Character(boardGraphism.getReal().getPrimaryXcoordLeft(),
				boardGraphism.getReal().getGroundLevelYCoord(), Color.red, redKeyBindings, boardGraphism);
		characterBlue = new Character(boardGraphism.getReal().getPrimaryXcoordRight(),
				boardGraphism.getReal().getGroundLevelYCoord(), Color.blue, blueKeyBindings, boardGraphism);

		// On charge les images, et on les met dans les objets (null si elles n'ont pas
		// reussi)
		loadAndSetAllImages();
	}


	/** Charge toutes les images du jeu et les ajoute aux objets */
	public void loadAndSetAllImages() {

	}


	/** Delay */
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
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
			// System.out.print("Code clavier "+ code + "\n ");

			togglePressedKeys(code, characterRed, true);
			togglePressedKeys(code, characterBlue, true);

		}

		@Override
		public void keyReleased(KeyEvent event) {
			int code = event.getKeyChar();
			// System.out.print("Code clavier "+ code + "\n ");

			togglePressedKeys(code, characterRed, false);
			togglePressedKeys(code, characterBlue, false);

		}

		@Override
		public void keyTyped(KeyEvent event) {
		}

		/** Toggle les booleens de KeyPressed */
		public void togglePressedKeys(int code, Character character, boolean toggle) {
			KeyBindings characterKeys = character.getKeyBindings();

			// Pour le personnage bleu
			// Sauter
			if (code == characterKeys.getJumpKey()) {
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
			// Gauche
			if (code == characterKeys.getLeftKey()) {
				character.getActionBooleans().setLeftPressed(toggle);
			}
			// Droite
			if (code == characterKeys.getRightKey()) {
				character.getActionBooleans().setRightPressed(toggle);
			}
			// Grab
			if (code == characterKeys.getGrabKey()) {
				character.getActionBooleans().setGrabPressed(toggle);
			}
			// Shield
			if (code == characterKeys.getShieldKey()) {
				character.getActionBooleans().setShieldPressed(toggle);
			}
			// Shoot Push
			if (code == characterKeys.getShootPushKey()) {
				character.getActionBooleans().setShootPushPressed(toggle);
			}
		}

	}


	public class GamePlayTimerListener implements ActionListener {

		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {

			if (isPlaying == false) {
				gamePlayTimer.stop();
			}

			updateWindow();

			updateAllCollisionBorders();
			updateActionBooleans();
			updatePositionAndMoveAll();
			checkActions();

			moveProjectiles();
		}

	}

}