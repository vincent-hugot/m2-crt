package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class ConsoleArea extends JTextPane {
	
	public static final String FONT = "Monospaced";
	public static final int FONT_SIZE = 13;
	
	MainWindow win;
	
	
	
	public ConsoleArea(MainWindow win) {
		this.win = win;
		
		
		this.setTransferHandler(new DragDropTransferHandler(win));
		
		this.setEditable(false);
		
		PrintStream printStreamOut = new PrintStream(new ConsoleOutputStream(
				this, new ByteArrayOutputStream()));
		PrintStream printStreamErr = new PrintStream(new ConsoleErrorStream(
						this, new ByteArrayOutputStream()));
		
		System.setOut(printStreamOut);
		System.setErr(printStreamErr);
		
		
		
		// Setting the font
		Font font = new Font(FONT, Font.PLAIN, FONT_SIZE);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(
					"resources/Consolas.ttf"));
			font = font.deriveFont(Font.PLAIN, FONT_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		
		this.setFont(font);
	}
	
	public void append(String str) {
		Document doc = getDocument();
		if (doc != null) {
			try {
				doc.insertString(doc.getLength(), str, null);
				this.setCaretPosition(doc.getLength());
			} catch (BadLocationException e) {}
		}
		
	}
	
	
	public void append(String str, Color color, Color bgColor, boolean isBold) {
		Document doc = this.getDocument();
		SimpleAttributeSet style = new SimpleAttributeSet();
		
		if (doc != null) {
			
			// Styles particuliers
			if (color != null) StyleConstants.setForeground(style, color);
			if (bgColor != null) StyleConstants.setBackground(style, bgColor);
			StyleConstants.setBold(style, isBold);
			
			try {
				doc.insertString(doc.getLength(), str, style);
				this.setCaretPosition(doc.getLength());
			} catch (BadLocationException e) {}
		}
	}
	
	
	
	class ConsoleOutputStream extends FilterOutputStream {
		
		ConsoleArea consoleArea;
		
		public ConsoleOutputStream(ConsoleArea consoleArea, OutputStream stream) {
			
			super(stream);
			this.consoleArea = consoleArea;
		}
		
		public void write(byte b[]) throws IOException {
			String str = new String(b);
			consoleArea.append(str);
		}
		
		public void write(byte b[], int off, int len) throws IOException {
			String str = new String(b, off, len);
			consoleArea.append(str);
		}
	}
	
	
	class ConsoleErrorStream extends FilterOutputStream {
		
		ConsoleArea consoleArea;
		
		public ConsoleErrorStream(ConsoleArea consoleArea, OutputStream stream) {
			
			super(stream);
			this.consoleArea = consoleArea;
		}
		
		public void write(byte b[]) throws IOException {
			String str = new String(b);
			consoleArea.append(str, Color.RED, null, false);
		}
		
		public void write(byte b[], int off, int len) throws IOException {
			String str = new String(b, off, len);
			consoleArea.append(str, Color.RED, null, false);
		}
	}
}
