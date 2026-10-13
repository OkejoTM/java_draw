package model;

/**
 * Subscriber (Observer) of a Drawing. Views subscribe to the drawing and
 * redraw themselves when they receive an event, so nobody has to call
 * repaint() by hand after changing the model.
 */
public interface DrawingListener {

	void drawingChanged(DrawingEvent event);

}
