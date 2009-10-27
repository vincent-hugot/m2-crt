package ac;

import java.util.HashSet;

import fitlibrary.traverse.workflow.SetVariableFixture;

import model.Variable;

public class Value {

	private Variable		var;
	private Integer			val;

	/**
	 * setVarVar contains all the Values (i, a) such that val is the smallest value supporting (i, a) on the constraint
	 * between i and var
	 */
	private HashSet<Value>	sVarVal;

	public Value() {
		var = null;
		val = null;
		sVarVal = null;
	}
	
	@SuppressWarnings("unchecked")
	public Value(Value v){
		this.var = v.var;
		this.val = new Integer(v.val);
		this.sVarVal = (HashSet<Value>) v.sVarVal.clone();
	}

	public Value(Variable var, Integer val) {
		this.var = var;
		this.val = val;
		sVarVal = new HashSet<Value>();
	}

	public Variable getVar() {
		return var;
	}

	public Integer getVal() {
		return val;
	}

	public HashSet<Value> getSVarVal() {
		return sVarVal;
	}

	public boolean equals(Value v) {
		return (this.val == v.val && this.var == v.var);
	}

	public boolean equals(Object obj) {
		return (obj instanceof Value && this.equals((Value) obj));
	}
	
	public void add(Value v) {
		sVarVal.add(v);
	}

	public void setVal(Integer val) {
		this.val = val;
	}

}
