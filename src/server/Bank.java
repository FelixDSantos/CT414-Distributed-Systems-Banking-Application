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
            Account account = getAccount(accountnum);
            if(account != null) {
                account.setBalance(account.getBalance() + amount);
                Transaction t = new Transaction(accountnum, "Deposit");
                t.setAmount(amount);
                t.setDate(new Date(System.currentTimeMillis()));
                account.addTransaction(t);
            }
        }
    }

    @Override
    public void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            Account account = getAccount(accountnum);
            if(account != null){
                if (account.getBalance() > 0 && account.getBalance() - amount > 0) {
                    account.setBalance(account.getBalance() - amount);

                    Transaction t = new Transaction(accountnum, "Withdrawal");
                    t.setAmount(accountnum);
                    t.setDate(new Date(System.currentTimeMillis()));
                    account.addTransaction(t);
                }
                else System.out.println("Insufficient Funds");
            }
        }
    }

    @Override
    public double inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            Account account = getAccount(accountnum);
            if(account != null) return account.getBalance();
        }
        return 0;
    }

    @Override
    public Statement getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            Account account = getAccount(accountnum);
            if (account != null) {
                Statement s = new Statement(account, from, to);
                System.out.println(s.getTransations());
            }
        }
        return null;
    }

    private Account getAccount(int acnum) {
        for(Account acc:accounts){
            if(acc.getAccountNumber() == acnum){
                return  acc;
            }
        }
        System.out.println("Account with account number: " + acnum + " does not exist");
        return null;
    }

    private boolean checkSessionActive(int acNum) {
        String accNum = String.valueOf(acNum);
        for(Session s : sessions){
            if(!s.isAlive()) deadSessions.add(s);
            if(s.getClientId().equals(accNum) && s.isAlive()) return true;
        }
        // cleanup dead sessions
        sessions.removeAll(deadSessions);
        System.out.println("Session deacivated, please log in again");
        return false;
    }
}