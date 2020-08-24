package Game;

import Menu.KeyBindings;
import Menu.KeyBinding;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/** keylistener for each player */
public class PlayerKeyListener implements KeyListener {
	private KeyBindings keyBindings;
	private BoardIO boardIO;
	private InputActions inputActions;

	public PlayerKeyListener(KeyBindings keyBindings, BoardIO boardIO, InputActions inputActions) {
		this.keyBindings = keyBindings;
		this.boardIO = boardIO;
		this.inputActions = inputActions;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		String action = getKeyActionDescription(event.getKeyChar());
		inputActions.togglePressedKeys(action, true);
		boardIO.handleAction(inputActions);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		String action = getKeyActionDescription(event.getKeyChar());
		inputActions.togglePressedKeys(action, false);
		boardIO.handleAction(inputActions);
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	/** returns the action relative to the keyBinding used */
	public String getKeyActionDescription(int code) {
		for (KeyBinding keyBinding : keyBindings.getKeyBindings()) {
			if (code == keyBinding.getKeyValue()) {
				return keyBinding.getKeyActionDescription();
			}
		}
		return null;
	}
}