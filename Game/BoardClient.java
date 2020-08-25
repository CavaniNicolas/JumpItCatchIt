package Game;

import Menu.FileFunctions;
import Menu.KeyBindings;
import Menu.MainMenu;

import java.io.*;
import java.net.*;

/** handles the key listener for online game */
public class BoardClient extends BoardIO implements Runnable {
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

    //address of the server
    private String address;

    //mainmenu to start displaying the game when everyone has joined
    private MainMenu mainMenu;

	public BoardClient(BoardGraphism boardGraphism, String address, MainMenu mainMenu) {
        this.boardGraphism = boardGraphism;
        this.address = address;
        this.mainMenu = mainMenu;
		KeyBindings playerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("red"));
        playerKeyListener = new PlayerKeyListener(playerBindings, this, playerInputActions);
    }
    
    public void run() {
        connect(address);
    }

	/** send the input action object to the server */
	public void handleAction(InputActions inputActions) {
        //System.out.println(inputActions);
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
                //System.out.println(obj);
                if (!inGame) {
                    if (obj instanceof String) {
                        if (((String)obj).equals("START GAME")) {
                            System.out.println("Starting game on server order");
                            inGame = true;
                            mainMenu.startDisplayingGame();
                        } else {
                            System.out.println((String)obj);
                        }
                    }
                } else {
                    if (obj instanceof Board) {
						boardGraphism.setBoard((Board)obj);
                    } else if (obj instanceof String) {
                        if (((String)obj).equals("GAME ENDED")) {
                            System.out.println("Closing game on server order");
                            connected = false;
                        }
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
            //use writeUnshared instead of writeObject if retransmitting same object with modifications
            objectOutput.writeUnshared(obj);
            //System.out.println("OUTPUTTING :" + obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerKeyListener getPlayerKeyListener() {
        return playerKeyListener;
    }
}