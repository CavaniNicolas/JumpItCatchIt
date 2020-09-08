package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class SpeedProjectileBall extends ItemBall {
	private static final long serialVersionUID = -6057207305438805500L;

	/** Constructeur pour creer un item lors du jeu */
	public SpeedProjectileBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.magenta;
	}


	@Override
	public void effects(Character character) {}

}