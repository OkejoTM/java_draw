package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The drawing: an ordered list of shapes (the first one is at the bottom)
 * and the size of the canvas. This is a pure model class: it knows nothing
 * about Swing. Views subscribe to it with addDrawingListener() (Publisher -
 * Subscriber) and go through its shapes with an Iterator.
 */
public class Drawing implements Iterable<Shape>, ShapeListener {

	/**
	 * Iterator over the shapes from bottom to top. It does not support
	 * remove(): shapes may only be removed through removeShape(), so that
	 * subscribers are notified. It is fail-fast: modifying the drawing during
	 * the iteration throws ConcurrentModificationException.
	 */
	private class ShapeIterator implements Iterator<Shape> {

		private int next = 0;
		private final int expectedModCount = modCount;

		@Override
		public boolean hasNext() {
			checkForModification();
			return next < shapes.size();
		}

		@Override
		public Shape next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return shapes.get(next++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"Use Drawing.removeShape() instead");
		}

		private void checkForModification() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

	private final List<Shape> shapes = new ArrayList<Shape>();
	private final List<DrawingListener> listeners = new ArrayList<DrawingListener>();

	private int width;
	private int height;
	private int modCount = 0;

	public Drawing(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void addDrawingListener(DrawingListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeDrawingListener(DrawingListener l) {
		listeners.remove(l);
	}

	private void fire(DrawingEvent.Type type, Shape s) {
		DrawingEvent e = new DrawingEvent(this, type, s);
		for (DrawingListener l : new ArrayList<DrawingListener>(listeners)) {
			l.drawingChanged(e);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Returns the topmost shape that includes the given point.
	 * 
	 * @return the shape or null if there is no shape at the point
	 */
	public Shape getShapeAt(Point p) {
		for (int i = shapes.size() - 1; i >= 0; i--) {
			if (shapes.get(i).includes(p)) {
				return shapes.get(i);
			}
		}
		return null;
	}

	public int indexOf(Shape s) {
		return shapes.indexOf(s);
	}

	public void insertShape(Shape s) {
		insertShape(shapes.size(), s);
	}

	/**
	 * Inserts a shape at the given position of the z-order (0 = bottom).
	 */
	public void insertShape(int index, Shape s) {
		shapes.add(index, s);
		modCount++;
		s.addShapeListener(this);
		fire(DrawingEvent.Type.SHAPE_ADDED, s);
	}

	@Override
	public Iterator<Shape> iterator() {
		return new ShapeIterator();
	}

	/**
	 * Moves the shape one step down (towards the bottom of the drawing).
	 */
	public void lower(Shape s) {
		int index = shapes.indexOf(s);
		if (index > 0) {
			swap(index, index - 1);
		}
	}

	/**
	 * Moves the shape one step up (towards the top of the drawing).
	 */
	public void raise(Shape s) {
		int index = shapes.indexOf(s);
		if ((index >= 0) && (index < shapes.size() - 1)) {
			swap(index, index + 1);
		}
	}

	private void swap(int i, int j) {
		Shape s = shapes.get(i);
		shapes.set(i, shapes.get(j));
		shapes.set(j, s);
		modCount++;
		fire(DrawingEvent.Type.ORDER_CHANGED, s);
	}

	public void removeShape(Shape s) {
		if (shapes.remove(s)) {
			modCount++;
			s.removeShapeListener(this);
			fire(DrawingEvent.Type.SHAPE_REMOVED, s);
		}
	}

	/**
	 * Removes all shapes and changes the size of the drawing.
	 */
	public void reset(int width, int height) {
		for (Shape s : shapes) {
			s.removeShapeListener(this);
		}
		shapes.clear();
		modCount++;
		this.width = width;
		this.height = height;
		fire(DrawingEvent.Type.DRAWING_RESET, null);
	}

	/**
	 * A shape of this drawing has changed: the drawing republishes the change
	 * to its own subscribers.
	 */
	@Override
	public void shapeChanged(Shape shape) {
		fire(DrawingEvent.Type.SHAPE_CHANGED, shape);
	}

	public int size() {
		return shapes.size();
	}

}
