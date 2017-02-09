package exceptions;

/**
 * Created by david on 09/02/2017.
 */
public class InvalidAccountException extends Exception {
    public InvalidAccountException(int acnum){
        super("Account with account number: " + acnum + " does not exist");
    }
}
