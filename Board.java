import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Board extends JLabel {
	private static final long serialVersionUID = 2L;

	/**true une fois que les valeurs graphiques fixes ont ete initialisees */
	private boolean isGraphicInitDone = false;

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


	/**Fonction d'affichage principale
	 * <p>
	 * Appelee a l'aide de repaint().
	 * <p>
	 * Les fonctions drawTruc sont pour les objets en mouvement
	 * Lesfonctions displayTruc sont pour les objets fixes
	 */
	public void paintComponent(Graphics g) {
		initGraphicFields();

		displayPlatforms(g);

		drawPlayers(g);
	}


	/**Dessine les deux personnages */
	public void drawPlayers(Graphics g) {
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
		g.fillRect(this.getWidth() - platformWidth, groundLevelYcoord, platformWidth, platformHeight);
	}


	/**Initialise les attributs graphiques fixes. <p>
	 * Cette methode n'est appelee qu'une seule fois
	 */
	public void initGraphicFields() {
		if (!isGraphicInitDone) {
			// dimensions des plateformes
			this.platformWidth = this.getWidth() * 32 / 100;
			this.platformHeight = this.getHeight() * 15 / 100;

			// dimensions des personnages
			this.characterWidth = this.getWidth() * 10 / 100;
			this.characterHeight = this.getHeight() * 20 / 100;

			// positions des personnages en X sur les plateforme
			int platformPrimaryPercentage = platformWidth * 60 / 100;
			int platformSecondaryPercentage = platformWidth * 10 / 100;

			this.primaryXcoordLeft = platformPrimaryPercentage;
			this.primaryXcoordRight = this.getWidth() - platformPrimaryPercentage - characterWidth;
			this.secondaryXcoordLeft = platformSecondaryPercentage;
			this.secondaryXcoordRight = this.getWidth() - platformSecondaryPercentage - characterWidth;

			// position au sol en Y
			this.groundLevelYcoord = this.getHeight()-this.platformHeight;

			// Initialisation terminee
			this.isGraphicInitDone = true;
		}
	}

}