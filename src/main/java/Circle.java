import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Represents Circle which is a type of the class Shape, start point is the
 * center of the circle
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-11-17
 */

public class Circle extends Shape implements Drawable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final double PI = 3.14;
	protected int thickness;

	// Call constructors
	public Circle(double x, double y, String color, int thickness) {
		super(x, y, color);
		this.thickness = thickness;
	}

	public Circle(Point p, String color, int thickness) {
		super(p, color);
		this.thickness = thickness;
	}

	public Circle() {}

	// Own methods
	double getRadius() throws MissingEndpointException {
		try {
			return Math.sqrt(Math.pow(points.get(1).getX() - points.get(0).getX(), 2)
					+ Math.pow(points.get(1).getY() - points.get(0).getY(), 2));
		}

		catch (Exception e) {
			throw new MissingEndpointException("Could not get radius, missing endpoint");
		}
	}

	// Abstract classes from Shape
	double getCircumference() throws MissingEndpointException {
		try {
			return 2 * PI * getRadius();
		}

		catch (MissingEndpointException e) {
			throw new MissingEndpointException("Could not get circumference, missing endpoint");
		}
	}

	double getArea() throws MissingEndpointException {
		try {
			return PI * getRadius() * getRadius();
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
		int width = Math.abs((int) (points.get(1).getX() - points.get(0).getX()));
		int height = Math.abs((int) (points.get(1).getY() - points.get(0).getY()));

		Graphics2D g2 = (Graphics2D)g;
		((Graphics2D) g2).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		String c = getColor();
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(getColorObject(c)); // Function take string color and returns Color object
		g2.drawOval(x, y, width, height);
	}

	public String toString() {
		try {
			return "[start=" + points.get(0) + "; end=" + points.get(1) + "; " + "radius=" + getRadius() + "; color="
					+ getColor() + "]";
		} catch (MissingEndpointException e) {
			e.getMessage();
		}

		return "[start=" + points.get(0) + "; end=N/A" + "; " + "radius=N/A" + "color=" + getColor() + "]";
	}
}