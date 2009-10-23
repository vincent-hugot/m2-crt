package ac;

import java.util.ArrayList;
import java.util.LinkedList;
import model.Constraint;
import model.Model;
import model.Variable;


/**
 * AC3 algorithm. Split int 4 functions, corresponding to the algorithm we have:
 * <br>- ac3(): the main algorithm (uses initQueue() and revise())
 * <br>- initQueue(): the part of AC3 that initialize QUEUE
 * <br>- revise(): the REVISE part, trying to exclude a value from Xi (uses findValXj)
 * <br>- findValXj(): the part of REVISE that looks for any Aj value so that
 * (Ai,Aj) complies to every Cij constraint
 * 
 * <br>To sum up, ac3() and initQueue() are part of the AC3 algorithm,
 * revise() and findValXj() are part of the REVISE operation
 * 
 * <br><br>A Couple class is used to have an (Xi,Xj) couple in this very order.
 * <br>Variables are usually given a name that matches the original algorithm.
 * 
 * <br><br>The main change with the original algorithm is the substitution handling.
 * It occurs whenever a "A < B + C" constraints is change by an "S1 = B + C"
 * expression, followed by the "A < S1" constraint.
 * 
 * <br>Since Constraint is a binary operator, and Expression a ternary, they are treated
 * the same way, yet one after another:
 * <br>When a change is done (reduce == true) there's a propagation to any (Xk,Xi)
 * couple.
 * If Xi was (right) part of an expression, an update is made to EVERY expression.
 * 
 * <br>This update consists in recalculating the domain of the right part.
 * For instance, S1=B+C, C has changed since REVISE, then we calculate dom(B+C),
 * and apply dom(S1) := dom(B+C).
 * <br>This is merely the same arc consistency we make in AC3, only more adapted
 * to the Expression model.
 * 
 * <br>It should be noted that since Expressions are added in the Expression list
 * in creation order (A+B+C creates S1=B+C, then S2=A+S2), checking in this same order
 * guarantee that the propagation is in the correct order.
 * 
 * <br><br>Lastly, once this "update" is done, AC3 continues.
 * 
 * @author Mehdi
 */
public class AC3 {
	
	// For general variable/constraints list purpose
	Model model;
	
	private ArrayList<Variable> X;
	
	
	public AC3(Model model) {
		this.model = model;
		this.X = model.getVariables();
	}
	
	
	/**
	 * Applying the AC3
	 */
	public void ac3() {
		// Initial domains are made consistent with unary constraints.
		// 'queue' contains all arcs we wish to prove consistent or not.
		
		LinkedList<Couple> queue = initQueue();
		
		while (!queue.isEmpty()) {
			
			Couple c = queue.poll();
			
			// Applying REVISE, then checking reduce
			if (arcReduce(c.getXi(), c.getXj())) {
				
				// TODO : Add Expression handling
				
				// Propagation to any (xk,xi) with k!=i && k!=j
				for (Variable xk : model.getVariables()) {
					if (xk != c.getXi() && xk != c.getXj())
						queue.offer(new Couple(xk, c.getXi()));
				}
			}
		}
	}
	
	public LinkedList<Couple> initQueue() {
		LinkedList<Couple> queue = new LinkedList<Couple>();
		
		for (Variable xi : X) {
			for (Variable xj : X) {
				if (!xi.equals(xj)) {
					Couple C = new Couple(xi,xj);
					queue.push(C);
				}
			}
		}
		return queue;
	}
	
	
	public Boolean arcReduce(Variable xi, Variable xj) {
		boolean change = false;
		
		for (Integer ai : xi.getDomain()) {
			
			// Trying to find Aj | (Ai,Aj) respects every Cij constraint
			if (!findAj(xi, xj, ai)) {
				xi.getDomain().remove(ai);
				change = true;
			}
		}
		return change;
	}
	
	
	/**
	 * Check if val, existing 
	 * @param xj
	 * @param xi
	 * @param ai
	 * @return
	 */
	public boolean findAj(Variable xi, Variable xj, Integer ai) {
		boolean valid = true;
		
		
		for (Integer aj : xj.getDomain()) {
			
			valid = true;
			
			for (Constraint crt : model.getConstraintConcerningVariables(xi,xj)) {
				if (!crt.areValidValues(ai,aj))
					valid = false;
			}
			
			if (valid == true) return true;
		}
		
		return false;
		
		/*for (int i = 0; i < model.getConstraintConcerningVariables(v1, v2).size(); i++) {
			Constraint crt = model.getConstraintConcerningVariables(v1, v2).get(i);
			
			for (int j = 0; j < v2RemainingDomain.size(); j++) {
				
				if (!crt.areValidValues(ai,v2.getRemainingDomain().get(j)))
					return false;
				
			}
		}
		return false;*/
	}
}
