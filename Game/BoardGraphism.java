package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;


/** Class BoardGraphism<p>
 * Gere l'affichage
 */
public class BoardGraphism extends JPanel {
	private static final long serialVersionUID = 2L;

	/** Le plateau de jeu */
	Board board;

	/**true une fois que les valeurs graphiques fixes ont ete initialisees */
	private boolean isGraphicUpdateDone = false;


	/**Timer d'affichage du jeu */
	private Timer gameDisplayTimer;


	public BoardGraphism(Board board) {
		this.board = board;
		gameDisplayTimer = new Timer(12, new GameDisplayTimerListener());
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
		updateGraphicCoordsAttributes(maxX, maxY, this.getWidth(), this.getHeight());

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


	/**Initialise les attributs graphiques fixes. <p>
	 * Cette methode n'est appelee qu'une seule fois
	 */
	public void initRealCoordsAttributes() {


		// HUD Character
		// Coeurs
		real.heartsXLeft = 500;
		real.heartsXRight = 15_100; //16_000 - 500 - 400 : maxX - memeDistanceDuBord - largeur
		real.heartsY = 400;
		real.heartWidth = 400;
		real.heartHeight = 800;
		real.interHearts = 200;

	}


	/**Actualise les coordonnees et les dimensions graphiques si la fenetre est redimensionnee */
	public void updateGraphicCoordsAttributes(int maxX, int maxY, int boardWidth, int boardHeight) {
		if (!isGraphicUpdateDone) {

			// HUD Character
			// Coeurs
			graphic.heartsXLeft = (int)(real.heartsXLeft * graphic.oneUnityWidth);
			graphic.heartsXRight = (int)(real.heartsXRight * graphic.oneUnityWidth);
			graphic.heartsY = (int)(real.heartsY * graphic.oneUnityHeight);
			graphic.heartWidth = (int)(real.heartWidth * graphic.oneUnityWidth);
			graphic.heartHeight = (int)(real.heartHeight * graphic.oneUnityHeight);
			graphic.interHearts = (int)(real.interHearts * graphic.oneUnityWidth);


			// Initialisation terminee
			this.isGraphicUpdateDone = true;
		}
	}


	/**Affiche les plateformes des deux joueurs */
	public void displayPlatforms(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, boardWidth, boardHeight);

		Color darkRed = new Color(92, 30, 31);
		g.setColor(darkRed);
		g.fillRect(0, graphic.groundLevelYCoord, graphic.platformWidth, graphic.platformHeight);

		Color darkBlue = new Color(20, 45, 93);
		g.setColor(darkBlue);
		g.fillRect(boardWidth - graphic.platformWidth, graphic.groundLevelYCoord, graphic.platformWidth, graphic.platformHeight);
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



	/**Class GraphicalAttributes<p>
	 * Contient les coordonnees et les dimensions qui n'ont pas forcement a etre modifiees des objets
	 */
	public class GraphicalAttributes {


		// HUD Character
		// Coeurs
		private int heartsXLeft;
		private int heartsXRight;
		private int heartsY;
		private int heartWidth;
		private int heartHeight;
		private int interHearts;


		/* ======= */
		/* Getters */
		/* ======= */


		// HUD Character
		// Coeurs
		public int getHeartsXLeft() {
			return heartsXLeft;
		}
		public int getHeartsXRight() {
			return heartsXRight;
		}
		public int getHeartsY() {
			return heartsY;
		}
		public int getHeartWidth() {
			return heartWidth;
		}
		public int getHeartHeight() {
			return heartHeight;
		}
		public int getInterHearts() {
			return interHearts;
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