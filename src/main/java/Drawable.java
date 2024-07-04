/**
 * This is just an interface which other classes can inherit, those who can draw
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-11-17
 */

public interface Drawable {
	void draw();
	void draw(java.awt.Graphics g) throws MissingEndpointException;
}