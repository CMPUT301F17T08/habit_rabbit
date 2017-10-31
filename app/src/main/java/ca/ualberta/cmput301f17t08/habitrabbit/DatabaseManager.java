package ca.ualberta.cmput301f17t08.habitrabbit;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.*;

public class DatabaseManager {

    private static DatabaseManager databaseManager = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public interface OnUserCreatedListener {
        public void onUserCreated(User user);
    }

    public static DatabaseManager getInstance(){
        if(databaseManager == null){
            databaseManager = new DatabaseManager();
        }

        return databaseManager;
    }


    /**
     * Creates user in database and returns user object
     * @param username
     * @return
     */
    public void createUser(final String username, final OnUserCreatedListener listener){
        // TODO: Data validation (not empty, etc)

        final DatabaseReference usersRef = database.getReference("users");
        final DatabaseReference userRef = usersRef.child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // User already exists with this username. Return null to listener:
                    Log.e("Database Manager Error", "User already exists");
                    listener.onUserCreated(null);
                    return;
                }

                // Else, user does not exist yet. Create, push to Firebase, and return user object:
                final User newUser = new User(username);

                userRef.setValue(newUser, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError != null){
                            Log.e("Database Manager Error", "Failed to add user to database.");
                            listener.onUserCreated(null);
                        }else{
                            listener.onUserCreated(newUser);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: handle database error
            }
        });

    }

    public Gson getUserData(String username){
        // TODO
        // find the user in the database and return the data
        return null;
    }

    public void saveUserData(User user){
        // TODO
        // if the network connection isn't available, save locally here
    }

    public void syncLocalData(){
        // TODO
        // saves the locally saved data to the database
        // make it so that this function automatically gets called when the network is available
        // after a disconnect

    }

}
