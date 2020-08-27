package Game;

import Menu.FileFunctions;
import Menu.KeyBindings;
import Menu.MainMenu;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

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

    //timer for ping tests
    private Timer ping;
    private long startTime;

	public BoardClient(BoardGraphism boardGraphism, String address, MainMenu mainMenu) {
        this.boardGraphism = boardGraphism;
        this.address = address;
        this.mainMenu = mainMenu;
		KeyBindings playerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("red"));
        playerKeyListener = new PlayerKeyListener(playerBindings, this, playerInputActions);
        ping = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                outputObject("PING");
                startTime = System.currentTimeMillis();
			}
        });
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

            //ping.start();

            inputObject();
            endConnection();
        } catch (Exception e) {
            mainMenu.displayConnectionErrorPanel();
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
                        connected = false;
                        mainMenu.displayPlayerLeftPanel();
                    } else if (((String)obj).equals("PING")) {
                        System.out.println("PING " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                } else if (obj instanceof Board) {
                    boardGraphism.setBoard((Board)obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void outputObject(Object obj) {
        try {
            //use writeUnshared instead of writeObject if retransmitting same object with modifications
            objectOutput.writeUnshared(obj);
            objectOutput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**closing flux and socket (output before input)*/
    public void endConnection() {
        try {
            objectOutput.close();
            objectInput.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void escapePanelInteraction() {}

    /** adds (true) or remove (false) the keylisteners */
	public void handleKeyListeners(JFrame frame, Boolean bool) {
        if (bool) {
            frame.addKeyListener(playerKeyListener);
        } else {
            frame.removeKeyListener(playerKeyListener);
        }
    }

    /** knows what to do when someone returns to the main menu */
	public void exitGame() {
        outputObject("PLAYER LEFT");
        endConnection();
    }

    public void restartGame() {
        outputObject("RESTART GAME");
    }

    public PlayerKeyListener getPlayerKeyListener() {
        return playerKeyListener;
    }
}