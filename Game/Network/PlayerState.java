package Game.Network;

import Game.InputActions;

public class PlayerState {
	private Boolean connected;
	private Boolean restartGame;
	private int id;

	public PlayerState(int id) {
		this.id = id;
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
}
