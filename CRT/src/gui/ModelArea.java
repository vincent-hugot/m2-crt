package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import model.Model;


public class ModelArea extends JTextPane {

	public static final String FONT = "Monospaced";
	public static final int FONT_SIZE = 16;
	
	MainWindow win;
	
	// Syntax highlighting
	private Style normalStyle;
	private Style[] specialStyles;
	private String[] keywords;
	private StyleContext styleContext;
	
	
	boolean dosMode;
	

	public ModelArea(MainWindow win, boolean dosMode) {
		
		this.win = win;
		this.dosMode = dosMode;
		
		
		this.setTransferHandler(new DragDropTransferHandler(win));
		
		this.setEditable(false);
		
		
		if (dosMode) {
			this.setBackground(Color.BLACK);
			this.setForeground(Color.ORANGE);
		}
		

		this.setDocument(new DefaultStyledDocument() {
			
			public void insertString(int a, String b, AttributeSet c) throws BadLocationException {
				super.insertString(a, b, c);
				syntaxHighlighting();
				hasChanged();
			}
			
			public void remove(int a, int b) throws BadLocationException {
				super.remove(a, b);
				syntaxHighlighting();
				hasChanged();
			}
		});
		

		// Setting the font
		Font font = new Font(FONT, Font.PLAIN, FONT_SIZE);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(
					"resources/Perfect DOS VGA 437.ttf"));
			font = font.deriveFont(Font.PLAIN, FONT_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		
		this.setFont(font);
		



		// Styles for syntax highlighting
		specialStyles = new Style[3];
		
		styleContext = new StyleContext();
		normalStyle = styleContext.addStyle(" ", null);
		
		// Keyword
		specialStyles[0] = styleContext.addStyle(" ", null);
		specialStyles[0].addAttribute(StyleConstants.Bold, true);
		if (dosMode)
			specialStyles[0].addAttribute(StyleConstants.Foreground, Color.CYAN);
		else
			specialStyles[0].addAttribute(StyleConstants.Foreground, Color.BLUE);
		
		// Number
		specialStyles[1] = styleContext.addStyle(" ", null);
		if (dosMode)
			specialStyles[1].addAttribute(StyleConstants.Foreground, Color.LIGHT_GRAY);
		else
			specialStyles[1].addAttribute(StyleConstants.Foreground, new Color(128, 128, 0));
		
		// Commentaires
		specialStyles[2] = styleContext.addStyle(" ", null);
		specialStyles[2].addAttribute(StyleConstants.Foreground, new Color(0, 128, 0));
		specialStyles[2].addAttribute(StyleConstants.FontSize, FONT_SIZE);
		specialStyles[2].addAttribute(StyleConstants.Italic, false);
		specialStyles[2].addAttribute(StyleConstants.Bold, false);
		

		// Keywords
		keywords = new String[] { "\\b(DOMAINS|CONSTRAINTS|SUBSTITUTIONS)\\b",
				"(\\b|-)([0-9]+)\\b" // "\\b([0-9]+)\\b"
		};
		
	}
	
	

	private void hasChanged() {
		win.STATE_HASCHANGED = true;
		win.setWindowTitle();
	}
	
	

	public void append(String str) {
		Document doc = getDocument();
		if (doc != null) {
			try {
				doc.insertString(doc.getLength(), str, null);
			} catch (BadLocationException e) {}
		}
	}
	
	

	/**
	 * Syntax highlighting
	 */
	public void syntaxHighlighting() {
		
		String content = null;
		
		try {
			
			Document documentCache = this.getDocument();
			content = documentCache.getText(0, documentCache.getLength());
			
		} catch (BadLocationException e) {}
		
		
		((DefaultStyledDocument) this.getDocument()).setCharacterAttributes(0, content.length(),
				normalStyle, true);
		
		for (int i = 0; i < keywords.length; i++) {
			
			Pattern pattern = Pattern.compile(keywords[i], Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(content);
			
			while (matcher.find()) {
				try {
					if (matcher.start() <= 0 || (matcher.start() > 0 && !this.getDocument().getText(matcher.start()-1, 1).equals("@")))
					((DefaultStyledDocument) this.getDocument()).setCharacterAttributes(
							matcher.start(), matcher.end() - matcher.start(), specialStyles[i], false);
				} catch (BadLocationException e) {}
			}
			
		}
		
		/* Comments */
		// Simple : looking for //, then to end of line
		Matcher simpleComment = Pattern.compile("(//.*)$", Pattern.MULTILINE).matcher(content);
		while (simpleComment.find()) {
			((DefaultStyledDocument) this.getDocument()).setCharacterAttributes(simpleComment
					.start(), simpleComment.end() - simpleComment.start(), specialStyles[2], false);
		}
		
		// Multiline comment : looking for /*, then */, coloring in between
		Matcher multiCommentStart = Pattern.compile("/\\*", Pattern.MULTILINE).matcher(content);
		Matcher multiCommentEnd = Pattern.compile("\\*/", Pattern.MULTILINE).matcher(content);
		
		while (multiCommentStart.find()) {
			
			if (multiCommentEnd.find(multiCommentStart.end())) { // found */
																	// AFTER /*
				((DefaultStyledDocument) this.getDocument()).setCharacterAttributes(
						multiCommentStart.start(), multiCommentEnd.end()
								- multiCommentStart.start(), specialStyles[2], false);
			}
			else { // Else, color the reste (EOF - start)
				((DefaultStyledDocument) this.getDocument()).setCharacterAttributes(
						multiCommentStart.start(), content.length() - multiCommentStart.start(),
						specialStyles[3], false);
			}
			
		}
		

	}
	
	
	
	public void update(Model model) {
		this.setText(model.toString());
	}
}
