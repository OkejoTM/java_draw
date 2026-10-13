package model;

/**
 * Visitor over the Shape hierarchy. Lets code outside the model (painting,
 * saving to a file, showing properties in the GUI) handle each concrete
 * shape type without instanceof checks and without the model knowing
 * about Graphics, files or Swing.
 */
public interface ShapeVisitor {

	void visitLine(Line line);

	void visitRectangle(Rectangle rectangle);

	void visitCircle(Circle circle);

	void visitText(Text text);

}
