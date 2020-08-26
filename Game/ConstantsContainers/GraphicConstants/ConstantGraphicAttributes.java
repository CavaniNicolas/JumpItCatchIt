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
	protected ConstantGraphicAttributes real;


	public ConstantGraphicAttributes(ConstantGraphicAttributes real, int boardJPanelWidth, int boardJPanelHeight) {
		this.real = real;
		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	public void updateConstantGraphicAttributes(int boardJPanelWidth, int boardJPanelHeight) {
		// dimensions d'une unite
		this.oneUnityWidth = (double)boardJPanelWidth / (double)real.maxX;
		this.oneUnityHeight = (double)boardJPanelHeight / (double)real.maxY;

		// dimensions du JPanel
		this.maxX = boardJPanelWidth;
		this.maxY = boardJPanelHeight;

	}

}