package server;

import exceptions.*;
import interfaces.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank implements BankInterface {

    private List<Account> accounts; // users accounts
    private List<Session> sessions, deadSessions;

    public Bank() throws RemoteException
    {
        super();
        accounts = new ArrayList<>();
        sessions = new ArrayList<>();
        deadSessions = new ArrayList<>();

        accounts.add(new Account("user1", "pass1"));
        accounts.add(new Account("user2", "pass2"));
        accounts.add(new Account("user3", "pass3"));
    }

    public static void main(String args[]) throws Exception {
        try {
//            System.setSecurityManager(new SecurityManager());
            System.out.println("Security Manager Set.");
            String name = "Bank";
            BankInterface bank = new Bank();
            BankInterface stub = (BankInterface) UnicastRemoteObject.exportObject(bank, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Bank server bound");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLoginException {
        for(Account acc : accounts) {
            if(username.equals(acc.getUserName()) && password.equals(acc.getPassword())){
                sessions.add(new Session(String.valueOf(acc.getAccountNumber())));
                System.out.println("Account: " + acc.getAccountNumber() + " logged in");
            }
            else {
                throw new InvalidLoginException();
            }
        }
        return 0;
    }

    @Override
    public void deposit(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                account.setBalance(account.getBalance() + amount);
                Transaction t = new Transaction(accountnum, "Deposit");
                t.setAmount(amount);
                t.setDate(new Date(System.currentTimeMillis()));
                account.addTransaction(t);
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                if (account.getBalance() > 0 && account.getBalance() - amount > 0) {
                    account.setBalance(account.getBalance() - amount);

                    Transaction t = new Transaction(accountnum, "Withdrawal");
                    t.setAmount(accountnum);
                    t.setDate(new Date(System.currentTimeMillis()));
                    account.addTransaction(t);
                }
                else System.out.println("Insufficient Funds");
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public double inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                return account.getBalance();
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public Statement getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException, StatementException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                Statement s = new Statement(account, from, to);
                return s;
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
        throw new StatementException("Could not generate statement for given account and dates");
    }

    private Account getAccount(int acnum) throws InvalidAccountException{
        for(Account acc:accounts){
            if(acc.getAccountNumber() == acnum){
                return  acc;
            }
        }
        throw new InvalidAccountException(acnum);
    }

    private boolean checkSessionActive(int acNum) throws InvalidSessionException{
        String accNum = String.valueOf(acNum);
        for(Session s : sessions){
            if(!s.isAlive()) deadSessions.add(s);
            if(s.getClientId().equals(accNum) && s.isAlive()) return true;
        }
        // cleanup dead sessions
        sessions.removeAll(deadSessions);
        throw new InvalidSessionException();
    }
}