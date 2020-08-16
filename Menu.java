import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame{
	KeyBindingMenu redPlayerBindings, bluePlayerBindings;
	JPanel content;
	String pathRedKeyBindings, pathBlueKeyBindings;

	public Menu(){
		this.setSize(450, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.setDefaultCloseOperation();
		content = new JPanel();
		content.setBackground(Color.white);

		//create the components of the menu
		createKeyBindingMenu();

		//and display the option menu
		this.setContentPane(content);
		this.setVisible(true);
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
		content.add(redPlayerBindings.getPanel());
		content.add(bluePlayerBindings.getPanel());

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
				System.out.println("YOU SHALL NOT PASS");
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
		content.add(saveButton);
		content.add(backButton);
		content.add(defaultButton);
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
		/* la partie qui suit est utilisée pour générer un set de touches par défaut
		// Touches joueur rouge
		int a=97, z=122, e=101, s=115, d=100, f=102;
		KeyBindings redKeyBindings = new KeyBindings(a, e, z, s, d, f);
		// Touches joueur bleu
		int u=117, i=105, o=111, k=107, l=108, m=109;
		KeyBindings blueKeyBindings = new KeyBindings(u, o, i, k, l, m);
		*/
		new Menu();
	}
}

