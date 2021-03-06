package tinymonkeys.vue;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Classe de la vue d'un tresor.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */

public class VueTresor extends VueElement {

	/**
	 * UID auto-genere.
	 */
	private static final long serialVersionUID = -6188222545656061618L;

	/**
	 * Emplacement de l'image d'une banane.
	 */
	private static final String IMAGE_TRESOR = "./img/Tresor.png";
	
	private static final Logger log = Logger.getGlobal();

	/**
	 * La vue d'un tresor.
	 * 
	 * @param tailleCase la taille d'une case en nombre de pixels
	 * @param xGrille l'abscissse du coin supérieur gauche de la grille ou
	 *            placer l'image (en pixels).
	 * @param yGrille l'ordonnee du coin supérieur gauche de la grille ou placer
	 *            l'image (en pixels).
	 * @param x la position en abscisse du tresor (nombre de cases).
	 * @param y la position en ordonnee du tresor (nombre de cases).
	 */
	public VueTresor(int tailleCase, int xGrille, int yGrille, int x, int y) {
		super(tailleCase, xGrille, yGrille, x, y);

		try {
			final File input = new File(IMAGE_TRESOR);
			this.setImageElement(ImageIO.read(input));
		} catch (IOException ie) {
			log.info("Error:" + ie.getMessage());
		}

	}

}
