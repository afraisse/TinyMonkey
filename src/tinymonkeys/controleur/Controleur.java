package tinymonkeys.controleur;

import javax.swing.JFrame;

import tinymonkeys.modele.Ile;
import tinymonkeys.vue.Fenetre;

/**
 * Controleur du modele MVC de TinyMonkeys.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */
public class Controleur {

	/**
	 * Largeur de la carte en nombre de cases.
	 */
	private static final int LARGEUR_GRILLE = 30;

	/**
	 * Hauteur de la carte en nombre de cases.
	 */
	private static final int HAUTEUR_GRILLE = 30;

	/**
	 * Emplacement de l'image du pirate.
	 */
	private static final String IMAGE_PIRATE = "./img/Mon_pirate.png";

	/**
	 * Le nombre de singes erratiques.
	 */
	private static final int NOMBRE_SINGES_ERRATIQUES = 50;

	/**
	 * Fenetre de l'interface graphique.
	 */
	private Fenetre fenetre;

	/**
	 * Modele du jeu.
	 */
	private Ile monkeyIsland;

	/**
	 * Constructeur du controleur.
	 */
	public Controleur() {
		this.fenetre = new Fenetre("TinyMonkeys", this);

		// Sortie de l'application quand la fenetre est fermee via la croix.
		this.fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.fenetre.setVisible(true);

		this.monkeyIsland = new Ile();
		this.monkeyIsland.enregistreEcIle(this.fenetre);
		this.monkeyIsland.getSingesErratiques().enregistreEcBandeSinges(
				this.fenetre);

		this.monkeyIsland.creationCarte(Controleur.exempleCarte());
		this.monkeyIsland.creationTresor();
		
		this.monkeyIsland.ajoutSingesErratiques(NOMBRE_SINGES_ERRATIQUES);

		this.monkeyIsland.getPirate().enregistreEcPirate(this.fenetre);
		this.monkeyIsland.ajoutPirate(IMAGE_PIRATE);

	}

	/**
	 * Lance l'ensemble des personnages ayant leur vie propre.
	 */
	public void lanceEvolutionsPersonnages() {
		this.monkeyIsland.getSingesErratiques().start();
	}

	/**
	 * Methode permettant de faire la demande de deplacement du pirate. Cette
	 * methode fait suite a un appui sur une fleche directionnelle du clavier.
	 * 
	 * @param dx la direction en abscisse.
	 * @param dy la direction en ordonnee.
	 */
	public void demandeDeplacementPirate(int dx, int dy) {
		this.monkeyIsland.demandeDeplacementPirate(dx, dy);
	}

	/**
	 * Methode permettant de remplir une carte de l'ile selon la taille de
	 * constantes.
	 * 
	 * @return la carte de l'ile.
	 */
	private static int[][] exempleCarte() {
		final int[][] carte = new int[LARGEUR_GRILLE][HAUTEUR_GRILLE];
		int i = -1;
		int j = -1;
		
		while(++i < LARGEUR_GRILLE) {
			carte[i][0] = 0;
			carte[i][HAUTEUR_GRILLE - 1] = 0;
		}
		while (++j < HAUTEUR_GRILLE) {
			carte[0][j] = 0;
			carte[LARGEUR_GRILLE - 1][j] = 0;
		}
		
		int k = 0;
		while (++k < LARGEUR_GRILLE - 1) {
			int l = 0;
			while (++l < HAUTEUR_GRILLE - 1) {
				carte[k][l] = 1;
			}
		}
		return carte;
	}

}
