package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class ShieldBreakBall extends ItemBall {
	private static final long serialVersionUID = -4523508120245174448L;

	/** Constructeur pour creer un item lors du jeu */
	public ShieldBreakBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.coolDownEffect = 5_000;
		this.colorItem = Color.pink;
	}


	@Override
	public void effects(Character character) {}


	@Override
	public void resetEffects(Character character) {}


}