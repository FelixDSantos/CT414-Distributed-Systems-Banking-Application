package server;

import interfaces.StatementInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by I320246 on 31/01/2017.
 */
public class Statement implements StatementInterface, Serializable {

    private List<Transaction> relevantTransactions;
    private Date startDate, endDate;
    private Account account;

    public Statement(Account account, Date start, Date end){
        this.relevantTransactions = new ArrayList<>();
        this.account = account;
        this.startDate = start;
        this.endDate = end;
    }

    @Override
    public int getAccountnum() {
        return this.account.getAccountNumber();
    }

    @Override
    public Date getStartDate() {
        return this.startDate;
    }

    @Override
    public Date getEndDate() {
        return this.endDate;
    }

    @Override
    public String getAccoutName() {
        return account.getUserName();
    }

    @Override
    public List getTransations() {
        for(Transaction t : this.account.getTransactions()){
            if(t.getDate().equals(this.startDate) || t.getDate().after(this.startDate)){
                relevantTransactions.add(t);
            }
            else if(!t.getDate().before(this.endDate)){
                relevantTransactions.add(t);
            }
        }
        return this.relevantTransactions;
    }
}
