package actions;

import java.util.ArrayList;
import java.util.List;

import model.FillableShape;
import model.Shape;
import model.ShapeVisitorAdapter;

/**
 * FillAction implements an undoable action where the fill status of all the
 * fillable Shapes in a given group is set.
 */
public class FillAction implements DrawAction {

	private final List<FillableShape> shapes = new ArrayList<FillableShape>();
	private final List<Boolean> oldStates = new ArrayList<Boolean>();
	private final boolean filled;

	/**
	 * Creates a FillAction that sets the fill status of all fillable shapes in
	 * the given group. Other shapes (lines, texts) are ignored.
	 * 
	 * @param group
	 *            the shapes to be modified
	 * @param filled
	 *            the new fill status
	 */
	public FillAction(List<Shape> group, boolean filled) {
		this.filled = filled;
		ShapeVisitorAdapter collector = new ShapeVisitorAdapter() {
			@Override
			public void visitFillable(FillableShape s) {
				shapes.add(s);
				oldStates.add(s.isFilled());
			}
		};
		for (Shape s : group) {
			s.accept(collector);
		}
	}

	public void execute() {
		for (FillableShape s : shapes) {
			s.setFilled(filled);
		}
	}

	public String getDescription() {
		return filled ? "Fill shapes" : "Unfill shapes";
	}

	public boolean isEmpty() {
		return shapes.isEmpty();
	}

	public void redo() {
		execute();
	}

	public void undo() {
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setFilled(oldStates.get(i));
		}
	}

}
