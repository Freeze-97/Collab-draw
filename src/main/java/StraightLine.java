/**
 * Straight line to be drawn
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2012-12-13
 */

import java.awt.*;

public class StraightLine extends Shape implements Drawable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int thickness;
	
	public StraightLine(double x, double y, String color, int thicknessLevel) {
		super(x, y, color);
		thickness = thicknessLevel;
	}
	
	public StraightLine(Point p, String color, int thicknessLevel) {
		super(p, color);
		thickness = thicknessLevel;
	}
	
	public StraightLine() {}
	

	@Override
	public void draw() {
		System.out.println(toString());
	}

	@Override
	public void draw(Graphics g) throws MissingEndpointException {
		int x1 = (int) points.get(0).getX();
		int y1 = (int) points.get(0).getY();
		int x2 = (int) points.get(1).getX();
		int y2 = (int) points.get(1).getY();
		
		Graphics2D g2 = (Graphics2D)g;
		
		// Draw straight line
		String c = getColor();
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(getColorObject(c)); // Function take string color and returns Color object
		g2.drawLine(x1, y1, x2, y2);
	}

	@Override
	double getCircumference() throws MissingEndpointException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	double getArea() throws MissingEndpointException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
