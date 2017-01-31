package exceptions;

/**
 * Created by I320246 on 25/01/2017.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException() {
        super("Your Login Details are Invalid");
    }
}
