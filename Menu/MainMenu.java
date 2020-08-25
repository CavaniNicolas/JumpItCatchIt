package Menu;

import Game.Board;
import Game.BoardClient;
import Game.BoardGraphism;
import Game.BoardIO;
import Game.BoardLocal;
import Game.BoardServer;
import Game.GameLoop;

import java.io.*;
import java.net.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 4L;
	
	/**Contient le jeu */
	private Board board;

	private BoardIO boardIO;

	/**Contient la boucle principale de calcul du jeu */
	private GameLoop gameLoop;
	
	//main menu panels
	private JPanel mainMenuPanel;
	private JPanel optionPanel;
	private BackgroundPanel backgroundPanel;

	//attributes relative to the option menu
	private KeyBindingMenu redPlayerBindings;
	private KeyBindingMenu bluePlayerBindings;
	private JPanel saveQuitOptionsPanel;
	private JPanel saveFailedPanel;
	
	/**Contient l'affichage du jeu */
	private BoardGraphism boardGraphism;

	// Multiplayer panels
	private JPanel createMultiplayerGamePanel;
	private JPanel joinMultiplayerGamePanel;
	private JPanel multiplayerPanel;

	/**escape panel*/
	private JPanel escapePanel;

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
		createEscapePanel();
		createSaveQuitOptionsPanel();
		createSaveFailedPanel();

		createMultiplayerPanel();
		createCreateMultiplayerGamePanel();
		createJoinMultiplayerGamePanel();

    	/** display main menu*/
		backgroundPanel.add(mainMenuPanel);
		isDisplayingMainMenu = true;
		this.frame.setContentPane(backgroundPanel);
		this.frame.setVisible(true);
	}


	/** starts the local board and sets the frame to display it */
	public void startLocalGame() {
		isDisplayingMainMenu = false;

		gameLoop = new GameLoop(board);

		//init game
		board.initGame();
		boardIO = new BoardLocal(board, boardGraphism);
		boardGraphism.startDisplaying();

		//add the key listeners
		boardIO.addKeyListeners(frame);

		//start game
		Thread thread = new Thread(new StartGame());
		thread.start();

		displayGame();
	}

	/** starts the online board and sets the frame to display it */
	public void startOnlineGame() {
		gameLoop = new GameLoop(board);

		Thread threadServer = new Thread(new BoardServer(board));
		threadServer.start();

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
		boardIO.addKeyListeners(frame);
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
		backgroundPanel.add(mainMenuPanel);
		isDisplayingMainMenu = true;
		reloadDisplay();
	}

	/** creates the mainMenuJPanel with its component */
	public void createMainMenuPanel() {
		mainMenuPanel = new JPanel();

		//create a panel to contain the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(160, 130));

		//start a game
		JButton playButton = new JButton("PLAY");
		playButton.setPreferredSize(new Dimension(150, 25));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startLocalGame();
			}
		});

		//multiplayer menu
		JButton multiplayerButton = new JButton("MULTIPLAYER");
		multiplayerButton.setPreferredSize(new Dimension(150, 25));
		multiplayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				backgroundPanel.remove(mainMenuPanel);
				backgroundPanel.add(multiplayerPanel);
				reloadDisplay();
			}
		});

		//open option menu
		JButton optionButton = new JButton("OPTIONS");
		optionButton.setPreferredSize(new Dimension(150, 25));
    	optionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the option panel 
				backgroundPanel.remove(mainMenuPanel);
				backgroundPanel.add(optionPanel);
				isDisplayingMainMenu = false;
				reloadDisplay();
			}
		});

		/**closes the app */
		JButton quitButton = new JButton("QUIT");
		quitButton.setPreferredSize(new Dimension(150, 25));
    	quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				System.exit(0);       
			}
		});

		buttonPanel.add(playButton);
		buttonPanel.add(multiplayerButton);
		buttonPanel.add(optionButton);
		buttonPanel.add(quitButton);

		mainMenuPanel.add(buttonPanel);
	}



	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setBindings() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("red"));
		bluePlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("blue"));
	}

	/** initiates the components of the menu */
	public void createKeyBindingMenu() {
		optionPanel = new JPanel();

		//smaller panel so that the options don't take the whole screen
		JPanel optionPanel2 = new JPanel();
		optionPanel2.setBorder(BorderFactory.createTitledBorder("OPTIONS"));
		optionPanel2.setBackground(Color.white);
		optionPanel2.setPreferredSize(new Dimension(630, 320));

		//create a panel to contain the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.white);
		buttonPanel.setPreferredSize(new Dimension(480, 30));

		//create each player bindings panel
		redPlayerBindings = new KeyBindingMenu("Red player bindings", this);
		bluePlayerBindings = new KeyBindingMenu("Blue player bindings", this);
		addAllKeySelectingPanels();

		redPlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("red"), "KeyBindings/redKeyBindingsDefault.txt");
		bluePlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("blue"), "KeyBindings/blueKeyBindingsDefault.txt");

		addAllKeySelectingPanels();

		/** save bindings */
		JButton saveButton = new JButton("SAVE BINDINGS");
		saveButton.setPreferredSize(new Dimension(150, 25));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				saveOptions(false);
				unsavedChanges = false;
			}
		});

		/** back to main menu */
		JButton backButton = new JButton("BACK");
		backButton.setPreferredSize(new Dimension(150, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				if (!unsavedChanges) {
					backToMainMenuFromOption();
				} else {
					backgroundPanel.remove(optionPanel);
					backgroundPanel.add(saveQuitOptionsPanel);
					reloadDisplay();
				}
			}
		});

		/** reset to default bindings */
		JButton defaultButton = new JButton("RESET BINDINGS");
		defaultButton.setPreferredSize(new Dimension(150, 25));
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				FileFunctions.deleteNonDefaultBindings();
				//recreate the menu with default settings 
				setBindings();
				unsavedChanges = false;
			}
		});

		//add the buttons to button panel
		buttonPanel.add(saveButton);
		buttonPanel.add(backButton);
		buttonPanel.add(defaultButton);

		//add the player's bindings panels to main panel
		optionPanel2.add(redPlayerBindings);
		optionPanel2.add(bluePlayerBindings);
		//and the buttons at the bottm
		//buttonPanel.setLocation(10, 500);
		optionPanel2.add(buttonPanel);

		optionPanel.add(optionPanel2);
	}


	//create an array list of all KeySelectingPanels
	public void addAllKeySelectingPanels() {
		allKeySelectingPanels.addAll(redPlayerBindings.getKeySelectingPanels());
		allKeySelectingPanels.addAll(bluePlayerBindings.getKeySelectingPanels());
	}



	/** creates the panel that opens when pressing escape */
	public void createEscapePanel() {
		escapePanel = new JPanel();
		escapePanel.setBorder(BorderFactory.createTitledBorder("PAUSE"));
		escapePanel.setBackground(Color.white);
		escapePanel.setPreferredSize(new Dimension(220, 90));

		/** go back to the main menu */
		JButton backButton = new JButton("BACK TO MAIN MENU");
		backButton.setPreferredSize(new Dimension(180, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//sets the content pane to the main menu and delete board (game has ended)
				toggleEscapePanel();

				boardIO.exitGame();
				isDisplayingMainMenu = true;
				frame.setContentPane(backgroundPanel);
			}
		});

		/** resume game */
		JButton resumeButton = new JButton("RESUME");
		resumeButton.setPreferredSize(new Dimension(180, 25));
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				toggleEscapePanel();
			}
		});
		escapePanel.add(backButton);
		escapePanel.add(resumeButton);
	}


	public void toggleEscapePanel() {
		//pause or not
		boardIO.setPause();

		//escape button
		if (!isEscapePanelDisplayed) {
			boardGraphism.add(escapePanel);
		} else {
			boardGraphism.remove(escapePanel);
		}
		frame.setVisible(true);
	}





	public void createSaveQuitOptionsPanel() {
		saveQuitOptionsPanel = new JPanel();
		saveQuitOptionsPanel.setBorder(BorderFactory.createTitledBorder("WARNING"));
		saveQuitOptionsPanel.setBackground(Color.white);
		saveQuitOptionsPanel.setPreferredSize(new Dimension(360, 85));

		JLabel info = new JLabel("Some changes have not been saved");
		info.setPreferredSize(new Dimension(250, 25));

		/** resume game */
		JButton saveQuitButton = new JButton("SAVE AND QUIT");
		saveQuitButton.setPreferredSize(new Dimension(160, 25));
		saveQuitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				backgroundPanel.remove(saveQuitOptionsPanel);
				backgroundPanel.add(optionPanel);
				reloadDisplay();
				saveOptions(true);
			}
		});

		/** go back to the main menu */
		JButton cancelQuitButton = new JButton("CANCEL AND QUIT");
		cancelQuitButton.setPreferredSize(new Dimension(160, 25));
		cancelQuitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				backgroundPanel.remove(saveQuitOptionsPanel);				
				backToMainMenuFromOption();
			}
		});

		// add all the components
		saveQuitOptionsPanel.add(info);
		saveQuitOptionsPanel.add(saveQuitButton);
		saveQuitOptionsPanel.add(cancelQuitButton);
	}


	public void createSaveFailedPanel() {
		saveFailedPanel = new JPanel();
		saveFailedPanel.setBorder(BorderFactory.createTitledBorder("WARNING"));
		saveFailedPanel.setBackground(Color.white);
		saveFailedPanel.setPreferredSize(new Dimension(275, 85));

		JLabel info = new JLabel("There's a problem with your bindings");
		info.setPreferredSize(new Dimension(250, 25));

		/** resume game */
		JButton saveFailedButton = new JButton("OK");
		saveFailedButton.setPreferredSize(new Dimension(75, 25));
		saveFailedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the option panel
				backgroundPanel.remove(saveFailedPanel);
				backgroundPanel.add(optionPanel);
				reloadDisplay();				
			}
		});

		// add all the components
		saveFailedPanel.add(info);
		saveFailedPanel.add(saveFailedButton);
	}


	/** creates the multiplayerJPanel with its component*/
	public void createMultiplayerPanel() {
		multiplayerPanel = new JPanel();
		multiplayerPanel.setBorder(BorderFactory.createTitledBorder("MULTIPLAYER"));
		multiplayerPanel.setBackground(Color.white);
		multiplayerPanel.setPreferredSize(new Dimension(130, 120));

		//create a joinable game
		JButton createButton = new JButton("CREATE GAME");
		createButton.setPreferredSize(new Dimension(120, 25));
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the creatingGame panel 
				backgroundPanel.remove(multiplayerPanel);
				backgroundPanel.add(createMultiplayerGamePanel);
				reloadDisplay();
				startOnlineGame();
			}
		});

		//join an existing game
		JButton joinButton = new JButton("JOIN GAME");
		joinButton.setPreferredSize(new Dimension(120, 25));
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the joiningGame panel 
				backgroundPanel.remove(multiplayerPanel);
				backgroundPanel.add(joinMultiplayerGamePanel);
				reloadDisplay();			
			}
		});

		//back to main menu
		JButton backButton = new JButton("BACK");
		backButton.setPreferredSize(new Dimension(120, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the mainMenu panel 
				backgroundPanel.remove(multiplayerPanel);
				backgroundPanel.add(mainMenuPanel);
				reloadDisplay();
			}
		});

		multiplayerPanel.add(createButton);
		multiplayerPanel.add(joinButton);
		multiplayerPanel.add(backButton);
	}


	/** creates the createMultiplayerGamePanel with its component*/
	public void createCreateMultiplayerGamePanel() {
		createMultiplayerGamePanel = new JPanel();
		createMultiplayerGamePanel.setBorder(BorderFactory.createTitledBorder("CREATING A GAME"));
		createMultiplayerGamePanel.setBackground(Color.white);
		createMultiplayerGamePanel.setPreferredSize(new Dimension(320, 120));

		//create a joinable game
		JLabel gameAvailable = new JLabel("Your game is available on : " + getPublicIPAddress());
		gameAvailable.setPreferredSize(new Dimension(300, 25));

		//join an existing game
		JLabel waiting = new JLabel("Waiting for your great enemy");
		waiting.setPreferredSize(new Dimension(300, 25));

		//back to main menu
		JButton backButton = new JButton("BACK");
		backButton.setPreferredSize(new Dimension(70, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the mainMenu panel 
				backgroundPanel.remove(createMultiplayerGamePanel);
				backgroundPanel.add(multiplayerPanel);
				reloadDisplay();
			}
		});

		createMultiplayerGamePanel.add(gameAvailable);
		createMultiplayerGamePanel.add(waiting);
		createMultiplayerGamePanel.add(backButton);
	}


	/** creates the joinMultiplayerGamePanel with its component*/
	public void createJoinMultiplayerGamePanel() {
		joinMultiplayerGamePanel = new JPanel();
		joinMultiplayerGamePanel.setBorder(BorderFactory.createTitledBorder("JOIN A GAME"));
		joinMultiplayerGamePanel.setBackground(Color.white);
		joinMultiplayerGamePanel.setPreferredSize(new Dimension(550, 120));

		//create a joinable game
		JLabel enterIP = new JLabel("Enter your great enemy's server IP : ");
		enterIP.setPreferredSize(new Dimension(300, 25));

		//join an existing game
		JTextField enemyIP = new JTextField();
		enemyIP.setPreferredSize(new Dimension(500, 25));

		//back to main menu
		JButton joinButton = new JButton("JOIN");
		joinButton.setPreferredSize(new Dimension(70, 25));
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				joinOnlineGame(enemyIP.getText());
			}
		});

		//back to main menu
		JButton backButton = new JButton("BACK");
		backButton.setPreferredSize(new Dimension(70, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the multiplayer panel 
				backgroundPanel.remove(joinMultiplayerGamePanel);
				backgroundPanel.add(multiplayerPanel);
				reloadDisplay();
			}
		});

		joinMultiplayerGamePanel.add(enterIP);
		joinMultiplayerGamePanel.add(enemyIP);
		joinMultiplayerGamePanel.add(joinButton);
		joinMultiplayerGamePanel.add(backButton);
	}



	/** reloads the displays (avoid former panels to be displayed) */
	public void reloadDisplay() {
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
			reloadDisplay();
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


	/**Le jeu tourne dans un thread a part, il contient les timer (thread) de jeu et d'affichage,
	 * Non : de jeu uniquement, depuis les modifications pour separer Local de Client/Server
	 */
	public class StartGame implements Runnable {
		public void run() {
			gameLoop.togglePause();
		}
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