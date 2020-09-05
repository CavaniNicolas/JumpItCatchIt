package Menu;

import Game.Board;
import Game.BoardGraphism;
import Game.Network.BoardClient;
import Game.Network.BoardIO;
import Game.Network.BoardLocal;
import Game.Network.BoardServer;
import Menu.Options.KeyOptionMenu;
import Menu.Options.OptionMenu;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class MainMenu {
	private static final long serialVersionUID = -3619542431333472213L;

	/** Contient le jeu */
	private Board board;
	private BoardIO boardIO;
	
	//main menu panels
	private Menu mainMenuPanel;
	private OptionMenu optionPanel;
	private BackgroundPanel backgroundPanel;
	private JPanel menuPanel;
	
	/**Contient l'affichage du jeu */
	private BoardGraphism boardGraphism;

	// Multiplayer panels
	private Menu createMultiplayerGamePanel;
	private Menu joinMultiplayerGamePanel;
	private Menu multiplayerPanel;
	private Menu playerLeftPanel;
	private Menu connectionErrorPanel;
	private Menu connectingPanel;

	/**escape panel*/
	private Menu escapePanel;
	private Menu endGamePanel;

	//boolean handling escape panel
	private Boolean isEscapePanelDisplayed = false;

	//the frame displaying all the stuff
	private JFrame frame;

	public MainMenu(JFrame frame) {		
		this.frame = frame;

		//add a key listener for client related keys (available everywhere)
		this.frame.addKeyListener(new ClientRelatedKeyListener());
		//give the frame the focus
		frame.setFocusable(true);
		//frame.setFocusTraversalKeysEnabled(false);

		//create the different panels to be displayed 
		backgroundPanel = new BackgroundPanel(frame);
		menuPanel = backgroundPanel.getMenuPanel();
		board = new Board();
		boardGraphism = new BoardGraphism(board);

		board.setBoardGraphism(boardGraphism);

		createMenus();

		displayMainMenu();
	}

	/** starts the local board and sets the frame to display it */
	public void startLocalGame() {
		//init game
		board.initGame();
		boardIO = new BoardLocal(board);

		// Indique a BoardGraphism que c'est une LocalGame, utile pour afficher les HUD
		boardGraphism.setTypeOfGame(BoardGraphism.LOCAL_GAME);

		//add the key listeners
		boardIO.handleKeyListeners(frame, true);

		//start game
		boardIO.togglePause(false);

		displayGame();
	}

	/** starts the online board and sets the frame to display it */
	public void startOnlineGame() {
		Thread threadServer = new Thread(new BoardServer());
		threadServer.start();

		// Indique a BoardGraphism que c'est une LocalGame, utile pour afficher les HUD
		boardGraphism.setTypeOfGame(BoardGraphism.ONLINE_GAME);

		//sleep to avoid joining a game before the server was started
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}

		joinOnlineGame("127.0.0.1");
	}

	/** starts the online board and sets the frame to display it */
	public void joinOnlineGame(String address) {
		//init game, shouldn't be necessary (if not included the platforms are not displayed)
		board.initGame();

		//start a client on a thread
		boardIO = new BoardClient(boardGraphism, address, this);
		Thread threadClient = new Thread(boardIO);
		threadClient.start();

		//add the key listeners
		boardIO.handleKeyListeners(frame, true);
	}

	public void displayGame() {
		//start gamedisplay timer
		boardGraphism.startDisplaying();

		//displays the game panel
		frame.setContentPane(boardGraphism);
		frame.setVisible(true);
	}

	public void displayMainMenu() {
		//displays the game panel
		menuPanel.removeAll();
		menuPanel.add(mainMenuPanel);
		reloadMenuDisplay();
	}

	public void createMenus() {
		mainMenuPanel = new Menu(backgroundPanel, mainMenuPanel);

		optionPanel = new OptionMenu(new KeyOptionMenu(), backgroundPanel, mainMenuPanel);
		multiplayerPanel = new Menu(backgroundPanel, mainMenuPanel);

		joinMultiplayerGamePanel = new Menu(backgroundPanel, multiplayerPanel);
		connectionErrorPanel = new Menu(backgroundPanel, joinMultiplayerGamePanel);
		connectingPanel = new Menu(backgroundPanel, joinMultiplayerGamePanel);
		playerLeftPanel = new Menu(backgroundPanel, mainMenuPanel);

		escapePanel = new Menu();
		createMultiplayerGamePanel = new Menu();
		endGamePanel = new Menu();

		createMainMenuPanel();

		createKeyBindingMenu();

		createMultiplayerPanel();
		createCreateMultiplayerGamePanel();
		createJoinMultiplayerGamePanel();

		createEscapePanel();
		createPlayerLeftPanel();
		createEndGamePanel();
		createConnectionErrorPanel();
		createConnectingPanel();
	}

	/** creates the mainMenuJPanel with its component */
	public void createMainMenuPanel() {
		//start a game
		mainMenuPanel.addNewButton("PLAY", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startLocalGame();
			}
		});

		//multiplayer menu
		mainMenuPanel.addNewButton("MULTIPLAYER", multiplayerPanel);
		mainMenuPanel.addNewButton("OPTIONS", optionPanel);

		/**closes the app */
		mainMenuPanel.addNewButton("QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				System.exit(0);       
			}
		});

		mainMenuPanel.setDimensions();
		mainMenuPanel.setOrder(true);

		mainMenuPanel.setOpaque(false);
	}

	/** initiates the components of the menu */
	public void createKeyBindingMenu() {
		optionPanel.displayBorder("OPTIONS");

		optionPanel.setOrder(true);
	}

	/** creates the panel that opens when pressing escape */
	public void createEscapePanel() {
		escapePanel.displayBorder("PAUSE");

		/** go back to the main menu */
		escapePanel.addNewButton("BACK TO MAIN MENU", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//sets the content pane to the main menu and delete board (game has ended)
				toggleEscapePanel();

				boardIO.exitGame();
				boardIO.handleKeyListeners(frame, false);

				displayMainMenu();
			}
		});

		/** resume game */
		escapePanel.addNewButton("RESUME", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				toggleEscapePanel();
			}
		});
		
		escapePanel.setDimensions();
		escapePanel.setOrder(true);
	}

	public void toggleEscapePanel() {
		//escape button
		if (!isEscapePanelDisplayed) {
			boardGraphism.add(escapePanel);
			boardIO.togglePause(true);
		} else {
			boardGraphism.remove(escapePanel);
			boardIO.togglePause(false);
		}
		isEscapePanelDisplayed = !isEscapePanelDisplayed;
		frame.setVisible(true);
	}

	/** creates the multiplayerJPanel with its component*/
	public void createMultiplayerPanel() {
		multiplayerPanel.displayBorder("MULTIPLAYER");

		//create a joinable game
		multiplayerPanel.addNewButton("CREATE GAME", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the creatingGame panel 
				menuPanel.remove(multiplayerPanel);
				menuPanel.add(createMultiplayerGamePanel);
				reloadMenuDisplay();
				startOnlineGame();
			}
		});

		multiplayerPanel.addNewButton("JOIN GAME", joinMultiplayerGamePanel);
		multiplayerPanel.addNewButton("BACK");

		multiplayerPanel.setDimensions();
		multiplayerPanel.setOrder(true);
	}

	/** creates the createMultiplayerGamePanel with its component*/
	public void createCreateMultiplayerGamePanel() {
		createMultiplayerGamePanel.displayBorder("CREATING A GAME");

		//create a joinable game
		createMultiplayerGamePanel.add(new JLabel("Your game is available on : "));

		Menu addressPanel = new Menu();
		addressPanel.add(new JLabel(getPublicIPAddress()));
		addressPanel.addNewButton("COPY", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(getPublicIPAddress()), null);
			}
		});

		createMultiplayerGamePanel.add(addressPanel);

		//join an existing game
		JLabel waiting = new JLabel("Waiting for your great enemy ...");
	
		Timer timer = new Timer(500, new ActionListener() {
			String baseString = "...   ";
			int beginning = 0;
			public void actionPerformed(ActionEvent arg0) {
				waiting.setText("Waiting for your great enemy " + (baseString + baseString).substring(beginning, beginning + 3));
				beginning = (baseString.length() + beginning - 1)% baseString.length();
			}
		});
		timer.start();

		createMultiplayerGamePanel.add(waiting);

		Menu buttonPanel = new Menu(backgroundPanel, multiplayerPanel);

		//back to main menu
		buttonPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the mainMenu panel 
				buttonPanel.menuInteraction();
				timer.stop();
			}
		});

		createMultiplayerGamePanel.add(buttonPanel);
		createMultiplayerGamePanel.setOrder(true);
	}

	/** creates the joinMultiplayerGamePanel with its component*/
	public void createJoinMultiplayerGamePanel() {
		joinMultiplayerGamePanel.displayBorder("JOIN A GAME");

		//create a joinable game
		joinMultiplayerGamePanel.add(new JLabel("Enter your great enemy's server IP : "));

		//join an existing game
		JTextField enemyIP = new JTextField();
		joinMultiplayerGamePanel.add(enemyIP);

		Menu buttonPanel = new Menu(backgroundPanel, multiplayerPanel);

		//back to main menu
		buttonPanel.addNewButton("JOIN", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonPanel.menuInteraction(connectingPanel);
				joinOnlineGame(enemyIP.getText());
			}
		});

		//back to main menu
		buttonPanel.addNewButton("BACK");

		buttonPanel.setOrder(false);

		joinMultiplayerGamePanel.add(buttonPanel);
		joinMultiplayerGamePanel.setDimensions();
		joinMultiplayerGamePanel.setOrder(true);
	}

	/** creates the playerLeftPanel with its component*/
	public void createPlayerLeftPanel() {
		playerLeftPanel.displayBorder("GAME ENDED");

		//create a joinable game
		playerLeftPanel.add(new JLabel("The other player has cowardly left "));

		//back to main menu
		playerLeftPanel.addNewButton("BACK TO MAIN MENU", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the multiplayer panel
				boardIO.handleKeyListeners(frame, false);
				menuPanel.removeAll();
				displayMainMenu();
			}
		});

		playerLeftPanel.setDimensions();
		playerLeftPanel.setOrder(true);
	}

	/** creates the gameEndedPanel with its component*/
	public void createEndGamePanel() {
		endGamePanel.displayBorder("GAME ENDED");

		//create a joinable game
		endGamePanel.add(new JLabel("Well that was fun"));

		Menu buttonPanel = new Menu(backgroundPanel, mainMenuPanel);

		//restart
		buttonPanel.addNewButton("RESTART", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boardGraphism.remove(endGamePanel);
				boardIO.restartGame();
			}
		});

		//back to main menu
		buttonPanel.addNewButton("BACK TO MAIN MENU", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonPanel.menuInteraction();
				boardIO.exitGame();
			}
		});

		buttonPanel.setDimensions();
		buttonPanel.setOrder(false);
		endGamePanel.add(buttonPanel);
		endGamePanel.setDimensions();
		endGamePanel.setOrder(true);
	}

	/** creates the playerLeftPanel with its component*/
	public void createConnectionErrorPanel() {
		connectionErrorPanel.displayBorder("ERROR");
		connectionErrorPanel.add(new JLabel("Couldn't connect to the server"));
		connectionErrorPanel.addNewButton("BACK");
		connectionErrorPanel.setOrder(true);
	}

	public void createConnectingPanel() {
		connectingPanel.displayBorder("CONNECTION");
		JLabel info = new JLabel(" Connecting ... ");

		Timer timer = new Timer(500, new ActionListener() {
			String baseString = "...   ";
			int beginning = 0;
			public void actionPerformed(ActionEvent arg0) {
				info.setText(" Connecting " + (baseString + baseString).substring(beginning, beginning + 3) + " ");
				beginning = (baseString.length() + beginning - 1)% baseString.length();
			}
		});
		timer.start();

		connectingPanel.add(info);
		connectingPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectingPanel.menuInteraction();
				boardIO.exitGame();
			}
		});

		connectingPanel.setOrder(true);
	}

	/** display connection error panel */
	public void displayConnectionErrorPanel() {
		menuPanel.removeAll();
		menuPanel.add(connectionErrorPanel);
		reloadMenuDisplay();
	}

	/** display player left panel */
	public void displayPlayerLeftPanel() {
		boardGraphism.add(playerLeftPanel);
		boardIO.handleKeyListeners(frame, false);
		frame.setVisible(true);
	}

	/** reloads the displays (avoid former panels to be displayed) */
	public void reloadMenuDisplay() {
		frame.setContentPane(backgroundPanel);
		frame.setVisible(true);
	}

	/** Find public IP address */
	public String getPublicIPAddress() {
		Boolean addressFound = false;
		try {
			Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
			
			while(list.hasMoreElements() && !addressFound){
			  
			NetworkInterface ni = list.nextElement();
			Enumeration<InetAddress> listAddress = ni.getInetAddresses();
			
				while(listAddress.hasMoreElements()){
					InetAddress address = listAddress.nextElement();
					if (!address.isAnyLocalAddress() && !address.isLoopbackAddress() && !address.isLinkLocalAddress() && !address.isSiteLocalAddress()) {
						return address.getHostAddress();
					}
				}
		   	}
		} catch (SocketException e) {
		   e.printStackTrace();
		}
		return null;
	}

	public Board getBoard() {
		return board;
	}

	public class ClientRelatedKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent event) {}

		@Override
		public void keyReleased(KeyEvent event) {}

		@Override
		public void keyTyped(KeyEvent event) {
			int code = event.getKeyChar();

			//escape
			if (code == 27) {
				if (frame.getContentPane() == backgroundPanel) {
					Menu menu =  (Menu)menuPanel.getComponent(menuPanel.getComponentCount()-1);
					menu.menuInteraction();
				} else {
					toggleEscapePanel();
				}
			}
		}

	}

}