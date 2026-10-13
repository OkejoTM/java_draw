package model;

public class Rectangle extends FillableShape {

	public Rectangle(int x, int y, boolean filled) {
		super(x, y, filled);
	}

	@Override
	public void accept(ShapeVisitor visitor) {
		visitor.visitRectangle(this);
	}

}
