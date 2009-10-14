package model;

import java.util.ArrayList;

public class Constant extends Variable {
	
	private int value;
	
	
	/**
	 * @param value
	 */
	public Constant(ArrayList<Integer> domain , boolean artificial, int value) {
		super("", domain, artificial);
		this.value = value;
	}
	

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
