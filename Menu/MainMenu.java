package Menu;

import Game.Board;
import Game.BoardClient;
import Game.BoardGraphism;
import Game.BoardIO;
import Game.BoardLocal;
import Game.BoardServer;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 4L;
	
	/**Contient le jeu */
	private Board board;
	private BoardIO boardIO;
	
	//main menu panels
	private Menu mainMenuPanel;
	private Menu optionPanel;
	private BackgroundPanel backgroundPanel;

	//attributes relative to the option menu
	private KeyBindingMenu redPlayerBindings;
	private KeyBindingMenu bluePlayerBindings;
	private Menu saveQuitOptionsPanel;
	private Menu saveFailedPanel;
	
	/**Contient l'affichage du jeu */
	private BoardGraphism boardGraphism;

	// Multiplayer panels
	private Menu createMultiplayerGamePanel;
	private Menu joinMultiplayerGamePanel;
	private Menu multiplayerPanel;
	private Menu playerLeftPanel;
	private Menu connectionErrorPanel;

	/**escape panel*/
	private Menu escapePanel;
	private Menu endGamePanel;

	//boolean handling escape panel
	private Boolean isDisplayingMainMenu;
	private Boolean isEscapePanelDisplayed = false;

	//boolean to handle unsaved changes in options
	private Boolean unsavedChanges = false;

	//the frame displaying all the stuff
	private JFrame frame;

	//all the KeySelectingPanels
	private ArrayList<KeySelectingPanel> allKeySelectingPanels = new ArrayList<KeySelectingPanel>();

	public MainMenu(JFrame frame) {		
		this.frame = frame;

		//add a key listener for client related keys (available everywhere)
		this.frame.addKeyListener(new ClientRelatedKeyListener());

		//create the 5 panels to be displayed (excluding board)
		backgroundPanel = new BackgroundPanel();
		board = new Board();
		boardGraphism = new BoardGraphism(board);

		board.setBoardGraphism(boardGraphism);

		createMainMenuPanel();

		createKeyBindingMenu();
		createSaveQuitOptionsPanel();
		createSaveFailedPanel();

		createMultiplayerPanel();
		createCreateMultiplayerGamePanel();
		createJoinMultiplayerGamePanel();

		createEscapePanel();
		createPlayerLeftPanel();
		createEndGamePanel();
		createConnectionErrorPanel();

		displayMainMenu();
	}


	/** starts the local board and sets the frame to display it */
	public void startLocalGame() {
		isDisplayingMainMenu = false;

		//init game
		board.initGame();
		boardIO = new BoardLocal(board);
		boardGraphism.startDisplaying();

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
		isDisplayingMainMenu = false;

		//init game
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

		//give the frame the focus
		frame.setFocusable(true);
		frame.setFocusTraversalKeysEnabled(false);

		//displays the game panel
		frame.setContentPane(boardGraphism);
		frame.setVisible(true);
	}

	public void displayMainMenu() {
		//displays the game panel
		backgroundPanel.removeAll();
		backgroundPanel.add(backgroundPanel.getLabel());
		backgroundPanel.add(mainMenuPanel);
		isDisplayingMainMenu = true;
		reloadMenuDisplay();
	}

	/** creates the mainMenuJPanel with its component */
	public void createMainMenuPanel() {
		mainMenuPanel = new Menu();

		//start a game
		mainMenuPanel.addNewButton("PLAY", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startLocalGame();
			}
		});

		//multiplayer menu
		mainMenuPanel.addNewButton("MULTIPLAYER", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				backgroundPanel.remove(mainMenuPanel);
				backgroundPanel.add(multiplayerPanel);
				reloadMenuDisplay();
			}
		});

		//open option menu
		mainMenuPanel.addNewButton("OPTIONS", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the option panel 
				backgroundPanel.remove(mainMenuPanel);
				backgroundPanel.add(optionPanel);
				isDisplayingMainMenu = false;
				reloadMenuDisplay();
			}
		});

		/**closes the app */
		mainMenuPanel.addNewButton("QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				System.exit(0);       
			}
		});

		mainMenuPanel.setDimensions();
		mainMenuPanel.setOrder(true);
	}

	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setBindings() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("red"));
		bluePlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("blue"));
	}

	/** initiates the components of the menu */
	public void createKeyBindingMenu() {
		optionPanel = new Menu();
		optionPanel.displayBorder("OPTIONS");

		// panel containing the bindings
		Menu keyBindingPanel = new Menu();
		//create each player bindings panel
		redPlayerBindings = new KeyBindingMenu("Red player bindings", this);
		bluePlayerBindings = new KeyBindingMenu("Blue player bindings", this);
		addAllKeySelectingPanels();

		redPlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("red"), "KeyBindings/redKeyBindingsDefault.txt");
		bluePlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("blue"), "KeyBindings/blueKeyBindingsDefault.txt");

		addAllKeySelectingPanels();

		redPlayerBindings.setDimensions();
		redPlayerBindings.setOrder(true);
		bluePlayerBindings.setDimensions();
		bluePlayerBindings.setOrder(true);
		
		keyBindingPanel.add(redPlayerBindings);
		keyBindingPanel.add(bluePlayerBindings);
		keyBindingPanel.setDimensions();
		keyBindingPanel.setOrder(false);

		//create a panel to contain the buttons
		Menu buttonPanel = new Menu();

		/** save bindings */
		buttonPanel.addNewButton("SAVE", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				saveOptions(false);
				unsavedChanges = false;
			}
		});

		/** back to main menu */
		buttonPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				if (!unsavedChanges) {
					backToMainMenuFromOption();
				} else {
					backgroundPanel.remove(optionPanel);
					backgroundPanel.add(saveQuitOptionsPanel);
					reloadMenuDisplay();
				}
			}
		});

		/** reset to default bindings */
		buttonPanel.addNewButton("RESET BINDINGS", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				FileFunctions.deleteNonDefaultBindings();
				//recreate the menu with default settings 
				setBindings();
				unsavedChanges = false;
			}
		});

		//make it horizontal
		buttonPanel.setDimensions();
		buttonPanel.setOrder(false);

		//add the player's bindings panels to main panel
		optionPanel.add(keyBindingPanel);
		//and the buttons at the bottm
		optionPanel.add(buttonPanel);

		optionPanel.setOrder(true);
	}

	//create an array list of all KeySelectingPanels
	public void addAllKeySelectingPanels() {
		allKeySelectingPanels.addAll(redPlayerBindings.getKeySelectingPanels());
		allKeySelectingPanels.addAll(bluePlayerBindings.getKeySelectingPanels());
	}

	/** creates the panel that opens when pressing escape */
	public void createEscapePanel() {
		escapePanel = new Menu();
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

	public void createSaveQuitOptionsPanel() {
		saveQuitOptionsPanel = new Menu();
		saveQuitOptionsPanel.displayBorder("WARNING");

		saveQuitOptionsPanel.add(new JLabel("Some changes have not been saved"));

		Menu buttonPanel = new Menu();

		/** resume game */
		buttonPanel.addNewButton("SAVE AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				backgroundPanel.remove(saveQuitOptionsPanel);
				backgroundPanel.add(optionPanel);
				reloadMenuDisplay();
				saveOptions(true);
			}
		});

		/** go back to the main menu */
		buttonPanel.addNewButton("CANCEL AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				backgroundPanel.remove(saveQuitOptionsPanel);				
				backToMainMenuFromOption();
			}
		});

		buttonPanel.setDimensions();
		buttonPanel.setOrder(false);

		// add all the components
		saveQuitOptionsPanel.add(buttonPanel);

		saveQuitOptionsPanel.setDimensions();
		saveQuitOptionsPanel.setOrder(true);
	}


	public void createSaveFailedPanel() {
		saveFailedPanel = new Menu();
		saveFailedPanel.displayBorder("WARNING");

		JLabel info = new JLabel("There's a problem with your bindings");
		saveFailedPanel.add(info);

		/** resume game */
		saveFailedPanel.addNewButton("OK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the option panel
				backgroundPanel.remove(saveFailedPanel);
				backgroundPanel.add(optionPanel);
				reloadMenuDisplay();				
			}
		});

		saveFailedPanel.setDimensions();		
		saveFailedPanel.setOrder(true);
	}

	/** creates the multiplayerJPanel with its component*/
	public void createMultiplayerPanel() {
		multiplayerPanel = new Menu();
		multiplayerPanel.displayBorder("MULTIPLAYER");

		//create a joinable game
		multiplayerPanel.addNewButton("CREATE GAME", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the creatingGame panel 
				backgroundPanel.remove(multiplayerPanel);
				backgroundPanel.add(createMultiplayerGamePanel);
				reloadMenuDisplay();
				startOnlineGame();
			}
		});

		//join an existing game
		multiplayerPanel.addNewButton("JOIN GAME", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the joiningGame panel 
				backgroundPanel.remove(multiplayerPanel);
				backgroundPanel.add(joinMultiplayerGamePanel);
				reloadMenuDisplay();			
			}
		});

		//back to main menu
		multiplayerPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the mainMenu panel 
				backgroundPanel.remove(multiplayerPanel);
				backgroundPanel.add(mainMenuPanel);
				reloadMenuDisplay();
			}
		});

		multiplayerPanel.setDimensions();
		multiplayerPanel.setOrder(true);
	}

	/** creates the createMultiplayerGamePanel with its component*/
	public void createCreateMultiplayerGamePanel() {
		createMultiplayerGamePanel = new Menu();
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

		Menu buttonPanel = new Menu();

		//back to main menu
		buttonPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the mainMenu panel 
				backgroundPanel.remove(createMultiplayerGamePanel);
				backgroundPanel.add(multiplayerPanel);
				reloadMenuDisplay();
				timer.stop();
			}
		});

		createMultiplayerGamePanel.add(buttonPanel);
		createMultiplayerGamePanel.setOrder(true);
	}

	/** creates the joinMultiplayerGamePanel with its component*/
	public void createJoinMultiplayerGamePanel() {
		joinMultiplayerGamePanel = new Menu();
		joinMultiplayerGamePanel.displayBorder("JOIN A GAME");

		//create a joinable game
		joinMultiplayerGamePanel.add(new JLabel("Enter your great enemy's server IP : "));

		//join an existing game
		JTextField enemyIP = new JTextField();
		joinMultiplayerGamePanel.add(enemyIP);

		Menu buttonPanel = new Menu();

		//back to main menu
		buttonPanel.addNewButton("JOIN", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				joinOnlineGame(enemyIP.getText());
			}
		});

		//back to main menu
		buttonPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the multiplayer panel 
				backgroundPanel.remove(joinMultiplayerGamePanel);
				backgroundPanel.add(multiplayerPanel);
				reloadMenuDisplay();
			}
		});

		buttonPanel.setOrder(false);

		joinMultiplayerGamePanel.add(buttonPanel);
		joinMultiplayerGamePanel.setDimensions();
		joinMultiplayerGamePanel.setOrder(true);
	}

	/** creates the playerLeftPanel with its component*/
	public void createPlayerLeftPanel() {
		playerLeftPanel = new Menu();
		playerLeftPanel.displayBorder("GAME ENDED");

		//create a joinable game
		playerLeftPanel.add(new JLabel("The other player has cowardly left "));

		//back to main menu
		playerLeftPanel.addNewButton("BACK TO MAIN MENU", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the multiplayer panel
				boardIO.handleKeyListeners(frame, false);
				backgroundPanel.removeAll();
				backgroundPanel.add(mainMenuPanel);
				boardGraphism.remove(playerLeftPanel);
				reloadMenuDisplay();
			}
		});

		playerLeftPanel.setDimensions();
		playerLeftPanel.setOrder(true);
	}

	/** creates the playerLeftPanel with its component*/
	public void createEndGamePanel() {
		endGamePanel = new Menu();
		endGamePanel.displayBorder("GAME ENDED");

		//create a joinable game
		endGamePanel.add(new JLabel("Well that was fun"));

		Menu buttonPanel = new Menu();

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
				//displays the main menu panel 
				backgroundPanel.removeAll();
				backgroundPanel.add(mainMenuPanel);
				boardGraphism.remove(endGamePanel);
				boardIO.exitGame();
				reloadMenuDisplay();
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
		connectionErrorPanel = new Menu();
		connectionErrorPanel.displayBorder("ERROR");

		//couldn't connect
		connectionErrorPanel.add(new JLabel("Couldn't connect to the server"));

		//back
		connectionErrorPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the main menu panel 
				boardIO.handleKeyListeners(frame, false);
				backgroundPanel.removeAll();
				backgroundPanel.add(joinMultiplayerGamePanel);
				reloadMenuDisplay();
			}
		});

		connectionErrorPanel.setDimensions();
		connectionErrorPanel.setOrder(true);
	}

	/** display connection error panel */
	public void displayConnectionErrorPanel() {
		backgroundPanel.removeAll();
		backgroundPanel.add(connectionErrorPanel);
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


	/** saves options if they are valid */
	public void saveOptions(Boolean back) {
		//saves the current (the ones being displayed) keyBindings if they are all unique
		if (checkUnicity()) {
			KeyBindings redBindings = redPlayerBindings.getCurrentKeyBindings();
			KeyBindings blueBindings = bluePlayerBindings.getCurrentKeyBindings();
			FileFunctions.saveObject(redBindings, "KeyBindings/redKeyBindings.txt");
			FileFunctions.saveObject(blueBindings, "KeyBindings/blueKeyBindings.txt");
			if (back) {
				backToMainMenuFromOption();
			}
		} else {
			backgroundPanel.remove(optionPanel);
			backgroundPanel.add(saveFailedPanel);
			reloadMenuDisplay();
		}
	}


	/** check if every binding is unique */
	public boolean checkUnicity() {
		for (KeySelectingPanel keySelectingPanel : allKeySelectingPanels) {
			for (KeySelectingPanel keySelectingPanel2 : allKeySelectingPanels) {
				if (keySelectingPanel != keySelectingPanel2 && keySelectingPanel.getCurrentKeyBinding().getKeyValue() == keySelectingPanel2.getCurrentKeyBinding().getKeyValue()) {
					return false;
				}
			}
			//there must be a way to decrease complexity by half and avoid testing twice
		}
		return true;
	}


	/** displays main menu and cancel unsaved bindings changes */
	public void backToMainMenuFromOption() {
		//all options are saved
		unsavedChanges = false;
		//displays the main menu again 
		displayMainMenu();
		//cancels changes if they are not saved
		setBindings();
	}

	/** Find public IP address */
	public String getPublicIPAddress() {
		try { 
			URL url_name = new URL("http://bot.whatismyipaddress.com"); 
	
			BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream())); 
	
			// reads system IPAddress 
			return sc.readLine().trim(); 
		} catch (Exception e) { 
			return "COULD NOT FIND ADDRESS"; 
		} 
	}

	public void setUnsavedChanges(Boolean bool) {
		unsavedChanges = bool;
	}

	public Board getBoard() {
		return board;
	}

	public KeyBindingMenu getRedPlayerBindingMenu() {
		return redPlayerBindings;
	}

	public KeyBindingMenu getBluePlayerBindingMenu() {
		return bluePlayerBindings;
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
				if (frame.getContentPane() == backgroundPanel && !isDisplayingMainMenu) {
					backToMainMenuFromOption();
				} else if (frame.getContentPane() == boardGraphism) {
					toggleEscapePanel();
				}
			}
		}

	}

}