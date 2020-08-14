
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


	public MyWindow() {
		createWindow();

		this.setContentPane(board);
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