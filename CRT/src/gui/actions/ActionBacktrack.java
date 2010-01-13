package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import valuation.Backtracking;


public class ActionBacktrack extends AbstractAction {
	MainWindow win;
	
	public ActionBacktrack(MainWindow win) {
		super("Backtracking solution");
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
			
			
			System.out.println("Applying Backtracking algorithm...");
			
			// Applying Backtracking
			Backtracking backtrack = new Backtracking(win.model);
			String result = backtrack.run();
			
			
			if (result.isEmpty()) {
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
			System.out.println("Backtracking applied, model may be now unusable.");
			win.getModelArea().update(win.model);
			
			
			// When finished
			win.unlock();
		}
		
	}
}
