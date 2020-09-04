package Menu;

/** Constantes utilisees pour determiner si la partie a ete lancee en local ou en ligne */
public interface TypeOfGameConstants {

	/** Le jeu est lance en local, deux joueurs sur un meme ordinateur */
	public static final int LOCAL_GAME = 0;

	/** Le jeu est lance en ligne, chaque joueur sur son propre ordinateur */
	public static final int ONLINE_GAME = 1;

}