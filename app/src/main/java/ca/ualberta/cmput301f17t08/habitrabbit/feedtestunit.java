package ca.ualberta.cmput301f17t08.habitrabbit;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jackson on 2017-12-01.
 */

public class feedtestunit implements Serializable {

    private Date dateCompleted;
    private String username;



    public feedtestunit(String username, Date dateCompleted){
        this.username = username;
        this.dateCompleted = dateCompleted;
    }

    public Date getDate(){
        return dateCompleted;
    }


    @Override
    // convert the counter to string
    public String toString() {
        return "Name: "+username +" | Date: "+ dateCompleted;
    }
}
