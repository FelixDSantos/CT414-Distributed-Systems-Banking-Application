package client;

import exceptions.*;
import interfaces.BankInterface;
import server.Account;
import server.Statement;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class ATM {
    static int serverAddress, serverPort, account, amount;
    static String operation, username, password;
    static BankInterface bank;
    static Date startDate, endDate;
    static long sessionId = 0;

    public static void main (String args[]) {
        try {
            getCommandLineArguments(args);
            String name = "Bank";
            Registry registry = LocateRegistry.getRegistry(serverAddress);
            bank = (BankInterface) registry.lookup(name);
            System.out.println("\n--------------------------\nClient Connected" + "\n--------------------------\n");
        } catch (InvalidArgumentException ie){
            ie.printStackTrace();
            System.out.println(ie);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        double balance;
        switch (operation){
            case "login":
                try {
                    sessionId = bank.login(username, password);
                    Account acc = bank.accountDetails(sessionId);
                    System.out.println("--------------------------\nAccount Details:\n--------------------------\n\n" + "Account Number: " + acc.getAccountNumber() +
                                       "\nUsername: " + acc.getUserName() + "\nSessionID: " + sessionId + "\n");
                    System.out.println("--------------------------\n");
                    System.out.println("Use SessionID " + sessionId + " for all other operations");
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidLoginException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
                break;
            case "deposit":
                try {
                    balance = bank.deposit(account, amount, account);
                    System.out.println("Successfully deposited E" + amount + " into account " + account);
                    System.out.println("New balance: E" + balance);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "withdraw":
                try {
                    balance = bank.withdraw(account, amount, account);
                    System.out.println("Successfully withdrew E " + amount + " from account " + account +
                                       "\nRemaining Balance: E" + balance);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "inquiry":
                try {
                    Account acc = bank.inquiry(account,account);
                    System.out.println("Balance for Account " + acc.getAccountNumber() + ": E" + acc.getBalance());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "statement":
                Statement s = null;
                try {
                    s = (Statement) bank.getStatement(account, startDate, endDate, account);
                    System.out.println(s.getTransations());
                    for(Object t : s.getTransations()) {
                        System.out.println(t);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    System.out.println(e.getMessage());
                } catch (StatementException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Operation not supported");
                break;
        }
    }

    public static void getCommandLineArguments(String args[]) throws InvalidArgumentException{
        if(args.length < 4) {
            throw new InvalidArgumentException();
        }
        operation = args[2];
        switch (operation){
            case "login":
                username = args[3];
                password = args[4];
                break;
            case "withdraw":
            case "deposit":
                amount = Integer.parseInt(args[4]);
                account = Integer.parseInt(args[3]);
                break;
            case "inquiry":
                account = Integer.parseInt(args[3]);
                break;
            case "statement":
                account = Integer.parseInt(args[3]);
                startDate = new Date(args[4]);
                endDate = new Date(args[5]);
                break;
        }
    }
}