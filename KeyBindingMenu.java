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
	
	public KeyBindingMenu(String name) {
		//JPanel binding player
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(name));
		panel.setBackground(Color.white);
		panel.setPreferredSize(new Dimension(200, 210));

		//left binding
		left = new JTextField();
		left.setPreferredSize(new Dimension(50, 25));
		leftLabel = new JLabel("Left");
		leftLabel.setPreferredSize(new Dimension(100,25));
		panel.add(leftLabel);
		panel.add(left);

		//right binding
		right = new JTextField();
		right.setPreferredSize(new Dimension(50, 25));
		rightLabel = new JLabel("Right");
		rightLabel.setPreferredSize(new Dimension(100,25));
		panel.add(rightLabel);
		panel.add(right);

		//jump binding
		jump = new JTextField();
		jump.setPreferredSize(new Dimension(50, 25));
		jumpLabel = new JLabel("Jump");
		jumpLabel.setPreferredSize(new Dimension(100,25));
		panel.add(jumpLabel);
		panel.add(jump);

		//grab binding
		grab = new JTextField();
		grab.setPreferredSize(new Dimension(50, 25));
		grabLabel = new JLabel("Grab");
		grabLabel.setPreferredSize(new Dimension(100,25));
		panel.add(grabLabel);
		panel.add(grab);

		//shield binding
		shield = new JTextField();
		shield.setPreferredSize(new Dimension(50, 25));
		shieldLabel = new JLabel("Shield");
		shieldLabel.setPreferredSize(new Dimension(100,25));
		panel.add(shieldLabel);
		panel.add(shield);

		//shoot and push binding
		shootPush = new JTextField();
		shootPush.setPreferredSize(new Dimension(50, 25));
		shootPushLabel = new JLabel("Shoot and push");
		shootPushLabel.setPreferredSize(new Dimension(100,25));
		panel.add(shootPushLabel);
		panel.add(shootPush);	
	}

	/** sets the JTextFields so they display the bindings saved in a file designated by a given path string */
	public void setBindings(String path) {
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
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//close the flux
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//set the JTextFields to the value of the bindings
		left.setText(String.valueOf((char)keyBindings.getLeftKey()));
		right.setText(String.valueOf((char)keyBindings.getRightKey()));
		jump.setText(String.valueOf((char)keyBindings.getJumpKey()));
		grab.setText(String.valueOf((char)keyBindings.getGrabKey()));
		shield.setText(String.valueOf((char)keyBindings.getShieldKey()));
		shootPush.setText(String.valueOf((char)keyBindings.getShootPushKey()));
	}

	/** return current key bindings */
	public KeyBindings getCurrentKeyBindings() {
		return new KeyBindings(getValueOfText(left), getValueOfText(right), getValueOfText(jump), getValueOfText(grab), getValueOfText(shield), getValueOfText(shootPush));
	}

	/** returns the ascii value of the first char of a jtextfield */
	public int getValueOfText(JTextField textField) {
		String str = textField.getText();
		char [] ch = str.toCharArray();
		return (int)ch[0];
	}

	public JPanel getPanel() {
		return panel;
	}
}