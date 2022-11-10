package tp1.umontpellier.fr;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

import java.io.IOException;

public class GTSudoku {

	static int n;
	static int s;
	private static int instance;
	private static long timeout = 3600000; // one hour

	IntVar[][] rows, cols, shapes;

	Model model;

	public static void main(String[] args) throws ParseException {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		boolean helpMode = line.hasOption("help"); // args.length == 0
		if (helpMode) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("sudoku", options, true);
			System.exit(0);
		}
		instance = 9;
		// Check arguments and options
		for (Option opt : line.getOptions()) {
			checkOption(line, opt.getLongOpt());
		}

		n = instance;
		s = (int) Math.sqrt(n);

		new GTSudoku().solve();
	}

	public void solve() {

		buildModel();
		model.getSolver().showStatistics();


		model.getSolver().solve();

		printGrid();

		model.getSolver().printStatistics();
	

	}

	public void printGrid() {

		String a;
		a = "┌───";
		String b;
		b = "├───";
		String c;
		c = "─┬────┐";
		String d;
		d = "─┼────┤";
		String e;
		e = "─┬───";
		String f;
		f = "─┼───";
		String g;
		g = "└────┴─";
		String h;
		h = "───┘";
		String k;
		k = "───┴─";
		String esp = " ";
		
		

		for (int i = 0; i < n; i++) {

			for (int line = 0; line < n; line++) {
				if (line == 0) {
					System.out.print(i == 0 ? a : b);
				} else if (line == n - 1) {
					System.out.print(i == 0 ? c : d);
				} else {
					System.out.print(i == 0 ? e : f);
				}
			}
			System.out.println("");
			System.out.print("│ ");
			for (int j = 0; j < n; j++) {
				if (rows[i][j].getValue() > 9)
					System.out.print(rows[i][j].getValue() + " │ ");
				else
					System.out.print(esp + rows[i][j].getValue() + " │ ");

			}

			if (i == n - 1) {
				System.out.println("");
				for (int line = 0; line < n; line++) {
					System.out.print(line == 0 ? g : (line == n - 1 ? h : k));
				}
			}
			System.out.println("");

		}
	}

	public void buildModel() {
		model = new Model();

		rows = new IntVar[n][n];
		cols = new IntVar[n][n];
		shapes = new IntVar[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				rows[i][j] = model.intVar("c_" + i + "_" + j, 1, n, false);
				cols[j][i] = rows[i][j];
			}
		}

		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				for (int k = 0; k < s; k++) {
					for (int l = 0; l < s; l++) {
						shapes[j + k * s][i + (l * s)] = rows[l + k * s][i + j * s];
					}
				}
			}
		}

		for (int i = 0; i < n; i++) {
			model.allDifferent(rows[i]).post();
			model.allDifferent(cols[i]).post();
			model.allDifferent(shapes[i]).post();

		}
       // addContraintes ();
        addContraintesGenerique ();
	}
		// --------------------------------------
		// TODO: add constraints here
	
    	

    //////////////////////////////////

   // Met la contrainte :  rows[i1][j1] < rows[i2][j2]
	private void putInferior (int i1, int j1, int i2, int j2) {
		(rows[i1][j1]).lt(rows[i2][j2]).post();
	}

	// Ajoute les contraintes de l'instance une à une.
	public void addContraintes () {

		// Carre 0 0
	putInferior(0, 0, 0, 1);
	putInferior(0, 1, 0, 2);

	putInferior(1, 0, 0, 0);
	putInferior(0, 1, 1, 1);
	putInferior(1, 2, 0, 2);

	putInferior(1,0, 1, 1);
	putInferior(1, 2, 1, 1);

	putInferior(2, 0, 1, 0);
	putInferior(2, 1, 1, 1);
	putInferior(1, 2, 2, 2);

	putInferior(2, 1, 2, 0);
	putInferior(2, 2, 2, 1);


	// Carre 0 1
	putInferior(0, 3, 0, 4);
	putInferior(0, 4, 0, 5);

	putInferior(0, 3, 1, 3);
	putInferior(0, 4, 1, 4);
	putInferior(1, 5, 0, 5);

	putInferior(1, 4, 1, 3);
	putInferior(1, 4, 1, 5);

	putInferior(1, 3, 2, 3);
	putInferior(1, 4, 2, 4);
	putInferior(2, 5, 1, 5);

	putInferior(2, 3, 2, 4);
	putInferior(2, 5, 2, 4);


	// Carre 0 2
	putInferior(0, 7, 0, 6);
	putInferior(0, 8, 0, 7);

	putInferior(1, 6, 0, 6);
	putInferior(0, 7, 1, 7);
	putInferior(1, 8, 0, 8);

	putInferior(1, 7, 1, 6);
	putInferior(1, 8, 1, 7);

	putInferior(2, 6, 1, 6);
	putInferior(1, 7, 2, 7);
	putInferior(2, 8, 1, 8);

	putInferior(2, 7, 2, 6);
	putInferior(2, 7, 2, 8);


	// Carre 1 0
	putInferior(3, 1, 3, 0);
	putInferior(3, 1, 3, 2);

	putInferior(4, 0, 3, 0);
	putInferior(4, 1, 3, 1);
	putInferior(4, 2, 3, 2);

	putInferior(4, 1, 4, 0);
	putInferior(4, 2, 4, 1);

	putInferior(5, 0, 4, 0);
	putInferior(4, 1, 5, 1);
	putInferior(4, 2, 5, 2);

	putInferior(5, 0, 5, 1);
	putInferior(5, 2, 5, 1);


	// Carre 1 1
	putInferior(3, 4, 3, 3);
	putInferior(3, 4, 3, 5);

	putInferior(3, 3, 4, 3);
	putInferior(3, 4, 4, 4);
	putInferior(4, 5, 3, 5);

	putInferior(4, 3, 4, 4);
	putInferior(4, 5, 4, 4);

	putInferior(5, 3, 4, 3);
	putInferior(5, 4, 4, 4);
	putInferior(4, 5, 5, 5);

	putInferior(5, 3, 5, 4);
	putInferior(5, 5, 5, 4);


	// Carre 1 2
	putInferior(3, 7, 3, 6);
	putInferior(3, 7, 3, 8);

	putInferior(4, 6, 3, 6);
	putInferior(4, 7, 3, 7);
	putInferior(3, 8, 4, 8);

	putInferior(4, 7, 4, 6);
	putInferior(4, 7, 4, 8);

	putInferior(5, 6, 4, 6);
	putInferior(5, 7, 4, 7);
	putInferior(4, 8, 5, 8);

	putInferior(5, 7, 5, 6);
	putInferior(5, 7, 5, 8);


	// Carre 2 0
	putInferior(6, 1, 6, 0);
	putInferior(6, 1, 6, 2);

	putInferior(6, 0, 7, 0);
	putInferior(6, 1, 7, 1);
	putInferior(7, 2, 6, 2);

	putInferior(7, 0, 7, 1);
	putInferior(7, 2, 7, 1);

	putInferior(8, 0, 7, 0);
	putInferior(8, 1, 7, 1);
	putInferior(8, 2, 7, 2);

	putInferior(8, 0, 8, 1);
	putInferior(8, 2, 8, 1);


	// Carre 2 1
	putInferior(6, 4, 6, 3);
	putInferior(6, 4, 6, 5);

	putInferior(7, 3, 6, 3);
	putInferior(6, 4, 7, 4);
	putInferior(7, 5, 6, 5);

	putInferior(7, 3, 7, 4);
	putInferior(7, 5, 7, 4);
	putInferior(7, 3, 8, 3);
	putInferior(7, 4, 8, 4);
	putInferior(7, 5, 8, 5);

	putInferior(8, 4, 8, 3);
	putInferior(8, 4, 8, 5);


	// Carre 2 2
	putInferior(6, 6, 6, 7);
	putInferior(6, 8, 6, 7);

	putInferior(6, 6, 7, 6);
	putInferior(7, 7, 6, 7);
	putInferior(7, 8, 6, 8);

	putInferior(7, 7, 7, 6);
	putInferior(7, 7, 7, 8);

	putInferior(8, 6, 7, 6);
	putInferior(7, 7, 8, 7);
	putInferior(7, 8, 8, 8);

	putInferior(8, 6, 8, 7);
	putInferior(8, 8, 8, 7);

		
	}
///////////////////////

public void addContraintesGenerique () {
	int [][] tab = {
			{0, 1, 0,   1, 2, 3,   3, 2, 3},
			{2, 3, 4,   0, 1, 4,   0, 1, 2},
			{1, 0, 1,   7, 6, 5,   1, 0, 1},

			{2, 1, 0,   0, 2, 1,   0, 1, 2},
			{0, 3, 1,   2, 1, 0,   3, 2, 0},
			{1, 2, 3,   0, 2, 1,   0, 3, 1},

			{1, 0, 1,   4, 3, 0,   3, 4, 5},
			{4, 3, 2,   5, 2, 1,   2, 1, 0},
			{1, 0, 1,   1, 0, 2,   3, 2, 1}
	};

	for (int i = 1; i < n; i+=3) {
		for (int j = 0; j < n; j++) {
			if (tab[i][j] < tab[i-1][j])
				putInferior(i, j, i-1, j);
			else if (tab[i][j] > tab[i-1][j])
				putInferior(i-1, j, i, j);
			if (tab[i][j] < tab[i+1][j])
				putInferior(i, j, i+1, j);
			else if (tab[i][j] > tab[i+1][j])
				putInferior(i+1, j, i, j);
		}
	}

	for (int i = 0; i < n; i++)
		for (int j = 1; j < n; j+=3) {
			if (tab[i][j] < tab[i][j-1])
				putInferior(i, j, i, j-1);
			else if (tab[i][j] > tab[i][j-1])
				putInferior(i, j-1, i, j);
			if (tab[i][j] < tab[i][j+1])
				putInferior(i, j, i, j+1);
			else if (tab[i][j] > tab[i][j+1])
				putInferior(i, j+1, i, j);
		}
}

    //////////////////////

	// Check all parameters values
	public static void checkOption(CommandLine line, String option) {

		switch (option) {
		case "inst":
			instance = Integer.parseInt(line.getOptionValue(option));
			break;
		case "timeout":
			timeout = Long.parseLong(line.getOptionValue(option));
			break;
		default: {
			System.err.println("Bad parameter: " + option);
			System.exit(2);
		}

		}

	}

	// Add options here
	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

		final Option instOption = Option.builder("i").longOpt("instance").hasArg(true).argName("sudoku instance")
				.desc("sudoku instance size").required(false).build();

		final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
				.desc("Set the timeout limit to the specified time").required(false).build();

		// Create the options list
		final Options options = new Options();
		options.addOption(instOption);
		options.addOption(limitOption);
		options.addOption(helpFileOption);

		return options;
	}

	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(rows)));

	}

}

