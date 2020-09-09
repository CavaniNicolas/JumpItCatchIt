package Game.Gameplay;

/** Constantes utilisees pour determiner la direction dans laquelle le grab est lance */
public interface LaunchGrabDir {

	/** Le grab n'a pas de direction, il est lance au dessus du vide */
	public static final int NO_GRAB = 0;

	/** Le grab est lance vers la droite */
	public static final int GRAB_RIGHT = 1;

	/** Le grab est lance vers la gauche */
	public static final int GRAB_LEFT = 2;

}
