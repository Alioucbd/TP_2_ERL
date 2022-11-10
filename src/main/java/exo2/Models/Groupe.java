package exo2.Models;

import java.util.List;

/**
 * Représente un élément de couple.
 */
public interface Groupe {

	/**
	 * Récupère les classes associées.
	 * @return Les classes associées.
	 */
	public List<String> getClasses();
	
	/**
	 * Récupère les éléments associés.
	 * @return Les éléments associés.
	 */
	public List<Groupe> getElements();
	
	/**
	 * Récupère le score.
	 * @return un score.
	 */
	public double getScore();
}