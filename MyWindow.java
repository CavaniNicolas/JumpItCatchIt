
import javax.swing.JFrame;

public class MyWindow extends JFrame {

	public MyWindow() {
		this.setTitle("Jump It Catch It");
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
	}
}