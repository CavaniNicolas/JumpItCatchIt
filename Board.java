import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


/**class Board extends JPanel<p>
 * Gere le jeu
 */
public class Board extends JPanel {
	private static final long serialVersionUID = 2L;

	/**Les attributs graphiques et les fonctions d'affichage (les attributs sont initialises au premier appel de paintComponent() */
	private BoardGraphism boardGraphism = new BoardGraphism();

	/**La classe KeyListener */
	private PlayerKeyListener playerKeyListener = new PlayerKeyListener();

	/**Booleen, true si le jeu est en cours */
	private boolean isPlaying = false;

	/**Personnage rouge (initialement a gauche) */
	private Character characterRed;
	/**Personnage bleu (initialement a droite) */
	private Character characterBlue;

	/** each player's key bindings */
	KeyBindings redKeyBindings;
	KeyBindings blueKeyBindings;



	/**Le jeu lui meme (la boucle while true) */
	public void startGame() {
		this.isPlaying = true;

		while (this.isPlaying) {

			updateWindow();

			
			updateAllCollisionBorders();
			updateActionBooleans();
			updatePositionAndMoveAll();

			sleep(12);
		}
	}


	/** Actualise les booleens d'actions de personnages */
	public void updateActionBooleans() {
		// Les personnages
		characterRed.updateActionBooleans();
		characterBlue.updateActionBooleans();
	}


	/** Actualise les coordonnees de collision minimale et maximale de tous les objets */
	public void updateAllCollisionBorders() {

		// Les personnages
		characterRed.updateCollisionBorders(boardGraphism, characterBlue);
		characterBlue.updateCollisionBorders(boardGraphism, characterRed);

	}


	/**Actualise la position de tous les objets, (les collisions sont gerees lors du deplacement des objets grace aux collision borders)*/
	public void updatePositionAndMoveAll() {

		// Les personnages
		characterRed.updatePosition(boardGraphism, characterBlue);
		characterBlue.updatePosition(boardGraphism, characterRed);
	}

	/** sets the players bindings to their correct value */
	public void setBindings() {
		redKeyBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("red"));
		blueKeyBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("blue"));
	}


	/**Initialise le jeu, creer les deux joueurs avec leurs touches claviers associees serialisees, la ArrayList d'objets */
	public void initGame() {

		// Initialise les coordonnees reelles des objets
		boardGraphism.initRealCoordsAttributes();

		// On charge les objets (sans image) tout doit etre fonctionnel
		// Les fonctions d'affichage s'occuperont d'afficher des images si elles existent, des carres sinon

		// On récupère les keyBindings des joueurs
		setBindings();

		// Creation des deux persos
		characterRed = new Character(boardGraphism.getReal().getPrimaryXcoordLeft(), boardGraphism.getReal().getGroundLevelYCoord(), Color.red, redKeyBindings);
		characterBlue = new Character(boardGraphism.getReal().getPrimaryXcoordRight(), boardGraphism.getReal().getGroundLevelYCoord(), Color.blue, blueKeyBindings);


		// On charge les images, et on les met dans les objets (null si elles n'ont pas reussi)
		loadAndSetAllImages();
	}


	/**Charge toutes les images du jeu et les ajoute aux objets */
	public void loadAndSetAllImages() {

	}


	/**Actualise l'affichage graphique */
	public void updateWindow() {
		repaint();
	}


	/**Fonction d'affichage principale
	 * <p>
	 * Appelee a l'aide de repaint().
	 * <p>
	 * Les fonctions drawTruc sont pour les objets en mouvement
	 * Lesfonctions displayTruc sont pour les objets fixes
	 */
	public void paintComponent(Graphics g) {
		// Initialisation des attributs graphiques, effectuees a chaque redimensionnement de la fenetre
		boardGraphism.updateGraphicCoordsAttributes(boardGraphism.getMaxX(), boardGraphism.getMaxY(), this.getWidth(), this.getHeight());

		boardGraphism.displayPlatforms(g);
		boardGraphism.drawCharacters(g, characterRed, characterBlue);
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


	/**class PlayerKeyListener implements KeyListener<p>
	 * Gere les saisies clavier
	 */
	public class PlayerKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {

			int code = event.getKeyChar();
			//System.out.print("Code clavier "+ code + "\n ");

			togglePressedKeys(code, characterRed, true);
			togglePressedKeys(code, characterBlue, true);

		}


		@Override
		public void keyReleased(KeyEvent event) {
			int code = event.getKeyChar();
			//System.out.print("Code clavier "+ code + "\n ");

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
					// Active le booleens qui permet dactiver le canSwitch si on est dans les airs et qu'on
					// relache le bouton sauter (pour pouvoir rappuyer dessus dans les airs pour switch)
					if (character.getActionBooleans().isJumping() == true && character.getActionBooleans().isJumpFirstReleaseDone() == false) {
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


}