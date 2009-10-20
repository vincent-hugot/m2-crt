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

	/*
	 * public void setValue(int value) { this.value = value; }
	 */
}
