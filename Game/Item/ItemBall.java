package Game.Item;

import Game.Entity;
import Game.BoardGraphism;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

public class ItemBall extends Entity {

	/** Quantite de cet Item actif en jeu */
	private int nbItem = 0;

	// Attributs d'initialisation des Items
	private int nbMaxItem;
	private int percentItem;
	protected Color colorItem;
	private Image imageItem = null;


	/** Constructeur pour creer un item lors du jeu */
	public ItemBall(int x, int y, int width, int height) {
		// Vitesse de chute des items initee ici
		super(x, y, 0, -50, 0, 0, width, height);
		this.minY = - height;
	}


	/** Constructeur pour la liste d'initialisation des items */
	public ItemBall(int nbMaxItem, int percentItem, Color colorItem, Image imageItem) {
		super();
		this.nbMaxItem = nbMaxItem;
		this.percentItem = percentItem;
		this.colorItem = colorItem;
		this.imageItem = imageItem;
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

	public int getNbMaxItem() {
		return nbMaxItem;
	}
	public int getPercentItem() {
		return percentItem;
	}
	public Color getColorItem() {
		return colorItem;
	}
	public Image getImageItem() {
		return imageItem;
	}


	public int getNbItem() {
		return nbItem;
	}
	public void setNbItem(int newNbItem) {
		nbItem = newNbItem;
	}
}
