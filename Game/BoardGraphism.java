package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

import Game.ConstantsContainers.GraphicConstants.*;


/** Class BoardGraphism<p>
 * Gere l'affichage
 */
public class BoardGraphism extends JPanel {
	private static final long serialVersionUID = 2L;

	/** Le plateau de jeu */
	Board board;

	/**true une fois que les valeurs graphiques fixes ont ete initialisees */
	private boolean isGraphicUpdateDone = false;

	// Containers a Constantes Graphiques
	/**MainConstants Graphiques */
	private MainConstants mainConstants;

	/**CharacterConstants Graphiques*/
	private CharacterConstants characterConstants;
	/**ProjectileConstants Graphiques*/
	private ProjectileConstants projectileConstants;
	/**ItemConstants Graphiques*/
	private ItemConstants itemConstants;
	/**HUDConstants Graphiques*/
	private HUDConstants HUDConstants;


	/**Timer d'affichage du jeu */
	private Timer gameDisplayTimer;


	public BoardGraphism(Board board) {
		this.board = board;

		initGraphicConstantsContainers();

		gameDisplayTimer = new Timer(12, new GameDisplayTimerListener());
	}


	/**Creer les Containers a Constantes Graphiques */
	public void initGraphicConstantsContainers() {

		mainConstants = new MainConstants(new MainConstants(), this.getWidth(), this.getHeight());

		double oneUnityWidth = mainConstants.getOneUnityWidth();
		double oneUnityHeight = mainConstants.getOneUnityHeight();

		characterConstants = new CharacterConstants(new CharacterConstants(), oneUnityWidth, oneUnityHeight);
		projectileConstants = new ProjectileConstants(new ProjectileConstants(), oneUnityWidth, oneUnityHeight);
		itemConstants = new ItemConstants(new ItemConstants(), mainConstants.getReal().getMaxX(), mainConstants.getReal().getPlatformHeight(), oneUnityWidth, oneUnityHeight);
		HUDConstants = new HUDConstants(new HUDConstants(), oneUnityWidth, oneUnityHeight);

	}


	/**Actualise l'affichage graphique */
	public void updateWindow() {
		repaint();
	}

	/** starts the game display timer */
	public void startDisplaying() {
		gameDisplayTimer.start();
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
		updateGraphicCoordsAttributes();

		// Affiche les plateformes
		displayPlatforms(g);

		// Affiche les items
		board.getItemBalls().drawItems(g, this);

		// Affiche les personnages
		board.getCharacterRed().drawCharacter(g, this);
		board.getCharacterBlue().drawCharacter(g, this);

		// Affiche les projectiles
		board.getCharacterRed().drawProjectiles(g, this);
		board.getCharacterBlue().drawProjectiles(g, this);

		// Affiche la vie des joueurs
		board.getCharacterRed().displayCharacterHUD(g, this);
		board.getCharacterBlue().displayCharacterHUD(g, this);
	}


	/**Actualise les coordonnees et les dimensions graphiques si la fenetre est redimensionnee */
	public void updateGraphicCoordsAttributes() {
		if (!isGraphicUpdateDone) {

			// Initialisation terminee
			this.isGraphicUpdateDone = true;
		}
	}


	/**Affiche les plateformes des deux joueurs */
	public void displayPlatforms(Graphics g) {
		int platformWidth = mainConstants.getPlatformWidth();
		int platformHeight = mainConstants.getPlatformHeight();

		// Le fond est gris clair
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, mainConstants.getMaxX(), mainConstants.getMaxY());

		// Plateforme de gauche
		Color darkRed = new Color(92, 30, 31);
		g.setColor(darkRed);
		g.fillRect(0, platformHeight, platformWidth, platformHeight);

		// Plateforme de droite
		Color darkBlue = new Color(20, 45, 93);
		g.setColor(darkBlue);
		g.fillRect(mainConstants.getMaxX() - platformWidth, platformHeight, platformWidth, platformHeight);
	}
	
	
	public class GameDisplayTimerListener implements ActionListener {
		
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			
			updateWindow();
			
		}
	}


	/* ======= */
	/* Setters */
	/* ======= */

	public void setIsGraphicUpdateDone(boolean isGraphicUpdateDone) {
		this.isGraphicUpdateDone = isGraphicUpdateDone;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	/* ======= */
	/* Getters */
	/* ======= */
	
}