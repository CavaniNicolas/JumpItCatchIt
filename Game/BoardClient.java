package Game;

import Menu.FileFunctions;
import Menu.KeyBindings;
import java.io.*;
import java.net.*;

/** handles the key listener for online game */
public class BoardClient extends BoardIO {
    //board graphism
    protected BoardGraphism boardGraphism;

    //client input related
	private PlayerKeyListener playerKeyListener;
	private InputActions playerInputActions = new InputActions();

	//waiting for everyone or playing
	private Boolean connected;
	private Boolean inGame;
	
	//object streams
    private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	
	//client socket
    private Socket socket;

	public BoardClient(BoardGraphism boardGraphism, String address) {
        this.boardGraphism = boardGraphism;
		KeyBindings playerBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("red"));
        playerKeyListener = new PlayerKeyListener(playerBindings, this, playerInputActions);
        connect(address);
	}

	/** send the input action object to the server */
	public void handleAction(InputActions inputActions) {
		outputObject(inputActions);
	}

    public void connect(String serverHostName) {
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
						boardGraphism.setBoard((Board)obj);
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

    public PlayerKeyListener getPlayerKeyListener() {
        return playerKeyListener;
    }
}