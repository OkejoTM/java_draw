package model;

public class Circle extends FillableShape {

	public Circle(int x, int y, boolean filled) {
		super(x, y, filled);
	}

	@Override
	public void accept(ShapeVisitor visitor) {
		visitor.visitCircle(this);
	}

}
