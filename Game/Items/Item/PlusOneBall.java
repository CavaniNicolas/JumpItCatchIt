package Game.Items.Item;

import Game.Items.ItemBall;

import java.awt.Color;

public class PlusOneBall extends ItemBall {
	private static final long serialVersionUID = 1L;


	/** Constructeur pour creer un item lors du jeu */
	public PlusOneBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.green;
	}


	@Override
	public void effects(Character character) {}

}