package Network;

//import Game.Board;
import java.net.*;
import java.io.*;

public class Server {
	private Boolean inGame = false;
	//private Board board;
	private int playerNumber = 2;
	private int currentPlayerNumber;
	private Boolean isRunning;
	private ServerSocket serverSocket = null;
	private int connectionNumber = 0;

	public static void main(String [] args) {
		new Server();
	}

	public Server()  { 
        try { 
            int portNumber = 5000;
			serverSocket = new ServerSocket(portNumber); 
			isRunning = true;
			Thread t = new Thread(new HandleServer());
			t.start();
        }  catch (IOException e) { 
			e.printStackTrace();
			isRunning = false;
        } 
	} 
	
 	/**Le server tourne dans un thread a part*/
	 public class HandleServer implements Runnable {
		public void run() {
			while (isRunning == true){
				//wait for the 2 connections
				currentPlayerNumber = 0;
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
				while (inGame) {
					/*
					board = new Board();
					//start game
					board.initGame();
					board.togglePause();*/
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
 

    public class ClientProcessor extends Thread {
        Socket clientSocket = null; 
        ClientProcessor(Socket clientSocket){
			connectionNumber++;
			System.out.println("CONNECTION " + connectionNumber + " STARTED");
			this.clientSocket = clientSocket;
        }


        @Override
        public void run(){
			String IP = clientSocket.getInetAddress().getHostAddress().toString();
			try { 
				ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
				while (isRunning) {	
					if (!inGame) {
						String str = "waiting for " + (playerNumber - currentPlayerNumber) + " players";
						objectOutput.writeObject(str);
						objectOutput.flush();
					} else {
						objectOutput.writeObject("Starting game");
						objectOutput.flush();
						objectOutput.writeObject("CONNECTION CLOSED");
						objectOutput.flush();
						//board.

					}			
				}
				objectOutput.writeObject("CONNECTION CLOSED");
				objectOutput.flush();
				// closing flux and socket (output before input)
				objectOutput.close(); 
				objectInput.close(); 
				clientSocket.close(); 
				System.out.println ("Connexion avec "+IP+" fermée");
			}  catch (IOException e) { 
				System.out.println ("Crash de la connexion avec "+IP);
			} 
		}
		/*
		public class InputProcessor extends Thread {
			public void run() {
				while (inGame) {
					Object obj = objectInput.readObject();
					if (obj instanceof String) {
						
					}
				}
			}
		}*/
	}
	
	
} 
