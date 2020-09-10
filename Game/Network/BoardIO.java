package Game.Network;

import Game.InputActions;

import javax.swing.JFrame;

/** mother class of boardClient and boardLocal */
public class BoardIO implements Runnable {
	protected JFrame frame;

	public BoardIO(JFrame frame) {
		this.frame = frame;
	}

	/** why are you running ? */
	public void run() {}

	/** knows what to do with an input action (send it to server or use it directly for local game) */
	public void handleAction(InputActions inputActions) {}

	/** adds (true ) or remove (false) the keylisteners */
	public void handleKeyListeners(Boolean bool) {}

	/** knows what to do when someone returns to the main menu */
	public void exitGame() {}

	/** pause */
	public void togglePause(Boolean bool) {}

	/** restart game */
	public void restartGame() {}
}