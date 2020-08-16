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
	KeyBindingMenu redPlayerBindings, bluePlayerBindings;
	String pathRedKeyBindings, pathBlueKeyBindings;

	private JPanel mainMenuPane;
	private JPanel optionPane;

	MainMenu self;

	public MainMenu() {
		self = this;
		
		this.setSize(450, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.setDefaultCloseOperation();

		mainMenuPane = new JPanel();
		mainMenuPane.setBackground(Color.white);
		optionPane = new JPanel();
		optionPane.setBackground(Color.white);

		createMainMenuPanel();
		createKeyBindingMenu();

		this.setContentPane(mainMenuPane);
		this.setVisible(true);
	}

	public void createMainMenuPanel() {
		mainMenuPane = new JPanel();
		mainMenuPane.setBackground(Color.white);

		JButton playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(150, 25));
    	playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				/*this.setContentPane(board);
				this.setVisible(true);
				board.initGame();
				Thread thread = new Thread(new StartGame());
				thread.start();
				//board.startGame();
				this.addKeyListener(new PlayerKeyListener());*/
				System.out.println("THIS GAME IS SO COOL");
			}
		});

		JButton optionButton = new JButton("Options");
		optionButton.setPreferredSize(new Dimension(150, 25));
    	optionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				self.setContentPane(optionPane);
				self.setVisible(true);
				//System.out.println("YOU SHALL NOT ENTER OPTIONS");      
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
				self.setContentPane(mainMenuPane);
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
	public void saveBindings(KeyBindings keyBindings, String path) {
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

	public static void main(String[] args) {
		new MainMenu();
	}
}