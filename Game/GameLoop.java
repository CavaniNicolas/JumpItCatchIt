package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/** the main loop of the game */
public class GameLoop {
	private final int updateEveryDt = 12;
	protected Board board;
	private Timer gamePlayTimer;
	private GamePlayTimerListener gamePlayTimerListener = new GamePlayTimerListener();

	public GameLoop(Board board) {
		this.board = board;
	}

	/** changes the pause state */
	public void togglePause(Boolean bool) {
		if (bool) {
			gamePlayTimer.stop();
		} else {
			gamePlayTimer = new Timer(updateEveryDt, gamePlayTimerListener);
			gamePlayTimer.start();
		}
	}

	/** functiun to be modified in extended classes */
	public void sendBoardToClients() {}

	/** updates all entities */
	public class GamePlayTimerListener implements ActionListener {
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			board.updateAll();
			sendBoardToClients();
		}
	}
}