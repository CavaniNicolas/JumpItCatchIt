package Game;

import Game.Network.BoardServer;

public class GameLoopServer extends GameLoop {
	private BoardServer boardServer;

	public GameLoopServer(Board board, BoardServer boardServer) {
		super(board);
		this.boardServer = boardServer;
	}

	public void sendBoardToClients() {
		boardServer.outputObjectToAll(board);
	}

}