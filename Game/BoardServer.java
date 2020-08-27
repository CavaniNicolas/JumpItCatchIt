package Game;

import java.net.*;
import java.io.*;

public class BoardServer implements Runnable {
	//gameLoop
	private GameLoopServer gameLoopServer;

	//board
	private Board board;

	//is the server running or not
	private Boolean isRunning = true;
	private Boolean isStarting = true;

	//player number management
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;
	private int connectionNumber = 0;
	private int initializedStreams = 0;

	//server socket and address
	private ServerSocket serverSocket = null;

	//object streams
	private ObjectOutputStream[] objectOutputs;
	private ObjectInputStream[] objectInputs;
	private Socket[] clientSockets;
	
	public void run() {		
		board = new Board();
		board.setBoardGraphism(new BoardGraphism(board));
		gameLoopServer = new GameLoopServer(this.board, this);

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
			clientSockets = new Socket[playerNumber];
			objectOutputs = new ObjectOutputStream[playerNumber];
			objectInputs = new ObjectInputStream[playerNumber];

			//loop keeping the server alive
			while (isRunning == true){
				//this loop happens only when server starts and waits for connections
				if (isStarting) {
					while (currentPlayerNumber < playerNumber) {
						createConnections();
					}
					isStarting = false;
					//just to make sure all communications are ready before starting game
					while (initializedStreams < playerNumber) {}
				}
				//when all players have connected
				if (currentPlayerNumber == playerNumber) {
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

	public void createConnections() {
		try {
			//On attend une connexion d'un client
			clientSockets[currentPlayerNumber] = serverSocket.accept();

			//Une fois reçue, on la traite dans un thread séparé
			Thread thread = new Thread(new ClientProcessor(clientSockets[currentPlayerNumber]));
			thread.start();
			currentPlayerNumber++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startGame() {
		board.initGame();

		outputObjectToAll("GAME STARTED");
		gameLoopServer.togglePause(false);	
	}
 

    public class ClientProcessor implements Runnable {
		private int number;
        Socket clientSocket = null; 
        ClientProcessor(Socket clientSocket){
			number = connectionNumber;
			connectionNumber++;
			this.clientSocket = clientSocket;
        }

        @Override
        public void run(){
			String IP = clientSocket.getInetAddress().getHostAddress().toString();
			try { 
				objectOutputs[number] = new ObjectOutputStream(clientSocket.getOutputStream());
				objectInputs[number] = new ObjectInputStream(clientSocket.getInputStream());
				initializedStreams++;
				inputProcessor();
			} catch (Exception e) { 
				e.printStackTrace();
				stopServer();
			} 
		}

		public void inputProcessor() {
			while (isRunning) {
				try {
					Object obj = objectInputs[number].readObject();
					if (obj instanceof InputActions) {
						if (number == 0) {
							board.getCharacterRed().setInputActions((InputActions)obj);;
						} else {
							board.getCharacterBlue().setInputActions((InputActions)obj);;
						}
					} else if (obj instanceof String) {
						if (((String)obj).equals("RESTART GAME")) {
							currentPlayerNumber++;
						} else if (((String)obj).equals("PING")) {
							outputObject("PING", number);
						}
					}
				//happens when remote client shutdown connection					
				} catch (Exception e) {
					e.printStackTrace();
					stopServer();
				}
			}
		}
	}

	public void stopServer() {
		isRunning = false;
		gameLoopServer.togglePause(true);
		outputObjectToAll("PLAYER LEFT");
		endAllConnections();
	}

	public void outputObject(Object obj, int i) {
		try {
			objectOutputs[i].writeObject(obj);
			objectOutputs[i].flush();
			objectOutputs[i].reset();
		//happens when remote client shutdowns connection					
		} catch (Exception e) {
			e.printStackTrace();
			endConnection(i);
		}
	}

	public void outputObjectToAll(Object obj) {
		for (int i = 0; i < objectOutputs.length; i++) {
			outputObject(obj, i);
		}
	}

	public void endConnection(int i) {
		try {
			objectOutputs[i].close(); 
			objectInputs[i].close(); 
			clientSockets[i].close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void endAllConnections() {
		for (int i = 0; i < playerNumber; i++) {
			endConnection(i);
		}
	}
}