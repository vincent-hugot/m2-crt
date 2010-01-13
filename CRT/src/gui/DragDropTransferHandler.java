package gui;

import gui.actions.ActionOpen;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class DragDropTransferHandler extends TransferHandler {

	private DataFlavor fileFlavor;
	private DataFlavor stringFlavor;
	private MainWindow win;
	//private Position p0, p1;
	//private boolean toRemove;

	/**
	 * Gere le Drag And Drop d'un fichier depuis un explorateur par exemple
	 * 
	 * @param zoneTexte
	 * @param barre
	 */
	public DragDropTransferHandler(MainWindow win) {
		this.win = win;
		fileFlavor = DataFlavor.javaFileListFlavor;
		stringFlavor = DataFlavor.stringFlavor;
	}

	// File import
	@SuppressWarnings("unchecked")
	public boolean importData(JComponent c, Transferable t) {

		// False if note dropable
		if (!canImport(c, t.getTransferDataFlavors())) {
			return false;
		}

		// type is File
		if (hasFileFlavor(t.getTransferDataFlavors())) {
			// String str = null;
			List<File> files;
			try {
				files = (List<File>) t.getTransferData(fileFlavor);

				// on boucle sur le nombre de fichiers que l'on drag
				for (int i = 0; i < files.size(); i++) {
					
					File file = (File) files.get(i);
					
					ActionOpen ao = new ActionOpen(win);
					
					/* VERIFICATION : Enregistrement avant ouverture */
					if (win.STATE_HASCHANGED && !ao.confirm())
						return false;
					
					ao.open(file.getAbsolutePath());
				}
			} catch (UnsupportedFlavorException e) {
			} catch (IOException e) {
			}

		}

		return true;

	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		if (hasFileFlavor(flavors)) {
			return true;
		}
		if (hasStringFlavor(flavors)) {
			return true;
		}
		return false;
	}

	/**
	 * @param flavors
	 * @return boolean
	 */
	private boolean hasFileFlavor(DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (fileFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param flavors
	 * @return boolean
	 */
	private boolean hasStringFlavor(DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (stringFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

}