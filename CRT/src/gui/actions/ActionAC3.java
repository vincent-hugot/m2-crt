package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import ac.AC3;


public class ActionAC3 extends AbstractAction {
	MainWindow win;
	
	public ActionAC3(MainWindow win) {
		super("Apply AC3");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent e) {
		Thread t = new AC3Thread();
		win.currentAlgorithmThread = t;
		t.start();
	}
	
	
	public class AC3Thread extends Thread {
		
		public void run() {
			
			
			
			if (win.model == null || !win.STATE_PARSED) {
				JOptionPane.showMessageDialog(win,
						"No model available. Please parse your current file.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			// Locking everything
			win.lock();
			
			
			System.out.println("Applying AC3 algorithm...");
			
			// Applying AC3
			AC3 ac3 = new AC3(win.model);
			ac3.run();
			
			
			// Updating model
			System.out.println("AC3 applied, model should have changed.");
			win.getModelArea().update(win.model);
			
			
			// When finished
			win.unlock();
		}
		
	}
}
