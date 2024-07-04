import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class FreeHand extends Shape implements Drawable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int thickness;
	
	public FreeHand() {
		super();
	}
	
	public FreeHand(double x, double y, String color, int thicknessLevel) {
		super(x, y, color);
		thickness = thicknessLevel;
	}
	
	public FreeHand(Point p, String color, int thicknessLevel) {
		super(p, color);
		thickness = thicknessLevel;
	}
	
	public FreeHand(Point[] p, String color, int thicknessLevel) {
		super(p[0], color);
		thickness = thicknessLevel;
		
		for(int i = 0; i < p.length; i++) {
			addPoint(p[i]);
		}
	}
	
	@Override
	public void addPoint(Point p) {
		points.add(p);
	}
	
	@Override
	public void addPoint(double x, double y) {
		this.addPoint(new Point(x, y));
	}

	@Override
	public void draw() {
		System.out.println(toString());
	}

	@Override
	public void draw(Graphics g) throws MissingEndpointException {
		Graphics2D g2 = (Graphics2D)g;
		
		// Draw 
		String c = getColor();
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(getColorObject(c)); // Function take string color and returns Color object
		
		for(int i = 0; i < points.size(); i++) {
			// Avoid error
			if(i == points.size() - 1) {
				continue;
			}
			
			g2.drawLine(
					(int) points.get(i).getX(), 
					(int) points.get(i).getY(), 
					(int) points.get(i + 1).getX(),
					(int) points.get(i + 1).getY());
		}
	}

	@Override
	double getCircumference() throws MissingEndpointException {
		return 0;
	}

	@Override
	double getArea() throws MissingEndpointException {
		return 0;
	}

}
