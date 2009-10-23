package ac;

import java.util.ArrayList;

import model.Model;
import model.Variable;

public class AC6 {
	private Model model;
	private ArrayList<Variable> variables;
	
	public AC6(Model m){
		model = m;
		variables = m.getVariables();
	}
	
	private boolean nextSupport(Variable i, Variable j, int a, Integer b){
		boolean res;
		int tmp = b.intValue();
		
		if(tmp <= j.getDomain().last())
		{
			res = false;
			while(!j.getDomain().contains(tmp)){
				tmp++;
			}
		}
		else
		{
			res = true;
		}
		b = new Integer(tmp);
		return res;
	}
}
