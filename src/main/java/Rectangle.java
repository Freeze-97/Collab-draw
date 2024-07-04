/**
 * Represents a Rectangle which extends the Shape class upper left point is the
 * start point and the lower right is the end point
 * 
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-11-17
 */

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Rectangle extends Shape implements Drawable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int thickness;
	
	// Call constructors
	public Rectangle(double x, double y, String color, int thickness) {
		super(x, y, color);
		this.thickness = thickness;
	}

	public Rectangle(Point p, String color, int thickness) {
		super(p, color);
		this.thickness = thickness;
	}

	public Rectangle() {}

	// Width and height, rectangles own unique methods
	double getWidth() throws MissingEndpointException {
		try {
			return (int) Math.abs(points.get(0).getX() - points.get(1).getX());
		}
		catch (Exception e) {
			throw new MissingEndpointException("Could not get width, missing endpoint");
		}
	}

	double getHeight() throws MissingEndpointException {
		try {
			return (int) Math.abs(points.get(0).getY() - points.get(1).getY());
		}
		catch (Exception e) {
			throw new MissingEndpointException("Could not get height, missing endpoint");
		}
	}

	// Abstract classes from Shape
	double getCircumference() throws MissingEndpointException {
		try {
			return getHeight() * 2 + getWidth() * 2;
		}
		catch (MissingEndpointException e) {
			throw new MissingEndpointException("Could not get circumference, missing endpoint");
		}
	}

	double getArea() throws MissingEndpointException {
		try {
			return getHeight() * getWidth();
		}
		catch (MissingEndpointException e) {
			throw new MissingEndpointException("Could not get area, missing endpoint");
		}
	}

	// Draw
	public void draw() {
		System.out.println(toString());
	}

	public void draw(Graphics g) throws MissingEndpointException {
		int x = (int) Math.min(points.get(0).getX(), points.get(1).getX());
		int y = (int) Math.min(points.get(0).getY(), points.get(1).getY());
		int width = (int) getWidth();
		int height = (int) getHeight();
		
		Graphics2D g2 = (Graphics2D)g;
		((Graphics2D) g2).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw rectangle with color
		String c = getColor();
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(getColorObject(c)); // Function take string color and returns Color object
		g2.drawRect(x, y, width, height);
	}

	public String toString() {
		// See if there is a value or not
		try {
			return "[start=" + points.get(0) + "; end=" + points.get(1) + "; " + "width=" + getWidth() + "; "
					+ "height=" + getHeight() + "; color=" + getColor() + "]";
		} catch (MissingEndpointException e) {
			e.getMessage();
		}
		return "[start=" + points.get(0) + "; end=N/A  " + "; " + "width=N/A" + "; " + "height=N/A" + "; color="
				+ getColor() + "]";
	}
}