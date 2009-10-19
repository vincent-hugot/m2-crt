package model;

public final class Global {

	private Global() {}

	public enum OpType {
		ADD, SUB, MUL, DIV,
		EQUAL, NOT_EQUAL,
		GREATER, LOWER, GREATER_OR_EQUAL, LOWER_OR_EQUAL
	}
}
