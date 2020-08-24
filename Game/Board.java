package Game;

import Game.Items.ItemBalls;
import Menu.KeyBindings;
import Menu.FileFunctions;

import java.awt.Color;


/**
 * class Board extends JPanel
 * <p>
 * Gere le jeu
 */
public class Board {

	/**
	 * Les attributs graphiques et les fonctions d'affichage (les attributs sont
	 * initialises au premier appel de paintComponent()
	 */
	private BoardGraphism boardGraphism = new BoardGraphism();

	/**Booleen, true si le jeu est en cours */
	private boolean isPlaying; // Mnt dans GameLoop

	/** Personnage rouge (initialement a gauche) */
	private Character characterRed;
	/** Personnage bleu (initialement a droite) */
	private Character characterBlue;

	/** Items du jeu, tombant entre les deux plateformes lors du jeu */
	private ItemBalls itemBalls;


	/** Actualise le jeu */
	public void updateAll() {
		// Characters
		updateAllCollisionBorders();
		updateActionBooleans();
		updatePositionAndMoveAll();
		checkActions();

		// Projectiles
		moveProjectiles();
		checkProjectilesCollision();
		
		// Items
		createItems();
		moveItems();
	}

	/**
	 * Actualise les coordonnees de collision minimale et maximale de tous les
	 * objets
	 */
	public void updateAllCollisionBorders() {

		// Les personnages
		characterRed.updateCollisionBorders(boardGraphism, characterBlue);
		characterBlue.updateCollisionBorders(boardGraphism, characterRed);

	}


	/** Actualise les booleens d'actions de personnages */
	public void updateActionBooleans() {
		// Les personnages
		characterRed.updateActionBooleans(characterBlue);
		characterBlue.updateActionBooleans(characterRed);
	}


	/**
	 * Actualise la position de tous les objets, (les collisions sont gerees lors du
	 * deplacement des objets grace aux collision borders)
	 */
	public void updatePositionAndMoveAll() {

		// Les personnages
		characterRed.updatePosition(boardGraphism, characterBlue);
		characterBlue.updatePosition(boardGraphism, characterRed);
	}


	/** Verifie et Lance les actions a effectuer (grab shield shoot push) */
	public void checkActions() {

		// Les personnages
		characterRed.checkActions(boardGraphism);
		characterBlue.checkActions(boardGraphism);
	}


	/** Deplace les projectiles */
	public void moveProjectiles() {
		characterRed.moveProjectiles();
		characterBlue.moveProjectiles();
	}


	/** Verifie la collision des projectiles et inflige des degats en faisant disparaitre le projectile */
	public void checkProjectilesCollision() {
		characterRed.checkProjectilesCollision(characterBlue);
		characterBlue.checkProjectilesCollision(characterRed);
	}


	/** Creer les items qui tombent au milieu du plateau */
	public void createItems() {
		itemBalls.createItems(boardGraphism);
	}


	public void moveItems() {
		itemBalls.moveItems();
	}


	/**
	 * Initialise le jeu, creer les deux joueurs avec leurs touches claviers
	 * associees serialisees, la ArrayList d'objets
	 */
	public void initGame() {
		isPlaying = false;


		// Initialise les coordonnees reelles des objets
		boardGraphism.initRealCoordsAttributes();

		// On charge les objets (sans image) tout doit etre fonctionnel
		// Les fonctions d'affichage s'occuperont d'afficher des images si elles
		// existent, des carres sinon

		// On récupère les keyBindings des joueurs
		KeyBindings redKeyBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("red"));
		KeyBindings blueKeyBindings = FileFunctions.getBindings(FileFunctions.getPathFileToUse("blue"));


		// Creation des deux persos
		characterRed = new Character(boardGraphism.getReal().getPrimaryXcoordLeft(),
				boardGraphism.getReal().getGroundLevelYCoord(), true, Color.red, redKeyBindings, boardGraphism);
		characterBlue = new Character(boardGraphism.getReal().getPrimaryXcoordRight(),
				boardGraphism.getReal().getGroundLevelYCoord(), false, Color.blue, blueKeyBindings, boardGraphism);
				
		// init ItemBalls
		itemBalls = new ItemBalls();

		// On charge les images, et on les met dans les objets (null si elles n'ont pas reussi)
		loadAndSetAllImages();
	}


	/** Charge toutes les images du jeu et les ajoute aux objets */
	public void loadAndSetAllImages() {

	}


	/**Delay */
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}



	/* ======= */
	/* Getters */
	/* ======= */


	public void setIsPlaying(Boolean bool) {
		isPlaying = bool;
	}
	public boolean getIsPlaying() {
		return isPlaying;
	}


	public BoardGraphism getBoardGraphism() {
		return boardGraphism;
	}
	public Character getCharacterRed() {
		return characterRed;
	}
	public Character getCharacterBlue() {
		return characterBlue;
	}
	public ItemBalls getItemBalls() {
		return itemBalls;
	}

}
