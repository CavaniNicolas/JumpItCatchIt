package Menu;

import java.io.Serializable;
import java.util.ArrayList;

/** an arrayList of all the keyBindings of a player */
public class KeyBindings implements Serializable {
	private static final long serialVersionUID = 3L;

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

	public Boolean equals(KeyBindings kb) {
		if (keyBindings.size() != kb.getKeyBindings().size()) {
			return false;
		}
		for (int i = 0; i < keyBindings.size(); i++) {
			if (keyBindings.get(i) != kb.getKeyBindings().get(i)) {
				return false;
			}
		}
		return true;
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