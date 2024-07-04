import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * <h1>DrawingPanel</h1> This class will handle drawing of different figures
 * 
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2020-02-04
 */

public class DrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Drawing drawing;

	public DrawingPanel() {
		this.drawing = new Drawing();
		setBackground(Color.white);
	}

	public DrawingPanel(Drawing drawing) {
		this.drawing = drawing;
		setBackground(Color.white);
	}

	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
	}

	public void addDrawing(Drawing drawing) {
		ArrayList<Shape> tmpList = drawing.getShapes(); // Store shapes in array

		// Add all shapes to the main drawing
		for (int i = 0; i < tmpList.size(); i++) {
			this.drawing.addShape(tmpList.get(i));
		}
	}

	public Drawing getDrawing() {
		return this.drawing;
	}
	
	public void addShape(Shape shape) {
			drawing.addShape(shape);
	}

	public void paintComponent(Graphics g) {
		if (this.drawing == null) {
			return;
		}
		try {
			super.paintComponent(g);
			this.drawing.draw(g);
			repaint();
		} catch (MissingEndpointException e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		drawing.clear();
		repaint();
	}
}
