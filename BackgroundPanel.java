import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.BorderFactory;

public class BackgroundPanel extends JPanel {
	//background color/image
	private String pathToBackground = "background.JPG";
	private Image background;
	private Color backgroundColor = Color.black;

	public BackgroundPanel() {
		this.setBorder(BorderFactory.createTitledBorder("JUMP IT AND CATCH IT"));
		this.setPreferredSize(new Dimension(1600, 1000));
		this.setBackground(backgroundColor);
		createBackground();
		if (background != null) {
			repaint();
		}
	}

	/** create the background image */
	public void createBackground() {
		try {
			File pathToImage = new File(pathToBackground);
			background = ImageIO.read(pathToImage);
		} catch (Exception e) {
			background = null;
			e.printStackTrace();
		}
		background = background.getScaledInstance(1175, 1000, Image.SCALE_DEFAULT);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
	}
}