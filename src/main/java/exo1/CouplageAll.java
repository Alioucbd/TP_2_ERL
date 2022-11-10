package exo1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import graphs.StaticCallGraph;
import utility.Utility;

/**
 * EXERCICE 1 QUESTION 1
 * Représente le couplage de toute l'application.
 */
public class CouplageAll {

	/**
	 * Calcul la métrique du couplage de toute l'application. (Exercice 1 - Question 2).
	 * @param poject chemin du projet.
	 * couplingAllClasses couplageAll
	 */
	public static void couplageAll(String poject) {

		try {

			StaticCallGraph graph = StaticCallGraph.createCallGraph(poject);

			List<String> classNames = graph.getClasses().stream().map(s -> Utility.getClassFullyQualifiedName(s))
					.collect(Collectors.toList());

			List<String> classVissiter = new ArrayList<>();

			int couplageTotal = CouplageAB.nbTotalRelaton(graph);

			for (String className1 : classNames) {

				for (String className2 : classNames) {

					if (!className1.equals(className2) && !classVissiter.contains(className2)) {

						int couplageAB = CouplageAB.nbRelation(className1, className2, graph);

						double couplage = (couplageAB / 1.0*couplageTotal) * 100;

						System.out.println("Couplage metrique entre " + className1 + " et " + className2 + " : "
								+ String.format("%.2f", couplage) + " %");
					}
				}

				classVissiter.add(className1);
			}

			System.out.println("\n\t");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
