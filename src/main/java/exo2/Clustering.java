package exo2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exo1.CouplageAB;
import exo2.Models.Cluster;
import exo2.Models.ClusteringClass;
import exo2.Models.Groupe;
import graphs.StaticCallGraph;
import utility.Utility;

public class Clustering {

	/**
	 * Récupère les classes sous forme de groupe d'elements.
	 * 
	 */
	private static List<Groupe> getGroupes(StaticCallGraph graph) {

		List<Groupe> groups = new ArrayList<>();

		List<String> classNames = new ArrayList<>();

		classNames = graph.getClasses().stream().map(s -> Utility.getClassFullyQualifiedName(s))
				.collect(Collectors.toList());

		for (String className : classNames) {

			groups.add(new ClusteringClass(className));
		}

		return groups;
	}

	private static List<Groupe> getCouplage(List<Groupe> clusters, StaticCallGraph graph) {

		List<Groupe> list = new ArrayList<>();

		try {

			List<Groupe> elementVisiter = new ArrayList<>();

			int couplageTotal = CouplageAB.nbTotalRelaton(graph);

			for (Groupe groupA : clusters) {
				for (Groupe groupB : clusters) {

					if (!groupA.equals(groupB) && !elementVisiter.contains(groupB)) {

						double couplage = 0;

						for (String classNameA : groupA.getClasses()) {

							for (String classNameB : groupB.getClasses()) {

								int couplageAB = CouplageAB.nbRelation(classNameA, classNameB,
										graph);

								couplage += (couplageAB / (1.0*couplageTotal)) * 100;
							}
						}

						Cluster ret = new Cluster(groupA, groupB, couplage);
						list.add(ret);
					}
				}

				elementVisiter.add(groupA);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Récupère le meilleur couplage.
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
	 * Crée la hiérarchie de couplages (Exercice 2 - Question 1).
	 * 
	 */
	public static Groupe createClusters(String projectPath) throws IOException {

		StaticCallGraph graph = StaticCallGraph.createCallGraph(projectPath);

		List<Groupe> groupElements = getGroupes(graph);

		List<Groupe> possibleClusters = getCouplage(groupElements, graph);

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
			possibleClusters = getCouplage(groupElements, graph);
		}

		System.out.println("\n");
		return groupElements.get(0);
	}
	
}