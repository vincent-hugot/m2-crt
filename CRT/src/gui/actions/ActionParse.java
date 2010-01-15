package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import model.Model;
import translator.Translator;


public class ActionParse extends AbstractAction {
	MainWindow win;
	
	public ActionParse(MainWindow win) {
		super("Parse current file");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent e) {
		Thread t = new ParseThread();
		win.currentAlgorithmThread = t;
		t.start();
	}
	
	
	
	public class ParseThread extends Thread {
		
		public void run() {
			
			// Saving before
			new ActionSave(win).actionPerformed(null);
			
			
			if (win.STATE_HASCHANGED || win.currentFilename.isEmpty()) {
				JOptionPane.showMessageDialog(win,
						"Cannot parse empty or non-existent file. Please save before.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			// Locking everything
			win.lock();
			

			System.out.println("Parsing "+ win.currentFilename);
			
			// Parsing
			Translator tr = new Translator(win.currentFilename);
			Model model = tr.translate();
			
			
			if (tr.fail()) { // errors
				System.err.println("There were errors during the translation:");
				tr.dumpErrors();
			}
			else { // no errors
				System.out.println("Translation was successful.");
				
				win.model = model;
				win.getModelArea().update(win.model);
			}
			
			
			// When finished
			win.unlock();
			win.STATE_PARSED = !tr.fail();
		}
		
	}
}
