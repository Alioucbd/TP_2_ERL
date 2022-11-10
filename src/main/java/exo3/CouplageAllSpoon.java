package exo3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exo1.CouplageAB;
import graphs.StaticCallGraph;
import spoon.Launcher;
import spoon.reflect.CtModel;

public class CouplageAllSpoon {

	public static void couplageAllSpoon(String projectPath) {
		
		try {
			
			System.out.println("Calcule ...");
			
			// Creation de Spoon
			Launcher spoon = new Launcher();
			spoon.addInputResource(projectPath);

			CtModel model = spoon.buildModel();
			
			StaticCallGraph graph = StaticCallGraph.createCallGraphSpoon(model);
			
			List<String> classNames = graph.getClassesSpoon().stream().map(c -> c.getQualifiedName()).collect(Collectors.toList());
			List<String> classVisiter = new ArrayList<>();

			int couplageTotal = CouplageABSpoon.nombreRelationSpoon(graph);

			for (String className1 : classNames) {
				for (String className2 : classNames) {

					if (!className1.equals(className2) && !classVisiter.contains(className2)) {

						int couplageAB = CouplageAB.nbRelation(className1, className2, graph);

						double couplage = (couplageAB / (1.0*couplageTotal)) * 100;

						System.out.println("couplage metrique entre " + className1 + " et " + className2 + " : "
								+ String.format("%.2f", couplage) + " %");
					}
				}
				
				classVisiter.add(className1);
			}
			
			System.out.print("\n\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
