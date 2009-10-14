package translator;


/**
 * Error handling class. Contains the following information on an error : <br>
 * - Filename <br>
 * - Line number <br>
 * - Error content
 * 
 * @author Mathias COQBLIN
 */
public class ConstraintsError implements Comparable<ConstraintsError> {
	
	private String filename;
	private int line;
	private String msg;
	
	public ConstraintsError(String filename, int line, String msg) {
		this.filename = filename;
		this.line = line;
		this.msg = msg;
	}
	
	
	public int getLine() {
		return line;
	}
	
	
	/**
	 * Returns full error message, including filename and line
	 * 
	 * @return Error message
	 */
	public String toString() {
		if (filename == null || filename.isEmpty()) return line + ": " + msg;
		return filename + ":" + line + ": " + msg;
	}
	
	
	/**
	 * Compares 2 errors by their line number, for soorting purpose
	 * 
	 * @param e compared error
	 * @return -1 if <code>this</code> is before de <code>e</code>, 1 if after,
	 *         0 otherwise
	 */
	public int compareTo(ConstraintsError e) {
		if (this.getLine() < e.getLine()) return -1;
		if (this.getLine() > e.getLine()) return 1;
		return 0;
	}
}
