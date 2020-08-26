package Game;

import javax.swing.JFrame;

/** mother class of boardClient and boardLocal */
public class BoardIO implements Runnable {

	/** why are you running ? */
	public void run() {}

	/** knows what to do with an input action (send it to server or use it directly for local game) */
	public void handleAction(InputActions inputActions) {}

	/** adds the keylisteners */
	public void addKeyListeners(JFrame frame) {}

	/** knows what to do when someone returns to the main menu */
	public void exitGame() {}

	/** pause */
	public void setPause() {}

	/** restart game */
	public void restartGame() {}
}