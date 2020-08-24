package Game;

public class BoardLocal extends BoardIO {
	private GameLoop gameLoop;
	private PlayerKeyListener redPlayerKeyListener;
	private PlayerKeyListener bluePlayerKeyListener;
	private Board board;

	public BoardLocal(Board board, BoardGraphism boardGraphism) {
		super(boardGraphism);
		this.board = board;
		gameLoop = new GameLoop(this.board);
		redPlayerKeyListener = new PlayerKeyListener();
		bluePlayerKeyListener = new PlayerKeyListener();
	}

	public void handleAction(String action, Boolean toggle) {
	}
}