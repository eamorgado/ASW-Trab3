package wwwordz.shared;

import java.io.Serializable;

/**
 * An exception in WWWordz. All constructors delegate in the super class
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class WWWordzException extends Exception implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public WWWordzException() {
		super();
	}
	
	public WWWordzException(String message) {
		super(message);
	}
	
	public WWWordzException(String message, Throwable cause) {
		super(message, cause);
	}

	public WWWordzException(Throwable cause) {
		super(cause);
	}
}
