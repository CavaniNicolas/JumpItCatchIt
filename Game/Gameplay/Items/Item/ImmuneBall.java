package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class ImmuneBall extends ItemBall {
	private static final long serialVersionUID = -6057207305438805500L;


	/** Constructeur pour creer un item lors du jeu */
	public ImmuneBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.coolDownEffect = 2_500;
		this.colorItem = Color.cyan;
	}


	@Override
	public void effects(Character character) {
		character.setImmune(true);
	}


	@Override
	public void resetEffects(Character character) {
		character.setImmune(false);
	}


}