package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Drawing;
import model.DrawingEvent;
import model.DrawingListener;
import model.Selection;
import controller.DrawingController;

/**
 * Graphical user interface for the Drawing editor "Draw"
 * 
 * @author Alex Lagerstedt
 * 
 */

public class DrawGUI extends JFrame implements DrawingListener {

	public class StatusBar extends JLabel {

		private static final long serialVersionUID = 0;

		public StatusBar() {
			super();
			super.setPreferredSize(new Dimension(100, 16));
			setMessage("Ready");
		}

		public void setMessage(String message) {
			setText(" " + message);
		}
	}

	private DrawingController controller;
	private DrawingPanel drawingPanel;
	private ToolBox tools;
	private JScrollPane scrollpane;

	// private StatusBar statusBar;

	private static final long serialVersionUID = 0;

	public static void main(String[] args) {

		new DrawGUI();

	}

	/**
	 * Constructs a new graphical user interface for the program and shows it.
	 */
	public DrawGUI() {

		this.setTitle("Draw 0.2");
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/logo.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Model
		Drawing drawing = new Drawing(500, 380);
		Selection selection = new Selection(drawing);

		// Controller
		controller = new DrawingController(drawing, selection);

		// Views
		drawingPanel = new DrawingPanel(drawing, selection);
		tools = new ToolBox(controller);

		MouseListener mouse = new MouseListener(controller, tools);
		drawingPanel.addMouseListener(mouse);
		drawingPanel.addMouseMotionListener(mouse);

		// A simple container that keeps the drawing centered.
		JPanel drawingContainer = new JPanel(new GridBagLayout());
		drawingContainer.add(drawingPanel);
		scrollpane = new JScrollPane(drawingContainer);
		updateScrollPaneSize();

		drawing.addDrawingListener(this);

		// statusBar = new StatusBar();

		getContentPane().add(tools, BorderLayout.WEST);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
		// getContentPane().add(statusBar, BorderLayout.SOUTH);

		MenuListener mainMenuListener = new MenuListener(controller,
				drawingPanel);
		JMenuBar mainMenu = new MainMenu(mainMenuListener);
		this.setJMenuBar(mainMenu);

		pack();
		setVisible(true);

	}

	/**
	 * Resizes the window when a new drawing with another size is started.
	 */
	@Override
	public void drawingChanged(DrawingEvent event) {
		if (event.getType() == DrawingEvent.Type.DRAWING_RESET) {
			updateScrollPaneSize();
			pack();
		}
	}

	private void updateScrollPaneSize() {
		Dimension d = drawingPanel.getPreferredSize();
		scrollpane.setPreferredSize(new Dimension(d.width + 100,
				d.height + 100));
	}
}
