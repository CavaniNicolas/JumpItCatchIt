package Game.Items.Item;

import Game.Items.ItemBall;

import java.awt.Color;

public class HealBall extends ItemBall {


	/** Constructeur pour creer un item lors du jeu */
	public HealBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.red;
	}


	@Override
	public void effects(Character character) {}

}