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

public class Bank extends UnicastRemoteObject implements BankInterface {

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
            System.out.println("\n--------------------\nSecurity Manager Set");
            String name = "Bank";
            BankInterface bank = new Bank();
            Registry registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
            registry.rebind(name, bank);
            System.out.println("Bank Server Bound");
            System.out.println("Server Stared\n--------------------\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLoginException {
        for(Account acc : accounts) {
            if(username.equals(acc.getUserName()) && password.equals(acc.getPassword())){
                System.out.println(">> Account " + acc.getAccountNumber() + " logged in");
                Session s = new Session(String.valueOf(acc.getAccountNumber()), acc);
                sessions.add(s);
                return Long.parseLong(s.getClientId());
            }
        }
        throw new InvalidLoginException();
    }

    @Override
    public double deposit(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            Account account;
            try {
                account = getAccount(accountnum);
                account.setBalance(account.getBalance() + amount);
                Transaction t = new Transaction(accountnum, "Deposit");
                t.setAmount(amount);
                t.setDate(new Date(System.currentTimeMillis()));
                account.addTransaction(t);

                System.out.println(">> E" + amount + " deposited to account " + accountnum + "\n");

                return account.getBalance();
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public double withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                if (account.getBalance() > 0 && account.getBalance() - amount > 0) {
                    account.setBalance(account.getBalance() - amount);

                    Transaction t = new Transaction(accountnum, "Withdrawal");
                    t.setAmount(accountnum);
                    t.setDate(new Date(System.currentTimeMillis()));
                    account.addTransaction(t);

                    System.out.println(">> E" + amount + " withdrawn from account " + accountnum + "\n");

                    return account.getBalance();
                }
                else System.out.println("Insufficient Funds");
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public Account inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                System.out.println(">> Balance requested for account " + accountnum + "\n");
                return account;
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Statement getStatement(int accountnum, Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException, StatementException {
        if(checkSessionActive(accountnum)) {
            try {
                Account account = getAccount(accountnum);
                Statement s = new Statement(account, from, to);
                System.out.println(account.getTransactions().size());
                return s;
            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }
        }
        throw new StatementException("Could not generate statement for given account and dates");
    }

    @Override
    public Account accountDetails(long sessionID) throws InvalidSessionException {
        for(Session s:sessions){
            if(Long.valueOf(s.getClientId()) == sessionID){
                return s.getAccount();
            }
        }
        throw new InvalidSessionException();
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
            if(s.getClientId().equals(accNum) && s.isAlive()) {
                System.out.println(">> Session " + s.getClientId() + " running for " + s.getTimeAlive() + "s");
                System.out.println(">> Time Remaining: " + (s.getMaxSessionLength() - s.getTimeAlive()) + "s");
                return true;
            }
            if(!s.isAlive()) {
                System.out.println("\n>> Cleaning up timed out sessions");
                System.out.println(">> SessionID: " + s.getClientId());
                deadSessions.add(s);
            }
        }
        System.out.println();
        
        // cleanup dead sessions
        sessions.removeAll(deadSessions);
        throw new InvalidSessionException();
    }
}