package tinymonkeys.modele;

import static org.junit.Assert.*;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Adrian Fraisse
 * @version 1.0
 * 
 * Classe de tests unitaires de la classe SingeErratique.
 * Utilise des mocks des classes Ile et Pirate.
 */
public class TestSingeErratiqueMock {
	
	private static final int NB_IT = 1000000;
	private static final int ORDONNEE = 13;
	private static final int ORDONNEE_HAUT = 14;
	private static final int ORDONNEE_BAS = 12;
	private static final int ABSCISSE = 3;
	private static final int ABSCISSE_GAUCHE = 2;
	private static final int ABSCISSE_DROITE = 4;
	
	private SingeErratique singe;
	private Ile islandMock;
	private Pirate pirateMock;
	
	/**
	 * Méthode éxecutée avant chaque test.
	 */
	@Before
	public void setUp() {
		islandMock = EasyMock.createMock(Ile.class);
		pirateMock = EasyMock.createMock(Pirate.class);
		singe = new SingeErratique(ABSCISSE, ORDONNEE, islandMock);
	}

	/**
	 * Assure, à l'aide d'une capture des paramètres de la méthode 
	 * de vérification des déplacements de l'île,
	 * que la case retournée par getNextRandomPos() 
	 * est bien la première validée par l'île.
	 */
	@Test
	public void testGetNextRandomPos() {
		// Test case libre
		final Capture<Integer> capturedX = EasyMock.newCapture();
		final Capture<Integer> capturedY = EasyMock.newCapture();
		
		// La première case valide est retournée
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.captureInt(capturedX), EasyMock.captureInt(capturedY))).andReturn(true).once();
		
		EasyMock.replay(islandMock);
		
		final CaseVide caseVide = singe.getNextRandomPos();
		
		assertNotNull(caseVide);
		assertTrue("La case générée ne correspond pas", caseVide.coordonneesEgales(capturedX.getValue().intValue(), capturedY.getValue().intValue()));
		EasyMock.verify(islandMock);
	}
	
	/**
	 * Teste unitairement le déplacement nominal d'un singe :
	 * Déplacement dans une direction aléatoire puis tentative de meurtre du pirate.
	 */
	@Test
	public void testDeplacementSinge() {
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(true).once();
		EasyMock.expect(islandMock.getPirate()).andReturn(pirateMock).once();
		
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().once();
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
		assertTrue("Singe non déplacé", 
				singe.coordonneesEgales(ABSCISSE, ORDONNEE_HAUT)
				|| singe.coordonneesEgales(ABSCISSE, ORDONNEE_BAS)
				|| singe.coordonneesEgales(ABSCISSE_GAUCHE, ORDONNEE)
				|| singe.coordonneesEgales(ABSCISSE_DROITE, ORDONNEE));
	}
	
	/**
	 * Vérifie que le singe reste à sa position actuelle si aucun déplacement ne lui est possible. 
	 */
	@Test
	public void testDeplacementSingeImpossible() {
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubReturn(false);
		
		EasyMock.replay(islandMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		
		assertTrue("Singe non déplacé", singe.coordonneesEgales(ABSCISSE, ORDONNEE));
	}
	
	/**
	 * Test le blocage du singe dans trois directions.
	 */
	@Test
	public void testDeplacementSingeUneDirection() {
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_DROITE, ORDONNEE)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_GAUCHE, ORDONNEE)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_BAS)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_HAUT)).andReturn(true).once();
		
		EasyMock.expect(islandMock.getPirate()).andReturn(pirateMock).once();
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().once();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
		
		assertTrue("Singe non déplacé sur la case haute", singe.coordonneesEgales(ABSCISSE, ORDONNEE_HAUT));
	}
	
	/**
	 * Teste le blocage du singe dans 2 directions.
	 * 
	 */
	@Test
	public void testDeplacementSinge2Directions() {
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_DROITE, ORDONNEE)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_GAUCHE, ORDONNEE)).andStubReturn(true);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_BAS)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_HAUT)).andStubReturn(true);
		
		EasyMock.expect(islandMock.getPirate()).andReturn(pirateMock).once();
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().once();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
		
		assertTrue("Singe déplacé en bas ou à droite", singe.coordonneesEgales(ABSCISSE, ORDONNEE_HAUT) || singe.coordonneesEgales(ABSCISSE_GAUCHE, ORDONNEE));
	}
	
	/**
	 * Teste le blocage du singe dans 1 direction.
	 * 
	 */
	@Test
	public void testDeplacementSinge3Directions() {
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_DROITE, ORDONNEE)).andStubReturn(true);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_GAUCHE, ORDONNEE)).andStubReturn(true);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_BAS)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_HAUT)).andStubReturn(true);
		
		EasyMock.expect(islandMock.getPirate()).andReturn(pirateMock).once();
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().once();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
		
		assertTrue("Singe déplacé en bas", singe.coordonneesEgales(ABSCISSE, ORDONNEE_HAUT) || 
				singe.coordonneesEgales(ABSCISSE_GAUCHE, ORDONNEE) || singe.coordonneesEgales(ABSCISSE_DROITE, ORDONNEE));
	}
	
	/**
	 * Verifie l'équiprobabilité de la distribution des déplacements du singe 
	 * erratique dans le cas où 4 déplacements sont possibles.
	 */
	@Test
	public void testEquiprobabilite4Cases() {
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubReturn(true);
		
		EasyMock.expect(islandMock.getPirate()).andStubReturn(pirateMock);
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = ABSCISSE;
		int previousY = ORDONNEE;
		
		for (int i = 0; i < NB_IT; i++) {
			singe.deplacerSinge();
			if (singe.x == previousX && singe.y == (previousY + 1)) ++deplacementsHaut;
			else if (singe.x == previousX && singe.y == (previousY - 1)) ++deplacementsBas;
			else if (singe.x == (previousX + 1) && singe.y == previousY) ++deplacementsDroite;
			else if (singe.x == (previousX - 1) && singe.y == previousY) ++deplacementsGauche;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", ((float) deplacementsBas/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsBas/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Haut", ((float) deplacementsHaut/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsHaut/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Gauche", ((float) deplacementsGauche/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsGauche/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", ((float) deplacementsDroite/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsDroite/NB_IT));
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
	}
	
	/**
	 * Verifie l'équiprobabilité de la distribution des déplacements du singe 
	 * erratique dans le cas où seuls 3 déplacements sont possibles.
	 */
	@Test
	public void testEquiprobabilite3Cases() {
		
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubAnswer(
				new IAnswer<Boolean>() {

					@Override
					public Boolean answer() throws Throwable {
						int x = (int) EasyMock.getCurrentArguments()[0];
						int y = (int) EasyMock.getCurrentArguments()[1];
						return !(x == singe.getX() && y == singe.getY() + 1);
					}
				});
		
		EasyMock.expect(islandMock.getPirate()).andStubReturn(pirateMock);
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = ABSCISSE;
		int previousY = ORDONNEE;
		
		for (int i = 0; i < NB_IT; i++) {
			singe.deplacerSinge();
			if (singe.x == previousX && singe.y == (previousY + 1)) ++deplacementsHaut;
			else if (singe.x == previousX && singe.y == (previousY - 1)) ++deplacementsBas;
			else if (singe.x == (previousX + 1) && singe.y == previousY) ++deplacementsDroite;
			else if (singe.x == (previousX - 1) && singe.y == previousY) ++deplacementsGauche;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", ((float) deplacementsBas/NB_IT) > 0.32 &&  0.34 > ((float) deplacementsBas/NB_IT));
		assertEquals("Equiprobabilité des 4 cases - Aucun deplacement Haut", 0, deplacementsHaut);
		assertTrue("Equiprobabilité des 4 cases - Deplacement Gauche", ((float) deplacementsGauche/NB_IT) > 0.32 &&  0.34 > ((float) deplacementsGauche/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", ((float) deplacementsDroite/NB_IT) > 0.32 &&  0.34 > ((float) deplacementsDroite/NB_IT));
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
	}
	
	/**
	 * Verifie l'équiprobabilité de la distribution des déplacements du singe 
	 * erratique dans le cas où seuls 2 déplacements sont possibles.
	 */
	@Test
	public void testEquiprobabilite2Cases() {
		
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubAnswer(
				new IAnswer<Boolean>() {

					@Override
					public Boolean answer() throws Throwable {
						int x = (int) EasyMock.getCurrentArguments()[0];
						int y = (int) EasyMock.getCurrentArguments()[1];
						return !(x == singe.getX() && y == singe.getY() + 1 || x == singe.getX() - 1 && y == singe.getY());
					}
				});
		
		EasyMock.expect(islandMock.getPirate()).andStubReturn(pirateMock);
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = ABSCISSE;
		int previousY = ORDONNEE;
		
		for (int i = 0; i < NB_IT; i++) {
			singe.deplacerSinge();
			if (singe.x == previousX && singe.y == (previousY + 1)) ++deplacementsHaut;
			else if (singe.x == previousX && singe.y == (previousY - 1)) ++deplacementsBas;
			else if (singe.x == (previousX + 1) && singe.y == previousY) ++deplacementsDroite;
			else if (singe.x == (previousX - 1) && singe.y == previousY) ++deplacementsGauche;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", ((float) deplacementsBas/NB_IT) > 0.49 &&  0.51 > ((float) deplacementsBas/NB_IT));
		assertEquals("Equiprobabilité des 4 cases - Aucun deplacement Haut", 0, deplacementsHaut);
		assertEquals("Equiprobabilité des 4 cases - Aucun deplacement Gauche", 0, deplacementsGauche);
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", ((float) deplacementsDroite/NB_IT) > 0.49 &&  0.51 > ((float) deplacementsDroite/NB_IT));
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
	}
	
}
