package ca.ualberta.cmput301f17t08.habitrabbit;

/**
 * Created by mseneshen on 2017-10-23.
 */

public class LoginManager {
    
    private User currentUser;

    public void login(String username){
        // TODO get the user object from the database here
        // this.currentUser = user;

    }
    public void logout(){
        // TODO update the database here if required
        this.currentUser = null;
    }

    public void signup(String username){
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.createUser(username, new DatabaseManager.OnUserCreatedListener() {
            @Override
            public void onUserCreated(User user) {
                if(user == null){
                    // TODO: handle failure
                }else{
                    currentUser = user;
                }
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
