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

		//create the 3 panels to be displayed
		mainMenuPane = new JPanel();
		mainMenuPane.setBackground(Color.white);
		optionPane = new JPanel();
		optionPane.setBackground(Color.white);


		//########################
		//uncomment this part and comment the following one to display a menu
		createMainMenuPanel();
		createKeyBindingMenu();
		createEscapePanel();
		board = new Board();
		board.setMainMenu(this);

		//the first panel to be displayed is the main menu
		this.frame.setContentPane(mainMenuPane);
		this.frame.setVisible(true);
		//#########################

		//########################
		//uncomment this part and comment the previous one to not display a menu
		/*
		board = new Board();
		startGame();*/
		//########################
	}


	/** starts the board and sets the frame to display it */
	public void startGame() {
		//start game
		board.initGame();
		Thread thread = new Thread(new StartGame());
		thread.start();
		//board.startGame();
		//add the keylistener
		frame.addKeyListener(board.getPlayerKeyListener());
		//give the frame the focus
		frame.setFocusable(true);
		frame.setFocusTraversalKeysEnabled(false);
		//displays the game panel
		frame.setContentPane(board);
		frame.setVisible(true);
	}


	/** creates the mainMenuJPanel with its component*/
	public void createMainMenuPanel() {
		mainMenuPane.setBorder(BorderFactory.createTitledBorder("JUMP IT AND CATCH IT"));
		mainMenuPane.setBackground(Color.white);

		JButton playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(150, 25));
    	playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				startGame();
			}
		});

		JButton optionButton = new JButton("Options");
		optionButton.setPreferredSize(new Dimension(150, 25));
    	optionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the option panel 
				frame.setContentPane(optionPane);
				frame.setVisible(true);
			}
		});

		JButton quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(150, 25));
    	quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				System.exit(0);       
			}
		});

		mainMenuPane.add(playButton);
		mainMenuPane.add(optionButton);
		mainMenuPane.add(quitButton);
	}


	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setBindings() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("red"));
		bluePlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("blue"));
	}

	/** initiates the components of the menu */
	public void createKeyBindingMenu() {
		optionPane.setBorder(BorderFactory.createTitledBorder("OPTIONS"));
		optionPane.setBackground(Color.white);

		//create each player bindings panel
		redPlayerBindings = new KeyBindingMenu("Red player bindings", this);
		bluePlayerBindings = new KeyBindingMenu("Blue player bindings", this);

		redPlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("red"), "redKeyBindingsDefault.txt");
		bluePlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("blue"), "blueKeyBindingsDefault.txt");

		addAllKeySelectingPanels();

		//add them to main panel
		optionPane.add(redPlayerBindings);
		optionPane.add(bluePlayerBindings);

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

		//add the buttons
		optionPane.add(saveButton);
		optionPane.add(backButton);
		optionPane.add(defaultButton);
	}

	/** creates the panel that opens when pressing escape */
	public void createEscapePanel() {
		escapePanel = new JPanel();
		escapePanel.setBorder(BorderFactory.createTitledBorder("PAUSE"));
		escapePanel.setBackground(Color.white);
		escapePanel.setPreferredSize(new Dimension(200, 30));

		JButton backButton = new JButton("BACK TO MAIN MENU");
		backButton.setPreferredSize(new Dimension(150, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				frame.setContentPane(mainMenuPane);
				isEscapePanelShown = false;
			}
		});
		JButton resumeButton = new JButton("RESUME");
		resumeButton.setPreferredSize(new Dimension(75, 25));
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
			System.out.println("unpausing");
			isEscapePanelShown = false;
			frame.setContentPane(board);
		} else {
			System.out.println("pausing");
			isEscapePanelShown = true;
			frame.setContentPane(escapePanel);
		}
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