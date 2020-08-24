package Game.Items.Item;

import Game.Items.ItemBall;

import java.awt.Color;

public class ShieldBreakBall extends ItemBall {

	/** Constructeur pour creer un item lors du jeu */
	public ShieldBreakBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.pink;
	}


	@Override
	public void effects(Character character) {}

}