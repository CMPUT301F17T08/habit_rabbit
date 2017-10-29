package ca.ualberta.cmput301f17t08.habitrabbit;

import com.google.gson.Gson;

/**
 * Created by maharshmellow on 2017-10-28.
 */

public class DatabaseManager {
    private String databaseLocation = "https://inserturlhere.com";

    public static User createUser(String username){
        // TODO
        // create a new user object with the empty arrays
        // store that user object on the database
        // return the user object

        return null;
    }

    public static Gson getUserData(String username){
        // TODO
        // find the user in the database and return the data
        return null;
    }

    public static void saveUserData(User user){
        // TODO
        // if the network connection isn't available, save locally here
    }

    public static void syncLocalData(){
        // TODO
        // saves the locally saved data to the database
        // make it so that this function automatically gets called when the network is available
        // after a disconnect

    }

}
