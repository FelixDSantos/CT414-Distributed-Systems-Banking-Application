package server;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by I320246 on 07/02/2017.
 */
public class Session extends TimerTask implements Serializable{

    private int timeAlive;
    private Timer timer;
    private volatile boolean alive;
    private Account account;
    public long sessionId;

    private static final int MAX_SESSION_LENGTH = 60 * 5;
    private static final long DELAY = 1000;

    public Session(Account account) {
        this.sessionId = (int)(Math.random()*9000)+1000;
        this.account = account;
        this.alive = true;
        this.timeAlive = 0;
        this.timer = new Timer();
        this.startTimer();
        System.out.println(">> Session " + sessionId + " created\n");
    }

    private void startTimer() {
        this.timer.scheduleAtFixedRate(this, new Date(System.currentTimeMillis()), DELAY);
    }

    @Override
    public void run() {
        this.timeAlive++;
        if(this.timeAlive == MAX_SESSION_LENGTH) {
            this.alive = false;
            this.timer.cancel();
            System.out.println("\n---------------------------\nSession " + this.sessionId + " terminated \n---------------------------");
            System.out.println(this);
            System.out.println("---------------------------");
        }
    }

    public boolean isAlive() {
        return this.alive;
    }

    public long getClientId(){
        return this.sessionId;
    }

    public int getTimeAlive(){
        return this.timeAlive;
    }

    public int getMaxSessionLength(){
        return MAX_SESSION_LENGTH;
    }

    public Account getAccount(){
        return this.account;
    }

    @Override
    public String toString() {
        return "Account: " + this.account.getAccountNumber() + "\nSessionID: " +
                this.sessionId +"\nTime Alive: " + this.timeAlive + "\nAlive: " + this.alive;
    }
}