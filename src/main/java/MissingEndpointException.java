

/**
 * Catches exceptions and writes an error message for the user
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-11-24
 */

public class MissingEndpointException extends Exception {
	private static final long serialVersionUID = 1L;

	// Takes the message to the "above" constructor
	public MissingEndpointException(String s) {
		super(s);
	}

	public MissingEndpointException() {}
}
