package model;

/**
 * Empty ShapeVisitor implementation. Rectangles and circles are also passed
 * to visitFillable(), so a subclass interested only in fillable shapes needs
 * to override a single method.
 */
public abstract class ShapeVisitorAdapter implements ShapeVisitor {

	public void visitLine(Line line) {
	}

	public void visitRectangle(Rectangle rectangle) {
		visitFillable(rectangle);
	}

	public void visitCircle(Circle circle) {
		visitFillable(circle);
	}

	public void visitText(Text text) {
	}

	public void visitFillable(FillableShape shape) {
	}
}
