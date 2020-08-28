package Game.Gameplay.Items.Item;

import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class HealBall extends ItemBall {
	private static final long serialVersionUID = -8967052746563391075L;

	/** Constructeur pour creer un item lors du jeu */
	public HealBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.red;
	}


	@Override
	public void effects(Character character) {}

}