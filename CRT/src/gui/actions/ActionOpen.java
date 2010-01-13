package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class ActionOpen extends AbstractAction {
	MainWindow win;
	
	public ActionOpen(MainWindow win) {
		super("Open");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (win.STATE_HASCHANGED && !confirm()) // confirm = false means cancel
			return;
		
		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(win.lastDir));
		
		if (fc.showOpenDialog(win) != JFileChooser.APPROVE_OPTION)
			return;
		
		String filePath = fc.getSelectedFile().getAbsolutePath();
		
		
		// reseting before open (fail at open = same as NEW)
		win.reset();
		
		
		open(filePath);
	}
	
	
	
	
	public boolean confirm() {
		int res = JOptionPane.showConfirmDialog(null,
				"Wanna save?", "Save",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		switch (res) {
		case JOptionPane.CANCEL_OPTION: // Cancel : do nothing
			return false;
			
		case JOptionPane.YES_OPTION: // Need to save
			new ActionSaveAs(win).actionPerformed(null);
			break;
		case JOptionPane.NO_OPTION: // No : no saving
			break;

		default:
			return false;
		}
		
		return true;
	}
	
	
	public void open(String filePath) {
		File f = new File(filePath);
		if (!f.isFile())
			return;
		
		try {
			Scanner scanner=new Scanner(f);
			StringBuilder content = new StringBuilder();
			
			while (scanner.hasNextLine()) {
				content.append(scanner.nextLine()+"\n");
			}
			scanner.close();
			
			win.getCodeArea().setText(content.toString());
			
			win.currentFilename = filePath; //f.getName();
			win.lastDir = filePath;
			win.noMoreChanges();
			
			System.out.println(filePath + " open.");
			
		} catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(win, "Error while opening: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
