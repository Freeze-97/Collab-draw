import javax.swing.SwingUtilities;

/**
 * <h1>Assignment 7</h1> This class is the starting point for the drawing
 * application. It creates our JFrame and makes it visible.
 * 
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2020-02-04
 */

public class CollaborativeDrawing {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JavaFrame().setVisible(true);				
			}
		});
	}
}
