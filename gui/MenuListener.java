package gui;

import gui.MainMenu.NewDrawingDialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.DrawingController;

/**
 * Listens to actions from the buttons in a menu and modifies the Drawing
 * through a DrawingController
 * 
 * @author Alex Lagerstedt
 * 
 */
public class MenuListener implements ActionListener {

	private final DrawingController controller;
	private final DrawingPanel view;
	private JFileChooser fileDialog;

	public MenuListener(DrawingController c, DrawingPanel view) {
		this.controller = c;
		this.view = view;
	}

	private static void showError(String message, IOException e) {
		JOptionPane.showMessageDialog(null, message + "\n" + e.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Quit")) {
			System.exit(0);
		}

		else if (cmd.equals("Undo")) {
			controller.undo();
		}

		else if (cmd.equals("Redo")) {
			controller.redo();
		}

		else if (cmd.equals("Select all")) {
			controller.selectAll();

		}

		else if (cmd.equals("Clear selection")) {
			controller.clearSelection();
		}

		else if (cmd.equals("Delete")) {
			controller.deleteSelectedShapes();
		}

		else if (cmd.equals("Open")) {
			fileDialog = new JFileChooser();
			fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileFilter filter = new FileNameExtensionFilter("Draw files",
					"draw");
			fileDialog.addChoosableFileFilter(filter);
			fileDialog.setFileFilter(filter);

			fileDialog.showOpenDialog(null);
			File f = fileDialog.getSelectedFile();
			if (f != null) {
				try {
					controller.open(f);
				}
				catch (IOException ex) {
					showError("Could not open the drawing.", ex);
				}
			}

		}

		else if (cmd.equals("Save as")) {
			fileDialog = new JFileChooser();
			fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);

			fileDialog.setSelectedFile(new File("new.draw"));
			FileFilter filter = new FileNameExtensionFilter("Draw files",
					"draw");
			fileDialog.addChoosableFileFilter(filter);
			fileDialog.setFileFilter(filter);

			fileDialog.showSaveDialog(null);

			File f = fileDialog.getSelectedFile();
			if (f != null) {
				try {
					controller.save(f);
				}
				catch (IOException ex) {
					showError("Could not save the drawing.", ex);
				}
			}
		}

		else if (cmd.equals("Export PNG")) {
			fileDialog = new JFileChooser();
			fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
			FileFilter filter = new FileNameExtensionFilter(
					"Portable Network Graphics", "png");
			fileDialog.addChoosableFileFilter(filter);

			fileDialog.setSelectedFile(new File("out.png"));
			fileDialog.showSaveDialog(null);

			File f = fileDialog.getSelectedFile();
			if (f != null) {
				try {
					ImageIO.write(view.getImage(), "png", f);
				}
				catch (IOException ex) {
					showError("Could not export the drawing.", ex);
				}
			}

		}

		else if (cmd.equals("New")) {
			NewDrawingDialog diag = new NewDrawingDialog();
			Dimension size = diag.getNewSize();
			if (size != null) {
				controller.newDrawing(size.width, size.height);
			}
		}

		else {
			JOptionPane.showMessageDialog(null, "Not implemented.",
					"Not implemented", JOptionPane.ERROR_MESSAGE);
		}
	}
}
