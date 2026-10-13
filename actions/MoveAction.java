package actions;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.Shape;

/**
 * MoveAction implements a single undoable action where a group of Shapes is
 * moved.
 */
public class MoveAction implements DrawAction {

	private final List<Shape> shapes;
	private final Point movement;

	/**
	 * Creates a MoveAction that moves all given Shapes in the direction given
	 * by the point. The movement is relative to the shapes original position.
	 * 
	 * @param shapes
	 *            the shapes to be moved
	 * @param m
	 *            the amount the shapes should be moved, relative to the
	 *            original position
	 */
	public MoveAction(List<Shape> shapes, Point m) {
		this.shapes = new ArrayList<Shape>(shapes);
		this.movement = new Point(m);
	}

	public void execute() {
		for (Shape s : shapes) {
			s.move(movement.x, movement.y);
		}
	}

	public String getDescription() {
		return "Move shapes";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		for (Shape s : shapes) {
			s.move(-movement.x, -movement.y);
		}
	}

}
