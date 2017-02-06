package client;



import server.BankInterface;

import java.rmi.Naming;

public class ATM {

    public static void main (String args[]) throws Exception {

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