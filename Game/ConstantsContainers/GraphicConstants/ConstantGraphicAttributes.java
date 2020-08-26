package Game.ConstantsContainers.GraphicConstants;

/** Class ConstantGraphicAttriubutes
 */
public class ConstantGraphicAttributes {

	/**Largeur max du board */
	private int maxX = 16_000;
	/**Hauteur max du board */
	private int maxY = 10_000;

	/**Largeur d'une unite graphique */
	private double oneUnityWidth = 1.0;
	/**Hauteur d'une unite graphique */
	private double oneUnityHeight = 1.0;

	/**Coordonnees reelles */
	private ConstantGraphicAttributes real;

	public ConstantGraphicAttributes(ConstantGraphicAttributes real, int boardJPanelWidth, int boardJPanelHeight) {
		this.real = real;
		initConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}

	public void initConstantGraphicAttributes(int boardJPanelWidth, int boardJPanelHeight) {
		// dimensions d'une unite, maxX et maxY sont encore reels a cet instant
		this.oneUnityWidth = (double)boardJPanelWidth / (double)maxX;
		this.oneUnityHeight = (double)boardJPanelHeight / (double)maxY;

		// dimensions du JPanel, maxX et maxY sont maintenant graphiques
		this.maxX = boardJPanelWidth;
		this.maxY = boardJPanelHeight;

	}

	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		// nouvelles dimensions du JPanel
		this.maxX = boardJPanelNewWidth;
		this.maxY = boardJPanelNewHeight;

		// nouvelles dimensions d'une unite
		this.oneUnityWidth = (double)boardJPanelNewWidth / (double)real.maxX;
		this.oneUnityHeight = (double)boardJPanelNewHeight / (double)real.maxY;

	}

}