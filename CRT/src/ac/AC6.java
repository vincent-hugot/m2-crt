package ac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import model.Constraint;
import model.Domain;
import model.Model;
import model.Substitution;
import model.Variable;

/**
 * Implementation of Arc Consistency algorithm AC6.
 * 
 * @author olivier
 * 
 */
public class AC6 {

	private Model						model;
	private HashSet<ValuedVariable>		values;
	private LinkedList<ValuedVariable>	waitingList;

	/**
	 * Constructor
	 * 
	 * @param m
	 *            The model we want to run the algorithm on
	 */
	public AC6(Model m) {
		model = m;
		values = null;
		waitingList = new LinkedList<ValuedVariable>();
	}

	/**
	 * Run the algorithm
	 */
	public void run() {
		init();
		propagation();
	}

	/**
	 * Used for a Constraint to find the smallest in the domain of the right variable not smaller than the given value b
	 * and supporting the given value a for the left variable. (The return value meaning was changed compared to the
	 * original algorithm)
	 * 
	 * @param cons
	 *            The constraint we are interested in..
	 * @param a
	 *            The given value for the left variable.
	 * @param b
	 *            The given value for the right variable, can be modified if the initial value does not support cons.
	 * @return Whether or not a relevant value can be found for b.
	 */
	private boolean nextSupport(Constraint cons, Integer a, Integer b) {
		boolean emptySupport;
		int tmpa, tmpb;

		Variable right;
		Variable left;

		right = cons.getRight();
		left = cons.getLeft();

		tmpb = b.intValue();
		tmpa = a.intValue();

		if (tmpb <= right.getDomain().last()) {
			emptySupport = false;

			// Search for the smallest value greater or equal than b that belongs right's domain
			while (!right.getDomain().contains(tmpb)) {
				tmpb++;
			}

			// Search of the smallest support for the couple (left, a) in right's domain
			while (!cons.areValidValues(left, right, tmpa, tmpb) && !emptySupport) {
				if (tmpb < right.getDomain().last()) {
					tmpb = right.getDomain().next(tmpb);
				}
				else {
					emptySupport = true;
				}
			}
		}
		else {
			emptySupport = true;
		}
		b = new Integer(tmpb);
		return !emptySupport;
	}

	/**
	 * Used to find in all the constraints beetween the variables stored in valA and valB and see if they support the
	 * given values for each variable (or at least not smaller than the one of valB)
	 * 
	 * @param valA
	 *            A couple Variable / Value for the left variable
	 * @param valB
	 *            Another couple Variable / Value for the right variable whose value can be modified
	 * @return true if all the constraints are satisfied
	 */
	private boolean nextSupport(ValuedVariable valA, ValuedVariable valB) {

		/*
		 * The principle here is basicaly the same as previously except that rather than searching in a unique
		 * constraint we look at all the constraints concerning the two given variables.
		 * 
		 * The function will return true if and only if there exist support for each constraint. The new value contained
		 * in valB if modified is the smallest of all the obtained values for the constraints.
		 */
		Variable i, j;
		Integer a, b;

		i = valA.getVar();
		j = valB.getVar();

		// Here we retrieve all the constraints concerning the two given variables
		ArrayList<Constraint> constraints = model.getConstraintConcerningVariables(i, j);
		boolean res;
		Integer tmp;
		Integer tmp2;

		a = valA.getVal();
		b = valB.getVal();

		tmp = new Integer(b.intValue());

		res = !constraints.isEmpty();

		// Then for each constraint we apply nextValue
		for (Constraint cons : constraints) {
			tmp2 = new Integer(b.intValue());
			res = (res && nextSupport(cons, a, tmp2));

			if (tmp.compareTo(tmp2) > 0) {
				tmp = new Integer(tmp2.intValue());
			}
		}

		valB.setVal(tmp);
		return res;
	}

	/**
	 * The initialisation of the algorithm
	 */
	private void init() {

		ValuedVariable tmp, tmp2;
		Integer b;

		// We build and retrieve in values all the possible couples (Variable, value) in the model
		values = model.getValues();

		// For each constraint
		for (Constraint cons : model.getConstraints()) {

			// For each possible value of the left's variable
			for (Integer a : cons.getLeft().getDomain()) {
				b = new Integer(1);

				// We retrieve from the set the ValuedVariable corresponding to the current Variable and value
				tmp = findValue(cons.getLeft(), a);

				// If there is no support in the constraint
				if (!nextSupport(cons, a, b)) {

					// We remove the current unnecessary value from the model
					cons.getLeft().getDomain().remove(a.intValue());

					// And then add the modified ValuedVariable to the waiting list
					if (tmp != null) {
						waitingList.add(tmp);
					}
				}
				else {
					// If this is the case we simply add the current couple (tmp) to the corresponding's one (tmp2)
					// support set
					tmp2 = findValue(cons.getRight(), b);
					if (tmp != null && tmp2 != null) {
						tmp2.add(tmp);
					}
				}
			}
		}
	}

	/**
	 * The propagation of the algorithm
	 */
	private void propagation() {
		ValuedVariable valb, valc;

		// Browsing through the waiting list
		while (!waitingList.isEmpty()) {
			valb = waitingList.pop();

			// For each of the ValuedVariables corresponding to the current one (valb)
			for (ValuedVariable vala : valb.getSVarVal()) {
				vala.getSVarVal().remove(vala);

				// If the given value for vala belongs to its domain (actually its Variable's domain)
				if (vala.getVar().getDomain().contains(vala.getVal())) {

					// Creating a copy of valb
					valc = new ValuedVariable(valb);

					// Checking if there is support for all the constraints between the two variables
					if (!nextSupport(vala, valc)) {

						// If not yet again we remove the value from the domain and replace the couple in the waiting
						// list
						vala.getVar().getDomain().remove(vala.getVal().intValue());
						waitingList.add(vala);

					}
					else {
						// If there is we simply add the current couple (vala) to the corresponding's one (valc) support
						// set
						valc.getSVarVal().add(vala);
					}
				}
			}
		}
	}

	/**
	 * Find the ValuedVariable correponding the given Variable and Value
	 * 
	 * @param var
	 *            The variable in question
	 * @param val
	 *            Its value
	 * @return The searched valuedVariable if it exist int he set. null if not.
	 */
	private ValuedVariable findValue(Variable var, Integer val) {
		ValuedVariable v = null;
		Iterator<ValuedVariable> it = values.iterator();
		boolean finished = !(it.hasNext());

		// Browsing the set until a match is found or the end is reached
		while (!finished) {
			v = it.next();

			if (v.getVar().equals(var) && v.getVal().equals(val)) {
				finished = true;
			}
			else {
				finished = !(it.hasNext());
			}
		}
		return v;
	}
	
	
	
	
	
	
	
	/* Substitution handling (from AC3) */
	
	
	
	/**
	 * General substitutions update, every step is done for every substitution
	 * (see AC3 class description)
	 * 
	 * Note: manual REDUCE calls are done on (Xi,Xi) couple
	 * (we wanna make propagation, but don't have any Xj exclude)
	 * 
	 * Comments are done from example: Z = A + B
	 */
	public void updateSubstitutions() {
		Domain toRemove = new Domain();
		
		
		//for (Substitution sub : model.getSubstitutions()) {
		for (int i=model.getSubstitutions().size()-1; i>=0; i--) {
			
			Substitution sub = model.getSubstitutions().get(i);
			
			
			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(),
					sub.getRight().getDomain()
			);
			
			// Restriction + reduce on every (xk,Z)
			if (sub.getSubstitutionVariable().getDomain().restrict(newDomain)) {
				
				for (Integer val : sub.getSubstitutionVariable().getDomain()) {
					waitingList.add(new ValuedVariable(sub.getSubstitutionVariable(), val));
				}
				
			}
			
			
			// Step 2: restraining A
			toRemove.clear();
			for (Integer aa : sub.getLeft().getDomain()) {
				
				if (!findSubstitutionVals(sub, aa, sub.getLeft())) { // reduce on every (xk,A)
					toRemove.add(aa);
					
					for (Integer val : sub.getSubstitutionVariable().getDomain()) {
						waitingList.add(new ValuedVariable(sub.getSubstitutionVariable(), val));
					}
				}
			}
			sub.getLeft().getDomain().removeAll(toRemove);
			
			
			// Step 3: restraining B
			toRemove.clear();
			for (Integer ab : sub.getRight().getDomain()) {
				
				if (!findSubstitutionVals(sub, ab, sub.getRight())) { // reduce on every (xk,B)
					toRemove.add(ab);
					for (Integer val : sub.getSubstitutionVariable().getDomain()) {
						waitingList.add(new ValuedVariable(sub.getSubstitutionVariable(), val));
					}
				}
			}
			sub.getRight().getDomain().removeAll(toRemove);
		}
	}
	
	
	/**
	 * (Assuming a Z=A+B substitution example)
	 * 
	 * Special findAj version, for substitution.
	 * Checks if a value of A or B doesn't break the substitution
	 * (it breaks if Z was reduced, and val in A or B can no longer give a single value in Z)
	 * 
	 * We are given:
	 * - The substitution
	 * - Value of A or B to be tested
	 * - Were (out of A and B) does this value belong
	 * 
	 * If val in A, we tests Z&B values, else we test Z&A ones.
	 * 
	 * Idea is that we must have a triplet of values so that
	 * Az = Aa [operator] Ab
	 * 
	 * @param sub the Substitution to test
	 * @param val the A or B value to check if it doesn't outright the substitution
	 * @param reference the Variable that contains val (A or B, aka left or right)
	 * @return true if there was a compatible aj, false otherwise
	 */
	public boolean findSubstitutionVals(Substitution sub, int val, Variable reference) {
		
		/* reference/val is Left (A) */
		if (reference == sub.getLeft()) {
			
			// For each Az value of D(Z)
			for (Integer az : sub.getSubstitutionVariable().getDomain()) {
				
				// For each Ab of D(B)
				for (Integer ab : sub.getRight().getDomain()) {
					
					// YATTA! Az = val + Ab
					if (sub.areValidValues(az, val, ab))
						return true;
				}
			}
		}

		/* reference/val is Right (B) */
		if (reference == sub.getRight()) {
			
			// For each Az value of D(Z)
			for (Integer az : sub.getSubstitutionVariable().getDomain()) {
				
				// For each Aa of D(A)
				for (Integer aa : sub.getLeft().getDomain()) {
					
					// YATTA! Az = Aa + val
					if (sub.areValidValues(az, aa, val))
						return true;
				}
			}
		}
		
		// If no valid value, or reference/val is not left nor right, no value found
		return false;
	}
}
