package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.Date;

/**
 * The like notification for habitevent
 */

public class LikeNotification extends Notification {

    public LikeNotification(){
        super();
    }

    public LikeNotification(String username, Date date){
        super(username, date);
    }

}
