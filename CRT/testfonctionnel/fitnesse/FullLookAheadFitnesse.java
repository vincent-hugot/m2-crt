package fitnesse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import model.Model;
import model.Variable;
import translator.Translator;
import valuation.FullLookAhead;

public class FullLookAheadFitnesse {

	private Translator translator;
	private Model model;
	private Collection<Variable> result;
	
	public FullLookAheadFitnesse(String fileContent){
		translator = new Translator("Test Fitnesse", fileContent);
		model = translator.translate();
		FullLookAhead fla = new FullLookAhead(model);
		
		result = fla.run();
		
	}
	
	public boolean oneOf(String list){
		boolean found = false;
		ArrayList<String> valuations = new ArrayList<String>();
		Set<Variable> vars;
		ArrayList<Set<Variable>> createdValuations = new ArrayList<Set<Variable>>();
		
		//Check that the list has the specified format
		if(list.matches("(?x)\\[(\\[(\\(\\w+,\\ -?\\d+\\))+\\])*\\]"))
		{
			//Creation of a list containing all the proposed valuations
			try {
				Pattern regex = Pattern.compile("\\[(?:\\(\\w+,\\ -?\\d+\\))+\\]", Pattern.COMMENTS);
				Matcher regexMatcher = regex.matcher(list);
				while (regexMatcher.find()) {
					valuations.add(regexMatcher.group());
				} 
			} catch (PatternSyntaxException ex) {
				// Syntax error in the regular expression
			}
			
			//Browsing the created list
			for (String val : valuations) {
				
				vars = new TreeSet<Variable>();
				try {
					Pattern regex = Pattern.compile("\\(\\w+,\\ -?\\d+\\)", Pattern.COMMENTS);
					Matcher regexMatcher = regex.matcher(val);
					
					//For each couple
					while (regexMatcher.find()) {
						
						//Obtention of the name of the variable
						String variableName = null;
						try {
							Pattern regex2 = Pattern.compile("\\w+", Pattern.COMMENTS);
							Matcher regexMatcher2 = regex2.matcher(regexMatcher.group());
							if (regexMatcher2.find()) {
								variableName = regexMatcher2.group(1);
							} 
						} catch (PatternSyntaxException ex) {
							// Syntax error in the regular expression
						}
						
						//Obtention of the value of the variable
						String variableValue = null;
						int value;
						try {
							Pattern regex3 = Pattern.compile("-?\\d+", Pattern.COMMENTS);
							Matcher regexMatcher3 = regex3.matcher(regexMatcher.group());
							if (regexMatcher3.find()) {
								variableValue = regexMatcher3.group();
							
							} 
						} catch (PatternSyntaxException ex) {
							// Syntax error in the regular expression
						}
						
						value = Integer.parseInt(variableValue);
						vars.add(new Variable(variableName, value, value));
					} 
				} catch (PatternSyntaxException ex) {
					// Syntax error in the regular expression
				}
				
				createdValuations.add(vars);
			}
			
			Iterator<Set<Variable>> it = createdValuations.iterator();
			
			boolean end = false;
			
			//Browsing the valuations to see if there is one corresponding to the result of FLA
			while(!end){
				if(it.hasNext()){
					
					if(result.equals(it.next())){
						end = true;
						found = true;
					}
				}
				else{
					end = true;
				}
				
			}			
		}
		
		return found;
	}
}
