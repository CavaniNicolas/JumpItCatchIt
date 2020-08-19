import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 4L;
	//attributes relative to the option menu
	private KeyBindingMenu redPlayerBindings, bluePlayerBindings;

	//the panels that can be displayed
	private JPanel mainMenuPane;
	private JPanel optionPane;
	private Board board;
	private JPanel escapePanel;

	//boolean handling escape panel
	private Boolean isEscapePanelShown = false;

	//the frame displaying all the stuff
	private JFrame frame;

	//all the KeySelectingPanels
	private ArrayList<KeySelectingPanel> allKeySelectingPanels = new ArrayList<KeySelectingPanel>();

	public MainMenu(JFrame frame) {		
		this.frame = frame;

		//create the 4 panels to be displayed
		createMainMenuPanel();
		createKeyBindingMenu();
		createBoard();
		createEscapePanel();

		//add the board's keylistener
		frame.addKeyListener(board.getPlayerKeyListener());

		//########################
		//uncomment this part and comment the following one to display a menu
		//the first panel to be displayed is the main menu
		this.frame.setContentPane(mainMenuPane);
		this.frame.setVisible(true);
		//#########################

		//########################
		//uncomment this part and comment the previous one to not display a menu
		/*
		startGame();*/
		//########################
	}


	/** starts the board and sets the frame to display it */
	public void startGame() {
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

	/** initiates board */
	public void createBoard() {
		isEscapePanelShown = false;
		board = new Board();
		board.setMainMenu(this);
	}

	/** creates the mainMenuJPanel with its component*/
	public void createMainMenuPanel() {
		mainMenuPane = new BackgroundPanel();

		//create a panel to contain the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.white);
		buttonPane.setPreferredSize(new Dimension(160, 90));

		//start a game
		JButton playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(150, 25));
    	playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				startGame();
			}
		});

		//open option menu
		JButton optionButton = new JButton("Options");
		optionButton.setPreferredSize(new Dimension(150, 25));
    	optionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the option panel 
				frame.setContentPane(optionPane);
				frame.setVisible(true);
			}
		});

		/**closes the app */
		JButton quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(150, 25));
    	quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				System.exit(0);       
			}
		});

		buttonPane.add(playButton);
		buttonPane.add(optionButton);
		buttonPane.add(quitButton);

		mainMenuPane.add(buttonPane);
	}


	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setBindings() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("red"));
		bluePlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("blue"));
	}

	/** initiates the components of the menu */
	public void createKeyBindingMenu() {
		optionPane = new BackgroundPanel();

		//smaller panel so that the options don't take the whole screen
		JPanel optionPane2 = new JPanel();
		optionPane2.setBorder(BorderFactory.createTitledBorder("OPTIONS"));
		optionPane2.setBackground(Color.white);
		optionPane2.setPreferredSize(new Dimension(630, 320));

		//create a panel to contain the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.white);
		buttonPane.setPreferredSize(new Dimension(480, 30));

		//create each player bindings panel
		redPlayerBindings = new KeyBindingMenu("Red player bindings", this);
		bluePlayerBindings = new KeyBindingMenu("Blue player bindings", this);

		redPlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("red"), "redKeyBindingsDefault.txt");
		bluePlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("blue"), "blueKeyBindingsDefault.txt");

		addAllKeySelectingPanels();

		/** save bindings */
		JButton saveButton = new JButton("Save bindings");
		saveButton.setPreferredSize(new Dimension(150, 25));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				//saves the current (the ones being displayed) keyBindings if they are all unique
				if (checkUnicity()) {
					KeyBindings redBindings = redPlayerBindings.getCurrentKeyBindings();
					KeyBindings blueBindings = bluePlayerBindings.getCurrentKeyBindings();
					FileFunctions.saveBindings(redBindings, "redKeyBindings.txt");
					FileFunctions.saveBindings(blueBindings, "blueKeyBindings.txt");
				} else {
					System.out.println("YOU SHALL NOT PASS");
				}
				//to rebuild the array list of all KeySelectingPanels
				addAllKeySelectingPanels();
			}
		});

		/** back to main menu */
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(150, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the main menu again    
				frame.setContentPane(mainMenuPane);
				//cancels changes if they are not saved
				setBindings();
			}
		});

		/** reset to default bindings */
		JButton defaultButton = new JButton("Reset bindings");
		defaultButton.setPreferredSize(new Dimension(150, 25));
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				FileFunctions.deleteNonDefaultBindings();
				//recreate the menu with default settings 
				setBindings();
			}
		});

		//add the buttons to button panel
		buttonPane.add(saveButton);
		buttonPane.add(backButton);
		buttonPane.add(defaultButton);

		//add the player's bindings panels to main panel
		optionPane2.add(redPlayerBindings);
		optionPane2.add(bluePlayerBindings);
		//and the buttons at the bottm
		//buttonPane.setLocation(10, 500);
		optionPane2.add(buttonPane);

		optionPane.add(optionPane2);
	}

	/** creates the panel that opens when pressing escape */
	public void createEscapePanel() {
		escapePanel = new JPanel();
		escapePanel.setBorder(BorderFactory.createTitledBorder("PAUSE"));
		escapePanel.setBackground(Color.white);
		escapePanel.setPreferredSize(new Dimension(220, 90));

		JButton backButton = new JButton("BACK TO MAIN MENU");
		backButton.setPreferredSize(new Dimension(180, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//sets the content pane to the main menu and delete board (game has ended)
				frame.setContentPane(mainMenuPane);
				createBoard();
			}
		});
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

	public void handleEscapePanel() {
		//escape button
		if (isEscapePanelShown) {
			System.out.println("not showing");
			board.remove(escapePanel);
		} else {
			System.out.println("showing");
			board.add(escapePanel);
		}
		isEscapePanelShown = !isEscapePanelShown;
		frame.setVisible(true);
	}

	//create an array list of all KeySelectingPanels
	public void addAllKeySelectingPanels() {
		allKeySelectingPanels.clear();
		allKeySelectingPanels.addAll(redPlayerBindings.getKeySelectingPanels());
		allKeySelectingPanels.addAll(bluePlayerBindings.getKeySelectingPanels());
	}

	public boolean checkUnicity() {
		for (KeySelectingPanel keySelectingPanel : allKeySelectingPanels) {
			for (KeySelectingPanel keySelectingPanel2 : allKeySelectingPanels) {
				if (keySelectingPanel == keySelectingPanel2) {
					return false;
				}
			}
			//to decrease complexity by half and avoid testing twice
			allKeySelectingPanels.remove(keySelectingPanel);
		}
		return true;
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

	/**Le jeu tourne dans un thread a part */
	public class StartGame implements Runnable {
		public void run() {
			board.startGame();
		}
	}
}