package main;

import model.Model;
import translator.Translator;


public class Main {

	public static void main(String[] args) throws Exception {
		
		String file = "src/parser/test.txt";
		Translator tr = new Translator(file);
		Model m = tr.translate();
		tr.dumpErrors();
		tr.getAST().dump(">");
		
		System.out.println(m);
	}

}
