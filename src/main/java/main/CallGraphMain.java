package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import exo1.CouplageAB;
import exo1.CouplageAll;
import exo2.Clustering;
import exo3.ClusteringSpoon;
import exo3.CouplageABSpoon;
import exo3.CouplageAllSpoon;
import graphs.CallGraph;
import graphs.StaticCallGraph;
import processors.ASTProcessor;

public class CallGraphMain extends AbstractMain {

	@Override
	protected void menu() {
		StringBuilder builder = new StringBuilder();
		builder.append("1. Static call graph.");
		builder.append("\n2. couplage entre 2 classes.");
		builder.append("\n3. couplage de tous les classes.");
		builder.append("\n4. Clustering.");
		builder.append("\n5. couplage entre 2 avec Spoon.");
		builder.append("\n6. couplage de tous les classe avec Spoon.");
		builder.append("\n7. Clustering avec Spoon.");
		builder.append("\n" + QUIT + ". pour quitter.");

		System.out.println(builder);
	}

	public static void main(String[] args) {
		CallGraphMain main = new CallGraphMain();
		BufferedReader inputReader;
		CallGraph callGraph = null;
		try {
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			if (args.length < 1)
				setTestProjectPath(inputReader);
			else
				verifyTestProjectPath(inputReader, args[0]);
			String userInput = "";

			do {
				main.menu();
				userInput = inputReader.readLine();
				main.processUserInput(userInput, callGraph);
				Thread.sleep(3000);

			} while (!userInput.equals(QUIT));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param userInput
	 * @param processor
	 */
	protected void processUserInput(String userInput, ASTProcessor processor) {
		CallGraph callGraph = (CallGraph) processor;
		Scanner sc = new Scanner(System.in);
		String classA, classB;
		try {
			switch (userInput) {
			case "1":
				callGraph = StaticCallGraph.createCallGraph(TEST_PROJECT_PATH);
				callGraph.log();
				break;

			case "2":
				
				System.out.println("Nom de Classe A : ");
				classA = sc.nextLine();
				System.out.println("Nom de Classe B : ");
				classB = sc.nextLine();
				
				System.out.println("Couplage ...\n");
				CouplageAB.couplageAB(TEST_PROJECT_PATH, classA, classB);
				
				break;

			case "3":
				System.out.println("Couplage ...\n");
				CouplageAll.couplageAll(TEST_PROJECT_PATH);
				break;
				
			case "4":
				System.out.println("Couplage...\n");
				Clustering.createClusters(TEST_PROJECT_PATH);
				break;			
			case "5":
				System.out.println("Nom de la Classe A : ");
				classA = sc.nextLine();
				System.out.println("Nom de la Classe B : ");
				classB = sc.nextLine();
				
				CouplageABSpoon.couplageABSpoon(TEST_PROJECT_PATH, classA, classB);
				break;
			
			case "6":
				System.out.println("\n Couplage ... \n");
				CouplageAllSpoon.couplageAllSpoon(TEST_PROJECT_PATH);
				break;
				
			case "7":
				System.out.println("\n Couplage ... \n");
				ClusteringSpoon.createClustersSpoon(TEST_PROJECT_PATH);
				break;
		
			case QUIT:
				System.out.println("Ciao ...");
				sc.close();
				return;

			default:
				System.err.println("Mauvaise entrée..");
				return;
			}
			
		} catch (IOException e) {
			sc.close();
			e.printStackTrace();
		}
	}

}
