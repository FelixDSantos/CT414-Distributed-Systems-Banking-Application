package client;

import exceptions.InvalidArgumentException;
import exceptions.InvalidLoginException;
import exceptions.InvalidSessionException;
import exceptions.StatementException;
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

    public static void main (String args[]) {
        try {
            getCommandLineArguments(args);
            String name = "Bank";
            Registry registry = LocateRegistry.getRegistry(serverAddress);
            bank = (BankInterface) registry.lookup(name);
            System.out.println("client connected");
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
                    bank.login(username, password);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidLoginException e) {
                    e.printStackTrace();
                }
                break;
            case "deposit":
                try {
                    balance = bank.deposit(account, amount, account);
                    System.out.println("Successfully deposited € " + balance + " into account " + account);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
                break;
            case "withdraw":
                try {
                    balance = bank.withdraw(account, amount, account);
                    System.out.println("Successfully withdrew € " + amount + " from account " + account +
                                       "\nRemaining Balance: " + balance);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
                break;
            case "inquiry":
                try {
                    Account acc = bank.inquiry(account,account);
                    System.out.println(acc);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
                break;
            case "statement":
                Statement s = null;
                try {
                    s = (Statement) bank.getStatement(account, startDate, endDate, account);
                    for(Object t : s.getTransations()) System.out.println(t);
                    System.out.println(s);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                } catch (StatementException e) {
                    e.printStackTrace();
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
                amount = Integer.parseInt(args[3]);
                account = Integer.parseInt(args[4]);
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