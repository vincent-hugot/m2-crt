package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


public class ActionNew extends AbstractAction {
	MainWindow win;
	
	public ActionNew(MainWindow win) {
		super("New");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (win.STATE_HASCHANGED && !confirm()) // confirm = false means cancel
			return;
		
		win.reset();
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
}
