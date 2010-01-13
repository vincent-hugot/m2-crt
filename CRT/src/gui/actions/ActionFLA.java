package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import model.Variable;
import valuation.FullLookAhead;


public class ActionFLA extends AbstractAction {
	MainWindow win;
	
	public ActionFLA(MainWindow win) {
		super("Full Look Ahead solution");
		this.win = win;
	}
	
	public void actionPerformed(ActionEvent e) {
		Thread t = new BacktrackThread();
		win.currentAlgorithmThread = t;
		t.start();
	}
	
	
	public class BacktrackThread extends Thread {
		
		public void run() {
			
			if (win.model == null || !win.STATE_PARSED) {
				JOptionPane.showMessageDialog(win,
						"No model available. Please parse your current file.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			// Locking everything
			win.lock();
			
			
			System.out.println("Applying Full Look-Ahead algorithm...");
			
			// Applying FLA
			FullLookAhead fla = new FullLookAhead(win.model);
			HashSet<Variable> result = fla.run();
			
			
			if (result == null || result.isEmpty()) {
				System.err.println("No solution found!");
				JOptionPane.showMessageDialog(win,
						"No solution found!",
						"Solution", JOptionPane.ERROR_MESSAGE);
			}
			else {
				System.out.println("First found solution: " + result);
				JOptionPane.showMessageDialog(win,
						"First found solution: " + result,
						"Solution", JOptionPane.INFORMATION_MESSAGE);
			}
			
			
			// Updating (destroyed) model
			System.out.println("Full Look-Ahead applied, model may be now unusable.");
			win.getModelArea().update(win.model);
			
			
			// When finished
			win.unlock();
		}
		
	}
}
