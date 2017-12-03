package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.Date;

/**
 * Created by micah on 03/12/17.
 */

public abstract class Notification {
    private String username;
    private Date date;
    private boolean handled;

    public Notification(){

    }

    public Notification(String username, Date date){
        this.username = username;
        this.date = date;
        handled = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
