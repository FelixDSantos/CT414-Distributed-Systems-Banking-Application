package server;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by I320246 on 31/01/2017.
 */
public class Transaction implements Serializable {

    private Date date;
    private String type;
    private double amount;
    private int accountNumber;

    public Transaction(int acnum, String type) {
        this.accountNumber = acnum;
        this.type = type;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return this.date.toString() + " " + this.type + " " + this.amount;
    }
}
