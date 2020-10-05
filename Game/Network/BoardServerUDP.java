package Game.Network;

import Game.Board;
import Game.BoardGraphism;
import Game.GameLoop;
import Game.InputActions;

import java.util.ArrayList;

public class BoardServerUDP {
	//gameLoop
	private GameLoop gameLoop;

	//board
	private Board board;

	//is the server running or not
	private Boolean isRunning = true;

	//player number management
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;

	//object streams
	private ExtendedSocketUDP extendedSocketUDP;
	private ArrayList<PlayerState> playerStates = new ArrayList<PlayerState>();

	public BoardServerUDP() {		
		board = new Board();
		board.setBoardGraphism(new BoardGraphism(board));
		gameLoop = new GameLoop(this.board, this);

		//start online server"
		extendedSocketUDP = new ExtendedSocketUDP(playerNumber, playerStates, this);
		Thread handleServer = new Thread(new HandleServer());
		handleServer.start();
		Thread handlePlayerInput = new Thread(new HandlePlayerInput());
		handlePlayerInput.start();
	} 

	/** checks if all streams are alive and want to restart */
	public Boolean testAllStreams() {
		for (PlayerState playerState : playerStates) {
			if (!playerState.getRestartGame()) {
				return false;
			}
		}
		return true;
	}

	/** start game */
	public void restartGame() {
		board.initGame();
		extendedSocketUDP.outputObjectToAll("GAME STARTED");
		gameLoop.togglePause(false);	
	}

	public void stopServer() {
		isRunning = false;
	}

	public ExtendedSocketUDP getExtendedSocketUDP() {
		return extendedSocketUDP;
	}
 
	/** loop keeping the server alive*/
	public class HandleServer extends Thread {
		public void run() {
			while (isRunning){
				//this line is necessary to exe the thread
				System.out.println(currentPlayerNumber == playerNumber && testAllStreams());
				if (currentPlayerNumber == playerNumber && testAllStreams()) {
					restartGame();
					for (PlayerState playerState : playerStates) {
						playerState.setRestartGame(false);
					}
				}
			}
			if (gameLoop.isRunning()) {
				gameLoop.togglePause(true);
			}
			extendedSocketUDP.endConnection();
		}
	}

	/** handles every object received */
	public class HandlePlayerInput extends Thread {
		public void run() {
			while (isRunning) {
				IdentifiedObject obj = extendedSocketUDP.readObject();
				if (obj.getObj() instanceof InputActions) {
					if (obj.getId() == 0) {
						board.getCharacterRed().setInputActions((InputActions)obj.getObj());
					} else {
						board.getCharacterBlue().setInputActions((InputActions)obj.getObj());
					}
				} else if (obj.getObj() instanceof String) {
					System.out.println(obj.getObj());
					if (((String)obj.getObj()).equals("PING")) {
						extendedSocketUDP.outputObject("PING", obj.getId());
					} else if (((String)obj.getObj()).equals("LEAVING")) {
						extendedSocketUDP.outputObjectToAll("PLAYER LEFT");
						stopServer();	
					} else {
						playerStates.get(obj.getId()).handleInput((String)obj.getObj());
					}
				}
			}
		}
	}

	public void setCurrentPlayerNumber(int i) {
		currentPlayerNumber = i;
	}
}