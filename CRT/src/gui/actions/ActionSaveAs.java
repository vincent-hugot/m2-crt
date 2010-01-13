package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class ActionSaveAs extends AbstractAction {
	MainWindow win;
	
	public ActionSaveAs(MainWindow win) {
		super("Save as...");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent evt) {
		
		if (!win.STATE_HASCHANGED) return;
		
		
		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(win.lastDir));
		
		if (fc.showSaveDialog(win) != JFileChooser.APPROVE_OPTION)
			return;
		
		String filePath = fc.getSelectedFile().getAbsolutePath();
		
		System.out.println("Saving " + filePath);
		
		try {
			FileWriter fw = new FileWriter(filePath);
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
		
		win.lastDir = filePath;
		win.currentFilename = filePath;
		// win.currentFilename = new File(filePath).getName();
		
		win.noMoreChanges();
	}
}
