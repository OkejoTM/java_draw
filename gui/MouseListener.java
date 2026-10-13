package gui;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import model.Shape;
import controller.DrawingController;
import controller.Tool;

/**
 * MouseListener listens to the mouse events in a drawing and modifies the
 * Drawing through a DrawingController
 * 
 * @author Alex Lagerstedt
 * 
 */

public class MouseListener extends MouseAdapter {

	private final DrawingController c;
	private final ToolBox tools;

	private Point startPos;
	private Point lastPos;

	private Shape newShape;

	/**
	 * Constructs a new MouseListener
	 * 
	 * @param c
	 *            the DrawingController through which the modifications will be
	 *            done
	 * @param t
	 *            the ToolBox
	 */
	public MouseListener(DrawingController c, ToolBox t) {
		this.tools = t;
		this.c = c;
		this.newShape = null;
		this.lastPos = new Point(0, 0);
	}

	public void mouseDragged(MouseEvent m) {

		int dx = m.getPoint().x - lastPos.x;
		int dy = m.getPoint().y - lastPos.y;

		if (newShape != null) {
			c.resizeShape(newShape, lastPos);
		}

		if (c.getTool() == Tool.SELECT) {
			c.dragSelectedShapes(dx, dy);
		}

		lastPos = m.getPoint();

	}

	public void mouseMoved(MouseEvent m) {
		lastPos = m.getPoint();
	}

	public void mousePressed(MouseEvent m) {
		startPos = m.getPoint();
		lastPos = startPos;

		Tool t = c.getTool();

		if (t == Tool.SELECT) {
			boolean shift = (m.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
			boolean firstSelected = c.getSelection().isEmpty();
			Shape selected = c.selectAt(startPos, shift);
			if (selected != null) {
				tools.showProperties(selected, firstSelected);
			}
		}
		else if (t == Tool.TEXT) {
			String text = JOptionPane.showInputDialog("Text to be inserted:");
			if ((text != null) && (text.length() > 0)) {
				c.addText(startPos, text, tools.getFontSize(), tools.getColor());
			}
		}
		else {
			newShape = c.startShape(startPos, tools.getColor(), tools.getFill());
		}

	}

	public void mouseReleased(MouseEvent m) {
		newShape = null;

		if (c.getTool() == Tool.SELECT) {

			Point total = new Point(m.getPoint().x - startPos.x, m.getPoint().y
					- startPos.y);

			if ((total.x != 0) || (total.y != 0)) {
				c.recordMovement(total);
			}

		}
	}

}
