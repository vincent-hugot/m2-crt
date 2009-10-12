package parser;

public class Test {
  @SuppressWarnings("static-access")
public static void main(String args[]) throws Exception {
    
    
    Parser p = new Parser(System.in);
    p.constraints();
    System.out.println("TREE");
    Node root = p.jjtree.rootNode();
    ((SimpleNode) root).dump(" > ");
    
    /*
    TermesClos parser = new TermesClos(System.in);
    parser.axiome();
    System.out.println("Debut arbre");
    Node racine = parser.jjtree.rootNode();
    ((SimpleNode) racine).dump(" > "); 

System.out.println("\nVisiteur de symboles :\n=========================\n");

racine.jjtAccept(new SymbVisitor(), null);

System.out.println("\nVisiteur d'arite :\n=========================\n");

racine.jjtAccept(new AriVisitor(), null);

System.out.println("\nVisiteur de type :\n=========================\n");

racine.jjtAccept(new TypeVisitor(), null);

    //System.out.println("Fin arbre = "  + "  " + ((Integer)racine.jjtAccept(new ValVisitor(), null)).intValue()  );
  }*/
  }
}
