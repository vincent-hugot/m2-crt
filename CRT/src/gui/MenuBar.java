package gui;

import gui.actions.ActionAC3;
import gui.actions.ActionAC6;
import gui.actions.ActionBacktrack;
import gui.actions.ActionFLA;
import gui.actions.ActionNew;
import gui.actions.ActionOpen;
import gui.actions.ActionParse;
import gui.actions.ActionQuit;
import gui.actions.ActionSave;
import gui.actions.ActionSaveAs;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class MenuBar extends JMenuBar {
	
	// File
	private JMenu menuFile = new JMenu("File");
	private JMenuItem buttonNew;
	private JMenuItem buttonOpen;
	private JMenuItem buttonSave;
	private JMenuItem buttonSaveAs;
	private JMenuItem buttonQuit;
	
	// Actions
	private JMenu menuRun = new JMenu("Action");
	private JMenuItem buttonParse;
	private JMenuItem buttonAC3;
	private JMenuItem buttonAC6;
	private JMenuItem buttonBacktrack;
	private JMenuItem buttonFLA;
	
	MainWindow win;
	
	

	public MenuBar(MainWindow win) {
		
		this.win = win;

		ActionNew actionNew = new ActionNew(win);
		ActionOpen actionOpen = new ActionOpen(win);
		ActionSave actionSave = new ActionSave(win);
		ActionSaveAs actionSaveAs = new ActionSaveAs(win);
		ActionQuit actionQuit = new ActionQuit(win);
		
		ActionParse actionParse = new ActionParse(win);
		ActionAC3 actionAC3 = new ActionAC3(win);
		ActionAC6 actionAC6 = new ActionAC6(win);
		ActionBacktrack actionBacktrack = new ActionBacktrack(win);
		ActionFLA actionFLA = new ActionFLA(win);
		

		buttonNew = new JMenuItem(actionNew);
		buttonNew.setIcon(new ImageIcon(getClass().getResource("resources/menubar_new.png")));
		buttonNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuFile.add(buttonNew);
		
		buttonOpen = new JMenuItem(actionOpen);
		buttonOpen.setIcon(new ImageIcon(getClass().getResource("resources/menubar_open.png")));
		buttonOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuFile.add(buttonOpen);
		
		menuFile.addSeparator();
		
		buttonSave = new JMenuItem(actionSave);
		buttonSave.setIcon(new ImageIcon(getClass().getResource("resources/menubar_save.png")));
		buttonSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuFile.add(buttonSave);
		
		buttonSaveAs = new JMenuItem(actionSaveAs);
		buttonSaveAs.setIcon(new ImageIcon(getClass().getResource("resources/menubar_save.png")));
		menuFile.add(buttonSaveAs);
		
		menuFile.addSeparator();
		
		buttonQuit = new JMenuItem(actionQuit);
		buttonQuit.setIcon(new ImageIcon(getClass().getResource("resources/menubar_quit.png")));
		buttonQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuFile.add(buttonQuit);
		

		add(menuFile);
		




		buttonParse = new JMenuItem(actionParse);
		menuRun.add(buttonParse);
		
		menuRun.addSeparator();
		
		buttonAC3 = new JMenuItem(actionAC3);
		menuRun.add(buttonAC3);
		buttonAC6 = new JMenuItem(actionAC6);
		menuRun.add(buttonAC6);
		
		menuRun.addSeparator();
		
		buttonBacktrack = new JMenuItem(actionBacktrack);
		menuRun.add(buttonBacktrack);
		buttonFLA = new JMenuItem(actionFLA);
		menuRun.add(buttonFLA);
		

		add(menuRun);
		
		
		cantSave();
	}
	
	
	public void activate() {
		for (int i=0; i<getComponentCount(); i++) {
			JMenu menu = (JMenu) getComponent(i);
			for (int j=0; j<menu.getItemCount();j++) {
				JMenuItem menuItem = menu.getItem(j);
				if(menuItem!=null)
					menuItem.setEnabled(true);
			}
			menu.setEnabled(true);
		}
		win.updateChangeState();
	}
	
	public void deactivate() {
		for (int i=0; i<getComponentCount(); i++) {
			JMenu menu = (JMenu) getComponent(i);
			for (int j=0; j<menu.getItemCount(); j++) {
				JMenuItem menuItem = menu.getItem(j);
				if(menuItem!=null)
					menuItem.setEnabled(false);
			}
			menu.setEnabled(false);
		}
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
