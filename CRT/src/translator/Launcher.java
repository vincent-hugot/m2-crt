package translator;

import java.util.ArrayList;


/**
 * Behold, the dreaded testing Launcher class!
 * 
 * @author Mathias COQBLIN
 */
public class Launcher {
	
	public static void main(String[] args) {
		
		String file = "src/parser/test.txt";
		Translator translator = new Translator(file);
		
		//Model model = translator.translate();
		translator.translate();
		
		// If any error, dumping them
		if (translator.fail()) {
			System.out.println("An error occurred!");
			
			ArrayList<ConstraintsError> errors = translator.getErrors();
			for (int i=0; i<errors.size(); i++) {
				ConstraintsError error = (ConstraintsError) errors.get(i);
				System.out.println(error);
			}
		}
		
		// else, for now, just dumping the AST
		else {
			System.out.println("Parsing successful!");
			translator.getAST().dump(">");
		}
	}
}
