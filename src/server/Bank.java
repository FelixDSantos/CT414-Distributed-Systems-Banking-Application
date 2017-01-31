package server;

import client.Account;
import client.StatementInterface;
import exceptions.InvalidLogin;
import exceptions.InvalidSession;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

public class Bank extends UnicastRemoteObject implements BankInterface {

    private List<Account> accounts; // users accounts

    public Bank() throws RemoteException
    {

    }

    public static void main(String args[]) throws Exception {

        // initialise Bank server - see sample code in the notes for details

    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLogin {
        return 0;
    }

    @Override
    public void deposit(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSession {

    }

    @Override
    public void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSession {

    }

    @Override
    public int inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSession {
        return 0;
    }

    @Override
    public StatementInterface getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSession {
        return null;
    }
}