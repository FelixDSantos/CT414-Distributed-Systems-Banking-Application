package server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by I320246 on 07/02/2017.
 */
public class Session extends TimerTask {

    private String clientId;
    private int timeAlive;
    private Timer timer;
    private volatile boolean alive;

    private static final int MAX_SESSION_LENGTH = 60 * 5;
    private static final long DELAY = 1000;

    public Session(String clientId) {
        this.clientId = clientId;
        this.alive = true;
        this.timeAlive = 0;
        this.timer = new Timer();
        this.startTimer();
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
        }
    }

    public boolean isAlive() {
        return this.alive;
    }

    public String getClientId(){
        return this.clientId;
    }

    @Override
    public String toString() {
        return "Client ID: " + this.clientId +"\nTime Alive: " + this.timeAlive + "\nAlive: " +this.alive;
    }
}