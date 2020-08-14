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

}