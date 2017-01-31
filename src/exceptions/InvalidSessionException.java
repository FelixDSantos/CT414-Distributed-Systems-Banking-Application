package exceptions;

/**
 * Created by I320246 on 25/01/2017.
 */
public class InvalidSessionException extends Exception {
    public InvalidSessionException() {
        super("Your session has timed out after 5 minutes of inactivity. Please Log In again");
    }
}
