package gui.actions;

import gui.MainWindow;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


public class ActionStop extends AbstractAction {
	MainWindow win;
	
	public ActionStop(MainWindow win) {
		super("Interrupt");
		this.win = win;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		
		win.currentAlgorithmThread.stop();
		
		win.unlock();
	}
}
