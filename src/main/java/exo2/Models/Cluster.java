package exo2.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un couple de classes.
 */
public class Cluster implements Groupe {
	
	/**
	 * Les noms des classes regroupées.
	 */
	private List<Groupe> classes = new ArrayList<>();
	
	/**
	 * Le score.
	 */
	private double score;
	
	/**
	 * Constructeur.
	 * @param groupA Premier élément du couple.
	 * @param groupB Second élément du couple.
	 * @param coupling Le score.
	 */
	public Cluster(Groupe groupA, Groupe groupB, double coupling) {
		
		classes.add(groupA);
		classes.add(groupB);
		
		score = coupling;
	}

	@Override
	public List<String> getClasses() {
		
		List<String> classNames = new ArrayList<>();
		
		for (Groupe group : classes) {
			
			classNames.addAll(group.getClasses());
		}
		
		return classNames;
	}
	
	/**
	 * Récupère le score.
	 * @return Le score.
	 */
	public double getScore() {
		
		return this.score;
	}

	@Override
	public List<Groupe> getElements() {

		return this.classes;
	}
	
	@Override
	public String toString() {

		String ret = "\n\tScore : " + this.score;
		
		for (Groupe element : classes) {
			
			if (element instanceof ClusteringClass) {
				
				ret += element.toString();
			}
		}
		
		return ret;
	}
}