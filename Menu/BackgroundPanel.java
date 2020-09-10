package Menu;

import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

public class BackgroundPanel extends GeneralPanel {
	private static final long serialVersionUID = -432619927090184212L;

	// background color/image
	private String pathToBackground = "assets/background.JPG";
	private Image background;
	private Color backgroundColor = Color.black;

	private JLabel title;

	public BackgroundPanel(JFrame frame) {
		super(frame);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(backgroundColor);
		createBackground();
		if (background != null) {
			repaint();
		}
		
		title = new JLabel("JUMP IT CATCH IT");
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

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

		//title on top, menu centred
		this.add(title);
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
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(background.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);
	}

	public JLabel getLabel() {
		return title;
	}
}