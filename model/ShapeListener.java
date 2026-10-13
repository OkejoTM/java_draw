package model;

/**
 * Subscriber (Observer) of a single Shape. Is notified every time the
 * geometry, color or another property of the shape changes.
 */
public interface ShapeListener {

	void shapeChanged(Shape shape);

}
