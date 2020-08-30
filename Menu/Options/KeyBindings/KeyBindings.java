package Menu.Options.KeyBindings;

import java.io.Serializable;
import java.util.ArrayList;

/** an arrayList of all the keyBindings of a player */
public class KeyBindings implements Serializable {
	private static final long serialVersionUID = -2235563260966788434L;

	private ArrayList<KeyBinding> keyBindings;

	public KeyBindings() {
		keyBindings = new ArrayList<KeyBinding>();
	}

	/** adds a KeyBinding to the ArrayList */
	public void addBinding(KeyBinding keyBinding) {
		keyBindings.add(keyBinding);
	}

	/* ======= */
	/* Getters */
	/* ======= */

	public ArrayList<KeyBinding> getKeyBindings() {
		return keyBindings;
	}

	@Override
	public String toString() {
		String str = "KeyBindings [";
		for (KeyBinding keyBinding : keyBindings) {
			str += keyBinding;
		}
		return str + "]";
	}
}