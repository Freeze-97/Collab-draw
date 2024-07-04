

import java.awt.Color;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * An abstract class which has all variables and methods needed for different
 * type of shapes.
 * 
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-11-17
 */

public abstract class Shape implements Drawable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Variables
	protected String color;
	protected ArrayList<Point> points = new ArrayList<Point>(2);

	// Constructors
	public Shape(double x, double y, String color) {
		this(new Point(x, y), color);
	}

	public Shape(Point p, String color) {
		points.add(0, p);
		this.color = color;
	}

	public Shape() {}

	// Set and get color
	void setColor(String color) {
		this.color = color;
	}

	String getColor() {
		return color;
	}

	ArrayList<Point> getPoints() {
		return points;
	}

	// Used by all types of shapes
	abstract double getCircumference() throws MissingEndpointException;
	abstract double getArea() throws MissingEndpointException;

	// Add point with x and y or just as object
	void addPoint(Point p) {
		points.add(1, p);
	}

	void addPoint(double x, double y) {
		addPoint(new Point(x, y));
	}

	// Get string color as Color object
	static Color getColorObject(String col) {
		Color color = null;
		switch (col.toLowerCase()) {
		case "black":
			color = Color.BLACK;
			break;
		case "blue":
			color = Color.BLUE;
			break;
		case "green":
			color = Color.GREEN;
			break;
		case "yellow":
			color = Color.YELLOW;
			break;
		case "red":
			color = Color.RED;
			break;
		case "white":
			color = Color.WHITE;
			break;
		}
		return color;
	}
}