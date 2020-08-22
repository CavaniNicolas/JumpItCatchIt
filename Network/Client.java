package Network;

import java.io.*;
import java.net.*;

public class Client {
    private Boolean connected = false;
    private Boolean inGame = false;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;
    private Socket socket;

	public static void main(String [] args) {
		new Client();
	}
	
    public Client() {
        String serverHostname = "2a01:e0a:40c:8ff0:f023:ce59:eb8f:f2c"; // L'adresse du serveur
        int portNumber = 5000; // Le port du serveur
        
        socket = null; // Un Socket TCP

        try {
            // Creation du socket et des flux d'entree/sortie
            socket = new Socket(serverHostname, portNumber);
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            System.out.println("CONNECTION STARTED");
            connected = true;

			// Recuperation et affichage de la reponse du serveur
			String str = "";
			while (!str.equals("CONNECTION CLOSED")) {
				try {
                    str = (String)objectInput.readObject();
                    System.out.println(str);
				} catch (ClassNotFoundException exc) {
					exc.printStackTrace();
				}
			}
        
            // closing flux and socket (output before input)
            objectOutput.close();
            objectInput.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public class InputProcessor extends Thread {
        public void run() {
            try {
                objectInput = new ObjectInputStream(socket.getInputStream());
                while (connected) {
                    try {
                        Object obj = objectInput.readObject();
                        if (obj instanceof String) {
                            if (((String)obj).equals("START GAME")) {
                                inGame = true;
                            }
                        }
                    } catch (ClassNotFoundException exc) {
                        exc.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }   
        }
    }
}