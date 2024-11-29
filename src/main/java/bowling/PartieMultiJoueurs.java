package bowling;

import java.util.ArrayList;
import java.util.List;


public class PartieMultiJoueurs {

	private final List<Integer> lancers = new ArrayList<>();
	private int tourCourant = 1;
	private int lancerDansTour = 1;
	private boolean partieTerminee = false;

	/**
	 * Constructeur
	 */
	public PartieMultiJoueurs() {
	}

	/**
	 * Enregistre un lancer.
	 *
	 * @param nombreDeQuillesAbattues le nombre de quilles abattues lors de ce lancer
	 * @throws IllegalStateException si la partie est terminée
	 * @return vrai si le joueur doit lancer à nouveau pour continuer son tour, faux sinon
	 */
	public boolean enregistreLancer(int nombreDeQuillesAbattues) {
		if (partieTerminee) {
			throw new IllegalStateException("La partie est terminée.");
		}

		// Enregistrer le lancer
		lancers.add(nombreDeQuillesAbattues);

		// Gestion du tour courant
		if (tourCourant < 10) {
			if (lancerDansTour == 1) {
				if (nombreDeQuillesAbattues == 10) { // Strike
					passerAuTourSuivant();
					return false;
				}
				lancerDansTour = 2; // Passer au deuxième lancer
				return true;
			} else { // Deuxième lancer
				passerAuTourSuivant();
				return false;
			}
		} else { // 10ème tour
			if (lancerDansTour == 1) {
				if (nombreDeQuillesAbattues == 10) { // Strike au premier lancer
					lancerDansTour = 2;
					return true;
				}
				lancerDansTour = 2;
				return true;
			} else if (lancerDansTour == 2) {
				if (scoreDesDeuxDerniersLancers() == 10) { // Spare
					lancerDansTour = 3;
					return true;
				}
				terminerPartie();
				return false;
			} else { // Troisième lancer
				terminerPartie();
				return false;
			}
		}
	}

	/**
	 * Calcul le score du joueur.
	 *
	 * @return Le score du joueur
	 */
	public int score() {
		int score = 0;
		int index = 0;

		for (int tour = 1; tour <= 10; tour++) {
			if (lancers.get(index) == 10) { // Strike
				score += 10 + bonusStrike(index);
				index++;
			} else if (index + 1 < lancers.size() && lancers.get(index) + lancers.get(index + 1) == 10) { // Spare
				score += 10 + bonusSpare(index);
				index += 2;
			} else { // Normal
				score += lancers.get(index) + (index + 1 < lancers.size() ? lancers.get(index + 1) : 0);
				index += 2;
			}
		}
		return score;
	}

	/**
	 * Vérifie si la partie est terminée.
	 *
	 * @return vrai si la partie est terminée, faux sinon
	 */
	public boolean estTerminee() {
		return partieTerminee;
	}

	/**
	 * Retourne le numéro du tour courant.
	 *
	 * @return Le numéro du tour courant [1..10], ou 0 si la partie est finie
	 */
	public int numeroTourCourant() {
		return partieTerminee ? 0 : tourCourant;
	}

	/**
	 * Retourne le numéro du prochain lancer pour le tour courant.
	 *
	 * @return Le numéro du prochain lancer [1..3], ou 0 si la partie est finie
	 */
	public int numeroProchainLancer() {
		return partieTerminee ? 0 : lancerDansTour;
	}

	// Méthodes privées utilitaires

	private void passerAuTourSuivant() {
		tourCourant++;
		lancerDansTour = 1;

		if (tourCourant > 10) {
			terminerPartie();
		}
	}

	private void terminerPartie() {
		partieTerminee = true;
	}

	private int bonusStrike(int index) {
		return lancers.get(index + 1) + (index + 2 < lancers.size() ? lancers.get(index + 2) : 0);
	}

	private int bonusSpare(int index) {
		return lancers.get(index + 2);
	}

	private int scoreDesDeuxDerniersLancers() {
		int size = lancers.size();
		return lancers.get(size - 1) + lancers.get(size - 2);
	}
	

}
