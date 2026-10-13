package io;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import model.Circle;
import model.Drawing;
import model.Line;
import model.Rectangle;
import model.Shape;
import model.Text;

/**
 * Reads and writes drawings in the .draw text format. Errors are reported
 * with exceptions; showing them to the user is the job of the GUI.
 */
public final class DrawingFile {

	private DrawingFile() {
	}

	static Point parsePoint(String str) {
		String[] p = str.split(",");
		return new Point(Integer.parseInt(p[0].trim()),
				Integer.parseInt(p[1].trim()));
	}

	/**
	 * Replaces the contents of the drawing with the contents of the file.
	 */
	public static void load(File f, Drawing drawing) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(f));
		try {
			String header = in.readLine();
			if (header == null) {
				throw new IOException("Empty file: " + f);
			}
			Point size = parsePoint(header);
			drawing.reset(size.x, size.y);

			String str;
			int lineNumber = 1;
			while ((str = in.readLine()) != null) {
				lineNumber++;
				if (str.trim().length() == 0) {
					continue;
				}
				try {
					drawing.insertShape(parseShape(str));
				}
				catch (RuntimeException e) {
					// ArrayIndexOutOfBounds, NumberFormat, IllegalArgument
					System.err.println("Could not read line " + lineNumber
							+ " in file \"" + f + "\"");
				}
			}
		}
		finally {
			in.close();
		}
	}

	static Shape parseShape(String str) {
		String[] parts = str.split(";");
		String type = parts[0].trim();
		Point p1 = parsePoint(parts[1]);
		Point p2 = parsePoint(parts[2]);
		Shape sh;

		if (type.equals("rect")) {
			sh = new Rectangle(p1.x, p1.y, parseFill(parts[4]));
		}
		else if (type.equals("circ")) {
			sh = new Circle(p1.x, p1.y, parseFill(parts[4]));
		}
		else if (type.equals("line")) {
			sh = new Line(p1.x, p1.y);
		}
		else if (type.equals("text")) {
			int fontSize = Integer.parseInt(parts[4].trim());
			sh = new Text(p1.x, p1.y, fontSize, parts[5]);
		}
		else {
			throw new IllegalArgumentException("Unknown shape: " + type);
		}

		sh.setPoint2(p2);
		sh.setColor(new Color(Integer.parseInt(parts[3].trim())));
		return sh;
	}

	private static boolean parseFill(String s) {
		return Integer.parseInt(s.trim()) != 0;
	}

	public static void save(File f, Drawing d) throws IOException {
		ShapeWriter writer = new ShapeWriter();
		BufferedWriter out = new BufferedWriter(new FileWriter(f));
		try {
			out.write(d.getWidth() + "," + d.getHeight() + "\n");
			for (Shape s : d) {
				out.write(writer.write(s) + "\n");
			}
		}
		finally {
			out.close();
		}
	}
}
