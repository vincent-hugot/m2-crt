package gui;

import gui.actions.ActionQuit;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import model.Model;

public class MainWindow extends JFrame implements WindowListener {

	private Dimension SCREEN_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	
	private MenuBar menuBar;
	private ToolBar toolBar;
	
	private CodeArea codeArea;
	private ModelArea modelArea;
	private ConsoleArea consoleArea;
	
	public Model model;
	
	
	public boolean STATE_PARSED = false;
	public boolean STATE_HASCHANGED = false;
	public boolean STATE_ACTION = false;
	
	public String lastDir = ".";
	public String currentFilename = "";
	
	public Thread currentAlgorithmThread = new Thread();
	
	
	
	public MainWindow() {
		
		model = null;
		
		
		setWindowTitle();
		
		setLayout(new BorderLayout());
		
		// Window position and size
		setSize((int) SCREEN_SIZE.getWidth()*2/3,(int) SCREEN_SIZE.getHeight()*2/3);
		setLocationRelativeTo(this.getParent());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//pack();
		
		addWindowListener(this);
		
		
		// Menu Bar
		menuBar = new MenuBar(this);
		this.setJMenuBar(menuBar);
		
		// Tool Bar
		toolBar = new ToolBar(this);
		
		
		// Areas
		codeArea = new CodeArea(this);
		modelArea = new ModelArea(this, true);
		consoleArea = new ConsoleArea(this);
		
		JScrollPane codeScroll = new JScrollPane(codeArea);
		codeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		codeScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollPane modelScroll = new JScrollPane(modelArea);
		modelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		modelScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollPane consoleScroll = new JScrollPane(consoleArea);
		consoleScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		consoleScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		
		JTabbedPane codeTab = new JTabbedPane();
		codeTab.add(codeScroll, "Source code");
		JTabbedPane modelTab = new JTabbedPane();
		modelTab.add(modelScroll, "Model output");
		JTabbedPane consoleTab = new JTabbedPane();
		consoleTab.add(consoleScroll, "Console output");
		
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, codeTab, modelTab);
		horizontalSplit.setOneTouchExpandable(true);
		horizontalSplit.setDividerLocation((int) (SCREEN_SIZE.width/2));
		
		JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplit, consoleTab);
		verticalSplit.setOneTouchExpandable(true);
		verticalSplit.setDividerLocation((int) (3*SCREEN_SIZE.height/5));
		
		
		verticalSplit.setTransferHandler(new DragDropTransferHandler(this));
		

		this.add(toolBar, BorderLayout.NORTH);
		this.add(verticalSplit, BorderLayout.CENTER);
		
		unlock();
	}
	
	
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		new ActionQuit(this).actionPerformed(null);
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}


	
	
	public ToolBar getToolBar() {
		return toolBar;
	}
	
	public MenuBar getMenuBarCRT() {
		return menuBar;
	}
	
	public CodeArea getCodeArea() {
		return codeArea;
	}
	
	public ModelArea getModelArea() {
		return modelArea;
	}
	
	public ConsoleArea getConsoleArea() {
		return consoleArea;
	}
	
	
	
	public void setWindowTitle() {
		String title = "Constraints Solver - ";
		
		if (STATE_HASCHANGED) title += "*";
		
		if (currentFilename.isEmpty()) title += "(No title)";
		else title += currentFilename;
		//else title += new File(currentFilename).getName();
		
		
		//title += ">";
		
		this.setTitle(title);
	}
	
	
	
	public void reset() {
		
		// GUI part
		try {
			codeArea.getDocument().remove(
					0, codeArea.getDocument().getLength());
			modelArea.getDocument().remove(
					0, modelArea.getDocument().getLength());
			consoleArea.getDocument().remove(
					0, consoleArea.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		currentFilename = "";
		noMoreChanges();
		
		// Model
		this.model = null;
	}
	
	
	public void hasChanged() {
		STATE_HASCHANGED = true;
		updateChangeState();
	}
	
	public void noMoreChanges() {
		STATE_HASCHANGED = false;
		updateChangeState();
	}
	
	
	public void updateChangeState() {
		if (STATE_HASCHANGED) {
			menuBar.canSave();
			toolBar.canSave();
		}
		else {
			menuBar.cantSave();
			toolBar.cantSave();
		}
		
		setWindowTitle();
	}
	
	
	public void lock() {
		boolean stateChanged = this.STATE_HASCHANGED;
		
		menuBar.deactivate();
		toolBar.deactivate();
		codeArea.lock();
		
		this.STATE_HASCHANGED = stateChanged;
		updateChangeState();
	}
	
	public void unlock() {
		boolean stateChanged = this.STATE_HASCHANGED;
		
		menuBar.activate();
		toolBar.activate();
		codeArea.unlock();
		
		this.STATE_HASCHANGED = stateChanged;
		updateChangeState();
	}
}
