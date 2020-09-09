package Game.Network;

import Game.Board;
import Game.BoardGraphism;
import Game.GameLoop;
import Game.InputActions;

import java.net.*;
import java.util.ArrayList;

public class BoardServer implements Runnable {
	//gameLoop
	private GameLoop gameLoop;

	//board
	private Board board;

	//is the server running or not
	private Boolean isRunning = true;
	private Boolean isStarting = true;

	//player number management
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;

	//server socket and address
	private ServerSocket serverSocket = null;

	//object streams
	private ArrayList<ExtendedSocket> extendedSockets;
	
	public void run() {		
		board = new Board();
		board.setBoardGraphism(new BoardGraphism(board));
		gameLoop = new GameLoop(this.board, this);

		//start online server"
        try { 
            int portNumber = 5000;
			serverSocket = new ServerSocket(portNumber);
			new Thread(new HandleServer());
        }  catch (Exception e) { 
			e.printStackTrace();
			isRunning = false;
        } 
	} 
	
 	/**Le server tourne dans un thread a part*/
	 public class HandleServer extends Thread {
		public HandleServer() {
			extendedSockets = new ArrayList<ExtendedSocket>();

			//loop keeping the server alive
			while (isRunning == true){
				//this loop happens only when server starts and waits for connections
				if (isStarting) {
					while (currentPlayerNumber < playerNumber) {
						createConnections();
					}
					isStarting = false;
				}
				//when all players have connected
				if (currentPlayerNumber == playerNumber && testAllStreams()) {
					startGame();
					//to avoid starting games again and prepare for restarting when over
					currentPlayerNumber = 0;
				}
			}

			//close the server
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
				serverSocket = null;
			}
		}
	}

	/** checks if all streams are alive */
	public Boolean testAllStreams() {
		for (ExtendedSocket extendedSocket : extendedSockets) {
			if (!extendedSocket.getReady()) {
				return false;
			}
		}
		return true;
	}

	/** waits for a new connection and creates its socket */
	public void createConnections() {
		try {
			//On attend une connexion d'un client
			ExtendedSocket extendedSocket = new ExtendedSocket(currentPlayerNumber, serverSocket.accept(), true);
			extendedSockets.add(extendedSocket);

			//Une fois reçue, on la traite dans un thread séparé
			Thread thread = new Thread(new ClientProcessor(extendedSocket));
			thread.start();
			currentPlayerNumber++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** start game */
	public void startGame() {
		board.initGame();
		outputObjectToAll("GAME STARTED");
		gameLoop.togglePause(false);	
	}
 
	/** handles client connections */
    public class ClientProcessor implements Runnable {
        ExtendedSocket clientSocket; 
        ClientProcessor(ExtendedSocket clientSocket){
			this.clientSocket = clientSocket;
        }

		/** handles every object received */
		public void run() {
			while (isRunning) {
				Object obj = clientSocket.readObject();
				if (obj == null) {
					extendedSockets.remove(clientSocket);
					outputObjectToAll("PLAYER LEFT");
					stopServer();
				} else if (obj instanceof InputActions) {
					if (clientSocket.getID() == 0) {
						board.getCharacterRed().setInputActions((InputActions)obj);;
					} else {
						board.getCharacterBlue().setInputActions((InputActions)obj);;
					}
				} else if (obj instanceof String) {
					if (((String)obj).equals("RESTART GAME")) {
						currentPlayerNumber++;
					} else if (((String)obj).equals("PING")) {
						clientSocket.outputObject("PING");
					}
				}
			}
		}
	}

	/** stops server */
	public void stopServer() {
		isRunning = false;
		gameLoop.togglePause(true);
		endAllConnections();
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** output an object to every connected client */
	public void outputObjectToAll(Object obj) {
		for (ExtendedSocket extendedSocket : extendedSockets) {
			extendedSocket.outputObject(obj);
		}
	}

	/** disconnects all connected clients */
	public void endAllConnections() {
		for (ExtendedSocket extendedSocket : extendedSockets) {
			extendedSocket.endConnection();
		}
	}
}