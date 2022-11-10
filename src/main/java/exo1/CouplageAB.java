package exo1;

import java.io.IOException;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import graphs.StaticCallGraph;
import utility.Utility;

/**
 * EXERCICE 1 QUESTION 1
 */
public class CouplageAB {

	/**
	 * Calcul la métrique du couplage entre 2 classes données. (Ex0 1 - Question1)
	 * @param projet Le chemin du projet
	 * @param classA Le nom de la classe A.
	 * @param classB Le nom de la classe B.
	 */
	public static void couplageAB(String projet, String classA, String classB) {
		try {
			
			StaticCallGraph graph = StaticCallGraph.createCallGraph(projet);

			int couplageAB = CouplageAB.nbRelation(classA, classB, graph);
			int totalCouplage = CouplageAB.nbTotalRelaton(graph);

			System.out.println("nombre de relation(s) entre 2 classes A et B: " + couplageAB);
			System.out.println("nombre total de relation(s) : " + totalCouplage);

			double couplage = (couplageAB / (1.0*totalCouplage) * 100);

			System.out.println("Couplage : " + String.format("%.3f", couplage) + " %\n\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * Calcule le nombre de relations entre deux classes données.
	 * calculateNumerator nbRelation
	 */
	public static int nbRelation(String classA, String classB, StaticCallGraph graph) throws IOException {

		int nbRelation = 0;
		// parcourt des methodes declarés dans la classe A
		for (String methode : graph.getInvocations().keySet().stream().filter(t -> {
			String s = t.split("::")[0];
			return s.contains("."+classA) || s.equals(classA);
		}).collect(Collectors.toList())) {

			// parcourt des methodes invoquées dans la classe A
			for (String invokedMethod : graph.getInvocations().get(methode).keySet().stream().filter(t -> {
				String s = t.split("::")[0];
				return s.contains("."+classB) || s.equals(classB);
			}).collect(Collectors.toList())) {

				if (invokedMethod != null) {
					nbRelation += graph.getInvocations().get(methode).get(invokedMethod);
				}
			}
		}

		// parcourt des methodes declarés dans la classe B
		for (String methode : graph.getInvocations().keySet().stream().filter(t -> {
			String s = t.split("::")[0];
			return s.contains("."+classB) || s.equals(classB);
		}).collect(Collectors.toList())) {

			// parcourt des methodes invoquées dans la classe B
			for (String invokedMethod : graph.getInvocations().get(methode).keySet().stream().filter(t -> {
				String s = t.split("::")[0];
				return s.contains("."+classA) || s.equals(classA);
			}).collect(Collectors.toList())) {

				if (invokedMethod != null) {
					nbRelation += graph.getInvocations().get(methode).get(invokedMethod);
				}
			}
		}
		
		return nbRelation;
	}

	/**
	 * Calcule le nombre de relations entre toutes les classes.
	 * calculateCoupling nbTotalRelaton
	 */
	public static int nbTotalRelaton(StaticCallGraph graph) throws IOException {

		int nbTotalRelaton = 0;
		for (TypeDeclaration class_a : graph.getClasses()) {
			
			String classA = Utility.getClassFullyQualifiedName(class_a);
			for (TypeDeclaration class_b : graph.getClasses()) {
				
				String classB = Utility.getClassFullyQualifiedName(class_b);
				if (!classA.equals(classB)) {
					nbTotalRelaton += nbRelation(classA, classB, graph);
				}
			}
		}

		return nbTotalRelaton;
	}
}

