package Game.Gameplay.Items.Item;

import Game.Gameplay.Character;
import Game.Gameplay.Items.ItemBall;

import java.awt.Color;

public class RadiusProjectileBall extends ItemBall {
	private static final long serialVersionUID = -6057207305438805500L;

	/** Valeur de reset de l'effet */
	private int radiusProjectileReset;

	/** Constructeur pour creer un item lors du jeu */
	public RadiusProjectileBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.coolDownEffect = 4_000;
		this.colorItem = Color.YELLOW;
	}


	@Override
	public void effects(Character character) {
		radiusProjectileReset = character.getRadiusProjectile();
		character.setRadiusProjectile(character.getGameCC().getRadiusProjectile() * 2);
	}


	@Override
	public void resetEffects(Character character) {
		character.setRadiusProjectile(radiusProjectileReset);
	}


}