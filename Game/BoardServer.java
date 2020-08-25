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

	//player number management
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;
	private int connectionNumber = 0;

	//server socket and address
	private ServerSocket serverSocket = null;

	//object streams
	private ObjectOutputStream[] objectOutputs;
	private ObjectInputStream[] objectInputs;
	private Socket[] clientSockets;

	public BoardServer(Board board) {
		this.board = board;
	}
	
	public void run() {
		gameLoopServer = new GameLoopServer(this.board, this);
		
		//start online server"
        try { 
            int portNumber = 5000;
			serverSocket = new ServerSocket(portNumber);
			new Thread(new HandleServer());
        }  catch (IOException e) { 
			e.printStackTrace();
			isRunning = false;
        } 
	} 
	
 	/**Le server tourne dans un thread a part*/
	 public class HandleServer extends Thread {
		public HandleServer() {
			objectOutputs = new ObjectOutputStream[playerNumber];
			objectInputs = new ObjectInputStream[playerNumber];
			clientSockets = new Socket[playerNumber];

			//loop keeping the server alive
			while (isRunning == true){
				//wait for the 2 connections
				while (currentPlayerNumber < playerNumber) {
					try {
						//On attend une connexion d'un client
						clientSockets[currentPlayerNumber] = serverSocket.accept();

						//Une fois reçue, on la traite dans un thread séparé
						Thread thread = new Thread(new ClientProcessor(clientSockets[currentPlayerNumber]));
						thread.start();
						currentPlayerNumber++;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (currentPlayerNumber == playerNumber) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							e.printStackTrace();
						}

						outputObject("GAME STARTED");
						gameLoopServer.togglePause();
					}	
				}
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				serverSocket = null;
			}
		}
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

				inputProcessor();
			}  catch (IOException e) { 
				System.out.println ("Crash de la connexion avec "+IP);
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
						if (((String)obj).equals("PLAYER LEFT")) {
							outputObject("GAME ENDED");
							endAllConnections();
							isRunning = false;
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void outputObject(Object obj) {
		for (ObjectOutputStream objectOutput : objectOutputs) {
			try {
				objectOutput.writeUnshared(obj);
				objectOutput.reset();
				//System.out.println("OUTPUTTING : " + obj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void endAllConnections() {
		for (int i = 0; i < playerNumber; i++) {
			try {
				objectOutputs[i].close(); 
				objectInputs[i].close(); 
				clientSockets[i].close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}