import java.io.Serializable;
/**
 * Represents coordinate system with x and y Includes constructor with
 * parameters and an empty one
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-11-17
 */

public class Point implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double y;
	private double x;

	// Empty constructor
	public Point() {}

	// Constructor with arguments
	public Point(double x, double y) {
		this.y = y; // "this" refers to the first variables in this class
		this.x = x;
	}

	// Set and get
	void setY(double y) {
		this.y = y;
	}

	double getY() {
		return y;
	}

	void setX(double x) {
		this.x = x;
	}

	double getX() {
		return x;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}