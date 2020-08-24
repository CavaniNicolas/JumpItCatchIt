package Game;

public class InputAction {
	// Booleens de pression sur les touches / (de demande d'actions)
	private boolean jumpPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean grabPressed = false;
	private boolean shieldPressed = false;
	private boolean shootPushPressed = false;

	// Getters et Setters des Booleens de pression sur les touches / (de demande d'actions)
	public boolean isJumpPressed() {
		return jumpPressed;
	}
	public void setJumpPressed(boolean jumpPressed) {
		this.jumpPressed = jumpPressed;
	}
	public boolean isLeftPressed() {
		return leftPressed;
	}
	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}
	public boolean isRightPressed() {
		return rightPressed;
	}
	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}
	public boolean isGrabPressed() {
		return grabPressed;
	}
	public void setGrabPressed(boolean grabPressed) {
		this.grabPressed = grabPressed;
	}
	public boolean isShieldPressed() {
		return shieldPressed;
	}
	public void setShieldPressed(boolean shieldPressed) {
		this.shieldPressed = shieldPressed;
	}
	public boolean isShootPushPressed() {
		return shootPushPressed;
	}
	public void setShootPushPressed(boolean shootPushPressed) {
		this.shootPushPressed = shootPushPressed;
	}
}