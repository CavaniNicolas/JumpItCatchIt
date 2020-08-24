package Game;

import java.net.*;
import java.io.*;

public class BoardServer {
	//gameLoop
	private GameLoopServer gameLoopServer;

	//inputActions
	private InputActions redPlayerInputActions;
	private InputActions bluePlayerInputActions;

	//board
	private Board board;

	//waiting for everyone or playing
	private Boolean isRunning = true;
	private Boolean inGame = false;

	//player number management
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;
	private int connectionNumber = 0;

	//server socket and address
	private ServerSocket serverSocket = null;

	//object streams
	private ObjectOutputStream[] objectOutputs;
	private ObjectInputStream[] objectInputs;

	public BoardServer(Board board) {
		this.board = board;
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

	/** Find public IP address */
	public String getPublicIPAddress() {
		try { 
			URL url_name = new URL("http://bot.whatismyipaddress.com"); 
	
			BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream())); 
	
			// reads system IPAddress 
			return sc.readLine().trim(); 
		} catch (Exception e) { 
			return "COULD NOT FIND ADDRESS"; 
		} 
	}
	
 	/**Le server tourne dans un thread a part*/
	 public class HandleServer extends Thread {
		public HandleServer() {
			objectOutputs = new ObjectOutputStream[playerNumber];
			objectInputs = new ObjectInputStream[playerNumber];

			//loop keeping the server alive
			while (isRunning == true){
				//wait for the 2 connections
				while (currentPlayerNumber < playerNumber) {
					System.out.println("waiting for " + (playerNumber - currentPlayerNumber) + " players");
					System.out.println("Current player number " + currentPlayerNumber);
					try {
						//On attend une connexion d'un client
						Socket client = serverSocket.accept();

						//Une fois reçue, on la traite dans un thread séparé
						Thread thread = new Thread(new ClientProcessor(client));
						thread.start();
						currentPlayerNumber++;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Starting game");
				inGame = true;
				gameLoopServer.togglePause();

			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				serverSocket = null;
			}
		}
	}
 

    public class ClientProcessor extends Thread {
		private int number;
        Socket clientSocket = null; 
        ClientProcessor(Socket clientSocket){
			number = connectionNumber;
			connectionNumber++;
			System.out.println("CONNECTION " + connectionNumber + " STARTED");
			this.clientSocket = clientSocket;
        }


        @Override
        public void run(){
			String IP = clientSocket.getInetAddress().getHostAddress().toString();
			try { 
				objectOutputs[number] = new ObjectOutputStream(clientSocket.getOutputStream());
				objectInputs[number] = new ObjectInputStream(clientSocket.getInputStream());

				new Thread(new InputProcessor(number));

				while (isRunning) {	
					if (!inGame) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							e.printStackTrace();
						}
						String str = "waiting for " + (playerNumber - currentPlayerNumber) + " players";
						objectOutputs[connectionNumber].writeObject(str);
						objectOutputs[connectionNumber].flush();
					} else {
						objectOutputs[connectionNumber].writeObject("Starting game");
						objectOutputs[connectionNumber].flush();
					}			
				}
				objectOutputs[number].writeObject("CONNECTION CLOSED");
				objectOutputs[number].flush();
				// closing flux and socket (output before input)
				objectOutputs[number].close(); 
				objectInputs[number].close(); 
				clientSocket.close(); 
				System.out.println ("Connexion avec "+IP+" fermée");
			}  catch (IOException e) { 
				System.out.println ("Crash de la connexion avec "+IP);
			} 
		}
	}

	public class InputProcessor extends Thread {
		InputProcessor(int number) {
			while (isRunning) {
				try {
					Object obj = objectInputs[number].readObject();
					if (obj instanceof InputActions) {
						if (number == 0) {
							board.getCharacterRed().setInputActions((InputActions)obj);;
						} else {
							board.getCharacterBlue().setInputActions((InputActions)obj);;
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

	public void outputBoard() {
		for (ObjectOutputStream objectOutput : objectOutputs) {
			try {
				objectOutput.writeObject(board);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}