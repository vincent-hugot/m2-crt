package ac;

import java.util.HashSet;

import model.Variable;

/**
 * Most of the time the AC6 algorithm is manipulating couples. These couples are pairs containing a Variable and a given
 * value for it. Also for each of these couples it is necessary to know all the variables supported by the current
 * ValuedVariable in constraints
 * 
 * @author olivier
 * 
 */
public class ValuedVariable {

	private Variable				var;
	private Integer					val;

	/*
	 * sVarVar contains all the ValuedVariables (i, a) such that val is the smallest value supporting (i, a) on the
	 * constraint between i and var
	 */
	private HashSet<ValuedVariable>	sVarVal;

	/**
	 * Default constructor everything is empty
	 */
	public ValuedVariable() {
		var = null;
		val = null;
		sVarVal = null;
	}

	/**
	 * Building a new Value as a copy of an existing one
	 * 
	 * @param v
	 *            The ValuedVariable to copy
	 */
	@SuppressWarnings("unchecked")
	public ValuedVariable(ValuedVariable v) {
		this.var = v.var;
		this.val = new Integer(v.val);
		this.sVarVal = (HashSet<ValuedVariable>) v.sVarVal.clone();
	}

	/**
	 * Building a ValuedVariable from a Variable and its value
	 * 
	 * @param var
	 *            The Variable
	 * @param val
	 *            Its given value
	 */
	public ValuedVariable(Variable var, Integer val) {
		this.var = var;
		this.val = val;
		sVarVal = new HashSet<ValuedVariable>();
	}

	/**
	 * Retrieve the variable
	 * 
	 * @return The contained Variable
	 */
	public Variable getVar() {
		return var;
	}

	/**
	 * Retrieve the value
	 * 
	 * @return The contained value
	 */
	public Integer getVal() {
		return val;
	}

	/**
	 * Retrieve all the ValuedVariables associated with the current one
	 * 
	 * @return A set containing the ValuedVariables associated to the current
	 */
	public HashSet<ValuedVariable> getSVarVal() {
		return sVarVal;
	}

	/**
	 * Determine if a valuedVariable is equal to the current one
	 * 
	 * @param v
	 *            The ValuedVariable to test
	 * @return if V is or not equal to the current object
	 */
	public boolean equals(ValuedVariable v) {
		return (this.val == v.val && this.var == v.var);
	}

	/**
	 * Determine if an Object is equal to the current one
	 * 
	 * @param obj
	 *            The object to test
	 * @return if V is or not equal to the current object
	 */
	public boolean equals(Object obj) {
		return (obj instanceof ValuedVariable && this.equals((ValuedVariable) obj));
	}

	/**
	 * Add a ValuedVariable to the set
	 * 
	 * @param v
	 *            The object we want to add
	 */
	public void add(ValuedVariable v) {
		sVarVal.add(v);
	}

	/**
	 * Modify the value
	 * 
	 * @param val
	 *            The new value
	 */
	public void setVal(Integer val) {
		this.val = val;
	}

	public String toString(){
		return var + " \n value : " + val;
	}
}
