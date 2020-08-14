
import java.awt.Color;
import java.awt.Graphics;

/** Class BoardGraphism<p>
 * Gere l'affichage
 */
public class BoardGraphism {

	/**true une fois que les valeurs graphiques fixes ont ete initialisees */
	private boolean isGraphicInitDone = false;

	/**Largeur du JPanel */
	private int JPanelWidth;
	/**Hauteur du JPanel */
	private int JPanelHeight;

	/**Largeur des plateformes */
	private int platformWidth;
	/**Hauteur des plateformes*/
	private int platformHeight;

	/**Largeur des personnages */
	private int characterWidth;
	/**Hauteur des personnages*/
	private int characterHeight;

	/**Coordonnee principale en X du personnage a gauche (position sur la plateforme) */
	private int primaryXcoordLeft;
	/**Coordonnee secondaire en X du personnage a gauche (position a l'atterrissage d'un changement de plateforme) */
	private int secondaryXcoordLeft;

	/**Coordonnee principale en X du personnage a droite (position sur la plateforme) */
	private int primaryXcoordRight;
	/**Coordonnee secondaire en X du personnage a droite (position a l'atterrissage d'un changement de plateforme) */
	private int secondaryXcoordRight;

	/**Coordonnee en Y du niveau du sol */
	private int groundLevelYcoord;


	/**Initialise les attributs graphiques fixes. <p>
	 * Cette methode n'est appelee qu'une seule fois
	 */
	public void initGraphicFields(Board board, int JPanelWidth, int JPanelHeight) {
		if (!isGraphicInitDone) {

			// dimensions du JPanel
			this.JPanelWidth = JPanelWidth;
			this.JPanelHeight = JPanelHeight;

			// dimensions des plateformes
			this.platformWidth = board.getWidth() * 32 / 100;
			this.platformHeight = board.getHeight() * 15 / 100;

			// dimensions des personnages
			this.characterWidth = board.getWidth() * 10 / 100;
			this.characterHeight = board.getHeight() * 20 / 100;

			// positions des personnages en X sur les plateforme
			int platformPrimaryPercentage = platformWidth * 60 / 100;
			int platformSecondaryPercentage = platformWidth * 10 / 100;

			this.primaryXcoordLeft = platformPrimaryPercentage;
			this.primaryXcoordRight = board.getWidth() - platformPrimaryPercentage - characterWidth;
			this.secondaryXcoordLeft = platformSecondaryPercentage;
			this.secondaryXcoordRight = board.getWidth() - platformSecondaryPercentage - characterWidth;

			// position au sol en Y
			this.groundLevelYcoord = board.getHeight()-this.platformHeight;

			// Initialisation terminee
			this.isGraphicInitDone = true;
		}
	}


	/**Dessine les deux personnages */
	public void drawCharacters(Graphics g, Character character1, Character character2) {
		character1.drawCharacter(g);
		character2.drawCharacter(g);
	}

	/**Dessine les deux personnages */
	public void drawCharacters(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(primaryXcoordLeft, groundLevelYcoord - characterHeight, characterWidth, characterHeight);
		g.fillRect(secondaryXcoordLeft, groundLevelYcoord - characterHeight, characterWidth, characterHeight);

		g.setColor(Color.blue);
		g.fillRect(primaryXcoordRight, groundLevelYcoord - characterHeight, characterWidth, characterHeight);
		g.fillRect(secondaryXcoordRight, groundLevelYcoord - characterHeight, characterWidth, characterHeight);
	}


	/**Affiche les plateformes des deux joueurs */
	public void displayPlatforms(Graphics g) {
		Color darkRed = new Color(92, 30, 31);
		g.setColor(darkRed);
		g.fillRect(0, groundLevelYcoord, platformWidth, platformHeight);

		Color darkBlue = new Color(20, 45, 93);
		g.setColor(darkBlue);
		g.fillRect(JPanelWidth - platformWidth, groundLevelYcoord, platformWidth, platformHeight);
	}



	/* ======= */
	/* Getters */
	/* ======= */

	public int getCharacterHeight() {
		return characterHeight;
	}

	public int getPrimaryXcoordLeft() {
		return primaryXcoordLeft;
	}

	public int getPrimaryXcoordRight() {
		return primaryXcoordRight;
	}

	public int getGroundLevelYcoord() {
		return groundLevelYcoord;
	}
}