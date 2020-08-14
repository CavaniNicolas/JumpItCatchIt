import java.awt.Color;
import java.awt.Image;

public class Character {

	KeyBindings keyBindings;

	/**Nombre de vies (en moities de coeur) */
	private int lives = 6;

	// Coordonnees
	private int x;
	private int y;

	// Booleens d'autorisation d'actions
	private boolean canJump;
	private boolean canGrab;
	private boolean canShield;
	private boolean canShoot;
	private boolean canPush;


	private Image imageCharacter;
	private Color colorCharacter;


	public Character() {
	}

}