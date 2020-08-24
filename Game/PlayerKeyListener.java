package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyListener {
	public class PlayerKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {

			int code = event.getKeyChar();
			//System.out.print("Code clavier "+ code + "\n ");

			//escape
			if (isPlaying) {
				togglePressedKeys(code, characterRed, true);
				togglePressedKeys(code, characterBlue, true);
			}
		}

		@Override
		public void keyReleased(KeyEvent event) {
			int code = event.getKeyChar();
			// System.out.print("Code clavier "+ code + "\n ");

			if (isPlaying) {
				togglePressedKeys(code, characterRed, false);
				togglePressedKeys(code, characterBlue, false);
			}

		}

		@Override
		public void keyTyped(KeyEvent event) {
		}

		/** Toggle les booleens de KeyPressed */
		public void togglePressedKeys(int code, Character character, boolean toggle) {
			KeyBindings characterKeys = character.getKeyBindings();

			// Pour le personnage bleu
			// Sauter (2)
			if (code == characterKeys.getKeyBindings().get(2).getKeyValue()) {
				character.getActionBooleans().setJumpPressed(toggle);

				// Si on relache le bouton sauter
				if (toggle == false) {
					// Active le booleens qui permet dactiver le canSwitch si on est dans les airs
					// et qu'on
					// relache le bouton sauter (pour pouvoir rappuyer dessus dans les airs pour
					// switch)
					if (character.getActionBooleans().isJumping() == true
							&& character.getActionBooleans().isJumpFirstReleaseDone() == false) {
						character.getActionBooleans().setIsJumpFirstReleaseDone(true);
					}
				}

      		}
			// Gauche (0)
			if (code == characterKeys.getKeyBindings().get(0).getKeyValue()) {
				character.getActionBooleans().setLeftPressed(toggle);
			}
			// Droite (1)
			if (code == characterKeys.getKeyBindings().get(1).getKeyValue()) {
				character.getActionBooleans().setRightPressed(toggle);
			}
			// Grab (3)
			if (code == characterKeys.getKeyBindings().get(3).getKeyValue()) {
				character.getActionBooleans().setGrabPressed(toggle);
			}
			// Shield (4)
			if (code == characterKeys.getKeyBindings().get(4).getKeyValue()) {
				character.getActionBooleans().setShieldPressed(toggle);
			}
			// Shoot Push (5)
			if (code == characterKeys.getKeyBindings().get(5).getKeyValue()) {
				character.getActionBooleans().setShootPushPressed(toggle);
			}
		}

	}
	
}