package Menu;

import java.awt.Dimension;
import java.util.ArrayList;

public class KeyBindingMenu extends Menu {
	private ArrayList<KeySelectingPanel> keySelectingPanels;
	private MainMenu mainMenu;

	public KeyBindingMenu(String name, MainMenu mainMenu) {
		super();
		this.mainMenu = mainMenu;
		//JPanel binding player
		displayBorder(name);

		keySelectingPanels = new ArrayList<KeySelectingPanel>();
	}

	/** creates a new KeySelectingPanel based on a KeyBinding */
	public void addKeySelectingPanels(String path, String defaultPath) {
		KeyBindings keyBindings = (KeyBindings)FileFunctions.getObject(path);

		//a default path is added in the constructor to allow resetting a binding
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
		KeyBindings keyBindings = (KeyBindings)FileFunctions.getObject(path);

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

	/** sets all component size to the size of the biggest component*/
	public void setDimensions() {
		height = keySelectingPanels.get(0).getLabel().getPreferredSize().getHeight();
		for (KeySelectingPanel panel : keySelectingPanels) {
			width = Math.max(width, panel.getLabel().getPreferredSize().getWidth());
		}
		for (KeySelectingPanel panel : keySelectingPanels) {
			panel.getLabel().setPreferredSize(new Dimension((int)width, (int)height));
			panel.setOrder(false);
		}
	}
}