package Game.ConstantsContainers.GraphicConstants;

/** Class ConstantGraphicAttriubutes
 */
public class ConstantGraphicAttributes {

	/**Largeur max du board */
	protected int maxX = 16_000;
	/**Hauteur max du board */
	protected int maxY = 10_000;

	/**Largeur d'une unite graphique */
	protected double oneUnityWidth = 1.0;
	/**Hauteur d'une unite graphique */
	protected double oneUnityHeight = 1.0;

	/**Coordonnees reelles */
	protected ConstantGraphicAttributes realAttributes;


	public ConstantGraphicAttributes(ConstantGraphicAttributes realAttributes, int boardJPanelWidth, int boardJPanelHeight) {
		this.realAttributes = realAttributes;
		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	public void updateConstantGraphicAttributes(int boardJPanelWidth, int boardJPanelHeight) {
		// dimensions d'une unite
		this.oneUnityWidth = (double)boardJPanelWidth / (double)realAttributes.maxX;
		this.oneUnityHeight = (double)boardJPanelHeight / (double)realAttributes.maxY;

		// dimensions du JPanel
		this.maxX = boardJPanelWidth;
		this.maxY = boardJPanelHeight;

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public int getMaxX() {
		return maxX;
	}
	public int getMaxY() {
		return maxY;
	}
	public double getOneUnityWidth() {
		return oneUnityWidth;
	}
	public double getOneUnityHeight() {
		return oneUnityHeight;
	}
	public ConstantGraphicAttributes getRealAttributes() {
		return realAttributes;
	}

}