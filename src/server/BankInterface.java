package server;

import client.StatementInterface;
import exceptions.InvalidLoginException;
import exceptions.InvalidSessionException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface BankInterface extends Remote {

    public long login(String username, String password) throws RemoteException, InvalidLoginException;

    public void deposit(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException;

    public void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException;

    public double inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException;

    public StatementInterface getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException;

}