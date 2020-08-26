package Game.ConstantsContainers.GraphicConstants.EntityConstants;

import Game.ConstantsContainers.GraphicConstants.ConstantGraphicAttributes;


public class CharacterConstants extends ConstantGraphicAttributes {

	/**Largeur des personnages */
	private int characterWidth = 1_600;
	/**Hauteur des personnages*/
	private int characterHeight = 2_000;

	/**Coordonnee principale en X du personnage a gauche (position sur la plateforme au debut) */
	private int primaryXcoordLeft = 3_800;
	/**Coordonnee secondaire en X du personnage a gauche (position lors d'un respawn) */
	private int secondaryXcoordLeft = 1_800;

	/**Coordonnee principale en X du personnage a droite (position sur la plateforme au debut) */
	private int primaryXcoordRight = 12_200;
	/**Coordonnee secondaire en X du personnage a droite (position lors d'un respawn) */
	private int secondaryXcoordRight = 14_200;


	public CharacterConstants(CharacterConstants realAttributes, int boardJPanelWidth, int boardJPanelHeight) {
		super(realAttributes, boardJPanelWidth, boardJPanelHeight);
		updateConstantGraphicAttributes(boardJPanelWidth, boardJPanelHeight);
	}


	public void updateConstantGraphicAttributes(int boardJPanelNewWidth, int boardJPanelNewHeight) {
		super.updateConstantGraphicAttributes(boardJPanelNewWidth, boardJPanelNewHeight);

		CharacterConstants real = (CharacterConstants)realAttributes;

		// dimensions des personnages
		characterWidth = (int)(real.characterWidth * oneUnityWidth);
		characterHeight = (int)(real.characterHeight * oneUnityHeight);

		// positions des personnages en X sur les plateforme
		primaryXcoordLeft = (int)(real.primaryXcoordLeft * oneUnityWidth);
		secondaryXcoordLeft = (int)(real.secondaryXcoordLeft * oneUnityWidth);
		primaryXcoordRight = (int)(real.primaryXcoordRight * oneUnityWidth);
		secondaryXcoordRight = (int)(real.secondaryXcoordRight * oneUnityWidth);
	}


	/* ======= */
	/* Getters */
	/* ======= */

	public int getCharacterWidth() {
		return characterWidth;
	}
	public int getCharacterHeight() {
		return characterHeight;
	}
	public int getPrimaryXcoordLeft() {
		return primaryXcoordLeft;
	}
	public int getSecondaryXcoordLeft() {
		return secondaryXcoordLeft;
	}
	public int getPrimaryXcoordRight() {
		return primaryXcoordRight;
	}
	public int getSecondaryXcoordRight() {
		return secondaryXcoordRight;
	}


}