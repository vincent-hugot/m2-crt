package model;


/**
 * Simple wraper class for "constant" variables.
 */
public class Constant extends Variable {

	private int	value;

	/**
	 * @param value
	 */
	public Constant(int value) {
		
		super(String.valueOf(value), value, value, true);
		this.value = value;
	}

	public int getValue() {

		return value;
	}
	
	public boolean equals(Object obj) {
		return (obj instanceof Constant && this.value == ((Constant) obj).value);
	}
	
	public String toString() {
		return String.valueOf(value) + " : " + domain;
	}
}
