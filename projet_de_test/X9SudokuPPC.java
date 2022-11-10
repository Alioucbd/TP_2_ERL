package tp1.umontpellier.fr;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

public class X9SudokuPPC {

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

		new X9SudokuPPC().solve();
	}

	public void solve() {
		buildModel();

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

	public void addContraintes () {

		int [][] tab = new int[n][n];

		tab[0][0] = 8;

		tab[1][2] = 3;
		tab[1][3] =6;

		tab[2][1] = 7;
		tab[2][4] = 9;
		tab[2][6] = 2;

		tab[3][1] = 5;
		tab[3][5] = 7;

		tab[4][4] = 4;
		tab[4][5] = 5;
		tab[4][6] = 7;

		tab[5][3] = 1;
		tab[5][7] = 3;

		tab[6][2] = 1;
		tab[6][7] = 6;
		tab[6][8] = 8;

		tab[7][2] = 8;
		tab[7][3] = 5;
		tab[7][7] = 1;

		tab[8][1] = 9;
		tab[8][6] = 4;

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (tab[i][j] != 0)
					model.arithm(rows[i][j],"=",tab[i][j]).post();
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
		addContraintes();
	}

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

