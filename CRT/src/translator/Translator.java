package translator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import model.Model;
import parser.ParseException;
import parser.Parser;
import parser.SimpleNode;
import parser.TokenMgrError;
import checking.CheckingVisitor;


/**
 * Main translating class.
 * Takes a filename as argument, reads through it, generates AST then graph/model.
 * If any error occurs during AST generation or checking, a ConstraintsError list is
 * generated, and no AST nor model is provided.
 * @author Mathias COQBLIN
 */
public class Translator {
	
	private String file;
	private String filename; // Avoids recalc of filename from file path
	private SimpleNode ast;
	private ArrayList<ConstraintsError> errors;
	private Model model;
	
	
	public Translator(String file) {
		this.file = file;
		this.filename = (new File(file)).getName();
		
		this.ast = null;
		this.errors = new ArrayList<ConstraintsError>();
		
		this.model = null;
	}
	
	
	public Model translate() {
		
		// First step: parsing
		parsing();
		
		if (fail()) {
			Collections.sort(errors);
			return model;
		}
		
		
		// Second step: checking
		checking();
		
		if (fail()) {
			Collections.sort(errors);
			return model;
		}
		
		
		// Last step: translating
		translation();
		
		
		return model;
	}
	
	
	
	/**
	 * First step: parsing the file and generating AST
	 */
	public void parsing() {
		FileReader f;
		
		try {
			f = new FileReader(file);
		}
		catch (IOException e) {
			errors.add(new ConstraintsError(
					filename,
					0,
					"Can't open \"" + filename + "\"."
			));
			return;
		}
		
		Parser parser = new Parser(f);
		
		// Parsing (AST generation)
		try {
			ast = parser.constraints();
		}
		catch (NumberFormatException e) { // Number format error (syntax)
			errors.add(new ConstraintsError(
				filename,
				0,
				e.getMessage()
			));
		}
		catch (TokenMgrError e) { // Lexer error
			errors.add(new ConstraintsError(
				filename,
				e.getLine(),
				e.getMessage()
			));
		}
		catch (ParseException e) { // Syntax error
			errors.add(new ConstraintsError(
				filename,
				e.getLine(),
				e.getMessage()
			));
		}
	}
	
	
	/**
	 * Second step: checking
	 */
	public void checking() {
		if (fail()) return;
		
		CheckingVisitor cv = new CheckingVisitor(ast,filename);
		cv.check();
		
		// Looking for errors
		errors.addAll(cv.getErrors());
	}
	
	
	/**
	 * Last step: translation
	 */
	public void translation() {
		if (fail()) return;
		
		// TODO : Translation (visitor)
		//TranslatingVisitor tv = new TranslatingVisitor(ast);
		//model = cv.translate();
	}
	
	
	
	
	/**
	 * Returns the parsing state
	 */
	public boolean fail() {
		return (!errors.isEmpty());
	}
	
	
	public SimpleNode getAST() {
		return ast;
	}
	
	public ArrayList<ConstraintsError> getErrors() {
		return errors;
	}
	
	public Model getModel() {
		return model;
	}
}
