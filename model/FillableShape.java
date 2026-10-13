package model;

import java.awt.Point;

/**
 * A shape that can be drawn either filled or as an outline.
 */
public abstract class FillableShape extends Shape {

	private boolean filled;

	public FillableShape(int xpos, int ypos, boolean filled) {
		super(new Point(xpos, ypos));
		this.filled = filled;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean f) {
		filled = f;
		fireShapeChanged();
	}

}
