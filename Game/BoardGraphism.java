package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

import Game.ConstantsContainers.GraphicConstants.*;
import Game.Gameplay.HUDCharacter;
import Menu.TypeOfGameConstants;


/** Class BoardGraphism<p>
 * Gere l'affichage
 */
public class BoardGraphism extends JPanel implements TypeOfGameConstants {
	private static final long serialVersionUID = 888598770874902032L;

	/** Le plateau de jeu */
	Board board;

	/***HUD */
	private HUDCharacter hudCharacter = new HUDCharacter();

	/** Type de partie : local ou en ligne, (par defaut en local) */
	private int typeOfGame = LOCAL_GAME;

	/**true une fois que les valeurs graphiques fixes ont ete initialisees */
	private boolean isGraphicUpdateDone = false;

	// Containers a Constantes Graphiques
	/**MainConstants Graphiques */
	private MainConstants mainConstants;

	/**CharacterConstants Graphiques*/
	private CharacterConstants characterConstants;
	/**ProjectileConstants Graphiques*/
	private ProjectileConstants projectileConstants;
	/**ItemConstants Graphiques*/
	private ItemConstants itemConstants;
	/**HUDConstants Graphiques*/
	private HUDConstants HUDConstants;
	/**GrabConstants Graphiques*/
	private GrabConstants grabConstants;


	/**Timer d'affichage du jeu */
	private Timer gameDisplayTimer;


	public BoardGraphism(Board board) {
		this.board = board;

		createGraphicConstantsContainers();

		gameDisplayTimer = new Timer(12, new GameDisplayTimerListener());
	}


	/**Creer les Containers a Constantes Graphiques */
	public void createGraphicConstantsContainers() {

		mainConstants = new MainConstants(new MainConstants());

		characterConstants = new CharacterConstants(new CharacterConstants());
		projectileConstants = new ProjectileConstants(new ProjectileConstants());
		itemConstants = new ItemConstants(new ItemConstants(mainConstants.getReal().getMaxX(), mainConstants.getReal().getMaxY()));
		HUDConstants = new HUDConstants(new HUDConstants());
		grabConstants = new GrabConstants(new GrabConstants());

	}


	/** starts the game display timer */
	public void startDisplaying() {
		gameDisplayTimer.start();
	}


	/**
	 * Fonction d'affichage principale
	 * <p>
	 * Appelee a l'aide de repaint().
	 * <p>
	 * Les fonctions drawTruc sont pour les objets en mouvement Lesfonctions
	 * displayTruc sont pour les objets fixes
	 * <p>
	 * TRES IMPORTANT : pour que this.getWidth() et this.getHeight() acquierent leur premiere valeur
	 * paintComponent() doit etre appllee au moins une fois ! Avant cela, la valeur de retour est 0.
	 */
	public void paintComponent(Graphics g) {
		// Initialisation des attributs graphiques, effectuee a chaque
		// redimensionnement de la fenetre
		updateGraphicCoordsAttributes();

		// Affiche les plateformes
		displayPlatforms(g);

		// Affiche les items
		board.getItemBalls().drawItems(g, mainConstants, itemConstants);

		// Affiche les personnages
		board.getCharacterRed().drawCharacter(g, mainConstants, characterConstants);
		board.getCharacterBlue().drawCharacter(g, mainConstants, characterConstants);

		// Affiche les grab
		board.getCharacterRed().drawGrab(g, mainConstants, grabConstants);
		board.getCharacterBlue().drawGrab(g, mainConstants, grabConstants);

		// Affiche les projectiles
		board.getCharacterRed().drawProjectiles(g, mainConstants, projectileConstants);
		board.getCharacterBlue().drawProjectiles(g, mainConstants, projectileConstants);

		// // Affiche la vie des joueurs
		hudCharacter.displayHUD(g, mainConstants, HUDConstants, board.getCharacterRed(), board.getCharacterBlue());
	}


	/**Actualise les coordonnees et les dimensions graphiques si la fenetre est redimensionnee */
	public void updateGraphicCoordsAttributes() {
		if (!isGraphicUpdateDone) {

			mainConstants.updateConstantGraphicAttributes(this.getWidth(), this.getHeight());

			double oneUnityWidth = mainConstants.getOneUnityWidth();
			double oneUnityHeight = mainConstants.getOneUnityHeight();

			characterConstants.updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
			projectileConstants.updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
			itemConstants.updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
			HUDConstants.updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);
			grabConstants.updateConstantGraphicAttributes(oneUnityWidth, oneUnityHeight);

			// Initialisation terminee
			this.isGraphicUpdateDone = true;
		}
	}


	/**Affiche les plateformes des deux joueurs */
	public void displayPlatforms(Graphics g) {
		int maxX = mainConstants.getMaxX();
		int maxY = mainConstants.getMaxY();

		int platformWidth = mainConstants.getPlatformWidth();
		int platformHeight = mainConstants.getPlatformHeight();

		// Le fond est gris clair
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, maxX, maxY);

		// Plateforme de gauche
		Color darkRed = new Color(92, 30, 31);
		g.setColor(darkRed);
		g.fillRect(0, maxY - platformHeight, platformWidth, platformHeight);

		// Plateforme de droite
		Color darkBlue = new Color(20, 45, 93);
		g.setColor(darkBlue);
		g.fillRect(maxX - platformWidth, maxY - platformHeight, platformWidth, platformHeight);
	}
	
	
	public class GameDisplayTimerListener implements ActionListener {
		
		/**Action a effectuer lorsque le timer renvoie un event */
		@Override
		public void actionPerformed(ActionEvent event) {
			
			//Actualise l'affichage graphique
			repaint();

		}
	}


	/* ======= */
	/* Setters */
	/* ======= */

	public void setIsGraphicUpdateDone(boolean isGraphicUpdateDone) {
		this.isGraphicUpdateDone = isGraphicUpdateDone;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setTypeOfGame(int typeOfGame) {
		this.typeOfGame = typeOfGame;
	}

	/* ======= */
	/* Getters */
	/* ======= */

	// Constant Containers
	public MainConstants getMainConstants() {
		return mainConstants;
	}
	public CharacterConstants getCharacterConstants() {
		return characterConstants;
	}
	public ProjectileConstants getProjectileConstants() {
		return projectileConstants;
	}
	public ItemConstants getItemConstants() {
		return itemConstants;
	}
	public HUDConstants getHUDConstants() {
		return HUDConstants;
	}
	public GrabConstants getGrabConstants() {
		return grabConstants;
	}


}