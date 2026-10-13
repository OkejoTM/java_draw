package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The set of currently selected shapes of a drawing. The selection is itself a
 * subscriber of the drawing: shapes removed from the drawing (delete, undo,
 * new drawing) are dropped from the selection automatically.
 */
public class Selection implements Iterable<Shape>, DrawingListener {

	private final List<Shape> selected = new ArrayList<Shape>();
	private final List<SelectionListener> listeners = new ArrayList<SelectionListener>();

	public Selection(Drawing drawing) {
		drawing.addDrawingListener(this);
	}

	public void addSelectionListener(SelectionListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeSelectionListener(SelectionListener l) {
		listeners.remove(l);
	}

	private void fireSelectionChanged() {
		for (SelectionListener l : new ArrayList<SelectionListener>(listeners)) {
			l.selectionChanged(this);
		}
	}

	public void add(Shape s) {
		if (!selected.contains(s)) {
			selected.add(s);
			fireSelectionChanged();
		}
	}

	public void clear() {
		if (!selected.isEmpty()) {
			selected.clear();
			fireSelectionChanged();
		}
	}

	@Override
	public void drawingChanged(DrawingEvent event) {
		switch (event.getType()) {
			case SHAPE_REMOVED:
				remove(event.getShape());
				break;
			case DRAWING_RESET:
				clear();
				break;
			default:
				break;
		}
	}

	public boolean contains(Shape s) {
		return selected.contains(s);
	}

	public boolean isEmpty() {
		return selected.isEmpty();
	}

	/**
	 * Read-only iterator: the selection can only be changed through add() and
	 * clear(), which notify the subscribers.
	 */
	@Override
	public Iterator<Shape> iterator() {
		return Collections.unmodifiableList(selected).iterator();
	}

	public void remove(Shape s) {
		if (selected.remove(s)) {
			fireSelectionChanged();
		}
	}

	public int size() {
		return selected.size();
	}

	/**
	 * Returns an independent copy of the selected shapes. Undoable actions
	 * keep such a copy because the selection itself keeps changing.
	 */
	public List<Shape> toList() {
		return new ArrayList<Shape>(selected);
	}

	@Override
	public String toString() {
		return "Selection" + selected;
	}
}
