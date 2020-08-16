import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.ComponentEvent;
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
		this.setSize(550, 270);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.setDefaultCloseOperation();
		content = new JPanel();
		content.setBackground(Color.white);

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

		//create each player bindings panel
		redPlayerBindings = new KeyBindingMenu("Red player bindings", pathRedKeyBindings);
		bluePlayerBindings = new KeyBindingMenu("Blue player bindings", pathBlueKeyBindings);

		content.add(redPlayerBindings.getPanel());
		content.add(bluePlayerBindings.getPanel());

		
		/*JButton saveButton = new JButton("Save bindings");
    	saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {        
				KeyBindings redBindings = new KeyBindings(redPlayerBindings.getLeft());
        setVisible(true);
      	}*/

		//and display the option menu
		this.setContentPane(content);
		this.setVisible(true);
	  }

	public int getValueOfText(JTextField textField) {
		String str = textField.getText();
		char [] ch = str.toCharArray();
		return (int)ch[0];
	}

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

