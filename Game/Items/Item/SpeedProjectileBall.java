package Game.Items.Item;

import Game.Items.ItemBall;

import java.awt.Color;
import java.awt.Image;

public class SpeedProjectileBall extends ItemBall {

	/** Constructeur pour creer un item lors du jeu */
	public SpeedProjectileBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.magenta;
	}


	/** Constructeur pour la liste d'initialisation des items */
	public SpeedProjectileBall(int nbMaxItem, int percentItem, Color colorItem, Image imageItem) {
		super(nbMaxItem, percentItem, colorItem, imageItem);
	}


	@Override
	public void effects(Character character) {}

}