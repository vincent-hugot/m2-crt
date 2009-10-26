package ac;

import java.util.ArrayList;
import java.util.LinkedList;

import model.Constraint;
import model.Model;
import model.Variable;

public class AC6 {
	private Model				model;
	private ArrayList<Variable>	variables;
	private LinkedList<Value>	waitingList;

	public AC6(Model m) {
		model = m;
		variables = m.getVariables();
		waitingList = new LinkedList<Value>();
	}

	/**
	 * Used for a Constraint to find the smallest in the domain of the right variable not smaller than the given value b
	 * and supporting the given value a for the left variable.
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

		right = cons.getRight();

		tmpb = b.intValue();
		tmpa = a.intValue();

		if (tmpb <= right.getDomain().last()) {
			emptySupport = false;
			while (!right.getDomain().contains(tmpb)) {
				tmpb++;
			}

			while (!cons.areValidValues(tmpa, tmpb) && !emptySupport) {
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
		return emptySupport;
	}

	private void init() {
		for (Constraint cons : model.getConstraints()) {
			//TODO
		}
	}
}
