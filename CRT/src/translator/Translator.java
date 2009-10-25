package translator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import model.Model;
import parser.ParseException;
import parser.Parser;
import parser.SimpleNode;
import parser.TokenMgrError;
import checking.CheckingVisitor;


/**
 * Main translating class. Takes a filename as argument, reads through it,
 * generates AST then graph/model. If any error occurs during AST generation or
 * checking, a ConstraintsError list is generated, and no AST nor model is
 * provided.
 * 
 * @author Mathias COQBLIN
 */
public class Translator {
	
	private String file;
	private String filename; // Avoids recalc of filename from file path
	private boolean isFile; // if the file called is a content String, not a pathname
	
	private SimpleNode ast;
	private ArrayList<ConstraintsError> errors;
	private Model model;
	
	
	/**
	 * Constructor for parsing a file
	 * @param file The filepath
	 */
	public Translator(String file) {
		this.file = file;
		this.filename = (new File(file)).getName();
		this.isFile = true;
		
		this.ast = null;
		this.errors = new ArrayList<ConstraintsError>();
		
		this.model = null;
	}
	
	
	/**
	 * Constructor for parsing a String
	 * Mostly used for testing purpose
	 * @param filename a fake filename (useless, only to have a different constructor)
	 * @param content the "file" content
	 */
	public Translator(String filename, String content) {
		this.file = content;
		this.filename = filename;
		this.isFile = false;
		
		this.ast = null;
		this.errors = new ArrayList<ConstraintsError>();
		
		this.model = null;
	}
	
	
	
	/** Main translation method, "set it and forget it" (run everything at once) */
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
		
		Reader r = getReader(); // File or String to read
		
		if (fail()) // File opening error
			return;
		
		Parser parser = new Parser(r);
		
		// Parsing (AST generation)
		try {
			ast = parser.constraints();
		} catch (NumberFormatException e) { // Number format error (syntax)
			errors.add(new ConstraintsError(filename, 0, e.getMessage()));
		} catch (TokenMgrError e) { // Lexer error
			errors.add(new ConstraintsError(filename, e.getLine(), e.getMessage()));
		} catch (ParseException e) { // Syntax error
			errors.add(new ConstraintsError(filename, e.getLine(), e.getMessage()));
		}
		
		
		try {
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Second step: checking
	 */
	public void checking() {
		if (fail()) return;
		
		CheckingVisitor cv = new CheckingVisitor(ast, filename);
		cv.check();
		
		// Looking for errors
		errors.addAll(cv.getErrors());
	}
	
	
	/**
	 * Last step: translation
	 */
	public void translation() {
		if (fail()) return;
		
		TranslationVisitor tv = new TranslationVisitor(ast);
		model = tv.translate();
	}
	
	
	
	/**
	 * Returns the reader to read through while parsing
	 * @return a FileReader or a StringReader, depending on the constructor call
	 */
	public Reader getReader() {
		Reader f;
		
		// Normal case: real file to parse
		if (isFile) {
			try {
				f = new FileReader(file);
			} catch (IOException e) {
				errors.add(new ConstraintsError(
					filename, 0, "Can't open \"" + filename + "\"."
				));
				return null;
			}
		}
		// Test case: reading through a file
		else {
			f = new StringReader(file);
		}
		
		
		return f;
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
	
	
	/**
	 * Dumping errors, for testing purpose
	 */
	public void dumpErrors() {
		for (int i=0; i<errors.size(); i++)
			System.out.println(errors.get(i));
	}
}
