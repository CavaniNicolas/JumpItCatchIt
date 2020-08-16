import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

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



	/**Le jeu lui meme (la boucle while true) */
	public void startGame() {
		this.isPlaying = true;

		while (this.isPlaying) {

			updateWindow();

			updateActionBooleans();

			updateAllCollisionBorders();
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
		characterRed.updatePosition();
		characterBlue.updatePosition();
	}


	/**Initialise le jeu, creer les deux joueurs avec leurs touches claviers associees serialisees, la ArrayList d'objets */
	public void initGame() {

		// Initialise les coordonnees reelles des objets
		boardGraphism.initRealCoordsAttributes();

		// On charge les objets (sans image) tout doit etre fonctionnel
		// Les fonctions d'affichage s'occuperont d'afficher des images si elles existent, des carres sinon

		// Touches joueur rouge
		int a=97, z=122, e=101, s=115, d=100, f=102;
		KeyBindings redKeyBindings = new KeyBindings(a, e, z, s, d, f);
		// Touches joueur bleu
		int u=117, i=105, o=111, k=107, l=108, m=109;
		KeyBindings blueKeyBindings = new KeyBindings(u, o, i, k, l, m);

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