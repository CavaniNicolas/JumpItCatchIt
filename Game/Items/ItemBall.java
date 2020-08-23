package Game.Items;

import Game.Entity;
import Game.BoardGraphism;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

public class ItemBall extends Entity {

	protected Color colorItem;
	private Image imageItem = null;

	/** Constructeur pour creer un item lors du jeu */
	public ItemBall(int x, int y, int width, int height) {
		// Vitesse de chute des items initee ici
		super(x, y, 0, -50, 0, 0, width, height);
		this.minY = - height;
	}



	/** Effets qu'applique cet Item, cette methode sera Override pour chaque Item */
	public void effects(Character character) {}


	/** Affiche l'item */
	public void drawItem(Graphics g, BoardGraphism boardGraphism) {
		g.setColor(colorItem);
		int x = (int)((double)(this.x - this.width / 2) * boardGraphism.getGraphic().getOneUnityWidth());
		int y = (int)((double)(boardGraphism.getMaxY() - (this.y + this.height / 2)) * boardGraphism.getGraphic().getOneUnityHeight());
		int width = (int)((double)(this.width) * boardGraphism.getGraphic().getOneUnityWidth());
		int height = (int)((double)(this.height) * boardGraphism.getGraphic().getOneUnityHeight());
		g.fillOval(x, y, width, height);
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public Color getColorItem() {
		return colorItem;
	}
	public Image getImageItem() {
		return imageItem;
	}

}
