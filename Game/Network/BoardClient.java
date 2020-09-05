package Game.Network;

import Game.Board;
import Game.BoardGraphism;
import Game.GameLoop;
import Game.PlayerKeyListener;
import Game.InputActions;
import Menu.FileFunctions;
import Menu.Options.KeyBindings.KeyBindings;
import Menu.MainMenu;

import java.net.*;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/** handles the key listener for online game */
public class BoardClient extends BoardIO {
    //board graphism
    protected BoardGraphism boardGraphism;

    // private Board board;

    //client input related
	private PlayerKeyListener playerKeyListener;
	private InputActions playerInputActions = new InputActions();

	//am i connected or not
	private Boolean connected;
	
	//client socket
    private ExtendedSocket socket;

    //address of the server
    private String address;

    //mainmenu to start displaying the game when everyone has joined
    private MainMenu mainMenu;

    //timer for ping tests
    private Timer ping;
    private long startTime;

    //port number
    private final int portNumber = 5000; 

    //game loop
	// private GameLoop gameLoop;

	public BoardClient(BoardGraphism boardGraphism, String address, MainMenu mainMenu) {
        this.boardGraphism = boardGraphism;
        this.address = address;
        this.mainMenu = mainMenu;
        
        // board = new Board();
        // gameLoop = new GameLoop(board);
		KeyBindings playerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("optionSaves/redKeyBindings.txt", "optionSaves/redKeyBindingsDefault.txt"));
        playerKeyListener = new PlayerKeyListener(playerBindings, this, playerInputActions);
        ping = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                outputObject("PING");
                startTime = System.currentTimeMillis();
			}
        });
    }

    public void run() {
        try {
            // Creation du socket et des flux d'entree/sortie
            socket = new ExtendedSocket(0, new Socket(address, portNumber), false);
            connected = true;

            //ping.start();
            inputObject();
        } catch (Exception e) {
            e.printStackTrace();
            mainMenu.displayConnectionErrorPanel();
        }
    }
    
    /** handles every object received */
    public void inputObject() {
        while (connected) {
            Object obj = socket.readObject();
            if (obj == null) {
                //server closed unexpectedly
                closeClient();
                mainMenu.displayConnectionErrorPanel();
            } else if (obj instanceof String) {
                if (((String)obj).equals("GAME STARTED")) {
                    mainMenu.displayGame();
                    // gameLoop.togglePause(false);
                } else if (((String)obj).equals("PLAYER LEFT")) {
                    connected = false;
                    mainMenu.displayPlayerLeftPanel();
                    // gameLoop.togglePause(true);
                } else if (((String)obj).equals("PING")) {
                    System.out.println("PING " + (System.currentTimeMillis() - startTime) + "ms");
                }
            } else if (obj instanceof Board) {
                // this.board = (Board)obj;
                boardGraphism.setBoard((Board)obj);
            }
        }
    }

    /** send the input action object to the server */
	public void handleAction(InputActions inputActions) {
		outputObject(inputActions);
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

    /** tries outputting an object to the server, if it fails it displays a connection error */
    public void outputObject(Object obj) {
        if (!socket.outputObject(obj)) {
            closeClient();
            mainMenu.displayConnectionErrorPanel();
        }
    }

    /** closes the client and displays connection error */
    public void closeClient() {
        connected = false;
        ping.stop();
    }

    /** knows what to do when someone returns to the main menu */
	public void exitGame() {
        socket.endConnection();
        closeClient();
        mainMenu.displayMainMenu();
    }

    /** ask the server to restart the game */
    public void restartGame() {
        outputObject("RESTART GAME");
    }
}