package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.Date;

/**
 * The class for the follow notification
 */

public class FollowNotification extends Notification {

    public FollowNotification(){
        super();
    }

    public FollowNotification(String username, Date date){
        super(username, date);
    }

}
