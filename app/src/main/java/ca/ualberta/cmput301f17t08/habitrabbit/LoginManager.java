package ca.ualberta.cmput301f17t08.habitrabbit;

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

    public void login(final String username, final OnLoginCompleteListener listener){
        DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
            @Override
            public void onUserData(User user) {
                currentUser = user;

                // Create listener to keep user up to date (will be cleared at logout)
                DatabaseManager.getInstance().getUserData(username, true, new DatabaseManager.OnUserDataListener() {
                    @Override
                    public void onUserData(User user) {
                        currentUser = user;
                    }

                    @Override
                    public void onUserDataFailed(String message) {

                    }
                });

                listener.onLoginComplete();
            }

            @Override
            public void onUserDataFailed(String message) {
                listener.onLoginFailed(message);
            }
        });

    }

    public void logout(){
        DatabaseManager.getInstance().stopRepeatListeners();
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



}
