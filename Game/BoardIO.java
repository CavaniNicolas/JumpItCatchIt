package Game;

/** mother class of boardClient and boardLocal */
public class BoardIO {

	/** knows what to do with an input action (send it to server or use it directly for local game) */
	public void handleAction(InputActions inputActions) {}

	/** knows what to do when someone uncovers the escape panel  */
	public void escapePanelInteraction() {}

	/** knows what to do when someone returns to the main menu */
	public void exitGame() {}
}