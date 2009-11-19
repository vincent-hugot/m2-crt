package fitnesse;
import java.util.ArrayList;
import translator.*;
import model.*;
import java.util.*;
import ac.*;

/**
 * 
 *
 *l'objectif de la classe AC3Fitnesse est de tester l'algorithme AC3 
 *à partir d'une seule classe.
 *
 */

public class AC3Fitnesse {
	
	private Translator translator; //contient le parseur
	private Model model;//contient le modele initial, on pourra l'afficher si besoin
	private ArrayList<Variable> X;//contient toutes les variables (avec leur domaine) apres application de l'algo ac3 
	
	/**
	 * @param fileContent
	 * Constructeur qui prend en argument le contenu d'un fichier
	 * puis applique AC3, il définit également les variables model et X
	 */
	public AC3Fitnesse(String fileContent){
		translator = new Translator("Test Fitnesse", fileContent);
		model = translator.translate();
		AC3 ac3=new AC3(model);
		ac3.run();
		X = ac3.getX();
		
	}
	/**
	 * @param val
	 * @param variableName
	 * @return
	 * retourne true si la valeur appartient au domaine de la variable 
	 * et que la variable existe
	 * retourne faux dans tous les autres cas
	 */
	public boolean ValeurAppartientAuDomaineDe(int val, String variableName){
		Iterator<Variable> i = X.iterator();
		Variable variable = null;
		boolean variableTrouvee = false;
		
		while (i.hasNext() && !variableTrouvee){
			variable = ((Variable)i.next());
			if (variable.getName().equals(variableName)){
				variableTrouvee = true;
			}
		}
		
		if (variableTrouvee){
			return variable.getDomain().contains(val);
		}
		else
			return false;
		
	}
	

	
	/**
	 * @param val
	 * @param variableName
	 * @return
	 * retourne true si toutes les valeurs de la liste (un tableau d'entier)
	 * appartiennent au domaine de la variable 
	 * et que la variable existe
	 * retourne faux dans tous les autres cas
	 * 
	 * sert à tester une assert true 
	 */
	public boolean ListeValeurAppartientAuDomaineDe(Integer[] val, String variableName){
		Iterator<Variable> i = X.iterator();
		Variable variable = null;
		boolean variableTrouvee = false;
		
		//on met le contenu du tableau dans une collection (arrayList)
		//pour pouvoir utiliser la fonction containsAll...
		ArrayList<Integer> listeVal = new ArrayList<Integer>();
		for (int j=0; j<val.length; j++){
			listeVal.add(val[j]);
		}
		
		while (i.hasNext() && !variableTrouvee){
			variable = ((Variable)i.next());
			if (variable.getName().equals(variableName)){
				variableTrouvee = true;
			}
		}
		if (variableTrouvee){
			return variable.getDomain().containsAll(listeVal);
		}
		else
			return false;
		
	}
	
	/**
	 * @param val
	 * @param variableName
	 * @return
	 * retourne true si toutes les valeurs de la liste (un tableau d'entier)
	 * n' appartiennent pas au domaine de la variable 
	 * et que la variable existe
	 * retourne faux dans tous les autres cas
	 * 
	 * sert à tester une assert true
	 */
	public boolean ListeValeurHorsDuDomaineDe(Integer[] val, String variableName){
		Iterator<Variable> i = X.iterator();
		Variable variable = null;
		boolean variableTrouvee = false;
		boolean toutesVariablesHorsDomaine = true;
		int k=0;
		
		while (i.hasNext() && !variableTrouvee){
			variable = ((Variable)i.next());
			if (variable.getName().equals(variableName)){
				variableTrouvee = true;
			}
		}
		if (variableTrouvee){
			while ( k < val.length && toutesVariablesHorsDomaine ){
				if (variable.getDomain().contains(val[k])){
					toutesVariablesHorsDomaine = false;
				}
				k++;
			}
			return toutesVariablesHorsDomaine;
		}
		else
			return false;
		// si la variable n'existe pas il vaudrait mieux retourner une exception
	}
	
	public String toString(){// en fait pas de modif visuelle du model
		return X.toString();
	}

}
