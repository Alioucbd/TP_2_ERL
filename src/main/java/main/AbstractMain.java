package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import processors.Processor;

public abstract class AbstractMain {
	public static String TEST_PROJECT_PATH;
	public static final String QUIT = "0";
	
	protected static void setTestProjectPath(BufferedReader inputReader) 
			throws IOException {
		
		System.out.println("entrer le chemin du projet: ");
		String userInput = inputReader.readLine();
		verifyTestProjectPath(inputReader, userInput);
	}
	
	protected static void verifyTestProjectPath(BufferedReader inputReader, 
			String userInput) throws IOException {
		
		while (!isJavaProject(userInput)) {
			System.err.println("Error: "+userInput+
					" le dossier/fichier n'existe pas. "
					+ "ressayer de nouveau: ");
			userInput = inputReader.readLine();
		}
		
		TEST_PROJECT_PATH = userInput;
	}
	
	protected static boolean isJavaProject(String projectPath) {
		return new File(projectPath).exists();
	}
	
	protected abstract void menu();
	
	protected void processUserInput(String userInput) {
		System.err.println("processUserInput(String userInput)"
				+ " isn't implemented for "+getClass().getCanonicalName());
	}
	
	protected void processUserInput(String userInput, Processor<?> processor) {
		System.err.println("processUserInput(String userInput, ASTProcessor processor)"
				+ " isn't implemented for "+getClass().getCanonicalName());
	}
	
	protected void processUserInput(BufferedReader reader, String userInput, Processor<?> processor) {
		System.err.println("processUserInput(BufferedReader reader, String userInput, ASTProcessor processor)"
				+ " isn't implemented for "+getClass().getCanonicalName());
	}
}
