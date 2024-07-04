
import java.awt.Graphics;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * This class handles different type of shapes and draws them for the user.
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2020-03-28
 */

public class Drawing implements Drawable, Serializable {
	private static final long serialVersionUID = 1L;
	// Variables
	private String name;
	private String author;
	private ArrayList<Shape> shapes = new ArrayList<Shape>();

	// Constructors
	public Drawing() {
		this.name = "";
		this.author = "";
	}

	public Drawing(String name, String author) {
		this.name = name;
		this.author = author;
	}

	// Set-methods
	public void setName(String name) {
		this.name = name;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	// Get-methods
	public String getName() {
		return this.name;
	}

	public String getAuthor() {
		return this.author;
	}

	public int getSize() {
		return shapes.size();
	}

	public ArrayList<Shape> getShapes() {
		return this.shapes;
	}

	public double getTotalCircumference() throws MissingEndpointException {
		
		double total = shapes.stream().mapToDouble((Shape s) -> {try {
			return s.getCircumference();
		} 
		catch (MissingEndpointException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}
		return 0;}).sum();
		return total;
	}

	public double getTotalArea() throws MissingEndpointException {
		double total = shapes.stream().mapToDouble((Shape s) -> {try {
			return s.getArea();
		} 
		catch (MissingEndpointException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}
		return 0;}).sum();
		return total;
	}

	// Add a shape to the list
	public void addShape(Shape shape) {
		if (shape == null) { // If null, don't add it to the list
			return;
		} else {
			shapes.add(shape);
		}
	}

	// Draw methods from interface Drawable
	public void draw() {
		String shapeInfo = null;
		for (int i = 0; i < getSize(); i++) {
			shapeInfo += shapes.get(i).toString() + "\n";
		}
		System.out.print("name=" + this.getName() + "; author=" + this.getAuthor() + "; " + shapeInfo);
	}

	// Clear-method
	public void clear() {
		shapes.clear();
		name = "";
		author = "";
	}

	public void draw(Graphics g) throws MissingEndpointException {
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).draw(g);
		}
	}

	// toString override
	public String toString() {
		try {
			return "[name= " + this.getName() + "; author=" + this.getAuthor() + "; size=" + this.getSize()
					+ "; circumference=" + this.getTotalCircumference() + "; area=" + this.getTotalArea() + ";]";
		} catch (Exception e) {
			e.getMessage();
		}
		return "[name=" + this.getName() + "; author=" + this.getAuthor() + "; size=0; circumference=0; area=0;]";
	}
}
