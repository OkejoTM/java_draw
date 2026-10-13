package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Drawing;
import model.DrawingEvent;
import model.DrawingListener;
import model.Selection;
import model.SelectionListener;
import model.Shape;

/**
 * View of a Drawing. Subscribes to the drawing and to the selection and
 * repaints itself whenever one of them publishes a change.
 */
public class DrawingPanel extends JPanel implements DrawingListener,
		SelectionListener {

	private static final long serialVersionUID = 0;

	private final Drawing drawing;
	private final Selection selection;

	public DrawingPanel(Drawing drawing, Selection selection) {
		this.drawing = drawing;
		this.selection = selection;
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.WHITE);
		updateSize();

		drawing.addDrawingListener(this);
		selection.addSelectionListener(this);
	}

	@Override
	public void drawingChanged(DrawingEvent event) {
		if (event.getType() == DrawingEvent.Type.DRAWING_RESET) {
			updateSize();
		}
		repaint();
	}

	/**
	 * Renders the drawing without the selection indicators into an image.
	 */
	public BufferedImage getImage() {
		BufferedImage bi = new BufferedImage(drawing.getWidth(),
				drawing.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, drawing.getWidth(), drawing.getHeight());
		ShapePainter painter = new ShapePainter(g);
		for (Shape s : drawing) {
			painter.paint(s);
		}
		g.dispose();
		return bi;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ShapePainter painter = new ShapePainter((Graphics2D) g);
		for (Shape s : drawing) {
			painter.paint(s);
		}
		for (Shape s : selection) {
			painter.paintSelectionIndicator(s);
		}
	}

	@Override
	public void selectionChanged(Selection s) {
		repaint();
	}

	private void updateSize() {
		setPreferredSize(new Dimension(drawing.getWidth(), drawing.getHeight()));
		revalidate();
	}
}
