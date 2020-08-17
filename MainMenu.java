import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class MainMenu extends JFrame {
	//attributes relative to the option menu
	private KeyBindingMenu redPlayerBindings, bluePlayerBindings;
	private String pathRedKeyBindings, pathBlueKeyBindings;

	//the panels that can be displayed
	private JPanel mainMenuPane;
	private JPanel optionPane;
	private Board board;

	//the frame displaying all the stuff
	private JFrame frame;

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
		board = new Board();

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
		//check if non default key settings exist
		pathRedKeyBindings = "redKeyBindings.txt";
		pathBlueKeyBindings = "blueKeyBindings.txt";

		File f = new File(pathRedKeyBindings);
		if(!f.exists() || f.isDirectory()) {
			pathRedKeyBindings = "redKeyBindingsDefault.txt";
		}
		f = new File(pathBlueKeyBindings);
		if(!f.exists() || f.isDirectory()) {
			pathBlueKeyBindings = "blueKeyBindingsDefault.txt";
		}

		//set their bindings
		redPlayerBindings.setBindings(pathRedKeyBindings);
		bluePlayerBindings.setBindings(pathBlueKeyBindings);
	}

	/** initiates the components of the menu */
	public void createKeyBindingMenu() {
		//create each player bindings panel
		redPlayerBindings = new KeyBindingMenu("Red player bindings");
		bluePlayerBindings = new KeyBindingMenu("Blue player bindings");

		setBindings();

		//add them to main panel
		optionPane.add(redPlayerBindings.getPanel());
		optionPane.add(bluePlayerBindings.getPanel());

		/** save bindings */
		JButton saveButton = new JButton("Save bindings");
		saveButton.setPreferredSize(new Dimension(150, 25));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				//saves the current (the ones being displayed) keyBindings     
				KeyBindings redBindings = redPlayerBindings.getCurrentKeyBindings();
				KeyBindings blueBindings = bluePlayerBindings.getCurrentKeyBindings();
				saveBindings(redBindings, "redKeyBindings.txt");
				saveBindings(blueBindings, "blueKeyBindings.txt");
				//just to display 1 char bindings
				setBindings();
			}
		});

		/** back to main menu */
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(150, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//displays the main menu again    
				frame.setContentPane(mainMenuPane);
			}
		});

		/** reset to default bindings */
		JButton defaultButton = new JButton("Reset bindings");
		defaultButton.setPreferredSize(new Dimension(150, 25));
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				pathRedKeyBindings = "redKeyBindings.txt";
				pathBlueKeyBindings = "blueKeyBindings.txt";

				File f = new File(pathRedKeyBindings);
				if(f.exists() && !f.isDirectory()) {
					f.delete();
				}
				f = new File(pathBlueKeyBindings);
				if(f.exists() && !f.isDirectory()) {
					f.delete();
				}
				//recreate the menu with default settings 
				setBindings();
			}
		});

		//add the buttons
		optionPane.add(saveButton);
		optionPane.add(backButton);
		optionPane.add(defaultButton);
	}

	/** saves a KeyBindings object to a file designated by a given path string */
	public static void saveBindings(KeyBindings keyBindings, String path) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(
					new BufferedOutputStream(
						new FileOutputStream(
							new File(path))));
					
			//write the bindings in a file
			oos.writeObject(keyBindings);
			//close the flux
			oos.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/** create default key bindings files in case they're deleted */
	public static void createDefaultBindings() {
		String pathRedKeyBindings = "redKeyBindingsDefault.txt";
		String pathBlueKeyBindings = "blueKeyBindingsDefault.txt";

		File file = new File(pathRedKeyBindings);
		if(file.exists() && !file.isDirectory()) {
			file.delete();
		}
		file = new File(pathBlueKeyBindings);
		if(file.exists() && !file.isDirectory()) {
			file.delete();
		}
		// Touches joueur rouge
		int a=97, z=122, e=101, s=115, d=100, f=102;
		KeyBindings redKeyBindings = new KeyBindings(a, e, z, s, d, f);
		// Touches joueur bleu
		int u=117, i=105, o=111, k=107, l=108, m=109;
		KeyBindings blueKeyBindings = new KeyBindings(u, o, i, k, l, m);
		saveBindings(redKeyBindings, pathRedKeyBindings);
		saveBindings(blueKeyBindings, pathBlueKeyBindings);
	}

	public Board getBoard() {
		return board;
	}

	/**Le jeu tourne dans un thread a part */
	public class StartGame implements Runnable {
		public void run() {
			board.startGame();
		}
	}

	public static void main(String[] args) {
		MainMenu.createDefaultBindings();
	}
}