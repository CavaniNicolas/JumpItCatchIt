package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class PlusOneBall extends ItemBall {
	private static final long serialVersionUID = -8612956912026402260L;

	/** Constructeur pour creer un item lors du jeu */
	public PlusOneBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.hasLongEffect = false;
		this.colorItem = Color.green;
	}


	@Override
	public void effects(Character character) {}

}