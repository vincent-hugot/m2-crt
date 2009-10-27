package ac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import model.Constraint;
import model.Model;
import model.Variable;

public class AC6 {
	private Model				model;
	private HashSet<Value>		values;
	private LinkedList<Value>	waitingList;

	public AC6(Model m) {
		model = m;
		values = null;
		waitingList = new LinkedList<Value>();
	}
	
	public void run(){
		init();
		propagation();
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
		Variable left;

		right = cons.getRight();
		left = cons.getLeft();

		tmpb = b.intValue();
		tmpa = a.intValue();

		if (tmpb <= right.getDomain().last()) {
			emptySupport = false;
			while (!right.getDomain().contains(tmpb)) {
				tmpb++;
			}

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
		return emptySupport;
	}

	private boolean nextSupport(Value valA, Value valB) {
		Variable i, j; 
		Integer a, b;
		
		i = valA.getVar();
		j = valB.getVar();		
		
		ArrayList<Constraint> constraints = model.getConstraintConcerningVariables(i, j);
		boolean res;
		Integer tmp;
		Integer tmp2;


		
		a = valA.getVal();
		b = valB.getVal();
		
		tmp = new Integer(b.intValue());
		
		res = !constraints.isEmpty();

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

	private void init() {

		Value tmp;
		Integer b;
		values = model.getValues();

		for (Constraint cons : model.getConstraints()) {
			for (Integer a : cons.getLeft().getDomain()) {
				b = new Integer(1);

				if (nextSupport(cons, a, b)) {
					cons.getLeft().getDomain().remove(a.intValue());
					tmp = findValue(cons.getLeft(), a);

					if (tmp != null) {
						waitingList.add(tmp);
					}

				}
				else {
					tmp = findValue(cons.getRight(), b);
					if (tmp != null) {
						tmp.add(tmp);
					}
				}
			}
		}
	}

	private void propagation() {
		Value valb, valc;
		//Integer c;
		while (!waitingList.isEmpty()) {
			valb = waitingList.pop();

			for (Value vala : valb.getSVarVal()) {
				vala.getSVarVal().remove(vala);

				if (vala.getVar().getDomain().contains(vala.getVal())) {
					valc = new Value(valb);
					
					if (nextSupport(vala, valc)) {
						vala.getVar().getDomain().remove(vala.getVal().intValue());
						waitingList.add(vala);
						
					}
					else{
						valc.getSVarVal().add(vala);
					}
				}
			}
		}
	}

	private Value findValue(Variable var, Integer val) {
		Value v = null;
		Iterator<Value> it = values.iterator();
		boolean finished = !(it.hasNext());

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
}
