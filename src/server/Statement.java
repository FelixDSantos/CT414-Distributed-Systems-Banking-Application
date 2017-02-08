package server;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by I320246 on 31/01/2017.
 */
public class Statement implements StatementInterface, Serializable {
    @Override
    public int getAccountnum() {
        return 0;
    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public Date getEndDate() {
        return null;
    }

    @Override
    public String getAccoutName() {
        return null;
    }

    @Override
    public List getTransations() {
        return null;
    }
}
