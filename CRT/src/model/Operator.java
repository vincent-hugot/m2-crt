package model;

public final class Operator {
	
	public enum Arithmetic {
		ADD, SUB, MUL, DIV
	}
	
	public enum Constraint {
		EQUAL, NOT_EQUAL,
		GREATER, LOWER, GREATER_OR_EQUAL, LOWER_OR_EQUAL
	}
}
