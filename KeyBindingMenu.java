import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;


public class KeyBindingMenu extends JPanel {
	private ArrayList<KeySelectingPanel> keySelectingPanels;
	private MainMenu mainMenu;

	public KeyBindingMenu(String name, MainMenu mainMenu) {
		this.mainMenu = mainMenu;
		//JPanel binding player
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.setBackground(Color.white);

		keySelectingPanels = new ArrayList<KeySelectingPanel>();
	}

	/** creates a new KeySelectingPanel based on a KeyBinding */
	public void addKeySelectingPanels(String path, String defaultPath) {
		KeyBindings keyBindings = FileFunctions.getBindings(path);
		this.setPreferredSize(new Dimension(270, 40*keyBindings.getKeyBindings().size()));

		for (int i = 0; i < keyBindings.getKeyBindings().size(); i++) {
			KeySelectingPanel keySelectingPanel = new KeySelectingPanel(keyBindings.getKeyBindings().get(i), i, defaultPath, mainMenu);
			keySelectingPanels.add(keySelectingPanel);
			this.add(keySelectingPanel);
		}
	}

	public ArrayList<KeySelectingPanel> getKeySelectingPanels() {
		return keySelectingPanels;
	}

	/** sets the JTextFields so they display the bindings saved in a file designated by a given path string */
	public void setAllBindings(String path) {
		KeyBindings keyBindings = FileFunctions.getBindings(path);

		//set the JTextFields to the value of the bindings
		for (int i = 0; i < keyBindings.getKeyBindings().size(); i++) {
			keySelectingPanels.get(i).setBinding(keyBindings.getKeyBindings().get(i));
		}
	}

	/** return current key bindings */
	public KeyBindings getCurrentKeyBindings() {
		KeyBindings currentKeyBindings = new KeyBindings();
		for (KeySelectingPanel keySelectingPanel : keySelectingPanels) {
			currentKeyBindings.addBinding(keySelectingPanel.getCurrentKeyBinding());
		}
		return currentKeyBindings;
	}
}