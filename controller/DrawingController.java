package controller;

import io.DrawingFile;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import model.Circle;
import model.Drawing;
import model.Line;
import model.Rectangle;
import model.Selection;
import model.Shape;
import model.Text;
import actions.AddAction;
import actions.ColorAction;
import actions.DeleteAction;
import actions.DrawAction;
import actions.FillAction;
import actions.MoveAction;
import actions.UndoManager;

/**
 * Changes the model (Drawing and Selection) on behalf of the user interface.
 * Every undoable change goes through an action recorded in the UndoManager.
 * The controller does not know the GUI and never repaints anything: the
 * views are notified by the model itself.
 */
public class DrawingController {

	private final Drawing drawing;
	private final Selection selection;
	private final UndoManager undoManager;
	private Tool tool;

	public DrawingController(Drawing drawing, Selection selection) {
		this.drawing = drawing;
		this.selection = selection;
		this.undoManager = new UndoManager();
		this.tool = Tool.LINE;
	}

	private void perform(DrawAction action) {
		action.execute();
		undoManager.addAction(action);
	}

	public void addShape(Shape s) {
		perform(new AddAction(drawing, s));
	}

	/**
	 * Creates a new shape of the current tool at the given point and adds it
	 * to the drawing. Returns null for tools that do not draw a shape this way
	 * (SELECT and TEXT).
	 */
	public Shape startShape(Point p, Color color, boolean filled) {
		Shape s;
		switch (tool) {
			case RECTANGLE:
				s = new Rectangle(p.x, p.y, filled);
				break;
			case CIRCLE:
				s = new Circle(p.x, p.y, filled);
				break;
			case LINE:
				s = new Line(p.x, p.y);
				break;
			default:
				return null;
		}
		s.setColor(color);
		addShape(s);
		return s;
	}

	/**
	 * Changes the second point of a shape that is being drawn.
	 */
	public void resizeShape(Shape s, Point p) {
		s.setPoint2(p);
	}

	public void addText(Point p, String text, int fontSize, Color color) {
		Shape s = new Text(p.x, p.y, fontSize, text);
		s.setColor(color);
		addShape(s);
	}

	public void clearSelection() {
		selection.clear();
	}

	public void colorSelectedShapes(Color c) {
		if (!selection.isEmpty()) {
			perform(new ColorAction(selection.toList(), c));
		}
	}

	public void deleteSelectedShapes() {
		if (!selection.isEmpty()) {
			perform(new DeleteAction(drawing, selection.toList()));
		}
	}

	/**
	 * Moves the selected shapes while the mouse is dragged. The movement is
	 * not recorded; call recordMovement() when the drag ends.
	 */
	public void dragSelectedShapes(int dx, int dy) {
		for (Shape s : selection) {
			s.move(dx, dy);
		}
	}

	public Drawing getDrawing() {
		return drawing;
	}

	public Selection getSelection() {
		return selection;
	}

	public Tool getTool() {
		return tool;
	}

	public void moveSelectedShapes(Point movement) {
		if (!selection.isEmpty()) {
			perform(new MoveAction(selection.toList(), movement));
		}
	}

	public void newDrawing(int width, int height) {
		undoManager.clear();
		drawing.reset(width, height);
	}

	public void open(File f) throws IOException {
		undoManager.clear();
		DrawingFile.load(f, drawing);
	}

	/**
	 * Records a movement that has already been done with
	 * dragSelectedShapes(), so that it can be undone.
	 */
	public void recordMovement(Point movement) {
		if (!selection.isEmpty()) {
			undoManager.addAction(new MoveAction(selection.toList(), movement));
		}
	}

	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}

	public void save(File f) throws IOException {
		DrawingFile.save(f, drawing);
	}

	public void selectAll() {
		selection.clear();
		for (Shape sh : drawing) {
			selection.add(sh);
		}
	}

	/**
	 * Selects the topmost shape at the given point.
	 * 
	 * @param extend
	 *            true to add the shape to the current selection (shift is
	 *            down), false to replace the selection
	 * @return the newly selected shape, or null if no new shape was selected
	 */
	public Shape selectAt(Point p, boolean extend) {
		Shape s = drawing.getShapeAt(p);
		if (!extend && !selection.contains(s)) {
			selection.clear();
		}
		if ((s != null) && !selection.contains(s)) {
			selection.add(s);
			return s;
		}
		return null;
	}

	public void setFilledSelectedShapes(boolean filled) {
		FillAction fill = new FillAction(selection.toList(), filled);
		if (!fill.isEmpty()) {
			perform(fill);
		}
	}

	public void setTool(Tool t) {
		this.tool = t;
	}

	public void undo() {
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
	}
}
