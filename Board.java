import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**class Board extends JPanel<p>
 * Gere le jeu
 */
public class Board extends JPanel {
	private static final long serialVersionUID = 2L;

	/**Les attributs graphiques et les fonctions d'affichage */
	private BoardGraphism boardGraphism;


	/**Booleen, true si le jeu est en cours */
	private boolean isPlaying = false;

	/**Personnage rouge (initialement a gauche) */
	private Character characterRed;
	/**Personnage bleu (initialement a droite) */
	private Character characterBlue;



	/**Initialise le jeu, creer les deux joueurs avec leurs touches claviers associees serialisees, la ArrayList d'objets */
	public void initGame() {

		// Creation de la classe qui contient les attributs et les methodes graphiques
		boardGraphism = new BoardGraphism();

		// On charge les objets (sans image) tout doit etre fonctionnel
		// Les fonctions d'affichage s'occuperont d'afficher des images si elles existent, des carres sinon
		
		characterRed = new Character(boardGraphism.getPrimaryXcoordLeft(), boardGraphism.getGroundLevelYcoord() - boardGraphism.getCharacterHeight(), Color.red);
		characterBlue = new Character(boardGraphism.getPrimaryXcoordRight(), boardGraphism.getGroundLevelYcoord() - boardGraphism.getCharacterHeight(), Color.red);
		
		// On charge les images, et on les met dans les objets (null si elles n'ont pas reussi)
		loadAndSetAllImages();
	}


	/**Le jeu lui meme (la boucle while true) */
	public void startGame() {
		this.isPlaying = true;

		while (this.isPlaying) {

			updateWindow();

			sleep(200);
		}
	}


	/**Charge toutes les images du jeu et les ajoute aux objets */
	public void loadAndSetAllImages() {

	}


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
		// Initialisation des attributs graphiques, elle n'est effectuee qu'une seule fois
		boardGraphism.initGraphicFields(this, this.getWidth(), this.getHeight());

		boardGraphism.displayPlatforms(g);
		boardGraphism.drawCharacters(g);
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

}