package Menu;

import java.awt.Dimension;
import java.util.ArrayList;

public class KeyBindingMenu extends Menu {
	private static final long serialVersionUID = 2018355376623911571L;

	private ArrayList<KeySelectingPanel> keySelectingPanels = new ArrayList<KeySelectingPanel>();
	private KeyBindings keyBindings;

	/** creates a new KeySelectingPanel based on a KeyBinding */
	public KeyBindingMenu(String path, String defaultPath, KeyOptionMenu keyOptionMenu) {
		keyBindings = (KeyBindings)FileFunctions.getObject(path);

		//a default path is added in the constructor to allow resetting a binding
		for (int i = 0; i < keyBindings.getKeyBindings().size(); i++) {
			KeySelectingPanel keySelectingPanel = new KeySelectingPanel(keyBindings.getKeyBindings().get(i), i, defaultPath, keyOptionMenu);
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

	public Boolean checkChanges() {
		return keyBindings.equals(getCurrentKeyBindings());
	}
}