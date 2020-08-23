package Game.Items.Item;

import Game.Items.ItemBall;

import java.awt.Color;
import java.awt.Image;

public class PlusOneBall extends ItemBall {

	/** Constructeur pour creer un item lors du jeu */
	public PlusOneBall(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.colorItem = Color.green;
	}


	/** Constructeur pour la liste d'initialisation des items */
	public PlusOneBall(int nbMaxItem, int percentItem, Color colorItem, Image imageItem) {
		super(nbMaxItem, percentItem, colorItem, imageItem);
	}


	@Override
	public void effects(Character character) {}

}