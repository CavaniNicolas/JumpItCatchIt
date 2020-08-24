package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameLoop{
	private final int updateEveryDt = 12;
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
			gamePlayTimer = new Timer(updateEveryDt, gamePlayTimerListener);
			gamePlayTimer.start();
		}
	}

	public class GamePlayTimerListener implements ActionListener {
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			if (isPlaying) {

				// Characters
				board.updateAllCollisionBorders();
				board.updateActionBooleans();
				board.updatePositionAndMoveAll();
				board.checkActions();

				// Projectiles
				board.moveProjectiles();
				board.checkProjectilesCollision();
				
				// Items
				board.createItems();
				board.moveItems();
			}
		}
	}
}