package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class HealBall extends ItemBall {
	private static final long serialVersionUID = -8967052746563391075L;

	/** Constructeur pour creer un item lors du jeu */
	public HealBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.coolDownEffect = 0;
		this.colorItem = Color.red;
	}


	@Override
	/* Redonne de la vie */
	public void effects(Character character) {
		// Si le perso n'a pas deja toute sa vie
		if (character.getLives() < character.getLivesMax()) {
			character.setLives(character.getLives() + 1);
		}
	}

	@Override
	public void resetEffects(Character character) {

	}


}