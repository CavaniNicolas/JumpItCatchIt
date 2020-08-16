import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class KeyBindingMenu {
	private KeyBindings keyBindings;
	private JPanel panel;
	private JTextField left, right, jump, grab, shield, shootPush;
	private JLabel leftLabel, rightLabel, jumpLabel, grabLabel, shieldLabel, shootPushLabel;
	
	public KeyBindingMenu(String name, String path) {
		ObjectInputStream ois;
		// create an input flux to read an object from a file
		try {
			ois = new ObjectInputStream(
					new BufferedInputStream(
						new FileInputStream(
							new File(path))));
			try {
				//create the object from the file
				keyBindings = (KeyBindings)ois.readObject();
				//System.out.println("\nLEFT : " + (char)keyBindings.getLeftKey());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			// don't forget to close the flux
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//JPanel binding player
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(name));
		panel.setBackground(Color.white);
		panel.setPreferredSize(new Dimension(200, 210));

		//left binding
		left = new JTextField(String.valueOf((char)keyBindings.getLeftKey()));
		left.setPreferredSize(new Dimension(50, 25));
		leftLabel = new JLabel("Left");
		leftLabel.setPreferredSize(new Dimension(100,25));
		panel.add(leftLabel);
		panel.add(left);

		//right binding
		right = new JTextField(String.valueOf((char)keyBindings.getRightKey()));
		right.setPreferredSize(new Dimension(50, 25));
		rightLabel = new JLabel("Right");
		rightLabel.setPreferredSize(new Dimension(100,25));
		panel.add(rightLabel);
		panel.add(right);

		//jump binding
		jump = new JTextField(String.valueOf((char)keyBindings.getJumpKey()));
		jump.setPreferredSize(new Dimension(50, 25));
		jumpLabel = new JLabel("Jump");
		jumpLabel.setPreferredSize(new Dimension(100,25));
		panel.add(jumpLabel);
		panel.add(jump);

		//grab binding
		grab = new JTextField(String.valueOf((char)keyBindings.getGrabKey()));
		grab.setPreferredSize(new Dimension(50, 25));
		grabLabel = new JLabel("Grab");
		grabLabel.setPreferredSize(new Dimension(100,25));
		panel.add(grabLabel);
		panel.add(grab);

		//shield binding
		shield = new JTextField(String.valueOf((char)keyBindings.getShieldKey()));
		shield.setPreferredSize(new Dimension(50, 25));
		shieldLabel = new JLabel("Shield");
		shieldLabel.setPreferredSize(new Dimension(100,25));
		panel.add(shieldLabel);
		panel.add(shield);

		//shoot and push binding
		shootPush = new JTextField(String.valueOf((char)keyBindings.getShootPushKey()));
		shootPush.setPreferredSize(new Dimension(50, 25));
		shootPushLabel = new JLabel("Shoot and push");
		shootPushLabel.setPreferredSize(new Dimension(100,25));
		panel.add(shootPushLabel);
		panel.add(shootPush);	
	}

	public JPanel getPanel() {
		return panel;
	}

	public JTextField getLeft() {
		return left;
	}

	public JTextField getRight() {
		return right;
	}

	public JTextField getJump() {
		return jump;
	}

	public JTextField getGrab() {
		return grab;
	}

	public JTextField getShield() {
		return shield;
	}

	public JTextField getShootPush() {
		return shootPush;
	}
}