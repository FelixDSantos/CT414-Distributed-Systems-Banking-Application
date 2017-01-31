package client;

import java.io.Serializable;

/**
 * Created by I320246 on 25/01/2017.
 */
public class Account implements Serializable {

    private double balance;
    private String password;
    private int accountNumber;
    private static int nextAcNum = 88769912;

    public Account (String uName, String pass) {
        this.userName = uName;
        this.password = pass;
        this.accountNumber = nextAcNum;
        nextAcNum++;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
