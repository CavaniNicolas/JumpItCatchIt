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
	public void drawCharacter(Graphics g, BoardGraphism boardGraphism) {
		g.setColor(colorCharacter);
		int x = (int)((double)(this.x) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double)(this.y) * boardGraphism.getGraphic().getOneUnityHeight());
		int width = (int)((double)(boardGraphism.getReal().getCharacterWidth()) * boardGraphism.getGraphic().getOneUnityWidth());
		int height = (int)((double)(boardGraphism.getReal().getCharacterHeight()) * boardGraphism.getGraphic().getOneUnityHeight());
		g.fillRect(x, y, width, height);
	}

}