package Game.ConstantsContainers.GraphicConstants.EntityConstants;

import Game.ConstantsContainers.GraphicConstants.ConstantGraphicAttributes;


public class ProjectileConstants extends ConstantGraphicAttributes {

	/**Largeur des projectiles */
	private int projectileWidth = 1_000;
	/**Hauteur des projectiles */
	private int projectileHeight = 1_000;


	public ProjectileConstants(ProjectileConstants realAttributes, int boardJPanelWidth, int boardJPanelHeight) {
		super(realAttributes, boardJPanelWidth, boardJPanelHeight);
		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		super.updateConstantGraphicAttributes(boardJPanelNewWidth, boardJPanelNewHeight);

		ProjectileConstants real = (ProjectileConstants)realAttributes;

		// dimensions des projectiles
		projectileWidth = (int)(real.projectileWidth * oneUnityWidth);
		projectileHeight = (int)(real.projectileHeight * oneUnityHeight);

	}


	/* ======= */
	/* Getters */
	/* ======= */

	public int getProjectileWidth() {
		return projectileWidth;
	}
	public int getProjectileHeight() {
		return projectileHeight;
	}


}