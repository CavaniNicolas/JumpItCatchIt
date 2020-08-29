package Game.Network;

import Game.Board;
import Game.GameLoop;
import Game.InputActions;
import Game.PlayerKeyListener;
import Menu.FileFunctions;
import Menu.KeyBindings;

import javax.swing.JFrame;

/** handles the key listeners and game for local game */
public class BoardLocal extends BoardIO {
	//game loop
	private GameLoop gameLoop;

	//keylisteners for both players
	private PlayerKeyListener redPlayerKeyListener;
	private PlayerKeyListener bluePlayerKeyListener;

	//the board to be modified
	private Board board;

	public BoardLocal(Board board) {
		this.board = board;
		gameLoop = new GameLoop(this.board);

		// On récupère les keyBindings des joueurs
		KeyBindings redPlayerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("KeyBindings/redPlayerBindings.txt", "KeyBindings/redPlayerBindingsDefault.txt"));
		KeyBindings bluePlayerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("KeyBindings/bluePlayerBindings.txt", "KeyBindings/bluePlayerBindingsDefault.txt"));

		//on crée les listeners correspondant
		redPlayerKeyListener = new PlayerKeyListener(redPlayerBindings, this, board.getCharacterRed().getInputActions());
		bluePlayerKeyListener = new PlayerKeyListener(bluePlayerBindings, this, board.getCharacterBlue().getInputActions());
	}

	/** uses directly the input action to change the course of the game */
	public void handleAction(InputActions inputActions) {
	}

	/** adds (true) or remove (false) the keylisteners */
	public void handleKeyListeners(JFrame frame, Boolean bool) {
		if (bool) {
			frame.addKeyListener(redPlayerKeyListener);
			frame.addKeyListener(bluePlayerKeyListener);
		} else {
			frame.removeKeyListener(redPlayerKeyListener);
			frame.removeKeyListener(bluePlayerKeyListener);
		}
	}

	/** knows what to do when someone returns to the main menu */
	public void togglePause(Boolean bool) {
		gameLoop.togglePause(bool);
	}

	/**exit game */
	public void exitGame() {
		gameLoop.togglePause(true);
	}

	public PlayerKeyListener getRedPlayerKeyListener() {
		return redPlayerKeyListener;
	}

	public PlayerKeyListener getBluePlayerKeyListener() {
		return bluePlayerKeyListener;
	}

	public void restartGame() {
		board.initGame();
	}
}