package Menu;

import Game.Board;
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
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 4L;
	//attributes relative to the option menu
	private KeyBindingMenu redPlayerBindings, bluePlayerBindings;

	//the panels that can be displayed
	private JPanel saveFailedPanel;
	private JPanel saveQuitOptionsPanel;
	private JPanel mainMenuPanel;
	private JPanel optionPanel;
	private Board board;
	private JPanel escapePanel;
	private BackgroundPanel backgroundPanel;

	//boolean handling escape panel
	private Boolean isDisplayingEscapePanel = false;
	private Boolean isDisplayingMainMenu;

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
		createBoard();
		createMainMenuPanel();
		createKeyBindingMenu();
		createEscapePanel();
		createSaveQuitOptionsPanel();
		createSaveFailedPanel();


    /** display main menu first*/
		backgroundPanel.add(mainMenuPanel);
		isDisplayingMainMenu = true;
		this.frame.setContentPane(backgroundPanel);
		this.frame.setVisible(true);

		//########################
		//uncomment this line to NOT display a menu
		//startGame()
		//########################
	}

	/** creates board*/
	public void createBoard() {
		board = new Board();
		//add the board's keylistener
		frame.addKeyListener(board.getPlayerKeyListener());
	}

	/** starts the board and sets the frame to display it */
	public void startGame() {
		isDisplayingEscapePanel = false;
		isDisplayingMainMenu = false;

		//start game
		board.initGame();
		Thread thread = new Thread(new StartGame());
		thread.start();

		//give the frame the focus
		frame.setFocusable(true);
		frame.setFocusTraversalKeysEnabled(false);

		//displays the game panel
		frame.setContentPane(board);
		frame.setVisible(true);
	}

	/** creates the mainMenuJPanel with its component*/
	public void createMainMenuPanel() {
		mainMenuPanel = new JPanel();

		//create a panel to contain the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(160, 90));

		//start a game
		JButton playButton = new JButton("PLAY");
		playButton.setPreferredSize(new Dimension(150, 25));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				startGame();
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
				frame.setContentPane(backgroundPanel);
				frame.setVisible(true);
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
					backgroundPanel.add(saveQuitOptionsPanel);
					frame.setVisible(true);
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
				handleEscapePanel();
				isDisplayingMainMenu = true;
				frame.setContentPane(backgroundPanel);
				board.togglePause();
			}
		});

		/** resume game */
		JButton resumeButton = new JButton("RESUME");
		resumeButton.setPreferredSize(new Dimension(180, 25));
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				handleEscapePanel();
			}
		});
		escapePanel.add(backButton);
		escapePanel.add(resumeButton);
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
				backgroundPanel.remove(saveFailedPanel);
				frame.setVisible(true);				
			}
		});

		// add all the components
		saveFailedPanel.add(info);
		saveFailedPanel.add(saveFailedButton);
	}

	public void handleEscapePanel() {
		//escape button
		if (isDisplayingEscapePanel) {
			board.remove(escapePanel);
		} else {
			board.add(escapePanel);
		}
		board.togglePause();
		isDisplayingEscapePanel = !isDisplayingEscapePanel;
		frame.setVisible(true);
	}

	//create an array list of all KeySelectingPanels
	public void addAllKeySelectingPanels() {
		allKeySelectingPanels.addAll(redPlayerBindings.getKeySelectingPanels());
		allKeySelectingPanels.addAll(bluePlayerBindings.getKeySelectingPanels());
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
		backgroundPanel.remove(optionPanel);
		backgroundPanel.add(mainMenuPanel);
		isDisplayingMainMenu = true;
		frame.setContentPane(backgroundPanel);
		frame.setVisible(true);
		//cancels changes if they are not saved
		setBindings();
	}

	/** saves options if they are valid */
	public void saveOptions(Boolean back) {
		//saves the current (the ones being displayed) keyBindings if they are all unique
		if (checkUnicity()) {
			KeyBindings redBindings = redPlayerBindings.getCurrentKeyBindings();
			KeyBindings blueBindings = bluePlayerBindings.getCurrentKeyBindings();
			FileFunctions.saveBindings(redBindings, "KeyBindings/redKeyBindings.txt");
			FileFunctions.saveBindings(blueBindings, "KeyBindings/blueKeyBindings.txt");
			if (back) {
				backgroundPanel.remove(saveQuitOptionsPanel);				
				backToMainMenuFromOption();
			}
		} else {
			backgroundPanel.add(saveFailedPanel);
			frame.setVisible(true);
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

	/**Le jeu tourne dans un thread a part, il contient les timer (thread) de jeu et d'affichage */
	public class StartGame implements Runnable {
		public void run() {
			board.togglePause();
		}
	}

	public class ClientRelatedKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent event) {
		}
		@Override
		public void keyReleased(KeyEvent event) {
		}
		@Override
		public void keyTyped(KeyEvent event) {
			int code = event.getKeyChar();

			//escape
			if (code == 27) {
				if (frame.getContentPane() == backgroundPanel && !isDisplayingMainMenu) {
					backToMainMenuFromOption();
				} else if (frame.getContentPane() == board) {
					handleEscapePanel();
				}
			}
		}
	}
}