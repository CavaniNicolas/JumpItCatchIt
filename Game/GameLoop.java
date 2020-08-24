package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameLoop{
	private Board board;
	private Timer gamePlayTimer;
	private Boolean isPlaying = false;
	private GamePlayTimerListener gamePlayTimerListener = new GamePlayTimerListener();

	public GameLoop(Board board) {
		this.board = board;
	}

	public void togglePause() {
		isPlaying = !isPlaying;
		if (!isPlaying) {
			gamePlayTimer.stop();
		} else {
			gamePlayTimer = new Timer(12, gamePlayTimerListener);
			gamePlayTimer.start();
		}
	}

	public class GamePlayTimerListener implements ActionListener {
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			if (isPlaying) {

				board.updateAllCollisionBorders();
				board.updateActionBooleans();
				board.updatePositionAndMoveAll();
				board.checkActions();

				board.moveProjectiles();
				board.checkProjectilesCollision();
			}
		}
	}
}