package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import model.Circle;
import model.Line;
import model.Rectangle;
import model.Shape;
import model.ShapeVisitor;
import model.Text;

/**
 * Paints shapes of the model on a Graphics2D. All Java2D code that used to be
 * inside the shape classes lives here now.
 */
public class ShapePainter implements ShapeVisitor {

	private static final Color SELECTION_COLOR = new Color(255, 0, 255);

	private final Graphics2D g;

	public ShapePainter(Graphics2D g) {
		this.g = g;
	}

	public void paint(Shape s) {
		g.setColor(s.getColor());
		g.setStroke(new BasicStroke((float) s.getStrokeWidth()));
		s.accept(this);
	}

	public void paintSelectionIndicator(Shape s) {

		g.setStroke(new BasicStroke(1.0f));
		g.setColor(SELECTION_COLOR);

		int len = 10;
		int off = 5;

		Point p1 = s.getPosition();
		Point p2 = new Point(p1.x + s.getSize().x, p1.y + s.getSize().y);

		g.drawPolyline(
				// left up
				new int[] { p1.x - off, p1.x - off, p1.x - off + len },
				new int[] { p1.y - off + len, p1.y - off, p1.y - off }, 3);

		g.drawPolyline(
				// right down
				new int[] { p2.x + off - len, p2.x + off, p2.x + off },
				new int[] { p2.y + off, p2.y + off, p2.y + off - len }, 3);

		g.drawPolyline(
				// right up
				new int[] { p2.x + off - len, p2.x + off, p2.x + off },
				new int[] { p1.y - off, p1.y - off, p1.y - off + len }, 3);

		g.drawPolyline(
				// left down
				new int[] { p1.x - off, p1.x - off, p1.x - off + len },
				new int[] { p2.y + off - len, p2.y + off, p2.y + off }, 3);

	}

	public void visitLine(Line line) {
		Point p1 = line.getPoint1();
		Point p2 = line.getPoint2();
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	public void visitRectangle(Rectangle r) {
		Point p = r.getPosition();
		Point size = r.getSize();
		if (r.isFilled()) {
			g.fillRect(p.x, p.y, size.x, size.y);
		}
		else {
			g.drawRect(p.x, p.y, size.x, size.y);
		}
	}

	public void visitCircle(Circle c) {
		Point p = c.getPosition();
		Point size = c.getSize();
		if (c.isFilled()) {
			g.fillOval(p.x, p.y, size.x, size.y);
		}
		else {
			g.drawOval(p.x, p.y, size.x, size.y);
		}
	}

	public void visitText(Text t) {
		Point p = t.getPoint1();
		g.setFont(t.getFont());
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString(t.getText(), p.x, p.y);
	}
}
