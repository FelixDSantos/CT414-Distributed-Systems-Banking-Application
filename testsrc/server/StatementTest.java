package server;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class StatementTest {
    private Account acc1;

    @Before
    public void setUp() throws Exception {
        acc1 =new Account("user1","12");
        Transaction transac1 = new Transaction(acc1,"food");
        Transaction transac2 = new Transaction(acc1,"clothes");
        Transaction transac3 = new Transaction(acc1,"electricity");
        Transaction transac4 = new Transaction(acc1,"gas");
        acc1.addTransaction(transac1);
        acc1.addTransaction(transac2);
        acc1.addTransaction(transac3);
        acc1.addTransaction(transac4);

    }

    @Test
    public void getTransations() throws Exception {

        Statement state = new Statement(acc1, new Date("02/12/2017"), new Date("02/13/2017"));
        List<Transaction> transactions = state.getTransations();
        assertEquals(transactions.size(),4);
    }

}