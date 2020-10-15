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
public class BoardClient extends BoardIO {
    //board graphism
    protected BoardGraphism boardGraphism;

    //is connected
    private Boolean connected;

    //client input related
	private PlayerKeyListener playerKeyListener;
	private InputActions playerInputActions = new InputActions();
	
	//client socket
    private ConnectionHandler socket;
    private DestinationMachine dest;

    //mainmenu to start displaying the game when everyone has joined
    private MainMenu mainMenu;

    //timer for ping tests
    private Timer ping;
    private long startTime;

    //port number
    private final int portServerTCP = 5000; 
    private final int portServerUDP = 5001;
    private final int portClientUDP = 6001;

    //game loop
	// private GameLoop gameLoop;

	public BoardClient(BoardGraphism boardGraphism, String address, MainMenu mainMenu, JFrame frame) {
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
        socket = new ConnectionHandler(portClientUDP);
        dest = socket.initializeConnection(address, portServerTCP, portServerUDP);
        if (socket.initializeStreams() && dest != null) {
            connected = true;
            new Thread(new HandleServerInput()).start();

            dest.outputObject("PORTDATA " + portClientUDP);
            dest.outputObject("RESTART GAME");
            //togglePinging(true);
        } else {
            mainMenu.displayConnectionErrorPanel();
        }
    }
    
    /** handles every object received */
    public class HandleServerInput extends Thread {
        public void run() {
            while (connected) {
                try {
                    Object obj = dest.getQueue().take();
                    if (obj instanceof String) {
                        System.out.println(obj);
                        if (((String)obj).equals("GAME STARTED")) {
                            mainMenu.displayGame();
                            // gameLoop.togglePause(false);
                        } else if (((String)obj).equals("PLAYER LEFT")) {
                            closeClient();
                            mainMenu.displayPlayerLeftPanel();
                        } else if (((String)obj).equals("PING")) {
                            System.out.println("PING " + (System.currentTimeMillis() - startTime) + "ms");
                        } else if (((String)obj).equals("SERVER STOPPED")){
                            closeClient();
                            mainMenu.displayServerStoppedPanel();
                        }
                    } else if (obj instanceof Board) {
                        // System.out.println("RECEIVED BOARD");
                        // this.board = (Board)obj;
                        boardGraphism.setBoard((Board)obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //togglePinging(false);
        }
    }

    /** send a ping request every second, computes the ping */
    public void togglePinging(Boolean start) {
        if (start) {
            ping = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    outputObject("PING", false);
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
		outputObject(inputActions, true);
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
    public void outputObject(Object obj, Boolean protocolUDP) {
        if (!socket.outputObjectToAll(obj, protocolUDP)) {
            closeClient();
            mainMenu.displayConnectionErrorPanel();
        }
    }

    /** closes the client and displays connection error */
    public void closeClient() {
        //togglePinging(false);
        socket.endConnection();
        handleKeyListeners(false);
        // gameLoop.togglePause(true);  
    }

    /** knows what to do when someone returns to the main menu */
	public void exitGame() {
        closeClient();
        mainMenu.displayMainMenu();
    }
}