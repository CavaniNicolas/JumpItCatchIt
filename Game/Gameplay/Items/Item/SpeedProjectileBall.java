package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class SpeedProjectileBall extends ItemBall {
	private static final long serialVersionUID = -6057207305438805500L;

	/** Valeur de reset de l'effet */
	private int speedProjectileReset;

	/** Constructeur pour creer un item lors du jeu */
	public SpeedProjectileBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.coolDownEffect = 4_000;
		this.colorItem = Color.magenta;
	}


	@Override
	public void effects(Character character) {
		speedProjectileReset = character.getSpeedProjectile();
		character.setSpeedProjectile(character.getGameCC().getSpeedProjectile() * 2);
	}


	@Override
	public void resetEffects(Character character) {
		character.setSpeedProjectile(speedProjectileReset);
	}


}