package Game;

import Game.Network.BoardIO;
import Menu.Options.KeyBindings.*;

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
		handleKey(event, true);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		handleKey(event, false);
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	/** sends input action to the board if the key is valid */
	public void handleKey(KeyEvent event, Boolean bool) {
		String action = getKeyActionDescription(event.getKeyChar());
		if (action != null) {
			inputActions.togglePressedKeys(action, bool);
			boardIO.handleAction(inputActions);
		}
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