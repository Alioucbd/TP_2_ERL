package tp1.umontpellier.fr;

import static org.junit.Assert.assertTrue;
import org.chocosolver.solver.Model;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SudokuBTTest 
{
    /**
     * Rigorous Test :-)
     * 
     *
     */

    @Test
	public void testTempsExécution() {
		int n = 4;
		
		SudokuPPC PPC = new SudokuPPC();
		SudokuBT  BT = new SudokuBT(n);
		long debutTempsBT, finTempsBT, tempsExecutionBT;
		long debutTempsPPC, finTempsPPC, tempsexecutionPPC;
		
	
		debutTempsBT = System.currentTimeMillis();
	
		 BT.findSolution(0, 0);
		finTempsBT = System.currentTimeMillis();
		tempsExecutionBT = finTempsBT - debutTempsBT;
		debutTempsPPC = System.currentTimeMillis();
		    PPC.solve();	
	    finTempsPPC = System.currentTimeMillis();
		tempsexecutionPPC = finTempsPPC - debutTempsPPC;

		System.out.println("Temps d'exécution SudokuBT : " + tempsExecutionBT + "ms");
		System.out.println("Temps d'exécution SudokuPPC : " + tempsexecutionPPC + "ms");
	}
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

}