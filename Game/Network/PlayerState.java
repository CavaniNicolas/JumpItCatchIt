package Game.Network;

public class PlayerState {
	private Boolean connected;
	private Boolean restartGame;
	private int id;

	public PlayerState() {
		connected = true;
		restartGame = true;
	}

	public int getId() {
		return id;
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
