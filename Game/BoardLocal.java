package Game;

import javax.swing.JFrame;

import Menu.FileFunctions;
import Menu.KeyBindings;

/** handles the key listeners and game for local game */
public class BoardLocal extends BoardIO {
	//game loop
	private GameLoop gameLoop;

	//keylisteners for both players
	private PlayerKeyListener redPlayerKeyListener;
	private PlayerKeyListener bluePlayerKeyListener;

	//input actions for both players
	private InputActions redPlayerInputActions;
	private InputActions bluePlayerInputActions;

	//the board to be modified
	private Board board;

	public BoardLocal(Board board, BoardGraphism boardGraphism) {
		this.board = board;
		gameLoop = new GameLoop(this.board);

		// On récupère les keyBindings des joueurs
		KeyBindings redPlayerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("red"));
		KeyBindings bluePlayerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("blue"));

		//on crée les listeners correspondant
		redPlayerKeyListener = new PlayerKeyListener(redPlayerBindings, this, board.getCharacterRed().getInputActions());
		bluePlayerKeyListener = new PlayerKeyListener(bluePlayerBindings, this, board.getCharacterBlue().getInputActions());

		//add the key listeners to board graphism
		boardGraphism.addKeyListener(redPlayerKeyListener);
		boardGraphism.addKeyListener(bluePlayerKeyListener);
	}

	/** uses directly the input action to change the course of the game */
	public void handleAction(InputActions inputActions) {
	}

	/** adds the keylisteners */
	public void addKeyListeners(JFrame frame) {
		frame.addKeyListener(redPlayerKeyListener);
		frame.addKeyListener(bluePlayerKeyListener);
	}

	/** knows what to do when someone returns to the main menu */
	public void exitGame() {
		gameLoop.togglePause();
	}

	public void setPause() {
		gameLoop.togglePause();
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