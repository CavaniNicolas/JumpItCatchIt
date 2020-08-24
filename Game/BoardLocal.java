package Game;

import Menu.FileFunctions;
import Menu.KeyBindings;

/** handles the key listeners and game for local game */
public class BoardLocal extends BoardIO {
	//game loop
	private GameLoop gameLoop;

	//keylisteners for both players
	private PlayerKeyListener redPlayerKeyListener;
	private PlayerKeyListener bluePlayerKeyListener;

	//input actions for both players
	private InputActions redPlayerInputActions;
	private InputActions bluePlayerInputActions;

	//the board to be modified
	private Board board;

	public BoardLocal(Board board) {
		this.board = board;
		gameLoop = new GameLoop(this.board);

		// On récupère les keyBindings des joueurs
		KeyBindings redPlayerBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("red"));
		KeyBindings bluePlayerBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("blue"));

		//on crée les listeners correspondant
		redPlayerKeyListener = new PlayerKeyListener(redPlayerBindings, this, board.getCharacterRed().getInputActions());
		bluePlayerKeyListener = new PlayerKeyListener(bluePlayerBindings, this, board.getCharacterBlue().getInputActions());
	}

	/** uses directly the input action to change the course of the game */
	public void handleAction(InputActions inputActions) {
	}
}