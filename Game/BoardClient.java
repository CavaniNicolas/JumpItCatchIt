package Game;

import Menu.KeyBindings;

public class BoardClient extends BoardIO {
	private GameLoop gameLoop;
	private KeyBindings playerBindings;
	private PlayerKeyListener playerKeyListener;
	private InputActions redPlayerInputActions;
	private InputActions bluePlayerInputActions;

	public BoardClient(BoardGraphism boardGraphism) {
		super(boardGraphism);
		playerKeyListener = new PlayerKeyListener(playerBindings, this, redPlayerInputActions);
	}

	public void handleAction(String action, Boolean toggle) {
	}
}