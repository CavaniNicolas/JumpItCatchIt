import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;


/**class MyWindow extends JFrame<p>
 * Gere la fenetre et les saisies clavier
*/
public class MyWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	/**Contient le jeu en soit */
	private Board board = new Board();


	public MyWindow() {
		createWindow();


		// ICI a faire plus tard: 
		// On lance le menu, avec 3 boutons : Play Options Quit
		// Dans les Options on peut modifier les touches claviers des deux joueurs
		// Le bouton Play appel les fonctions initGame() et startGame();
		// Pour le moment on lance juste le jeu

		board.initGame();

		Thread thread = new Thread(new StartGame());
		thread.start();
		//board.startGame();

		this.addKeyListener(board.getPlayerKeyListener());

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
		
		this.setContentPane(board);


		// this.setResizable(false);
		
		// Permet ensuite d'actualiser les valeurs des coordonnees et dimensions graphiques lors du prochain appel de paintComponent()
		this.addComponentListener(new ComponentAdapter() {
			// Si la fenetre est redimensionnee
			public void componentResized(ComponentEvent componentEvent) {
				board.getBoardGraphism().setIsGraphicUpdateDone(false);
			}
		});
		

		// Plein ecran
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// this.setUndecorated(true);

		this.setVisible(true);
	}


	/**Le jeu tourne dans un thread a part */
	public class StartGame implements Runnable {
		public void run() {
			board.startGame();
		}
	}

}