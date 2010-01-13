package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


public class ActionSave extends AbstractAction {
	MainWindow win;
	
	public ActionSave(MainWindow win) {
		super("Save as...");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent evt) {
		
		if (!win.STATE_HASCHANGED) return;
		
		// No filename (No title)
		if (win.currentFilename.isEmpty()) {
			new ActionSaveAs(win).actionPerformed(null);
			return;
		}
		
		
		// Already a file, trying to save...
		System.out.println("Saving " + win.currentFilename);
		
		try {
			FileWriter fw = new FileWriter(win.currentFilename);
			try {
				fw.write(win.getCodeArea().getText());
			} finally {
				fw.close();
				System.out.println("Save done.");
			}
		} catch (IOException e) {
			System.err.println("Error while saving: " + e.getMessage());
			JOptionPane.showMessageDialog(win, "Error while saving: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		
		win.noMoreChanges();
	}
}
