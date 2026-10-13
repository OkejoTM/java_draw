package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class of all shapes of a drawing. Contains only data and behaviour of
 * the model: how a shape is painted is decided by the view (see
 * gui.ShapePainter), which visits the shape through accept().
 */
public abstract class Shape {

	private Point point1;
	private Point point2;

	private Color color;
	private double strokeWidth;

	private final List<ShapeListener> listeners = new ArrayList<ShapeListener>();

	public Shape(Point p) {
		point1 = new Point(p);
		point2 = new Point(p.x + 25, p.y + 25);
		color = Color.BLACK;
		strokeWidth = 2;
	}

	/**
	 * Calls the visitor method matching the concrete type of this shape.
	 */
	public abstract void accept(ShapeVisitor visitor);

	public void addShapeListener(ShapeListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeShapeListener(ShapeListener l) {
		listeners.remove(l);
	}

	protected void fireShapeChanged() {
		for (ShapeListener l : new ArrayList<ShapeListener>(listeners)) {
			l.shapeChanged(this);
		}
	}

	public Color getColor() {
		return color;
	}

	public Point getPoint1() {
		return new Point(point1);
	}

	public Point getPoint2() {
		return new Point(point2);
	}

	public Point getPosition() {
		return new Point(Math.min(point1.x, point2.x), Math.min(point1.y,
				point2.y));
	}

	public Point getSize() {
		return new Point(Math.abs(point2.x - point1.x), Math.abs(point2.y
				- point1.y));
	}

	public double getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Checks if the Shape contains the given point. Has a 2 pixel margin in all
	 * directions.
	 * 
	 * @param p
	 *            point to check
	 * @return true if shape includes given point
	 */
	public boolean includes(Point p) {
		Point pos = getPosition();
		Point size = getSize();
		return (p.x - pos.x > -2) && (p.x - pos.x < size.x + 2)
				&& (p.y - pos.y > -2) && (p.y - pos.y < size.y + 2);
	}

	public void move(int x, int y) {
		point1.translate(x, y);
		point2.translate(x, y);
		fireShapeChanged();
	}

	public void setColor(Color c) {
		color = c;
		fireShapeChanged();
	}

	public void setPoint1(Point p) {
		point1 = new Point(p);
		fireShapeChanged();
	}

	public void setPoint2(Point p) {
		point2 = new Point(p);
		fireShapeChanged();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + point1.x + "," + point1.y
				+ " - " + point2.x + "," + point2.y + "]";
	}

}
