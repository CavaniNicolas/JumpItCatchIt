package Network;

import java.io.*;
import java.net.*;

import Game.Board;
import Menu.MainMenu;

public class Client {
    private Boolean connected;
    private Boolean inGame;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;
    private Socket socket;
    private MainMenu mainMenu;

	public static void main(String [] args) {
		new Client();
	}
    
    public Client() {
        this("2a01:e0a:40c:8ff0:f023:ce59:eb8f:f2c");
    }

    public Client(String serverHostName) {
        int portNumber = 5000; // Le port du serveur
        
        socket = null; // Un Socket TCP

        try {
            // Creation du socket et des flux d'entree/sortie
            socket = new Socket(serverHostName, portNumber);
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            System.out.println("CONNECTION STARTED");
            connected = true;
            inGame = false;

            inputObject();
        
            // closing flux and socket (output before input)
            objectOutput.close();
            objectInput.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + serverHostName);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
    
    public void inputObject() {
        while (connected) {
            try {
                Object obj = objectInput.readObject();
                if (!inGame) {
                    if (obj instanceof String) {
                        if (((String)obj).equals("START GAME")) {
                            inGame = true;
                        }
                    }
                } else {
                    if (obj instanceof Board) {
                        mainMenu.setBoard((Board)obj);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void outputObject(Object obj) {
        try {
            objectOutput.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}