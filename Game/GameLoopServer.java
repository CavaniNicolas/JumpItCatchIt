package Game;

public class GameLoopServer extends GameLoop {
	private BoardServer boardServer;

	public GameLoopServer(Board board, BoardServer boardServer) {
		super(board);
	}

	public void sendBoardToClients() {
		boardServer.outputObject(board);
	}

}