package Game.Network;

/** memory state of what each client is doing */
public class PlayerState {
	private Boolean connected;
	private Boolean restartGame;

	public PlayerState() {
		connected = true;
		restartGame = true;
	}

	public Boolean getConnected() {
		return connected;
	}

	public Boolean getRestartGame() {
		return restartGame;
	}

	public void handleInput(String str) {
		if (str.equals("RESTART GAME")) {
			restartGame = true;
		}
	}

	public void setRestartGame(Boolean bool) {
		restartGame = bool;
	}
}
