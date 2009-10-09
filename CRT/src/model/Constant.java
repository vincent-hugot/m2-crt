package model;

public class Constant extends Variable {
	
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @param value
	 */
	public Constant(int value) {
		super();
		this.value = value;
	}

	/**
	 * @param name
	 * @param value
	 */
	public Constant(String name, int value) {
		super(name);
		this.value = value;
	}
	
	
}
