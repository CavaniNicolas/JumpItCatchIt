package Game;

import Menu.KeyBindings;

/** handles the key listener for online game */
public class BoardClient extends BoardIO {
	private KeyBindings playerBindings;
	private PlayerKeyListener playerKeyListener;
	private InputActions playerInputActions;

	public BoardClient(BoardGraphism boardGraphism) {
		super(boardGraphism);
		playerKeyListener = new PlayerKeyListener(playerBindings, this, playerInputActions);
	}

	/** send the input action object to the server */
	public void handleAction(InputActions inputActions) {
	}
}