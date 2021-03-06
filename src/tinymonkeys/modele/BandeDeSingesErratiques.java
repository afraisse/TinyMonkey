package tinymonkeys.modele;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

/**
 * Classe d'une bande de singes erratiques.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */
public class BandeDeSingesErratiques extends Thread {

	/**
	 * Temporisation entre chaque déplacement de singe.
	 */
	private static final int TEMPO_DEPLACEMENT = 250;

	/**
	 * Vecteur contenant l'ensemble des singes erratiques.
	 */
	private Vector<SingeErratique> erratiques;
	
	private static final Logger log = Logger.getGlobal();

	/**
	 * L'ile.
	 */
	private Ile monkeyIsland;

	/**
	 * Liste des écouteurs sur la bande de singes erratiques.
	 */
	final private EventListenerList bandeSingesEcouteurs;

	/**
	 * Constructeur d'une bande de singes erratiques vide.
	 * 
	 * @param ile l'ile contenant l'ensemble des elements de celle-ci.
	 */
	public BandeDeSingesErratiques(Ile ile) {
		super();
		this.erratiques = new Vector<SingeErratique>();
		this.monkeyIsland = ile;
		this.bandeSingesEcouteurs = new EventListenerList();
	}

	/**
	 * Accesseur en lecture a l'ensemble des singes erratiques.
	 * 
	 * @return le vecteur de singes erratiques.
	 */
	public Vector<SingeErratique> getSingesErratiques() {
		return this.erratiques;
	}

	/**
	 * Ajout du nombre indique de singes erratiques a des positions libres
	 * aleatoires.
	 * 
	 * @param n le nombre de singes a ajouter.
	 */
	public void ajoutSingesErratiques(int n) {
		
		// On récupère toutes les cases terre
		final List<CaseVide> casesTerre = this.monkeyIsland.genererListCasesTerre();
		
		// Si n est supérieur au nombre de case disponibles, on prend cette valeur (- 2 pour laisser une case au pirate)
		int nombreSinges = n;
		if (n >= casesTerre.size()) {
			nombreSinges = casesTerre.size() - 2;
		}
		
		final Random random = new Random();
		
		for (int i=0; i<nombreSinges; i++) {
			// On prend une case terre de manière aléatoire
			final CaseVide nextCase = casesTerre.remove(random.nextInt(casesTerre.size() + 1));
			this.erratiques.add(new SingeErratique(nextCase.x, nextCase.y, this.monkeyIsland));
			
			final int id = i;
			Arrays.asList(this.bandeSingesEcouteurs.getListeners(BandeDeSingesErratiquesEcouteur.class))
			 	.forEach(listener -> listener.creationSingeErratique(id, nextCase.x, nextCase.y));
		}
	}

	/**
	 * Enregistre dans la liste des ecouteurs de bande de singes l'ecouteur
	 * passe en parametre.
	 * 
	 * @param ecouteur ecouteur de la bande de singes.
	 */
	public void enregistreEcBandeSinges(BandeDeSingesErratiquesEcouteur ecouteur) {
		this.bandeSingesEcouteurs.add(BandeDeSingesErratiquesEcouteur.class,
				ecouteur);
	}

	@Override
	public void run() {
		while (true) {
			// Déplace les singes, les uns après les autres, de manière temporisée
			int size = this.erratiques.size();
			for (int i = 0; i < size; i++) {
				final SingeErratique singe = this.erratiques.get(i);
				singe.deplacerSinge();
				final int id = i;
				Arrays.asList(
						this.bandeSingesEcouteurs
								.getListeners(BandeDeSingesErratiquesEcouteur.class))
						.forEach(
								listener -> listener.deplacementSingeErratique(
										id, singe.getX(), singe.getY()));
			}
			try {
				Thread.sleep(TEMPO_DEPLACEMENT);
			} catch (InterruptedException e) {
				log.info("Thread interrompu:" + e.getCause().getMessage());
			}
		}
	}

}
