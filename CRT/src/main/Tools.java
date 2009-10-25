package main;

import java.util.Iterator;
import java.util.List;


/**
 * Various tools class.
 * @author Mathias COQBLIN
 */
public class Tools {
	
	
	/**
	 * Join list elements with a glue string. (from PHP)
	 * 
	 * @param pieces The array of strings to implode.
	 * @param glue
	 * @return
	 */
	public static String implode(String[] pieces, String glue) {
		String implodedString;
		
		if (pieces.length == 0)
			implodedString = "";
		
		else {
			StringBuffer sb = new StringBuffer();
			
			sb.append(pieces[0]);
			for (int i = 1; i < pieces.length; i++) {
				sb.append(glue);
				sb.append(pieces[i]);
			}
			
			implodedString = sb.toString();
		}
		
		return implodedString;
	}
	
	
	
	/**
	 * Join list elements with a glue string. (from PHP)
	 * Type of pieces does not matter, toString() method is called.
	 * 
	 * @param pieces The array of strings to implode.
	 * @param glue
	 * @return
	 */
	public static <T extends Object> String implode(List<T> pieces, String glue) {
		String implodedString;
		
		if (pieces.size() == 0)
			implodedString = "";
		
		else {
			StringBuffer sb = new StringBuffer();
			
			sb.append(pieces.get(0));
			
			Iterator<T> it = pieces.iterator();
			it.next(); // Avoid already treated first element
			while (it.hasNext()) {
				T piece = it.next();
				
				sb.append(glue);
				sb.append(piece.toString());
			}
			
			implodedString = sb.toString();
		}
		
		return implodedString;
	}
}
