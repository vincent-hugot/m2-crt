package ac;

import java.util.ArrayList;
import java.util.LinkedList;
import model.Constraint;
import model.Domain;
import model.Model;
import model.Operator;
import model.Substitution;
import model.Variable;


/**
 * AC3 algorithm.
 * First idea is to follow the algorithm we were given, thus every variable and method
 * is named as close to its algorithm equivalent as possible, needing less explanation.
 * 
 * The composition of methods is as follow:
 * 
 * <br>- The 2 main functions: ac3() and revise() (main algorithm and REVISE)
 * <br>
 * <br>- AC3 uses initQueue(): the part that initialize QUEUE with every (xi,xj)
 * <br>- AC3 uses reduce(): the part that adds new (xk,xi) couples after a positive
 * return value from REVISE(xi,xj)
 * <br>
 * <br>- REVISE looks through every Ai from D(Xi), then uses findAj(): the part that
 * looks for any Aj from D(Xj) that make (Ai,Aj) compatible with every Cij
 * <br>
 * <br>To sum up, we have:
 * AC3 uses initQueue(). Then while(queue != {}), it takes a Couple, call REVISE on it,
 * if reduce is true, it uses reduce(Xi,Xj) to update the queue, this until queue is
 * empty.
 * <br>REVISE, goes through every Ai value from Xi, and asks findAj() if Ai is compatible
 * with Xj values and every Cij (every constraints between Xi and Xj).
 * 
 * 
 * <br><br>A Couple class is used to have an (Xi,Xj) couple/arc in this very order.
 * 
 * 
 * <br><br><br>There were 2 changes from the original AC3 algorithm.
 * 
 * <br><br>First, constants/numbers handling is done by declaring them as
 * special variables, only defined in [value..value].
 * <br>AC3 does not handle it, and remove this sole value if necessary.
 * It's up to the labelling part to check for illogic issues.
 * 
 * <br><br>Then, AC3 can only handle binary constraints.
 * Thus, constraints with more than 2 members (constants including) are transformed into
 * a set of substitution, in the form of:
 * A < B + C  =>  A < S1, S1 = B + C.
 * (this example will be used next)
 * 
 * <br><br>Substitutions cannot be treated as constraints (still not binary), so
 * special handling is necessary. Here is the adopted strategy:
 * 
 * <br>When/IF D(Xi) is restricted by REVISE, and Xi is part of a substitution,
 * a general "substitutions update" is runned. It's done on every substitution, and
 * done in 3 steps (example of S1=B+C):
 * <br>- D(B+C) is recalculated (in the case of previous B or C reduction), then
 * D(S1) is restricted according to new D(B+C)
 * <br>- Observing from B, for each Ab value of D(B), if there is no As1 � D(S1)
 * and Ab � D(B) combination so that As1 = Ab + Ac (or any operator), Ab is remove from
 * D(B).
 * <br>- Same thing from C to restrain D(C).
 * 
 * <br><br>When any of S1, B or C has changed, reduce() is applied on it.
 * <br>Step 1 updates S1 according to B+C (removes values that can no more be obtained)
 * <br>Step 2 and 3 update B and C (remove values of B or C that will NEVER
 * fit in the substitution anymore)
 * 
 * <br><br>This update is done through updateSubstitutions() method, and in the
 * substitutions creation order (every dependency is respected.)
 */
public class AC3 {
	
	// For general variable/constraints lists purpose
	private Model model;
	
	private ArrayList<Variable> X;
	
	private LinkedList<Couple> queue;
	
	
	public AC3(Model model) {
		this.model = model;
		this.X = model.getVariables();
		this.queue = new LinkedList<Couple>();
	}
	
	
	/**
	 * Applying AC3
	 */
	public void run() {
		
		queue = initQueue();
		
		while (!queue.isEmpty()) {
			
			Couple c = queue.poll();
			
			// Applying REVISE, then checking reduce
			if (revise(c.getXi(), c.getXj())) {
				
				// There was a reduction, propagation to any (xk,xi) with k!=i && k!=j
				reduce(c.getXi(), c.getXj());
			}
		}
	}
	
	
	/**
	 * Queue initialisation (every couple)
	 * @return
	 */
	public LinkedList<Couple> initQueue() {
		LinkedList<Couple> queue = new LinkedList<Couple>();
		
		for (Variable xi : X) {
			for (Variable xj : X) {
				//if (!xi.equals(xj)) {
					Couple C = new Couple(xi,xj);
					queue.push(C);
				//}
			}
		}
		return queue;
	}
	
	
	
	/**
	 * Revise part of the algorithm
	 * @param xi
	 * @param xj
	 * @return
	 */
	public Boolean revise(Variable xi, Variable xj) {
		boolean reduce = false;
		Domain toRemove = new Domain();
		
		// No constraints, no reduce.
		if (model.getConstraintConcerningVariables(xi,xj).isEmpty()) return false;
		
		
		/* Special case: Xi [op] Xi.
		 * If every [op] is =, <= or >=, domain is left.
		 * If any [op] is !=, < or >, domain is emptied.
		 * No other constraint study is done
		 */
		if (xi == xj) {
			for (Constraint crt : model.getConstraintConcerningVariables(xi,xj)) {
				
				// If one Cij is not an equal-type, emptying and reduction
				if (crt.getOp() == Operator.Constraint.NOT_EQUAL
						|| crt.getOp() == Operator.Constraint.GREATER
						|| crt.getOp() == Operator.Constraint.LOWER) {
					
					xi.getDomain().clear();
					
					// Xi is reduced and belongs to a substitution
					if (!xi.getAssociatedSubstitutions().isEmpty())
						updateSubstitutions();
					
					return true; // There was a reduction
				}
			}
		}
		
		
		// Note: not removing directly, since iteration AND removal is prohibited
		for (Integer ai : xi.getDomain()) {
			
			// Trying to find Aj | (Ai,Aj) respects every Cij constraint
			if (!findAj(xi, xj, ai)) {
				
				// No Aj valid: Ai makes like a tree, and leaves. (HURR DURR sorry.)
				//xi.getDomain().remove(ai);
				toRemove.add(ai);
				
				reduce = true; // There was a reduction
				
				// Xi is reduced and belongs to a substitution
				if (!xi.getAssociatedSubstitutions().isEmpty())
					updateSubstitutions();
			}
		}
		
		// Removing what was reduced
		xi.getDomain().removeAll(toRemove);
		
		return reduce;
	}
	
	
	/**
	 * Given Ai in D(Xi), trying to find Aj | (Ai,Aj) respects every Cij constraint
	 * @param xj
	 * @param xi
	 * @param ai
	 * @return true if there was a compatible aj, false otherwise
	 */
	public boolean findAj(Variable xi, Variable xj, Integer ai) {
		boolean valid = true;
		
		// For each value of D(Xj)
		for (Integer aj : xj.getDomain()) {
			
			valid = true;
			
			// For each Cij
			for (Constraint crt : model.getConstraintConcerningVariables(xi,xj)) {
				
				
				// Only 1 non-respected constraint => Aj gets away.
				if (!crt.areValidValues(xi,xj,ai,aj))
					valid = false;
			}
			
			// Every Cij passed for (Ai,Aj), an Aj was found.
			if (valid == true) return true;
		}
		
		// No valid Aj was found, good bye Ai.
		return false;
	}
	
	
	
	/**
	 * Method used when reacting to a positive "reduce" value in AC3's REVISE call:
	 * Add any (xk,xi) couple to "queue", so that xk != xi && xk != xj
	 * @param xi
	 * @param xj
	 */
	public void reduce(Variable xi, Variable xj) {
		
		for (Variable xk : X) {
			if (xk != xj)
				queue.offer(new Couple(xk, xi));
		}
	}
	
	
	
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
		
		for (Substitution s : model.getSubstitutions()) {
			
			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = s.getLeft().getDomain().arithmeticOperation(
					s.getSubstitutionOperator(),
					s.getRight().getDomain()
			);
			
			// Restriction + reduce on every
			if (s.getSubstitutionVariable().getDomain().restrict(newDomain))
				reduce(s.getSubstitutionVariable(),s.getSubstitutionVariable());
			
			
			// Step 2: restraining A
			/*for (Integer aa : s.getLeft().getDomain()) {
				
				//if ()
			}*/
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
