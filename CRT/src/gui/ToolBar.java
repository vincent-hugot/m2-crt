package gui;

import gui.actions.ActionAC3;
import gui.actions.ActionAC6;
import gui.actions.ActionBacktrack;
import gui.actions.ActionFLA;
import gui.actions.ActionNew;
import gui.actions.ActionOpen;
import gui.actions.ActionParse;
import gui.actions.ActionSave;
import gui.actions.ActionSaveAs;
import gui.actions.ActionStop;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;


public class ToolBar extends JToolBar {
	
	private final static int BUTTONS_SIZE = 24;
	
	private JButton buttonNew;
	private JButton buttonOpen;
	private JButton buttonSave;
	private JButton buttonSaveAs;
	
	private JButton buttonParse;
	private JButton buttonAC3;
	private JButton buttonAC6;
	private JButton buttonBacktrack;
	private JButton buttonFLA;
	
	private JButton buttonStop;
	
	
	MainWindow win;
	
	

	public ToolBar(MainWindow win) {
		
		this.win = win;
		
		Dimension buttonsSize = new Dimension(BUTTONS_SIZE,BUTTONS_SIZE);
		
		ActionNew actionNew = new ActionNew(win);
		ActionOpen actionOpen = new ActionOpen(win);
		ActionSave actionSave = new ActionSave(win);
		ActionSaveAs actionSaveAs = new ActionSaveAs(win);
		
		ActionParse actionParse = new ActionParse(win);
		ActionAC3 actionAC3 = new ActionAC3(win);
		ActionAC6 actionAC6 = new ActionAC6(win);
		ActionBacktrack actionBacktrack = new ActionBacktrack(win);
		ActionFLA actionFLA = new ActionFLA(win);
		
		ActionStop actionStop = new ActionStop(win);
		
		
		buttonNew = new JButton(new ImageIcon(getClass().getResource("resources/menubar_new.png")));
		buttonNew.setMaximumSize(buttonsSize);
		buttonNew.addActionListener(actionNew);
		buttonNew.setToolTipText("New file");
		
		buttonOpen = new JButton(new ImageIcon(getClass().getResource("resources/menubar_open.png")));
		buttonOpen.setMaximumSize(buttonsSize);
		buttonOpen.addActionListener(actionOpen);
		buttonOpen.setToolTipText("Open");
		
		buttonSave = new JButton(new ImageIcon(getClass().getResource("resources/menubar_save.png")));
		buttonSave.setMaximumSize(buttonsSize);
		buttonSave.addActionListener(actionSave);
		buttonSave.setToolTipText("Save");
		
		buttonSaveAs = new JButton(new ImageIcon(getClass().getResource("resources/menubar_save.png")));
		buttonSaveAs.setMaximumSize(buttonsSize);
		buttonSaveAs.addActionListener(actionSaveAs);
		buttonSaveAs.setToolTipText("Save as...");
		
		
		
		buttonParse = new JButton("Parse");
		//buttonParse.setMaximumSize(buttonsSize);
		buttonParse.addActionListener(actionParse);
		buttonParse.setToolTipText("Parse current file");
		
		buttonAC3 = new JButton("AC3");
		//buttonAC3.setMaximumSize(buttonsSize);
		buttonAC3.addActionListener(actionAC3);
		buttonAC3.setToolTipText("Apply AC3 algorithm");
		
		buttonAC6 = new JButton("AC6");
		//buttonAC6.setMaximumSize(buttonsSize);
		buttonAC6.addActionListener(actionAC6);
		buttonAC6.setToolTipText("Apply AC3 algorithm");
		
		buttonBacktrack = new JButton("Backtracking");
		//buttonBacktrack.setMaximumSize(buttonsSize);
		buttonBacktrack.addActionListener(actionBacktrack);
		buttonBacktrack.setToolTipText("Find a solution with Backtracking");
		
		buttonFLA = new JButton("FLA");
		//buttonFLA.setMaximumSize(buttonsSize);
		buttonFLA.addActionListener(actionFLA);
		buttonFLA.setToolTipText("Find a solution with Full Look-Ahead");
		
		

		buttonStop = new JButton(new ImageIcon(getClass().getResource("resources/stop.png")));
		//buttonStop.setMaximumSize(buttonsSize);
		buttonStop.addActionListener(actionStop);
		buttonStop.setToolTipText("Interrupt current action");
		
		
		add(buttonNew);
		add(buttonOpen);
		add(buttonSave);
		add(buttonSaveAs);
		addSeparator(buttonsSize);
		add(buttonParse);
		add(buttonAC3);
		add(buttonAC6);
		add(buttonBacktrack);
		add(buttonFLA);
		addSeparator(buttonsSize);
		add(buttonStop);
		
		cantSave();
	}
	
	
	public void activate() {
		for (int i=0; i<getComponentCount(); i++) {
			getComponent(i).setEnabled(true);
		}
		buttonStop.setEnabled(false);
		win.updateChangeState();
	}
	
	public void deactivate() {
		for (int i=0; i<getComponentCount(); i++) {
			getComponent(i).setEnabled(false);
		}
		buttonStop.setEnabled(true);
		win.updateChangeState();
	}
	
	
	public void canSave() {
		buttonSave.setEnabled(true);
		buttonSaveAs.setEnabled(true);
	}
	public void cantSave() {
		buttonSave.setEnabled(false);
		buttonSaveAs.setEnabled(false);
	}
}
