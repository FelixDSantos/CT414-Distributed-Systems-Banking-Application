package exceptions;

/**
 * Created by david on 12/02/2017.
 */
public class InsufficientFundsException extends Exception{
    public InsufficientFundsException(){
        super("Insufficient Funds");
    }
}
