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

	// /**Coordonnees reelles */
	// private GraphicalAttributes real = new GraphicalAttributes();
	// /**Coordonnees graphiques */
	// private GraphicalAttributes graphic = new GraphicalAttributes();


	// /**Largeur reelle max du board */
	// private int maxX = 16_000;
	// /**Hauteur reelle max du board */
	// private int maxY = 10_000;


	// /**Largeur du JPanel (Board) */
	// private int boardWidth;
	// /**Hauteur du JPanel (Board) */
	// private int boardHeight;


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
		// // dimensions d'une unite
		// real.oneUnityWidth = 1.0;
		// real.oneUnityHeight = 1.0;

		// // dimensions des plateformes
		// real.platformWidth = 5_000;
		// real.platformHeight = 1_500;

		// // dimensions des personnages
		// real.characterWidth = 1_600;
		// real.characterHeight = 2_000;

		// // dimensions des projectiles
		// real.projectileWidth = 1_000;
		// real.projectileHeight = 1_000;

		// // positions des personnages en X sur les plateforme
		// real.primaryXcoordLeft = 3_800;
		// real.primaryXcoordRight = 12_200;
		// real.secondaryXcoordLeft = 1_800;
		// real.secondaryXcoordRight = 14_200;

		// // position au sol en Y
		// real.groundLevelYCoord = 1_500;


		// Items
		real.itemWidth = 1_200;
		real.itemHeight = 1_200;
		real.itemFirstX = maxX / 2;
		real.itemFirstY = maxY + real.itemHeight / 2;


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

			// // dimensions du JPanel
			// this.boardWidth = boardWidth;
			// this.boardHeight = boardHeight;

			// // dimensions d'une unite
			// graphic.oneUnityWidth = (double)boardWidth / (double)maxX;
			// graphic.oneUnityHeight = (double)boardHeight / (double)maxY;


			// // dimensions des plateformes
			// graphic.platformWidth = (int)(real.platformWidth * graphic.oneUnityWidth);
			// graphic.platformHeight = (int)(real.platformHeight * graphic.oneUnityHeight);

			// // dimensions des personnages
			// graphic.characterWidth = (int)(real.characterWidth * graphic.oneUnityWidth);
			// graphic.characterHeight = (int)(real.characterHeight * graphic.oneUnityHeight);

			// // dimensions des projectiles
			// graphic.projectileWidth = (int)(real.getProjectileWidth() * graphic.oneUnityWidth);
			// graphic.projectileHeight = (int)(real.getProjectileHeight() * graphic.oneUnityHeight);

			// // positions des personnages en X sur les plateforme
			// graphic.primaryXcoordLeft = (int)(real.primaryXcoordLeft * graphic.oneUnityWidth);
			// graphic.primaryXcoordRight = (int)(real.primaryXcoordRight * graphic.oneUnityWidth);
			// graphic.secondaryXcoordLeft = (int)(real.secondaryXcoordLeft * graphic.oneUnityWidth);
			// graphic.secondaryXcoordRight = (int)(real.secondaryXcoordRight * graphic.oneUnityWidth);

			// // position au sol en Y
			// graphic.groundLevelYCoord = (int)((maxY - real.groundLevelYCoord) * graphic.oneUnityHeight);


			// Items
			graphic.itemWidth = (int)(real.itemWidth * graphic.oneUnityWidth);
			graphic.itemHeight = (int)(real.itemHeight * graphic.oneUnityHeight);
			graphic.itemFirstX = (int)(real.itemFirstX * graphic.oneUnityWidth);
			graphic.itemFirstY = (int)(real.itemFirstY * graphic.oneUnityHeight);


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

	// public GraphicalAttributes getReal() {
	// 	return real;
	// }

	// public GraphicalAttributes getGraphic() {
	// 	return graphic;
	// }

	// public int getMaxX() {
	// 	return maxX;
	// }

	// public int getMaxY() {
	// 	return maxY;
	// }



	/**Class GraphicalAttributes<p>
	 * Contient les coordonnees et les dimensions qui n'ont pas forcement a etre modifiees des objets
	 */
	public class GraphicalAttributes {

		// /**Largeur d'une unite graphique */
		// private double oneUnityWidth;
		// /**Hauteur d'une unite graphique */
		// private double oneUnityHeight;

		// /**Largeur des plateformes */
		// private int platformWidth;
		// /**Hauteur des plateformes*/
		// private int platformHeight;

		// /**Largeur des personnages */
		// private int characterWidth;
		// /**Hauteur des personnages*/
		// private int characterHeight;

		// /**Largeur des projectiles */
		// private int projectileWidth;
		// /**Hauteur des projectiles */
		// private int projectileHeight;

		// /**Coordonnee principale en X du personnage a gauche (position sur la plateforme) */
		// private int primaryXcoordLeft;
		// /**Coordonnee secondaire en X du personnage a gauche (position a l'atterrissage d'un changement de plateforme) */
		// private int secondaryXcoordLeft;

		// /**Coordonnee principale en X du personnage a droite (position sur la plateforme) */
		// private int primaryXcoordRight;
		// /**Coordonnee secondaire en X du personnage a droite (position a l'atterrissage d'un changement de plateforme) */
		// private int secondaryXcoordRight;

		// /**Coordonnee en Y du niveau du sol reelle */
		// private int groundLevelYCoord;


		// Items
		private int itemWidth;
		private int itemHeight;
		private int itemFirstX;
		private int itemFirstY;


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

		public double getOneUnityWidth() {
			return oneUnityWidth;
		}

		public double getOneUnityHeight() {
			return oneUnityHeight;
		}

		public int getPlatformWidth() {
			return platformWidth;
		}

		public int getPlatformHeight() {
			return platformHeight;
		}

		public int getCharacterWidth() {
			return characterWidth;
		}

		public int getCharacterHeight() {
			return characterHeight;
		}

		public int getPrimaryXcoordLeft() {
			return primaryXcoordLeft;
		}

		public int getSecondaryXcoordLeft() {
			return secondaryXcoordLeft;
		}

		public int getPrimaryXcoordRight() {
			return primaryXcoordRight;
		}

		public int getSecondaryXcoordRight() {
			return secondaryXcoordRight;
		}

		public int getGroundLevelYCoord() {
			return groundLevelYCoord;
		}

		public int getProjectileWidth() {
			return projectileWidth;
		}

		public int getProjectileHeight() {
			return projectileHeight;
		}


		// Items
		public int getItemWidth() {
			return itemWidth;
		}
		public int getItemHeight() {
			return itemHeight;
		}
		public int getItemFirstX() {
			return itemFirstX;
		}
		public int getItemFirstY() {
			return itemFirstY;
		}


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