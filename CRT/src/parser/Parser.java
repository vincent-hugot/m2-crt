package parser;

/* Generated By:JJTree&JavaCC: Do not edit this line. Parser.java */
@SuppressWarnings("static-access")
    public class Parser/*@bgen(jjtree)*/implements ParserTreeConstants, ParserConstants {/*@bgen(jjtree)*/
  protected static JJTParserState jjtree = new JJTParserState();

  // END TOKENS 


/** PARSER */
  static final public void constraints() throws ParseException {
                      /*@bgen(jjtree) constraints */
  ASTconstraints jjtn000 = new ASTconstraints(JJTCONSTRAINTS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      domain_definition();
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        jj_consume_token(COMMA);
        domain_definition();
      }
      jj_consume_token(SEMICOLON);
      constraint();
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(COMMA);
        constraint();
      }
      jj_consume_token(0);
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void domain_definition() throws ParseException {
                                    /*@bgen(jjtree) domain */
  ASTdomain jjtn000 = new ASTdomain(JJTDOMAIN);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      variable();
      jj_consume_token(DOMAIN_DEF);
      jj_consume_token(LB);
      integer();
      jj_consume_token(BOUNDS);
      integer();
      jj_consume_token(RB);
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void variable() throws ParseException {
                        /*@bgen(jjtree) var */
  ASTvar jjtn000 = new ASTvar(JJTVAR);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(IDENTIFIER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
       jjtn000.name = token.image;
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void integer() throws ParseException {
                           /*@bgen(jjtree) integer */
  ASTinteger jjtn000 = new ASTinteger(JJTINTEGER);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(INTEGER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
      jjtn000.value = Integer.parseInt(token.image);
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void cons_eq() throws ParseException {
                      /*@bgen(jjtree) eq */
  ASTeq jjtn000 = new ASTeq(JJTEQ);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      expression();
      jj_consume_token(EQ);
      expression();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static final public void cons_leq() throws ParseException {
                        /*@bgen(jjtree) leq */
  ASTleq jjtn000 = new ASTleq(JJTLEQ);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      expression();
      jj_consume_token(LEQ);
      expression();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static final public void cons_geq() throws ParseException {
                        /*@bgen(jjtree) geq */
  ASTgeq jjtn000 = new ASTgeq(JJTGEQ);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      expression();
      jj_consume_token(GEQ);
      expression();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static final public void cons_neq() throws ParseException {
                        /*@bgen(jjtree) neq */
  ASTneq jjtn000 = new ASTneq(JJTNEQ);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      expression();
      jj_consume_token(NEQ);
      expression();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static final public void cons_g() throws ParseException {
                    /*@bgen(jjtree) g */
  ASTg jjtn000 = new ASTg(JJTG);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      expression();
      jj_consume_token(G);
      expression();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static final public void cons_l() throws ParseException {
                    /*@bgen(jjtree) l */
  ASTl jjtn000 = new ASTl(JJTL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      expression();
      jj_consume_token(L);
      expression();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static final public void constraint() throws ParseException {
                     /*@bgen(jjtree) constraint */
  ASTconstraint jjtn000 = new ASTconstraint(JJTCONSTRAINT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_1(2)) {
        cons_eq();
      } else if (jj_2_2(2)) {
        cons_leq();
      } else if (jj_2_3(2)) {
        cons_geq();
      } else if (jj_2_4(2)) {
        cons_l();
      } else if (jj_2_5(2)) {
        cons_g();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
        case INTEGER:
        case LP:
          cons_neq();
          break;
        default:
          jj_la1[2] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void atomic_expression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      variable();
      break;
    case INTEGER:
      integer();
      break;
    case LP:
      jj_consume_token(LP);
      expression();
      jj_consume_token(RP);
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void multiplicative_expression() throws ParseException {
    atomic_expression();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
      case DIV:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
        jj_consume_token(TIMES);
                ASTmul jjtn001 = new ASTmul(JJTMUL);
                boolean jjtc001 = true;
                jjtree.openNodeScope(jjtn001);
        try {
          atomic_expression();
        } catch (Throwable jjte001) {
                if (jjtc001) {
                  jjtree.clearNodeScope(jjtn001);
                  jjtc001 = false;
                } else {
                  jjtree.popNode();
                }
                if (jjte001 instanceof RuntimeException) {
                  {if (true) throw (RuntimeException)jjte001;}
                }
                if (jjte001 instanceof ParseException) {
                  {if (true) throw (ParseException)jjte001;}
                }
                {if (true) throw (Error)jjte001;}
        } finally {
                if (jjtc001) {
                  jjtree.closeNodeScope(jjtn001,  2);
                }
        }
        break;
      case DIV:
        jj_consume_token(DIV);
              ASTdiv jjtn002 = new ASTdiv(JJTDIV);
              boolean jjtc002 = true;
              jjtree.openNodeScope(jjtn002);
        try {
          atomic_expression();
        } catch (Throwable jjte002) {
              if (jjtc002) {
                jjtree.clearNodeScope(jjtn002);
                jjtc002 = false;
              } else {
                jjtree.popNode();
              }
              if (jjte002 instanceof RuntimeException) {
                {if (true) throw (RuntimeException)jjte002;}
              }
              if (jjte002 instanceof ParseException) {
                {if (true) throw (ParseException)jjte002;}
              }
              {if (true) throw (Error)jjte002;}
        } finally {
              if (jjtc002) {
                jjtree.closeNodeScope(jjtn002,  2);
              }
        }
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void additive_expression() throws ParseException {
    multiplicative_expression();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
              ASTplus jjtn001 = new ASTplus(JJTPLUS);
              boolean jjtc001 = true;
              jjtree.openNodeScope(jjtn001);
        try {
          multiplicative_expression();
        } catch (Throwable jjte001) {
              if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
              } else {
                jjtree.popNode();
              }
              if (jjte001 instanceof RuntimeException) {
                {if (true) throw (RuntimeException)jjte001;}
              }
              if (jjte001 instanceof ParseException) {
                {if (true) throw (ParseException)jjte001;}
              }
              {if (true) throw (Error)jjte001;}
        } finally {
              if (jjtc001) {
                jjtree.closeNodeScope(jjtn001,  2);
              }
        }
        break;
      case MINUS:
        jj_consume_token(MINUS);
               ASTminus jjtn002 = new ASTminus(JJTMINUS);
               boolean jjtc002 = true;
               jjtree.openNodeScope(jjtn002);
        try {
          multiplicative_expression();
        } catch (Throwable jjte002) {
               if (jjtc002) {
                 jjtree.clearNodeScope(jjtn002);
                 jjtc002 = false;
               } else {
                 jjtree.popNode();
               }
               if (jjte002 instanceof RuntimeException) {
                 {if (true) throw (RuntimeException)jjte002;}
               }
               if (jjte002 instanceof ParseException) {
                 {if (true) throw (ParseException)jjte002;}
               }
               {if (true) throw (Error)jjte002;}
        } finally {
               if (jjtc002) {
                 jjtree.closeNodeScope(jjtn002,  2);
               }
        }
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void expression() throws ParseException {
    additive_expression();
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  static private boolean jj_3R_7() {
    if (jj_3R_10()) return true;
    if (jj_scan_token(GEQ)) return true;
    return false;
  }

  static private boolean jj_3R_20() {
    if (jj_scan_token(LP)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  static private boolean jj_3R_19() {
    if (jj_3R_24()) return true;
    return false;
  }

  static private boolean jj_3R_6() {
    if (jj_3R_10()) return true;
    if (jj_scan_token(LEQ)) return true;
    return false;
  }

  static private boolean jj_3R_18() {
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_14() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) return true;
    }
    }
    return false;
  }

  static private boolean jj_3R_5() {
    if (jj_3R_10()) return true;
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  static private boolean jj_3_5() {
    if (jj_3R_9()) return true;
    return false;
  }

  static private boolean jj_3R_24() {
    if (jj_scan_token(INTEGER)) return true;
    return false;
  }

  static private boolean jj_3_4() {
    if (jj_3R_8()) return true;
    return false;
  }

  static private boolean jj_3_3() {
    if (jj_3R_7()) return true;
    return false;
  }

  static private boolean jj_3R_10() {
    if (jj_3R_11()) return true;
    return false;
  }

  static private boolean jj_3_2() {
    if (jj_3R_6()) return true;
    return false;
  }

  static private boolean jj_3R_23() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  static private boolean jj_3R_17() {
    if (jj_scan_token(MINUS)) return true;
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_3R_5()) return true;
    return false;
  }

  static private boolean jj_3R_13() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_16()) {
    jj_scanpos = xsp;
    if (jj_3R_17()) return true;
    }
    return false;
  }

  static private boolean jj_3R_16() {
    if (jj_scan_token(PLUS)) return true;
    return false;
  }

  static private boolean jj_3R_8() {
    if (jj_3R_10()) return true;
    if (jj_scan_token(L)) return true;
    return false;
  }

  static private boolean jj_3R_11() {
    if (jj_3R_12()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_13()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_9() {
    if (jj_3R_10()) return true;
    if (jj_scan_token(G)) return true;
    return false;
  }

  static private boolean jj_3R_22() {
    if (jj_scan_token(DIV)) return true;
    return false;
  }

  static private boolean jj_3R_21() {
    if (jj_scan_token(TIMES)) return true;
    return false;
  }

  static private boolean jj_3R_15() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) return true;
    }
    return false;
  }

  static private boolean jj_3R_12() {
    if (jj_3R_14()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_15()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[8];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x8000,0x8000,0x1060,0x1060,0x300000,0x300000,0xc0000,0xc0000,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[5];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  
static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  @SuppressWarnings("serial")
  
static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  
@SuppressWarnings("unchecked")
static private java.util.List jj_expentries = new java.util.ArrayList();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  @SuppressWarnings("unchecked")
static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  @SuppressWarnings("unchecked")
static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[28];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 28; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 5; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

                         }
