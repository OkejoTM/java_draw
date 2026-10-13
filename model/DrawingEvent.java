package model;

/**
 * Describes a change that happened in a Drawing.
 */
public class DrawingEvent {

	public enum Type {
		SHAPE_ADDED, SHAPE_REMOVED, SHAPE_CHANGED, ORDER_CHANGED, DRAWING_RESET
	}

	private final Drawing source;
	private final Type type;
	private final Shape shape;

	public DrawingEvent(Drawing source, Type type, Shape shape) {
		this.source = source;
		this.type = type;
		this.shape = shape;
	}

	public Drawing getSource() {
		return source;
	}

	public Type getType() {
		return type;
	}

	/**
	 * @return the shape the event is about, or null for DRAWING_RESET
	 */
	public Shape getShape() {
		return shape;
	}
}
