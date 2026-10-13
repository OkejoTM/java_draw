package actions;

import java.util.ArrayList;
import java.util.List;

import model.Drawing;
import model.Shape;

/**
 * DeleteAction implements a single undoable action where a group of Shapes is
 * removed from a Drawing.
 */
public class DeleteAction implements DrawAction {

	private final Drawing d;
	private final List<Shape> shapes = new ArrayList<Shape>();
	private final List<Integer> positions = new ArrayList<Integer>();

	/**
	 * Creates a DeleteAction that removes the given shapes from the given
	 * Drawing.
	 * 
	 * @param drawing
	 *            the drawing from which the shapes should be removed.
	 * @param toDelete
	 *            the shapes to be removed.
	 */
	public DeleteAction(Drawing drawing, List<Shape> toDelete) {
		this.d = drawing;
		// Remember the shapes in z-order, so undo() can put every shape back
		// to its original position.
		for (Shape s : drawing) {
			if (toDelete.contains(s)) {
				shapes.add(s);
				positions.add(drawing.indexOf(s));
			}
		}
	}

	public void execute() {
		for (Shape s : shapes) {
			d.removeShape(s);
		}
	}

	public String getDescription() {
		return "Delete shapes";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		for (int i = 0; i < shapes.size(); i++) {
			d.insertShape(positions.get(i), shapes.get(i));
		}
	}

}
