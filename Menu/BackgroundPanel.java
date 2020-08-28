package Menu;

import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = -432619927090184212L;

	// background color/image
	private String pathToBackground = "assets/background.JPG";
	private Image background;
	private JLabel title;
	private Color backgroundColor = Color.black;

	public BackgroundPanel() {
		title = new JLabel("JUMP IT CATCH IT");
		title.setPreferredSize(new Dimension(1000, 100));

		//create a font from a file
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("../assets/Stylewars2011.TTF").openStream());  
			// makesure to derive the size
			font = font.deriveFont(80f);
			title.setFont(font);
		} catch (FontFormatException | IOException ex) {
			ex.printStackTrace();
		}

		this.add(title);
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

	public JLabel getLabel() {
		return title;
	}
}