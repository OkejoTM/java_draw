package io;

import java.awt.Point;

import model.Circle;
import model.FillableShape;
import model.Line;
import model.Rectangle;
import model.Shape;
import model.ShapeVisitor;
import model.Text;

/**
 * Converts a Shape into a line of the .draw file format. Used instead of
 * Shape.toString(), which is now only meant for debugging.
 */
class ShapeWriter implements ShapeVisitor {

	private String result;

	public String write(Shape s) {
		s.accept(this);
		return result;
	}

	private static String point(Point p) {
		return p.x + "," + p.y;
	}

	private static String common(Shape s) {
		return point(s.getPoint1()) + ";" + point(s.getPoint2()) + ";"
				+ s.getColor().getRGB();
	}

	private static String fill(FillableShape s) {
		return s.isFilled() ? "1" : "0";
	}

	public void visitLine(Line line) {
		result = "line;" + common(line);
	}

	public void visitRectangle(Rectangle r) {
		result = "rect;" + common(r) + ";" + fill(r);
	}

	public void visitCircle(Circle c) {
		result = "circ;" + common(c) + ";" + fill(c);
	}

	public void visitText(Text t) {
		result = "text;" + common(t) + ";" + t.getFontSize() + ";"
				+ t.getText().replace(';', '?');
	}
}
