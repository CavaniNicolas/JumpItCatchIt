import java.io.Serializable;

public class KeyBindings implements Serializable {
	private static final long serialVersionUID = 3L;

	private int leftKey;
	private int rightKey;
	private int jumpKey;
	private int grabKey;
	private int shieldKey;
	private int shootPushKey;


	public KeyBindings(int leftKey, int rightKey, int jumpKey, int grabKey, int shieldKey, int shootPushKey) {
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.jumpKey = jumpKey;
		this.grabKey = grabKey;
		this.shieldKey = shieldKey;
		this.shootPushKey = shootPushKey;
	}

	/* ======= */
	/* Getters */
	/* ======= */

	public int getLeftKey() {
		return leftKey;
	}
	public int getRightKey() {
		return rightKey;
	}
	public int getJumpKey() {
		return jumpKey;
	}
	public int getGrabKey() {
		return grabKey;
	}
	public int getShieldKey() {
		return shieldKey;
	}
	public int getShootPushKey() {
		return shootPushKey;
	}


	/* ======= */
	/* Setters */
	/* ======= */

	public void setLeftKey(int leftKey) {
		this.leftKey = leftKey;
	}
	public void setRightKey(int rightKey) {
		this.rightKey = rightKey;
	}
	public void setJumpKey(int jumpKey) {
		this.jumpKey = jumpKey;
	}
	public void setGrabKey(int grabKey) {
		this.grabKey = grabKey;
	}
	public void setShieldKey(int shieldKey) {
		this.shieldKey = shieldKey;
	}
	public void setShootPushKey(int shootPushKey) {
		this.shootPushKey = shootPushKey;
	}

}