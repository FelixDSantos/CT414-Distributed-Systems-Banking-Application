package client;

import server.BankInterface;

import java.rmi.Naming;
import java.util.UUID;

public class ATM {

    private String clientID;

    public ATM(String clientID) {
        this.clientID = clientID;
    }

    public static void main (String args[]) throws Exception {

        ATM atm = new ATM(UUID.randomUUID().toString());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    // get user’s input, and perform the operations
        BankInterface bank = (BankInterface) Naming.lookup("//localhost/Bank");
        try{
            bank.test();

        }catch(Exception e){
            e.getStackTrace();
        }
    }

}