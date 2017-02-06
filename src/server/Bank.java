package server;

import client.Account;
import client.Statement;
import client.Transaction;
import exceptions.InvalidLoginException;
import exceptions.InvalidSessionException;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank extends UnicastRemoteObject implements BankInterface {

    private List<Account> accounts; // users accounts
    private  List<Transaction> transactions;

    public Bank() throws RemoteException
    {
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();

        Account acc1 = new Account("user1", "pass1");
        Account acc2 = new Account("user2", "pass2");
        Account acc3 = new Account("user3", "pass3");

        accounts.add(acc1);
        accounts.add(acc2);
        accounts.add(acc3);
    }

    public static void main(String args[]) throws Exception {

        // initialise Bank server - see sample code in the notes for details
        try {
            System.setSecurityManager(new RMISecurityManager());
            System.out.println("Security Manager Set.");
            Bank bankserver = new Bank();
//            Registry reg = LocateRegistry.getRegistry();
            BankInterface bank = new Bank();
//            reg.bind("Bank", bank);

            Naming.rebind("Bank", bankserver);
            System.out.println("Instance of Bank Server Created");
            System.out.println("Server Ready");
        } catch (Exception e) {

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
                // TODO:Figure out how that works
            }
            else {
                throw new InvalidLoginException();
            }
        }
        return 0;
    }


    @Override
    public void deposit(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        // TODO: session stuff

        for(Account acc : accounts){
            if(accountnum == acc.getAccountNumber()) {
                acc.setBalance(acc.getBalance() + amount);

                // TODO: Create transaction object
                Transaction t = new Transaction();
                transactions.add(t);
                break;
            }
            else {
                System.out.println("Account does not exist");
            }
        }
    }

    @Override
    public void withdraw(int accountnum, int amount, long sessionID) throws RemoteException, InvalidSessionException {
        for(Account acc : accounts) {
            if(accountnum == acc.getAccountNumber()) {
                if(acc.getBalance() > 0 && acc.getBalance() - amount > 0) {
                    acc.setBalance(acc.getBalance() - amount);

                    Transaction t = new Transaction();
                    transactions.add(t);
                    break;
                }
                else {
                    System.out.println("Insfufficient Funds");
                }
            }
            else {
                System.out.println("Account does not exist");
            }
        }
    }

    @Override
    public double inquiry(int accountnum, long sessionID) throws RemoteException, InvalidSessionException {
        for(Account acc : accounts) {
            if(acc.getAccountNumber() == accountnum) {
                return acc.getBalance();
            }
        }
        return 0;
    }

    @Override
    public Statement getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSessionException {
        return null;
    }
}