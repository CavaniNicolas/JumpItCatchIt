
import java.awt.Color;
import java.awt.Graphics;

/** Class BoardGraphism<p>
 * Gere l'affichage
 */
public class BoardGraphism {

	/**true une fois que les valeurs graphiques fixes ont ete initialisees */
	private boolean isGraphicUpdateDone = false;

	/**Coordonnees reelles */
	private GraphicalAttributes real = new GraphicalAttributes();
	/**Coordonnees graphiques */
	private GraphicalAttributes graphic = new GraphicalAttributes();


	/**Largeur reelle max du board */
	private int maxX = 16_000;
	/**Hauteur reelle max du board */
	private int maxY = 10_000;


	/**Largeur du JPanel (Board) */
	private int boardWidth;
	/**Hauteur du JPanel (Board) */
	private int boardHeight;


	/**Initialise les attributs graphiques fixes. <p>
	 * Cette methode n'est appelee qu'une seule fois
	 */
	public void initRealCoordsAttributes() {
			// dimensions d'une unite
			real.oneUnityWidth = 1.0;
			real.oneUnityHeight = 1.0;

			// dimensions des plateformes
			real.platformWidth = 5_000;
			real.platformHeight = 1_500;

			// dimensions des personnages
			real.characterWidth = 1_600;
			real.characterHeight = 2_000;

			// dimensions des projectiles
			real.projectileWidth = 1_000;
			real.projectileHeight = 1_000;

			// positions des personnages en X sur les plateforme
			real.primaryXcoordLeft = 3_800;
			real.primaryXcoordRight = 12_200;
			real.secondaryXcoordLeft = 1_800;
			real.secondaryXcoordRight = 14_200;

			// position au sol en Y
			real.groundLevelYCoord = 1_500;

	}

	/**Actualise les coordonnees et les dimensions graphiques si la fenetre est redimensionnee */
	public void updateGraphicCoordsAttributes(int maxX, int maxY, int boardWidth, int boardHeight) {
		if (!isGraphicUpdateDone) {

			// dimensions du JPanel
			this.boardWidth = boardWidth;
			this.boardHeight = boardHeight;

			// dimensions d'une unite
			graphic.oneUnityWidth = (double)boardWidth / (double)maxX;
			graphic.oneUnityHeight = (double)boardHeight / (double)maxY;


			// dimensions des plateformes
			graphic.platformWidth = (int)(real.platformWidth * graphic.oneUnityWidth);
			graphic.platformHeight = (int)(real.platformHeight * graphic.oneUnityHeight);

			// dimensions des personnages
			graphic.characterWidth = (int)(real.characterWidth * graphic.oneUnityWidth);
			graphic.characterHeight = (int)(real.characterHeight * graphic.oneUnityHeight);

			// dimensions des projectiles
			graphic.projectileWidth = (int)(real.getProjectileWidth() * graphic.oneUnityWidth);
			graphic.projectileHeight = (int)(real.getProjectileHeight() * graphic.oneUnityHeight);

			// positions des personnages en X sur les plateforme
			graphic.primaryXcoordLeft = (int)(real.primaryXcoordLeft * graphic.oneUnityHeight);
			graphic.primaryXcoordRight = (int)(real.primaryXcoordRight * graphic.oneUnityHeight);
			graphic.secondaryXcoordLeft = (int)(real.secondaryXcoordLeft * graphic.oneUnityHeight);
			graphic.secondaryXcoordRight = (int)(real.secondaryXcoordRight * graphic.oneUnityHeight);

			// position au sol en Y
			graphic.groundLevelYCoord = (int)((maxY - real.groundLevelYCoord) * graphic.oneUnityHeight);

			// Initialisation terminee
			this.isGraphicUpdateDone = true;
		}
	}


	/**Dessine les deux personnages */
	public void drawCharacters(Graphics g, Character character1, Character character2) {
		character1.drawCharacter(g, this);
		character2.drawCharacter(g, this);
	}


	/**Dessine les projectiles */
	public void drawProjectiles(Graphics g, Character character1, Character character2) {
		character1.drawProjectiles(g, this);
		character2.drawProjectiles(g, this);
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

	/* ======= */
	/* Getters */
	/* ======= */

	public GraphicalAttributes getReal() {
		return real;
	}

	public GraphicalAttributes getGraphic() {
		return graphic;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}



	/**Class GraphicalAttributes<p>
	 * Contient les coordonnees et les dimensions qui n'ont pas forcement a etre modifiees des objets
	 */
	public class GraphicalAttributes {

		/**Largeur d'une unite graphique */
		private double oneUnityWidth;
		/**Hauteur d'une unite graphique */
		private double oneUnityHeight;

		/**Largeur des plateformes */
		private int platformWidth;
		/**Hauteur des plateformes*/
		private int platformHeight;

		/**Largeur des personnages */
		private int characterWidth;
		/**Hauteur des personnages*/
		private int characterHeight;

		/**Largeur des projectiles */
		private int projectileWidth;
		/**Hauteur des projectiles */
		private int projectileHeight;

		/**Coordonnee principale en X du personnage a gauche (position sur la plateforme) */
		private int primaryXcoordLeft;
		/**Coordonnee secondaire en X du personnage a gauche (position a l'atterrissage d'un changement de plateforme) */
		private int secondaryXcoordLeft;

		/**Coordonnee principale en X du personnage a droite (position sur la plateforme) */
		private int primaryXcoordRight;
		/**Coordonnee secondaire en X du personnage a droite (position a l'atterrissage d'un changement de plateforme) */
		private int secondaryXcoordRight;

		/**Coordonnee en Y du niveau du sol reelle */
		private int groundLevelYCoord;


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
	}

}