package Game;

import Menu.FileFunctions;
import Menu.KeyBindings;
import Menu.MainMenu;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;

/** handles the key listener for online game */
public class BoardClient extends BoardIO {
    //board graphism
    protected BoardGraphism boardGraphism;

    //client input related
	private PlayerKeyListener playerKeyListener;
	private InputActions playerInputActions = new InputActions();

	//am i connected or not
	private Boolean connected;
	
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

	/** send the input action object to the server */
	public void handleAction(InputActions inputActions) {
        //System.out.println(inputActions);
		outputObject(inputActions);
	}

    public void run() {
        int portNumber = 5000; // Le port du serveur
        
        socket = null; // Un Socket TCP

        try {
            // Creation du socket et des flux d'entree/sortie
            socket = new Socket(address, portNumber);
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            connected = true;

            inputObject();
            endConnection();
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + address);
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
                if (obj instanceof String) {
                    if (((String)obj).equals("GAME STARTED")) {
                        mainMenu.displayGame();
                    } else if (((String)obj).equals("GAME ENDED")) {
                        endConnection();
                        connected = false;
                    }
                } else if (obj instanceof Board) {
                    boardGraphism.setBoard((Board)obj);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**closing flux and socket (output before input)*/
    public void endConnection() {
        try {
            objectOutput.close();
            objectInput.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainMenu.displayMainMenu();
    }

    public void escapePanelInteraction() {}

    /** adds the keylisteners */
	public void addKeyListeners(JFrame frame) {
        frame.addKeyListener(playerKeyListener);
    }

    /** knows what to do when someone returns to the main menu */
	public void exitGame() {
        outputObject("PLAYER LEFT");
        endConnection();
    }

    public PlayerKeyListener getPlayerKeyListener() {
        return playerKeyListener;
    }
}