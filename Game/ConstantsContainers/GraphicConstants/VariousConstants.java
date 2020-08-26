package Game.ConstantsContainers.GraphicConstants;


/** Class VariousConstants <p>
 * Contient les attributs graphiques constants divers
 */
public class VariousConstants extends ConstantGraphicAttributes {

	/**Largeur des plateformes */
	private int platformWidth = 5_000;
	/**Hauteur des plateformes (et en consequent hauteur du sol) */
	private int platformHeight = 1_500;


	/** Constructeur pour le stockage des constantes coordonnees Reelles */
	public VariousConstants() {
		super();
	}


	/** Constructeur pour le stockage des constantes coordonnees Graphiques */
	public VariousConstants(VariousConstants realAttributes, int boardJPanelWidth, int boardJPanelHeight) {
		super(realAttributes, boardJPanelWidth, boardJPanelHeight);
		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	@Override
	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		super.updateConstantGraphicAttributes(boardJPanelNewWidth, boardJPanelNewHeight);

		VariousConstants real = (VariousConstants)realAttributes;

		// dimensions des plateformes
		platformWidth = (int)(real.platformWidth * oneUnityWidth);
		platformHeight = (int)(real.platformHeight * oneUnityHeight);
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public int getPlatformWidth() {
		return platformWidth;
	}

	public int getPlatformHeight() {
		return platformHeight;
	}


}