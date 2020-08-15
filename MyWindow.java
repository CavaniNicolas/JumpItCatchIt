import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

		this.addKeyListener(new PlayerKeyListener());

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


	/**class PlayerKeyListener implements KeyListener<p>
	 * Gere les saisies clavier
	 */
	public class PlayerKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {

			int code = event.getKeyChar();
			//System.out.print("Code clavier "+ code + "\n ");

			Character characterRed = board.getCharacterRed();
			Character characterBlue = board.getCharacterBlue();

			KeyBindings charaRedKeys = board.getCharacterRed().getKeyBindings();
			KeyBindings charaBlueKeys = board.getCharacterBlue().getKeyBindings();


			/* Pour le personnage rouge */
			// Sauter
			if (code == charaRedKeys.getJumpKey()) {
				characterRed.getActionBooleans().setJumpPressed(true);
			}
			// Gauche
			if (code == charaRedKeys.getLeftKey()) {
				characterRed.getActionBooleans().setLeftPressed(true);
			}
			// Droite
			if (code == charaRedKeys.getRightKey()) {
				characterRed.getActionBooleans().setRightPressed(true);
			}
			// Grab
			if (code == charaRedKeys.getGrabKey()) {
				characterRed.getActionBooleans().setGrabPressed(true);
			}
			// Shield
			if (code == charaRedKeys.getShieldKey()) {
				characterRed.getActionBooleans().setShieldPressed(true);
			}
			// Shoot Push
			if (code == charaRedKeys.getShootPushKey()) {
				characterRed.getActionBooleans().setShootPushPressed(true);
			}
			
			// Pour le personnage bleu
			// Sauter
			if (code == charaBlueKeys.getJumpKey()) {
				characterBlue.getActionBooleans().setJumpPressed(true);
			}
			// Gauche
			if (code == charaBlueKeys.getLeftKey()) {
				characterBlue.getActionBooleans().setLeftPressed(true);
			}
			// Droite
			if (code == charaBlueKeys.getRightKey()) {
				characterBlue.getActionBooleans().setRightPressed(true);
			}
			// Grab
			if (code == charaBlueKeys.getGrabKey()) {
				characterBlue.getActionBooleans().setGrabPressed(true);
			}
			// Shield
			if (code == charaBlueKeys.getShieldKey()) {
				characterBlue.getActionBooleans().setShieldPressed(true);
			}
			// Shoot Push
			if (code == charaBlueKeys.getShootPushKey()) {
				characterBlue.getActionBooleans().setShootPushPressed(true);
			}

		}

		@Override
		public void keyReleased(KeyEvent event) {
			int code = event.getKeyChar();
			//System.out.print("Code clavier "+ code + "\n ");

			Character characterRed = board.getCharacterRed();
			Character characterBlue = board.getCharacterBlue();

			KeyBindings charaRedKeys = board.getCharacterRed().getKeyBindings();
			KeyBindings charaBlueKeys = board.getCharacterBlue().getKeyBindings();


			/* Pour le personnage rouge */
			// Sauter
			if (code == charaRedKeys.getJumpKey()) {
				characterRed.getActionBooleans().setJumpPressed(false);
			}
			// Gauche
			if (code == charaRedKeys.getLeftKey()) {
				characterRed.getActionBooleans().setLeftPressed(false);
			}
			// Droite
			if (code == charaRedKeys.getRightKey()) {
				characterRed.getActionBooleans().setRightPressed(false);
			}
			// Grab
			if (code == charaRedKeys.getGrabKey()) {
				characterRed.getActionBooleans().setGrabPressed(false);
			}
			// Shield
			if (code == charaRedKeys.getShieldKey()) {
				characterRed.getActionBooleans().setShieldPressed(false);
			}
			// Shoot Push
			if (code == charaRedKeys.getShootPushKey()) {
				characterRed.getActionBooleans().setShootPushPressed(false);
			}


			// Pour le personnage bleu
			// Sauter
			if (code == charaBlueKeys.getJumpKey()) {
				characterBlue.getActionBooleans().setJumpPressed(false);
			}
			// Gauche
			if (code == charaBlueKeys.getLeftKey()) {
				characterBlue.getActionBooleans().setLeftPressed(false);
			}
			// Droite
			if (code == charaBlueKeys.getRightKey()) {
				characterBlue.getActionBooleans().setRightPressed(false);
			}
			// Grab
			if (code == charaBlueKeys.getGrabKey()) {
				characterBlue.getActionBooleans().setGrabPressed(false);
			}
			// Shield
			if (code == charaBlueKeys.getShieldKey()) {
				characterBlue.getActionBooleans().setShieldPressed(false);
			}
			// Shoot Push
			if (code == charaBlueKeys.getShootPushKey()) {
				characterBlue.getActionBooleans().setShootPushPressed(false);
			}

		}

		@Override
		public void keyTyped(KeyEvent event) {
		}
	}


	/**Le jeu tourne dans un thread a part */
	public class StartGame implements Runnable {
		public void run() {
			board.startGame();
		}
	}

}