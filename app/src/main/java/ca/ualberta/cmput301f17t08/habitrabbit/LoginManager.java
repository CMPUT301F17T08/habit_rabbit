package ca.ualberta.cmput301f17t08.habitrabbit;

import android.view.View;

/**
 * Login manager could manage different users
 */

public class LoginManager {
    private static LoginManager loginManager;
    private User currentUser;



    public interface OnLoginCompleteListener{
        public void onLoginComplete();
        public void onLoginFailed(String message);
    }

    private LoginManager(){
        // Private constructor for singleton.
    }

    public static LoginManager getInstance(){
        if(loginManager == null){
            loginManager = new LoginManager();
        }

        return loginManager;
    }

    public void login(String username, final OnLoginCompleteListener listener){
        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                currentUser = user;
                listener.onLoginComplete();
            }

            @Override
            public void onUserDataFailed(String message) {
                listener.onLoginFailed(message);
            }
        });

    }

    public void logout(){
        // TODO update the database here if required
        this.currentUser = null;
    }

    public void signup(String username, final OnLoginCompleteListener listener){
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.createUser(username, new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                currentUser = user;
                listener.onLoginComplete();
            }

            @Override
            public void onUserDataFailed(String message) {
                listener.onLoginFailed(message);
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /* NOTE
    * We won't need the setCurrentUser() even though it's in the UML since login() can do its job
    * */



}
