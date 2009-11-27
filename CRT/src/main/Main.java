package main;

import ac.AC3;
import ac.AC6;
import model.Model;
import translator.Translator;
import valuation.Backtracking;


public class Main {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 1)
		{
			System.err.println("Bad command line: not enough arguments");
			System.exit(1);
		}
		
		String source = args[0];
		
		/* TRANSLATION */
		System.out.println("Opening file <" + source + "> for translation...");
		Translator tr = new Translator(source);
		Model currentModel = tr.translate();
		if (tr.fail())
		{
			System.out.println("There were errors during the translation:");
			tr.dumpErrors();
			System.exit(2);
		}
		else
		{
			System.out.println("Translation was successful.");
		}
		
		//System.out.println("Translated model:\n" + currentModel);
		
		/* DEALING WITH THE COMMAND-LINE */
		if (args.length < 2) 
		{
			System.out.println("No commands to execute.");
			System.exit(0);
		}
		
		//System.out.println("Executing commands:");
		
		/* save/restore system is deactivated for now... I'll be back... */
		/* saved model */ 
		//Model savedModel = null;
		
		
		/* Let's iterate over the array of commands */
		boolean verbose = false;
		for (int i = 1 ; i < args.length ; i++) {
			String cmd = args[i];
			
			/*if (cmd.equals("save"))
			{
				System.out.println("Saving current model...");
				//TODO: clone
				savedModel = currentModel;
			}
			else if (cmd.equals("restore"))
			{
				System.out.println("Restoring saved model...");
				if (savedModel != null)
				{
					currentModel = savedModel;
				}
				else
				{
					System.err.println("There is no spoon... nor is there a saved model, dude!");
				}
			}
			else*/
			
			
			if (verbose)
				System.out.println("[***] Executing <" + cmd + ">:");
			
			
			if (cmd.equals("-v")) {
				verbose = true;
			}
			
			else if (cmd.equals("disp"))
			{
				System.out.println("Displaying current model:\n" + currentModel);
			}
			else if (cmd.equals("msg"))
			{
				if (args.length <= i)
				{
					System.err.println("No argument was provided to command <msg>!");
				}
				String msg = args[++i];
				System.out.println(">>>>>>>\n  " + msg + "\n<<<<<<<");
			}
			else if (cmd.equals("ac3"))
			{
				if (verbose)
					System.out.println("Applying AC3 algorithm...");
				AC3 ac3 = new AC3(currentModel);
				ac3.run();
			}
			else if (cmd.equals("latex"))
			{
				if (verbose) {
					System.out.println("% LaTeX graph of the current model:");
					// default warp values are +0.01 to avoid X.XXX E-30 or something
					// which LaTeX doesn't like (loss of float precision with sinus)
					System.out.println(currentModel.toLaTeX(2, 3.5, 0.51, 0.01));
				}
			}
			else if (cmd.equals("ac6"))
			{
				if (verbose)
					System.out.println("Applying AC6 algorithm...");
				AC6 ac6 = new AC6(currentModel);
				ac6.run();
				//System.out.println("AC6 not implemented yet... please come back later.");
			}
			else if (cmd.equals("backtrack")) {
				Backtracking backtrack = new Backtracking(currentModel);
				String result = backtrack.run();
				System.out.println("First found solution: " + result);
			}
			else 
			{
				System.err.println("Unknown command! Ignoring it...");
			}
		} /* end iteration over array of commands */
		
	}

}
