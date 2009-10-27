package main;

import ac.AC3;
import model.Model;
import translator.Translator;


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
		System.out.println("Translated model:\n" + currentModel);
		
		/* DEALING WITH THE COMMAND-LINE */
		if (args.length < 2) 
		{
			System.out.println("No commands to execute.");
			System.exit(0);
		}
		
		System.out.println("Executing commands:");
		
		/* save/restore system is deactivated for now... I'll be back... */
		/* saved model */ 
		//Model savedModel = null;
		
		
		/* Let's iterate over the array of commands */
		for (int i = 1 ; i < args.length ; i++) {
			String cmd = args[i];
			
			System.out.println("[***] Executing <" + cmd + ">:");
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
			else*/ if (cmd.equals("disp"))
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
				AC3 ac3 = new AC3(currentModel);
				ac3.ac3();
			}
			else if (cmd.equals("ac6"))
			{
				System.out.println("AC6 not implemented yet... please come back later.");
			}
			else 
			{
				System.err.println("Unknown command! Ignoring it...");
			}
		} /* end iteration over array of commands */
		
	}

}
