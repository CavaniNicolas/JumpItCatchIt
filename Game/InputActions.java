package Game;

import java.io.Serializable;

public class InputActions implements Serializable {
	// Booleens de pression sur les touches / (de demande d'actions)
	private boolean jumpPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean grabPressed = false;
	private boolean shieldPressed = false;
	private boolean shootPushPressed = false;

	// pointeur vers le character (non sérialisé)
	private transient Character character;

	public void togglePressedKeys(String action, Boolean toggle) {
		if (action.equals("Left")) {
			leftPressed = toggle;
		} else if (action.equals("Right")) {
			rightPressed = toggle;
		} else if (action.equals("Jump")) {
			jumpPressed = toggle;
		} else if (action.equals("Grab")) {
			grabPressed = toggle;
		} else if (action.equals("Shield")) {
			shieldPressed = toggle;
		} else if (action.equals("Shoot and push")) {
			shootPushPressed = toggle;	
		}
	}

	// Getters et Setters des Booleens de pression sur les touches / (de demande d'actions)
	public boolean getJumpPressed() {
		return jumpPressed;
	}
	public void setJumpPressed(boolean jumpPressed) {
		this.jumpPressed = jumpPressed;
	}
	public boolean getLeftPressed() {
		return leftPressed;
	}
	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}
	public boolean getRightPressed() {
		return rightPressed;
	}
	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}
	public boolean getGrabPressed() {
		return grabPressed;
	}
	public void setGrabPressed(boolean grabPressed) {
		this.grabPressed = grabPressed;
	}
	public boolean getShieldPressed() {
		return shieldPressed;
	}
	public void setShieldPressed(boolean shieldPressed) {
		this.shieldPressed = shieldPressed;
	}
	public boolean getShootPushPressed() {
		return shootPushPressed;
	}
	public void setShootPushPressed(boolean shootPushPressed) {
		this.shootPushPressed = shootPushPressed;
	}

	@Override
	public String toString() {
		return "InputActions [character=" + character + ", grabPressed=" + grabPressed + ", jumpPressed=" + jumpPressed
				+ ", leftPressed=" + leftPressed + ", rightPressed=" + rightPressed + ", shieldPressed=" + shieldPressed
				+ ", shootPushPressed=" + shootPushPressed + "]";
	}
}