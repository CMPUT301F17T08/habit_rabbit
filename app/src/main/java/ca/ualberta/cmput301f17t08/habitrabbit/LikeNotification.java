package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.Date;

/**
 * Created by micah on 03/12/17.
 */

public class LikeNotification extends Notification {

    public LikeNotification(){
        super();
    }

    public LikeNotification(String username, Date date){
        super(username, date);
    }

}
