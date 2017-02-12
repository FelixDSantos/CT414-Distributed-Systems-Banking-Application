package server;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by I320246 on 31/01/2017.
 */
public class Transaction implements Serializable {

    private Date date;
    private String type;
    private double amount;
    private Account account;
    private double accBalance;

    public Transaction(Account account, String type) {
        this.account = account;
        this.accBalance = 0;
        this.type = type;
        date = new Date(System.currentTimeMillis());
    }


    public Date getDate() {
        return date;
    }

    public int getAccountNumber() {
        return this.account.getAccountNumber();
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        double amt = this.account.getBalance();
        this.accBalance = this.type.equals("Deposit")?this.accBalance = amt + this.amount : amt - this.amount;
        this.amount = amount;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if(this.type.equals("Deposit"))
            return dateFormat.format(this.date) + "\t" + this.type + "\t\t\t" + this.amount + "\t\t" + this.accBalance;
        else
            return dateFormat.format(this.date) + "\t" + this.type + "\t\t" + this.amount + "\t\t" + this.accBalance;
    }
}
