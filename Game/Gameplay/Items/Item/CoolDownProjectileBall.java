package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class CoolDownProjectileBall extends ItemBall {
	private static final long serialVersionUID = -6057207305438805500L;


	/** Constructeur pour creer un item lors du jeu */
	public CoolDownProjectileBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.coolDownEffect = 5_000;
		this.colorItem = Color.gray;
	}


	@Override
	public void effects(Character character) {
		character.setCoolDownProjectile(character.getGameCC().getCoolDownProjectile() / 2);
	}


	@Override
	public void resetEffects(Character character) {
		character.setCoolDownProjectile(character.getGameCC().getCoolDownProjectile());
	}


}