package model;

import java.awt.Point;

public class Line extends Shape {

	public Line(int x, int y) {
		super(new Point(x, y));
	}

	@Override
	public void accept(ShapeVisitor visitor) {
		visitor.visitLine(this);
	}

}
