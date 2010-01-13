package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import ac.AC6;


public class ActionAC6 extends AbstractAction {
	
	MainWindow win;
	
	public ActionAC6(MainWindow win) {
		super("Apply AC6");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent e) {
		Thread t = new AC6Thread();
		win.currentAlgorithmThread = t;
		t.start();
	}
	
	
	public class AC6Thread extends Thread {
		
		public void run() {
			
			if (win.model == null || !win.STATE_PARSED) {
				JOptionPane.showMessageDialog(win,
						"No model available. Please parse your current file.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			

			// Locking everything
			win.lock();
			

			System.out.println("Applying AC6 algorithm...");
			
			// Applying AC6
			AC6 ac6 = new AC6(win.model);
			ac6.run();
			

			// Updating model
			System.out.println("AC6 applied, model should have changed.");
			win.getModelArea().update(win.model);
			

			// When finished
			win.unlock();
		}
		
	}
}
