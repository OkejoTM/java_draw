package actions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.Shape;

/**
 * ColorAction implements a single undoable action where the color of a group
 * of Shapes is changed.
 */
public class ColorAction implements DrawAction {

	private final List<Shape> shapes;
	private final List<Color> oldColors = new ArrayList<Color>();
	private final Color newColor;

	/**
	 * Creates a ColorAction that changes the color of the given Shapes.
	 * 
	 * @param shapes
	 *            the shapes to be modified.
	 * @param newColor
	 *            the new color for the shapes.
	 */
	public ColorAction(List<Shape> shapes, Color newColor) {
		this.shapes = new ArrayList<Shape>(shapes);
		for (Shape s : this.shapes) {
			oldColors.add(s.getColor());
		}
		this.newColor = newColor;
	}

	public void execute() {
		for (Shape s : shapes) {
			s.setColor(newColor);
		}
	}

	public String getDescription() {
		return "Change color";
	}

	public void redo() {
		this.execute();
	}

	public void undo() {
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setColor(oldColors.get(i));
		}
	}

}
