package model;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;

/**
 * A text label. point1 is the left end of the baseline; the second point is
 * always computed from the text and the font size, so the bounds of the
 * shape are known without painting it.
 */
public class Text extends Shape {

	private static final FontRenderContext FRC = new FontRenderContext(null,
			true, true);

	private final String text;
	private final Font font;

	/**
	 * Constructs a new Text shape with the given string as the text.
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param fontSize
	 *            font size
	 * @param str
	 *            the text for the shape
	 */
	public Text(int x, int y, int fontSize, String str) {
		super(new Point(x, y));
		if ((str == null) || (str.length() == 0)) {
			throw new IllegalArgumentException("Empty text");
		}
		font = new Font(null, Font.PLAIN, fontSize);
		text = str;
		updateBounds();
	}

	@Override
	public void accept(ShapeVisitor visitor) {
		visitor.visitText(this);
	}

	public Font getFont() {
		return font;
	}

	public int getFontSize() {
		return font.getSize();
	}

	public String getText() {
		return text;
	}

	/**
	 * The size of a text is defined by its content, so the second point can
	 * not be set from outside: it is recalculated instead.
	 */
	@Override
	public void setPoint1(Point p) {
		super.setPoint1(p);
		updateBounds();
	}

	@Override
	public void setPoint2(Point p) {
		updateBounds();
	}

	private void updateBounds() {
		int w = (int) Math.ceil(font.getStringBounds(text, FRC).getWidth());
		Point p1 = getPoint1();
		super.setPoint2(new Point(p1.x + w, p1.y - font.getSize()));
	}

}
