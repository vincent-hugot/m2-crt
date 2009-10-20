package model;


public class Constant extends Variable {

	private int	value;

	/**
	 * @param value
	 */
	public Constant(int value) {
		
		super("", value, value, true);
		this.value = value;
	}

	public int getValue() {

		return value;
	}
	
	public boolean equals(Object obj){
		return (obj instanceof Constant && this.value == ((Constant) obj).value);
	}
}
