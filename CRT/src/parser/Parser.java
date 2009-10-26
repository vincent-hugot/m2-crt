package parser;

/* Generated By:JJTree&JavaCC: Do not edit this line. Parser.java */
    public class Parser/*@bgen(jjtree)*/implements ParserTreeConstants, ParserConstants {/*@bgen(jjtree)*/
  protected JJTParserState jjtree = new JJTParserState();

/** PARSER */

/* Rroot node */
  final public SimpleNode constraints() throws ParseException {
                            /*@bgen(jjtree) constraints */
  ASTconstraints jjtn000 = new ASTconstraints(JJTCONSTRAINTS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOMAINS:
        jj_consume_token(DOMAINS);
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
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CONSTRAINTS:
        jj_consume_token(CONSTRAINTS);
        constraint();
        label_2:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case COMMA:
            ;
            break;
          default:
            jj_la1[2] = jj_gen;
            break label_2;
          }
          jj_consume_token(COMMA);
          constraint();
        }
        break;
      default:
        jj_la1[3] = jj_gen;
        ;
      }
      jj_consume_token(0);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          {if (true) return jjtn000;}
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
    throw new Error("Missing return statement in function");
  }

  final public void domain_definition() throws ParseException {
                                    /*@bgen(jjtree) domain */
        ASTdomain jjtn000 = new ASTdomain(JJTDOMAIN);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);jjtn000.setLine(getToken(1).beginLine);
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

  final public void variable() throws ParseException {
                        /*@bgen(jjtree) var */
        ASTvar jjtn000 = new ASTvar(JJTVAR);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);jjtn000.setLine(getToken(1).beginLine);
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

  final public void integer() throws ParseException {
                           /*@bgen(jjtree) integer */
        ASTinteger jjtn000 = new ASTinteger(JJTINTEGER);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);jjtn000.setLine(getToken(1).beginLine);
        String value;
    try {
      jj_consume_token(INTEGER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
      /*jjtThis.value = Integer.parseInt(token.image);*/
          value = token.image; // String, then parsed with NumberFormatException
      jjtn000.setValue(value);
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void constraint() throws ParseException {
                     /*@bgen(jjtree) constraint */
                      ASTconstraint jjtn000 = new ASTconstraint(JJTCONSTRAINT);
                      boolean jjtc000 = true;
                      jjtree.openNodeScope(jjtn000);Token t;
    try {
      expression();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
        t = jj_consume_token(EQ);
        expression();
                            ASTeq jjtn001 = new ASTeq(JJTEQ);
                            boolean jjtc001 = true;
                            jjtree.openNodeScope(jjtn001);
        try {
                            jjtree.closeNodeScope(jjtn001,  2);
                            jjtc001 = false;
                           jjtn001.setLine(t.beginLine);
        } finally {
                            if (jjtc001) {
                              jjtree.closeNodeScope(jjtn001,  2);
                            }
        }
        break;
      case NEQ:
        t = jj_consume_token(NEQ);
        expression();
                            ASTneq jjtn002 = new ASTneq(JJTNEQ);
                            boolean jjtc002 = true;
                            jjtree.openNodeScope(jjtn002);
        try {
                            jjtree.closeNodeScope(jjtn002,  2);
                            jjtc002 = false;
                           jjtn002.setLine(t.beginLine);
        } finally {
                            if (jjtc002) {
                              jjtree.closeNodeScope(jjtn002,  2);
                            }
        }
        break;
      case LEQ:
        t = jj_consume_token(LEQ);
        expression();
                            ASTleq jjtn003 = new ASTleq(JJTLEQ);
                            boolean jjtc003 = true;
                            jjtree.openNodeScope(jjtn003);
        try {
                            jjtree.closeNodeScope(jjtn003,  2);
                            jjtc003 = false;
                           jjtn003.setLine(t.beginLine);
        } finally {
                            if (jjtc003) {
                              jjtree.closeNodeScope(jjtn003,  2);
                            }
        }
        break;
      case GEQ:
        t = jj_consume_token(GEQ);
        expression();
                            ASTgeq jjtn004 = new ASTgeq(JJTGEQ);
                            boolean jjtc004 = true;
                            jjtree.openNodeScope(jjtn004);
        try {
                            jjtree.closeNodeScope(jjtn004,  2);
                            jjtc004 = false;
                           jjtn004.setLine(t.beginLine);
        } finally {
                            if (jjtc004) {
                              jjtree.closeNodeScope(jjtn004,  2);
                            }
        }
        break;
      case G:
        t = jj_consume_token(G);
        expression();
                            ASTg jjtn005 = new ASTg(JJTG);
                            boolean jjtc005 = true;
                            jjtree.openNodeScope(jjtn005);
        try {
                            jjtree.closeNodeScope(jjtn005,  2);
                            jjtc005 = false;
                           jjtn005.setLine(t.beginLine);
        } finally {
                            if (jjtc005) {
                              jjtree.closeNodeScope(jjtn005,  2);
                            }
        }
        break;
      case L:
        t = jj_consume_token(L);
        expression();
                            ASTl jjtn006 = new ASTl(JJTL);
                            boolean jjtc006 = true;
                            jjtree.openNodeScope(jjtn006);
        try {
                            jjtree.closeNodeScope(jjtn006,  2);
                            jjtc006 = false;
                           jjtn006.setLine(t.beginLine);
        } finally {
                            if (jjtc006) {
                              jjtree.closeNodeScope(jjtn006,  2);
                            }
        }
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
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

  final public void atomic_expression() throws ParseException {
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
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void multiplicative_expression() throws ParseException {
        Token t;
    atomic_expression();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
      case DIV:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
        t = jj_consume_token(TIMES);
        atomic_expression();
                                      ASTmul jjtn001 = new ASTmul(JJTMUL);
                                      boolean jjtc001 = true;
                                      jjtree.openNodeScope(jjtn001);
        try {
                                      jjtree.closeNodeScope(jjtn001,  2);
                                      jjtc001 = false;
                                     jjtn001.setLine(t.beginLine);
        } finally {
                                      if (jjtc001) {
                                        jjtree.closeNodeScope(jjtn001,  2);
                                      }
        }
        break;
      case DIV:
        t = jj_consume_token(DIV);
        atomic_expression();
                                    ASTdiv jjtn002 = new ASTdiv(JJTDIV);
                                    boolean jjtc002 = true;
                                    jjtree.openNodeScope(jjtn002);
        try {
                                    jjtree.closeNodeScope(jjtn002,  2);
                                    jjtc002 = false;
                                   jjtn002.setLine(t.beginLine);
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

  final public void additive_expression() throws ParseException {
        Token t;
    multiplicative_expression();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        t = jj_consume_token(PLUS);
        multiplicative_expression();
                                             ASTplus jjtn001 = new ASTplus(JJTPLUS);
                                             boolean jjtc001 = true;
                                             jjtree.openNodeScope(jjtn001);
        try {
                                             jjtree.closeNodeScope(jjtn001,  2);
                                             jjtc001 = false;
                                            jjtn001.setLine(t.beginLine);
        } finally {
                                             if (jjtc001) {
                                               jjtree.closeNodeScope(jjtn001,  2);
                                             }
        }
        break;
      case MINUS:
        t = jj_consume_token(MINUS);
        multiplicative_expression();
                                              ASTminus jjtn002 = new ASTminus(JJTMINUS);
                                              boolean jjtc002 = true;
                                              jjtree.openNodeScope(jjtn002);
        try {
                                              jjtree.closeNodeScope(jjtn002,  2);
                                              jjtc002 = false;
                                             jjtn002.setLine(t.beginLine);
        } finally {
                                              if (jjtc002) {
                                                jjtree.closeNodeScope(jjtn002,  2);
                                              }
        }
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void expression() throws ParseException {
    additive_expression();
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[10];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x40000,0x800,0x40000,0x1000,0x3f000000,0x80c0,0xc00000,0xc00000,0x300000,0x300000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[35];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 10; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 35; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

                         }
