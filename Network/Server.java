package Network;

import java.net.*;
import java.io.*;

public class Server {
	private Boolean inGame = false;
	private int playerNumber = 2;
	private int currentPlayerNumber = 0;
	private Boolean isRunning;
	private ServerSocket serverSocket = null;
	private int connectionNumber = 0;
	private ObjectOutputStream[] objectOutputs;
	private ObjectInputStream[] objectInputs;


	public static void main(String [] args) {
		new Server();
	}

	public Server()  { 
        try { 
			int portNumber = 5000;
			//System.out.println(getPublicIPAddress());
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
	 public class HandleServer extends Thread {
		public HandleServer() {
			objectOutputs = new ObjectOutputStream[playerNumber];
			objectInputs = new ObjectInputStream[playerNumber];

			while (isRunning == true){
				//wait for the connections
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
						if (currentPlayerNumber == playerNumber) {
							System.out.println("Starting game");
							inGame = true;
							outputBoard("START GAME");
						}
					} catch (IOException e) {
						e.printStackTrace();
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
						//objectOutputs[connectionNumber].writeObject("START GAME");
						//objectOutputs[connectionNumber].flush();
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
					if (obj instanceof String) {
						System.out.println("USER " + number + ": " + (String)obj);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

	/** Find public IP address */
	public String getPublicIPAddress() {
		try { 
			URL url_name = new URL("http://bot.whatismyipaddress.com"); 
	
			BufferedReader sc = 
			new BufferedReader(new InputStreamReader(url_name.openStream())); 
	
			// reads system IPAddress 
			return sc.readLine().trim(); 
		} catch (Exception e) { 
			return "COULD NOT FIND ADDRESS"; 
		} 
	}

	public void outputBoard(String str) {
		for (ObjectOutputStream objectOutput : objectOutputs) {
			try {
				objectOutput.writeObject(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
} 
