
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;


/**class MyWindow extends JFrame<p>
 * Gere la fenetre et les saisies clavier
 * et tout le jeu en soit
*/
public class MyWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private Board board = new Board();

	/**Booleen, true si le jeu est en cours */
	private boolean isPlaying = false;

	/**Personnage rouge (initialement a gauche) */
	private Character characterRed;
	/**Personnage bleu (initialement a droite) */
	private Character characterBlue;


	public MyWindow() {
		createWindow();

		this.setContentPane(board);

		// ICI : 
		// On lance le menu, avec 3 boutons : Play Options Quit
		// Dans les Options on peut modifier les touches claviers des deux joueurs
		// Le bouton Play appel les fonctions initGame() et startGame();
		// Pour le moment on lance juste le jeu
		
		initGame();
		startGame();
	}


	/**Le jeu lui meme (la boucle while true) */
	public void startGame() {
		this.isPlaying = true;

		while (this.isPlaying) {

			updateWindow();

			sleep(200);
		}
	}


	/**Initialise le jeu, creer les deux joueurs avec leurs touches claviers associees serialisees, la ArrayList d'objets */
	public void initGame() {
		// On charge les objets (sans image) tout est fonctionnel
		// Les fonctions d'affichage s'occupent dafficher les images si elles existent, des carres sinon
		
		characterRed = new Character(board.getPrimaryXcoordLeft(), board.getGroundLevelYcoord() - board.getCharacterHeight(), Color.red);
		characterBlue = new Character(board.getPrimaryXcoordRight(), board.getGroundLevelYcoord() - board.getCharacterHeight(), Color.red);
		
		// On charge les images, et on les met dans les objets (null si elles n'ont pas reussi)
		loadAndSetAllImages();
	}


	/**Charge toutes les images du jeu et les ajoute aux objets */
	public void loadAndSetAllImages() {

	}


	public void updateWindow() {
		repaint();
	}


	/**Cree la fenetre principale
	 * <p>
	 * La fenetre ne sera pas resizable par l'utilisateur (meme si le code y est adapte),
	 * mais il aura la possibilite dans le menu de passer d'un mode fenetre a un mode plein ecran.
	 */
	public void createWindow() {
		this.setTitle("Jump It Catch It");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Fenetre hors plein ecran : width = 80% et height = 90% (les proportions sont a peu pres conservees)
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)(screenSize.getWidth()*0.8);
		int height = (int)(screenSize.getHeight()*0.9);
		this.setSize(width, height);
		this.setLocation( (int)(screenSize.getWidth() * 0.1), (int)(screenSize.getHeight() * 0.05) );
		
		// this.setResizable(false);

		// Plein ecran
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// this.setUndecorated(true);

		this.setVisible(true);
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


	/**class PlayerKeyListener implements KeyListener<p>
	 * Gere les saisies clavier
	 */
	public class PlayerKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
		
	}
}