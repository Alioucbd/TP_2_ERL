package exo3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exo1.CouplageAB;
import exo2.Models.Cluster;
import exo2.Models.ClusteringClass;
import exo2.Models.Groupe;
import graphs.StaticCallGraph;
import spoon.Launcher;
import spoon.reflect.CtModel;

public class ClusteringSpoon {


	/**
	 * Récupère les classes sous forme d'éléments de couplage.
	 * 
	 * @param model Le modèle Spoon.
	 * @return Les éléments de couplage.
	 */
	private static List<Groupe> getGroupes(StaticCallGraph graph) {

		List<Groupe> groups = new ArrayList<>();
		List<String> classNames = graph.getClassesSpoon().stream().map(c -> c.getQualifiedName()).collect(Collectors.toList());
		

		for (String className : classNames) {

			groups.add(new ClusteringClass(className));
		}

		return groups;
	}

	private static List<Groupe> getCouplage(StaticCallGraph graph, List<Groupe> clusters) {

		List<Groupe> list = new ArrayList<>();

		try {

			List<Groupe> observedElement = new ArrayList<>();

			int couplageTotal = CouplageABSpoon.nombreRelationSpoon(graph);

			for (Groupe groupA : clusters) {
				for (Groupe groupB : clusters) {

					if (!groupA.equals(groupB) && !observedElement.contains(groupB)) {

						double couplage = 0;

						for (String classNameA : groupA.getClasses()) {

							for (String classNameB : groupB.getClasses()) {

								int couplageAB = CouplageAB.nbRelation(classNameA, classNameB,
										graph);

								couplage += (couplageAB / (1.0*couplageTotal)) * 100;
							}
						}

						Cluster tempCluster = new Cluster(groupA, groupB, couplage);
						list.add(tempCluster);
					}
				}

				observedElement.add(groupA);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Récupère le meilleur couplage.
	 * 
	 * @param clusters Tous les couplages possibles.
	 * @return Le meilleur couplage.
	 */
	private static Groupe getBestCoupling(List<Groupe> clusters) {

		if (clusters.isEmpty()) {
			return null;
		}

		double maxScore = clusters.stream().mapToDouble(c -> c.getScore()).max().orElse(0.0);

		return clusters.stream().filter(c -> c.getScore() == maxScore).findFirst().orElse(null);
	}

	/**
	 * Crée la hiérarchie de couplages.
	 * 
	 * @param model Le modèle Spoon.
	 * @return Le regroupement.
	 */
	public static Groupe createClustersSpoon(String projectPath) {

		// Creation de Spoon
		Launcher spoon = new Launcher();
		spoon.addInputResource(projectPath);

		CtModel model = spoon.buildModel();
		
		StaticCallGraph graph = StaticCallGraph.createCallGraphSpoon(model);

		List<Groupe> groupElements = getGroupes(graph);

		List<Groupe> possibleClusters = getCouplage(graph, groupElements);

		while (groupElements.size() > 1) {

			Groupe bestCouple = getBestCoupling(possibleClusters);
			List<Groupe> clusterToRemove = new ArrayList<>();

			for (Groupe couple : groupElements) {

				for (String className : couple.getClasses()) {

					if (bestCouple.getClasses().contains(className)) {
						clusterToRemove.add(couple);
					}
				}
			}

			groupElements.removeAll(clusterToRemove);
			groupElements.add(bestCouple);
			System.out.println("Cluster : " + bestCouple.toString());
			possibleClusters = getCouplage(graph, groupElements);
		}
		
		System.out.println("\n");
		return groupElements.get(0);
	}

}