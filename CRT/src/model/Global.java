package model;

public final class Global {

	private Global() {}

	public enum OpType {
		ADD, SUB, MUL, DIV,
		EQUAL, NOT_EQUAL,
		GREATER, LOWER, GREATER_OR_EQUAL, LOWER_OR_EQUAL
	}
	
	public final static Operator add = new Operator(OpType.ADD);
	public final static Operator sub = new Operator(OpType.SUB);
	public final static Operator mul = new Operator(OpType.MUL);
	public final static Operator div = new Operator(OpType.DIV);
	public final static Operator equal = new Operator(OpType.EQUAL);
	public final static Operator not_equal = new Operator(OpType.NOT_EQUAL);
	public final static Operator greater = new Operator(OpType.GREATER);
	public final static Operator lower = new Operator(OpType.LOWER);
	public final static Operator greater_or_equal = new Operator(OpType.GREATER_OR_EQUAL);
	public final static Operator lower_or_equal = new Operator(OpType.LOWER_OR_EQUAL);
	
	public static Operator getOperator(OpType type) {
		Operator res;
		
		switch(type){
			case ADD : res = add;
						break;
			case SUB : res = sub;
						break;
			case MUL : res = mul;
						break;
			case DIV : res = div;
						break;
			case EQUAL : res = equal;
						break;
			case NOT_EQUAL : res = not_equal;
							break;
			case GREATER : res = greater;
							break;
			case LOWER : res = lower;
						break;
			case GREATER_OR_EQUAL : res = greater_or_equal;
									break;
			default : res = lower_or_equal;
		}
		
		return res;
	}
	
}
