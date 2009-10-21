package ac;

import java.util.ArrayList;
import java.util.HashMap;


import model.Constraint;
import model.Model;
import model.Variable;

public class AC3 {
	 // inputs variables
		   private ArrayList<Variable> X;
		   //private Vector<ArrayList<Integer>> D_X ;//for each variable x in X. D(x) contains vx0, vx1... vxn, the possible values of x
		   
		   //Vector<Constraint> R_1; //constraints R1(x) on variable x that must be satisfied
		   private ArrayList<Constraint> R_2; //R2(x, y) on variables x and y that must be satisfied
		   
	//	output variables
		   //Arc consistent domains for each variable.  A VOIR !!!!!!!!
 		 	 
		 public void ac3 (Model M){
		 // Initial domains are made consistent with unary constraints.
		 //'worklist' contains all arcs we wish to prove consistent or not.
		 
			 Couple C=new Couple();
			 X  =M.getVariables();
			 HashMap<Integer,Couple > worklist = chargerWorkList();
		     for (int i = 0; i < worklist.size(); i++) {
				 C.setV1(worklist.get(i).getV1());
				 C.setV2(worklist.get(i).getV2());				 
		    	 worklist.remove(i);
				if(arc_reduce (M,C.getV1(),C.getV2())){
					if(C.getV1().getDomain().size()==C.getV1().getExcludedDomain().size()){
						System.out.println("Verification échouée " +
								"Car Les valeurs dans le domaine de x est vide donc ne " +
								"verifie pas la contraine" +
								"Pas de Solution");
					}
				}
		     }
		 }
		 
		 public HashMap<Integer,Couple > chargerWorkList(){
			 Integer k = new Integer(0);
			 HashMap<Integer,Couple > htemp=new HashMap<Integer, Couple>();
			 Couple C=new Couple();
			 for (int i = 0; i < X.size(); i++) {
				 Variable V=X.get(i);
				 for (int j = 0; j < X.size(); j++) {
					if(! X.get(j).equals(V)){
						C.setV1(V);
						C.setV2(X.get(j));
						htemp.put(k, C);
						k++;
					}
				}
			 }
			 return htemp;
		 }
		 
		 public Boolean arc_reduce (Model M,Variable V1,Variable V2 ){
		     boolean change = false;
		     for (int i = 0; i < V1.getDomain().size(); i++) {
		    	 Integer val= V1.getDomain().get(i);
		    	 if(! Find_val_V2(M, V2, V1,val)){
		    		 V1.exclude(val);
		    		 change=true;
		    	 }
			 }
		     return change;
		 }
		 
		 public Boolean Find_val_V2(Model M,Variable v2,Variable v1,Integer val){
			
			for (int i = 0; i < M.getConstraintConcerningVariables(v1, v2).size(); i++) {
				Constraint Crt=M.getConstraintConcerningVariables(v1, v2).get(i);
				for (int j = 0; j < v2.getDomain().size(); j++) {
					/*if(crt.IsValideVariable(val,v2.getDomain().get(j))){
						return true;
					}*/
				}
			}
			 return false;
		 }	 
}

