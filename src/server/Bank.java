package server;

import exceptions.InvalidLoginException;
import exceptions.InvalidSessionException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank extends UnicastRemoteObject implements BankInterface {

    private List<Account> accounts; // users accounts
    private  List<Transaction> transactions;
    private List<Session> sessions, deadSessions;

    public Bank() throws RemoteException
    {
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        sessions = new ArrayList<>();
        deadSessions = new ArrayList<>();

        accounts.add(new Account("user1", "pass1"));
        accounts.add(new Account("user2", "pass2"));
        accounts.add(new Account("user3", "pass3"));
    }

    public static void main(String args[]) throws Exception {
        try {
            System.setSecurityManager(new SecurityManager());
            System.out.println("Security Manager Set.");
            Bank bankserver = new Bank();
            Naming.rebind("Bank", bankserver);
            System.out.println("Server Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void test(){
        System.out.println("TEST CALLED");
    }

    @Override
    public long login(String username, String password) throws RemoteException, InvalidLoginException {
        for(Account acc : accounts) {
            if(username.equals(acc.getUserName()) && password.equals(acc.getPassword())){
                // Create session here
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
            for (Account acc : accounts) {
                if (accountnum == acc.getAccountNumber()) {
                    acc.setBalance(acc.getBalance() + amount);

                    // TODO: Create transaction object
                    Transaction t = new Transaction();
                    transactions.add(t);
                    break;
                } else {
                    System.out.println("Account does not exist");
                }
            }
        }
    }

    @Override
    public void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            for (Account acc : accounts) {
                if (accountnum == acc.getAccountNumber()) {
                    if (acc.getBalance() > 0 && acc.getBalance() - amount > 0) {
                        acc.setBalance(acc.getBalance() - amount);

                        Transaction t = new Transaction();
                        transactions.add(t);
                        break;
                    } else {
                        System.out.println("Insfufficient Funds");
                    }
                } else {
                    System.out.println("Account does not exist");
                }
            }
        }
    }

    @Override
    public double inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException {
        if(checkSessionActive(accountnum)) {
            for (Account acc : accounts) {
                if (acc.getAccountNumber() == accountnum) {
                    return acc.getBalance();
                }
            }
        }
        return 0;
    }

    @Override
    public Statement getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException {
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