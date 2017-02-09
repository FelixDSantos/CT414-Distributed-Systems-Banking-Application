package client;

import interfaces.BankInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ATM {

    public static void main (String args[]) throws Exception {
        try{
            String name = "Bank";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            BankInterface bank = (BankInterface) registry.lookup(name);
            System.out.println("client connected");
        }catch(Exception e){
            e.getStackTrace();
        }
    }
}