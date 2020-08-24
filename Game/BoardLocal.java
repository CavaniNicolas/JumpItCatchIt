package Game;

import Menu.KeyBindings;

/** handles the key listeners and game for local game */
public class BoardLocal extends BoardIO {
	private GameLoop gameLoop;
	private KeyBindings redPlayerBindings;
	private KeyBindings bluePlayerBindings;
	private PlayerKeyListener redPlayerKeyListener;
	private PlayerKeyListener bluePlayerKeyListener;
	private InputActions redPlayerInputActions;
	private InputActions bluePlayerInputActions;
	private Board board;

	public BoardLocal(Board board, BoardGraphism boardGraphism) {
		super(boardGraphism);
		this.board = board;
		gameLoop = new GameLoop(this.board);
		redPlayerKeyListener = new PlayerKeyListener(redPlayerBindings, this, redPlayerInputActions);
		bluePlayerKeyListener = new PlayerKeyListener(bluePlayerBindings, this, bluePlayerInputActions);
	}

	/** uses directly the input action to change the course of the game */
	public void handleAction(String action, Boolean toggle) {
	}
}