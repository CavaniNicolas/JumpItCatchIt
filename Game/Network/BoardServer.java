package Game.Network;

import Game.Board;
import Game.BoardGraphism;
import Game.GameLoop;
import Game.InputActions;

import java.util.ArrayList;

public class BoardServer {
	//gameLoop
	private GameLoop gameLoop;

	//board
	private Board board;

	//is the server running or not
	private Boolean isRunning = false;

	//player number management
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;

	//object streams
	private ConnectionHandler extendedSocketUDP;
	private ArrayList<PlayerState> playerStates = new ArrayList<PlayerState>();

	private final int portNumberTCP = 5000;
	private final int portNumberUDP = 5001;

	public BoardServer() {		
		board = new Board();
		board.setBoardGraphism(new BoardGraphism(board));
		gameLoop = new GameLoop(this.board, this);

		//start online server"
		extendedSocketUDP = new ConnectionHandler(portNumberUDP);
		if (extendedSocketUDP.createServer(portNumberTCP)) {
			isRunning = true;
			new Thread(new HandleServer()).start();
		}
	} 

	/** kills all thread loops, the game loop and the connections */
	public void stopServer() {
		if (gameLoop.isRunning()) {
			gameLoop.togglePause(true);
		}
		extendedSocketUDP.endConnection();
	}

	public ConnectionHandler getExtendedSocketUDP() {
		return extendedSocketUDP;
	}

	/** checks if all players want to restart, if yes : restart and set the players restart state to false */
	public void tryToRestart() {
		if (currentPlayerNumber == playerNumber && checkForRestart()) {
			restartGame();
			for (PlayerState playerState : playerStates) {
				playerState.setRestartGame(false);
			}
			currentPlayerNumber = 0;
		}
	}

	/** checks if all players want to restart */
	public Boolean checkForRestart() {
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
		extendedSocketUDP.outputObjectToAll("GAME STARTED", false);
		gameLoop.togglePause(false);	
	}

	/** waits for all players to be connected and handles them */
	public void acceptConnections() {
		while (currentPlayerNumber < playerNumber) {
			DestinationMachine dest = extendedSocketUDP.awaitConnection();
			PlayerState playerState = new PlayerState();
			playerStates.add(playerState);
			new Thread(new HandlePlayerInput(dest, currentPlayerNumber, playerState)).start();
			currentPlayerNumber++;
		}
	}
	
	/** handles every object received */
	public class HandleServer extends Thread {
		public void run() {
			while (isRunning) {
				acceptConnections();
				tryToRestart();
			}
			stopServer();
		}
	}

	/** handles every object received */
	public class HandlePlayerInput extends Thread {
		private DestinationMachine dest;
		private int characterRed;
		private PlayerState playerState;

		public HandlePlayerInput(DestinationMachine dest, int characterRed, PlayerState playerState) { 
			this.dest = dest;
			this.characterRed = characterRed;
			this.playerState = playerState;
		}

		public void run() {
			while (isRunning) {
				try {
					Object obj = dest.getQueue().take();
					if (obj instanceof InputActions) {
						// System.out.println("RECEIVED INPUT ACTIONS");
						if (characterRed == 0) {
							board.getCharacterRed().setInputActions((InputActions)obj);
						} else {
							board.getCharacterBlue().setInputActions((InputActions)obj);
						}
					} else if (obj instanceof String) {
						System.out.println(obj);
						if (((String)obj).split(" ")[0].equals("PORTDATA")) {
							dest.setDestPortUDP(Integer.parseInt(((String)obj).split(" ")[1]));
						} else if (((String)obj).equals("PING")) {
							dest.outputObject("PING");
						} else if (((String)obj).equals("PLAYER DISCONNECTION")) {
							extendedSocketUDP.outputObjectToAll("PLAYER LEFT", false);
							isRunning = false;
						} else {
							playerState.handleInput((String)obj);
							tryToRestart();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}