package Game;

/** mother class of boardClient and boardLocal */
public class BoardIO {
	protected BoardGraphism boardGraphism;

	public BoardIO(BoardGraphism boardGraphism) {
		this.boardGraphism = boardGraphism;
	}

	/** knows what to do with an input action (send it to server or use it directly for local game) */
	public void handleAction(InputActions inputActions) {
	}
}