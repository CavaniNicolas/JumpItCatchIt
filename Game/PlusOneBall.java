package Game;

import java.awt.Color;
import java.awt.Image;

public class PlusOneBall extends ItemBall {



	public PlusOneBall(int nbMaxItem, int percentItem, Color colorItem, Image imageItem) {
		super(nbMaxItem, percentItem, colorItem, imageItem);
	}


	@Override
	public void effects(Character character) {}
}