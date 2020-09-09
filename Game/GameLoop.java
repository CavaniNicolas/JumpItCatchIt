package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import Game.Network.BoardServer;

/** the main loop of the game */
public class GameLoop {
	private final int updateEveryDt = 12;
	protected Board board;
	private Timer gamePlayTimer;
	private BoardServer boardServer;
	private GamePlayTimerListener gamePlayTimerListener = new GamePlayTimerListener();

	public GameLoop(Board board) {
		this.board = board;
		this.boardServer = null;
	}

	public GameLoop(Board board, BoardServer boardServer) {
		this.board = board;
		this.boardServer = boardServer;
	}

	/** changes the pause state (true = pausing) */
	public void togglePause(Boolean bool) {
		if (bool) {
			gamePlayTimer.stop();
		} else {
			gamePlayTimer = new Timer(updateEveryDt, gamePlayTimerListener);
			gamePlayTimer.start();
		}
	}

	/** updates all entities */
	public class GamePlayTimerListener implements ActionListener {
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			board.updateAll();
			if (boardServer != null) {
				boardServer.outputObjectToAll(board);
			}
		}
	}
}