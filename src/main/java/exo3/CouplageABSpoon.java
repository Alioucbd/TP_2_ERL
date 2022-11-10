package exo3;

import java.io.IOException;

import exo1.CouplageAB;
import graphs.StaticCallGraph;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
/*
 * EXERCICE 3 QUESTION 1 DE L'EXERCICE 1 AVEC SPOON
 */
public class CouplageABSpoon {

	public static void couplageABSpoon(String projectPath, String classA, String classB) {
		
		try {

			System.out.println("Calcule ...");

			// Creation de Spoon
			Launcher spoon = new Launcher();
			spoon.addInputResource(projectPath);
			
			CtModel model = spoon.buildModel();
			
			StaticCallGraph graph = StaticCallGraph.createCallGraphSpoon(model);
			
			int couplageAB = CouplageAB.nbRelation(classA, classB, graph);
			int couplageTotal = nombreRelationSpoon(graph);

			System.out.println("Nombre de relations entre " + classA + " et " + classB + " : " + couplageAB);
			System.out.println("Nombre total de relation(s) : " + couplageTotal);

			double couplage = (couplageAB / (1.0*couplageTotal)) * 100;

			System.out.println("couplage metric : " + String.format("%.3f", couplage) + " %\n\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Calculer toutes les relations entre les classes avec Spoon.
	 * calculateCouplingSpoon
	 */
	public static int nombreRelationSpoon(StaticCallGraph graph) throws IOException {
		int nbRelation = 0;
		for (CtClass<?> a : graph.getClassesSpoon()) {
			
			String classA = a.getQualifiedName();
			for (CtClass<?> b : graph.getClassesSpoon()) {
				
				String classB = b.getQualifiedName();
				if (!classA.equals(classB)) {
					nbRelation += CouplageAB.nbRelation(classA, classB, graph);
				}
			}
		}

		return nbRelation;
	}
}
