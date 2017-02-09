package interfaces;

import exceptions.InvalidAccountException;
import exceptions.InvalidLoginException;
import exceptions.InvalidSessionException;
import exceptions.StatementException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface BankInterface extends Remote {
    long login(String username, String password) throws RemoteException, InvalidLoginException;
    void deposit(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException;
    void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException;
    double inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException;
    StatementInterface getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException, StatementException;
}