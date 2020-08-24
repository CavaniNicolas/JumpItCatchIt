package Game;

public class BoardServer {
	private GameLoop gameLoop;
	private InputActions redPlayerInputActions;
	private InputActions bluePlayerInputActions;
	private Board board;

	public BoardServer(Board board) {
		this.board = board;
		gameLoop = new GameLoop(this.board);
	}
}