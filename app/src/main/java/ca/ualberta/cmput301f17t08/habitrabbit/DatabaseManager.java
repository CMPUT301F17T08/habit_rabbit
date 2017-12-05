package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * The class for database
 */
public class DatabaseManager {

    private static DatabaseManager databaseManager = null;

    private FirebaseDatabase database;
    private boolean disconnected;
    private DatabaseReference.CompletionListener nullCompletionListener;
    private ChildEventListener nullChildEventListener;
    private ArrayMap<DatabaseReference, ValueEventListener> repeatListeners;

    private DatabaseManager(){
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        disconnected = false;

        repeatListeners = new ArrayMap<DatabaseReference, ValueEventListener>();

        // Create null completion listener that does nothing
        // Used when we are offline to prevent triggering the setValue completion listener when we regain connection.
        nullCompletionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                // Do nothing.
            }
        };

        // Create null value event listener that does nothing
        // Used to keep the local cache updated with the server, to ensure that the SingleValueEventListener pulls from recent data.
        nullChildEventListener = new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Nothing needed.
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // Set up continuous listener for disconnected boolean key
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);

                disconnected = !connected;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        // Set up a listener for the root path, ensuring that our caches stay in sync
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("/");
        rootRef.addChildEventListener(nullChildEventListener);

    }

    public interface OnUserDataListener {
        public void onUserData(User user);
        public void onUserDataFailed(String message);
    }

    public interface OnHabitsListener {
        public void onHabitsSuccess(HashMap<String, Habit> habits);
        public void onHabitsFailed(String message);
    }

    public interface OnHabitEventsListener {
        public void onHabitEventsSuccess(HashMap<String, HabitEvent> habitEvents);
        public void onHabitEventsFailed(String message);
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
     * Creates user in database and returns user object to listener
     * @param username
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
        this.getUserData(username, false, listener);
    }

    public void getUserData(final String username, boolean repeat, final OnUserDataListener listener){

        final DatabaseReference usersRef = database.getReference("users");
        usersRef.keepSynced(true);

        final DatabaseReference userRef = usersRef.child(username);

        ValueEventListener valListener = new ValueEventListener() {
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
        };

        if(repeat){
            repeatListeners.put(userRef, valListener);
            userRef.addValueEventListener(valListener);
        }else {
            userRef.addListenerForSingleValueEvent(valListener);
        }

    }

    public void stopRepeatListeners(){
        for(Map.Entry<DatabaseReference, ValueEventListener> entry: this.repeatListeners.entrySet()){
            DatabaseReference ref = entry.getKey();
            ValueEventListener val = entry.getValue();

            ref.removeEventListener(val);
        }

        this.repeatListeners.clear();
    }

    public void saveUserData(User user, final OnSaveListener listener){
        // TODO
        // if the network connection isn't available, save locally here

        final DatabaseReference userRef = database.getReference("users").child(user.getUsername());

        if(disconnected) {
            userRef.setValue(user, nullCompletionListener);
            listener.onSaveSuccess();
        }else {
            userRef.setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        listener.onSaveFailure(databaseError.getMessage());
                        return;
                    }

                    listener.onSaveSuccess();
                }
            });
        }
    }

    public void syncLocalData(){
        // TODO
        // saves the locally saved data to the database
        // make it so that this function automatically gets called when the network is available
        // after a disconnect

    }

    public void getHabitsInSet(final Set<String> habitKeys, final OnHabitsListener listener){

        final HashMap<String, Habit> habits = new HashMap<String, Habit>();

        final DatabaseReference habitsRef = database.getReference("habits");

        habitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

        if(disconnected){
            habitRef.setValue(habit, nullCompletionListener);
            listener.onSaveSuccess();
        }else {
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

    public void deleteHabit(Habit habit) {
        String habitId = habit.getId();

        final DatabaseReference habitsRef = database.getReference("habits");
        final DatabaseReference habitRef;

        if(habitId != null) {

            habitRef = habitsRef.child(habitId);
            habitRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    // Nothing to do.
                }
            });

        }
    }

    public void getHabitEventsInSet(final Set<String> habitEventKeys, final OnHabitEventsListener listener){

        final HashMap<String, HabitEvent> habitEvents = new HashMap<String, HabitEvent>();

        final DatabaseReference habitEventsRef = database.getReference("habit_events");

        habitEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(habitEventKeys.contains(child.getKey())){
                        habitEvents.put(child.getKey(), child.getValue(HabitEvent.class));
                    }
                }

                listener.onHabitEventsSuccess(habitEvents);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onHabitEventsFailed(databaseError.getMessage());
            }
        });
    }

    public void saveHabitEvent(final HabitEvent habitEvent, final OnSaveListener listener) {

        // Get ID of habit
        String habitEventId = habitEvent.getId();

        final DatabaseReference habitsRef = database.getReference("habit_events");
        final DatabaseReference habitRef;

        if(habitEventId == null){
            // Habit is new, needs an ID.
            habitEventId = habitsRef.push().getKey();
        }

        habitEvent.setSynced(true);
        habitEvent.setId(habitEventId);

        habitRef = habitsRef.child(habitEventId);

        if(disconnected){
            habitRef.setValue(habitEvent, nullCompletionListener);
            listener.onSaveSuccess();
        }else {
            habitRef.setValue(habitEvent, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.e("Database Manager Error", "Database error: " + databaseError.getMessage());
                        listener.onSaveFailure("Failed to sync habit event due to a database error: " + databaseError.getMessage());
                        habitEvent.setSynced(false);
                    } else {
                        listener.onSaveSuccess();
                    }
                }
            });
        }
    }

    public void deleteHabitEvent(HabitEvent habitEvent) {
        String habitEventId = habitEvent.getId();

        final DatabaseReference habitEventsRef = database.getReference("habit_events");
        final DatabaseReference habitEventRef;

        if(habitEventId != null) {

            habitEventRef = habitEventsRef.child(habitEventId);
            habitEventRef.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    // Nothing to do.
                }
            });

        }
    }
}
