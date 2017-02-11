package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by I320246 on 25/01/2017.
 */
public class Account implements Serializable {

    private double balance;
    private String username, password;
    private int accountNumber;
    private static int nextAcNum = 88769912;
    private List<Transaction> transactions;

    public Account (String uName, String pass) {
        this.transactions = new ArrayList<>();
        this.username = uName;
        this.password = pass;
        this.accountNumber = nextAcNum;
        this.balance = 0;
        nextAcNum++;
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Transaction> getTransactions(){
        return this.transactions;
    }

    @Override
    public String toString() {
        return this.accountNumber + " " + this.balance;
    }
}
