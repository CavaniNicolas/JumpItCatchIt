package Game.Items.Item;

import Game.Items.ItemBall;

import java.awt.Color;

public class SpeedProjectileBall extends ItemBall {


	/** Constructeur pour creer un item lors du jeu */
	public SpeedProjectileBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.magenta;
	}


	@Override
	public void effects(Character character) {}

}