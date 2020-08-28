package Menu;

import java.io.Serializable;

/** a key + description of what it does in the game */
public class KeyBinding implements Serializable {
	private int keyValue;
	private String keyActionDescription;

	public KeyBinding(int keyValue, String keyActionDescription) {
		this.keyValue = keyValue;
		this.keyActionDescription = keyActionDescription;
	}

	/* ======= */
	/* Setters */
	/* ======= */

	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	/* ======= */
	/* Getters */
	/* ======= */

	public int getKeyValue() {
		return this.keyValue;
	}

	public String getKeyActionDescription() {
		return this.keyActionDescription;
	}

	public Boolean equals(KeyBinding kb) {
		return (keyValue == kb.getKeyValue() && keyActionDescription.equals(kb.getKeyActionDescription()));
	}

	@Override
	public String toString() {
		return "KeyBinding [keyActionDescription=" + keyActionDescription + ", keyValue=" + keyValue + "]";
	}

}