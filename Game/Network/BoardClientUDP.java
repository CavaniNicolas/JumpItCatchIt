package Game.Network;

import Game.Board;
import Game.BoardGraphism;
// import Game.GameLoop;
import Game.PlayerKeyListener;
import Game.InputActions;
import Menu.FileFunctions;
import Menu.Options.KeyBindings.KeyBindings;
import Menu.MainMenu;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/** handles the key listener for online game */
public class BoardClientUDP extends BoardIO {
    //board graphism
    protected BoardGraphism boardGraphism;

    //is connected
    private Boolean connected;

    //client input related
	private PlayerKeyListener playerKeyListener;
	private InputActions playerInputActions = new InputActions();
	
	//client socket
    private ExtendedSocketUDP socket;

    //mainmenu to start displaying the game when everyone has joined
    private MainMenu mainMenu;

    //timer for ping tests
    private Timer ping;
    private long startTime;

    //port number
    private final int portNumber = 5000; 

    //game loop
	// private GameLoop gameLoop;

	public BoardClientUDP(BoardGraphism boardGraphism, String address, MainMenu mainMenu, JFrame frame) {
        super(frame);
        this.boardGraphism = boardGraphism;
        this.mainMenu = mainMenu;
        
        connect(address);

        // board = new Board();
        // gameLoop = new GameLoop(board);
		KeyBindings playerBindings = (KeyBindings)FileFunctions.getObject(FileFunctions.getPathFileToUse("optionSaves/redKeyBindings.txt", "optionSaves/redKeyBindingsDefault.txt"));
        playerKeyListener = new PlayerKeyListener(playerBindings, this, playerInputActions);
    }

    /** connect to the server represented by its address */
    public void connect(String address) {
        socket = new ExtendedSocketUDP();
        if (socket.initializeConnection(address, portNumber) && socket.initializeStreams(false)) {
            connected = true;
            outputObject("RESTART GAME");
            Thread input = new Thread(new HandleServerInput());
            input.start();
            //togglePinging(true);
        } else {
            mainMenu.displayConnectionErrorPanel();
        }
    }
    
    /** handles every object received */
    public class HandleServerInput extends Thread {
        public void run() {
            while (connected) {
                Object obj = socket.readObject().getObj();
                if (obj == null) {
                    //server closed unexpectedly
                    closeClient();
                    mainMenu.displayServerStoppedPanel();
                } else if (obj instanceof String) {
                    System.out.println(obj);
                    if (((String)obj).equals("GAME STARTED")) {
                        mainMenu.displayGame();
                        // gameLoop.togglePause(false);
                    } else if (((String)obj).equals("PLAYER LEFT")) {
                        closeClient();
                        mainMenu.displayPlayerLeftPanel();
                    } else if (((String)obj).equals("PING")) {
                        System.out.println("PING " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                } else if (obj instanceof Board) {
                    // this.board = (Board)obj;
                    boardGraphism.setBoard((Board)obj);
                }
            }
        }
    }

    /** send a ping request every second, computes the ping */
    public void togglePinging(Boolean start) {
        if (start) {
            ping = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    outputObject("PING");
                    startTime = System.currentTimeMillis();
                }
            });
            ping.start();
        } else {
            ping.stop();
        }
    }

    /** send the input action object to the server */
	public void handleAction(InputActions inputActions) {
		outputObject(inputActions);
	}

    public void escapePanelInteraction() {}

    /** adds (true) or remove (false) the keylisteners */
	public void handleKeyListeners(Boolean bool) {
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
        //togglePinging(false);
        handleKeyListeners(false);
        // gameLoop.togglePause(true);  
    }

    /** knows what to do when someone returns to the main menu */
	public void exitGame() {
        socket.endConnection();
        closeClient();
        mainMenu.displayMainMenu();
    }
}