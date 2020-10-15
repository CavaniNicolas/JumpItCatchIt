package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import Game.Network.BoardServerUDP;

/** the main loop of the game */
public class GameLoop {
	private final int updateEveryDt = 12;
	protected Board board;
	private Timer gamePlayTimer;
	private BoardServerUDP boardServerUDP;
	private GamePlayTimerListener gamePlayTimerListener = new GamePlayTimerListener();
	private Boolean running = false;

	public GameLoop(Board board) {
		this.board = board;
		this.boardServerUDP = null;
	}

	public GameLoop(Board board, BoardServerUDP boardServerUDP) {
		this.board = board;
		this.boardServerUDP = boardServerUDP;
	}

	/** changes the pause state (true = pausing) */
	public void togglePause(Boolean bool) {
		if (bool) {
			running = false;
			gamePlayTimer.stop();
		} else {
			running = true;
			gamePlayTimer = new Timer(updateEveryDt, gamePlayTimerListener);
			gamePlayTimer.start();
		}
	}

	public Boolean isRunning() {
		return running;
	}

	/** updates all entities */
	public class GamePlayTimerListener implements ActionListener {
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			board.updateAll();
			if (boardServerUDP != null) {
				boardServerUDP.getExtendedSocketUDP().outputObjectToAll(board, true);
			}
		}
	}
}