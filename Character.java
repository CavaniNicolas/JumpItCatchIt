import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

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


	private Color colorCharacter;
	private Image imageCharacter = null;


	public Character(int x, int y, Color colorCharacter, Image imageCharacter) {
		this.x = x;
		this.y = y;
		this.colorCharacter = colorCharacter;
		this.imageCharacter = imageCharacter;
	}

	public Character(int x, int y, Color colorCharacter) {
		this(x, y, colorCharacter, null);
	}


	/**Dessine le personnage */
	public void drawCharacter(Graphics g) {
		g.setColor(colorCharacter);
		g.fillRect(x, y - 600, 150, 250);
		System.out.println(g);
	}

}