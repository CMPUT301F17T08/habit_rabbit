package ca.ualberta.cmput301f17t08.habitrabbit;

import android.text.style.TtsSpan;
import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
/**
 * The class for database
 */
public class DatabaseManager {

    private static DatabaseManager databaseManager = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseManager(){
        database = FirebaseDatabase.getInstance();
    }

    public interface OnUserDataListener {
        public void onUserData(User user);
        public void onUserDataFailed(String message);
    }

    public interface OnHabitsListener {
        public void onHabitsSuccess(ArrayMap<String, Habit> habits);
        public void onHabitsFailed(String message);
    }

    public interface OnSaveListener {
        public void onSaveSuccess();
        public void onSaveFailure(String message);
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
    public void createUser(final String username, final OnUserDataListener listener){
        // TODO: Data validation (not empty, etc)

        final DatabaseReference usersRef = database.getReference("users");
        final DatabaseReference userRef = usersRef.child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // User already exists with this username. Return failure:
                    Log.e("Database Manager Error", "User already exists");
                    listener.onUserDataFailed("A user already exists with this username. Please choose a different name.");
                    return;
                }

                // Else, user does not exist yet. Create, push to Firebase, and return user object:
                final User newUser = new User(username);

                userRef.setValue(newUser, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError != null){
                            Log.e("Database Manager Error", "Database error: " + databaseError.getMessage());
                            listener.onUserDataFailed("Failed to create user due to a database error: " + databaseError.getMessage());
                        }else{
                            listener.onUserData(newUser);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Manager Error", "Database error: " + databaseError.getMessage());
                listener.onUserDataFailed("Failed to create user due to a database error: " + databaseError.getMessage());
            }
        });

    }

    public void getUserData(final String username, final OnUserDataListener listener){

        final DatabaseReference userRef = database.getReference("users").child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    // User does not exist!
                    Log.e("Database Manager Error", "User does not exist: " + username);
                    listener.onUserDataFailed("Login failed: user does not exist.");
                    return;
                }

                // Else, return user object:
                final User user = dataSnapshot.getValue(User.class);

                listener.onUserData(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Manager Error", "Database error: " + databaseError.getMessage());
                listener.onUserDataFailed("Login failed: user does not exist.");
            }
        });
    }

    public void saveUserData(User user, final OnSaveListener listener){
        // TODO
        // if the network connection isn't available, save locally here

        final DatabaseReference userRef = database.getReference("users").child(user.getUsername());

        userRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null){
                    listener.onSaveFailure(databaseError.getMessage());
                    return;
                }

                listener.onSaveSuccess();
            }
        });
    }

    public void syncLocalData(){
        // TODO
        // saves the locally saved data to the database
        // make it so that this function automatically gets called when the network is available
        // after a disconnect

    }

    public void getHabitsInSet(final Set<String> habitKeys, final OnHabitsListener listener){

        final ArrayMap<String, Habit> habits = new ArrayMap<String, Habit>();

        final DatabaseReference habitsRef = database.getReference("habits");

        habitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(habitKeys.contains(child.getKey())){
                        habits.put(child.getKey(), child.getValue(Habit.class));
                    }
                }

                listener.onHabitsSuccess(habits);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onHabitsFailed(databaseError.getMessage());
            }
        });
    }

    public void saveHabit(final Habit habit, final OnSaveListener listener) {

        // Get ID of habit
        String habitId = habit.getId();

        final DatabaseReference habitsRef = database.getReference("habits");
        final DatabaseReference habitRef;

        if(habitId == null){
            // Habit is new, needs an ID.
            habitId = habitsRef.push().getKey();
        }

        habit.setSynced(true);
        habit.setId(habitId);

        habitRef = habitsRef.child(habitId);
        habitRef.setValue(habit, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("Database Manager Error", "Database error: " + databaseError.getMessage());
                    listener.onSaveFailure("Failed to sync habit due to a database error: " + databaseError.getMessage());
                    habit.setSynced(false);
                } else {
                    listener.onSaveSuccess();
                }
            }
        });

    }
}
