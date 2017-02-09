package exceptions;

/**
 * Created by david on 09/02/2017.
 */
public class InvalidArgumentException extends Exception{
    public InvalidArgumentException() {
        super("Invalid command line arguments entered");
    }
}
