package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.Date;

/**
 * Created by micah on 03/12/17.
 */

public class FollowNotification extends Notification {

    public FollowNotification(){
        super();
    }

    public FollowNotification(String username, Date date){
        super(username, date);
    }

}
