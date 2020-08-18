import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KeySelectingPanel extends JPanel {
	private int position;
	private String path;
	private JButton selectingButton;
	private JLabel label;
	private JButton resetButton;

	public KeySelectingPanel(KeyBinding keyBinding, int position, String path) {
		this.position = position;
		this.path = path;

		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(250, 30));

		label = new JLabel();
		label.setPreferredSize(new Dimension(100,25));
		selectingButton = new JButton();
		selectingButton.setPreferredSize(new Dimension(50, 25));
		selectingButton.addKeyListener(new keyButtonListener());
		resetButton = new JButton("Reset");
		resetButton.setPreferredSize(new Dimension(70, 25));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				String defaultKeyValue = intToString(FileFunctions.getBindings(path).getKeyBindings().get(position).getKeyValue());
				selectingButton.setText(defaultKeyValue);
			}
		});
		setBinding(keyBinding);

		this.add(label);
		this.add(selectingButton);
		this.add(resetButton);
	}

	public void setBinding(KeyBinding keyBinding) {
		label.setText(keyBinding.getKeyActionDescription());
		selectingButton.setText(intToString(keyBinding.getKeyValue()));
	}

	/** returns the key binding described currently by the button and key description */
	public KeyBinding getCurrentKeyBinding() {
		return new KeyBinding(stringToInt(selectingButton), label.getText());
	}

	/** returns the ascii value of the first char of a jtextfield */
	public int stringToInt(JButton button) {
		String str = button.getText();
		char [] ch = str.toCharArray();
		return (int)ch[0];
	}

	/** returns a single caracter string = ascii caractere of an int */
	public String intToString(int number) {
		return String.valueOf((char)number);
	}

	public class keyButtonListener implements KeyListener {	
		public void keyPressed(KeyEvent event) {
		}
	
		public void keyReleased(KeyEvent event) {
		}
	
		public void keyTyped(KeyEvent event) {	
			String cara = String.valueOf((char)event.getKeyChar());
			//if (cara in validCaracters) {
				selectingButton.setText(cara);
			//}
		}
	}
}