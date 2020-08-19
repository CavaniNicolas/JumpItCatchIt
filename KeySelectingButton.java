import javax.swing.JButton;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeySelectingButton extends JButton implements KeyListener, ActionListener {
	private boolean isPressed = false;

	public void keyPressed(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {	
		if (isPressed) {
			String cara = String.valueOf((char)event.getKeyChar());
			//if (cara in validCaracters) {
				this.setText(cara);
			//}
			isPressed = false;
		}
	}

	public void actionPerformed(ActionEvent event) {
		isPressed = true;
	}
}