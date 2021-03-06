package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;


/**
 * The class for a signal user, has all user's property, like name, and user's habit
 */


public class User {
    private String username;
    private HashSet<String> habitKeyList;
    private HashMap<String, Habit> habitList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<FollowNotification> followRequests;
    private ArrayList<LikeNotification> likes;

    private Boolean habitsLoaded;

    /**
     * Initializes a user object
     */
    public User(){
        this.habitList = new HashMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<FollowNotification>();
        this.likes = new ArrayList<LikeNotification>();
        this.habitsLoaded = false;
    }

    public User(String username){
        this.username = username;
        this.habitList = new HashMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<FollowNotification>();
        this.likes = new ArrayList<LikeNotification>();
        this.habitsLoaded = false;
    }

    /**
     * Sets the user's username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the habit keys
     * @param habits
     */
    public void setHabitKeys(ArrayList<String> habits){
        habitKeyList = new HashSet<String>(habits);
        habitsLoaded = false;
        habitList.clear();
    }

    /**
     * Gets the habit keys hashset
     * @return
     */
    @Exclude
    public HashSet<String> getHabitKeysSet(){
        if(habitsLoaded){
            return new HashSet<String>(this.habitList.keySet());
        }else{
            return this.habitKeyList;
        }
    }

    /**
     * gets the habit keys
     * @return
     */
    public ArrayList<String> getHabitKeys(){

        if(habitsLoaded){
            return new ArrayList<String>(this.habitList.keySet());
        }else{
            return new ArrayList<String>(this.habitKeyList);
        }
    }


    /**
     * gets the username
     * @return
     */
    public String getUsername(){
        return this.username;
    }


    /**
     * gets a list of all the user's habits
     * @param listener
     */
    @Exclude
    public void getHabits(final DatabaseManager.OnHabitsListener listener){

        // Do not use habitsLoaded flag here and return cached! Causes bug when habit is edited.

        DatabaseManager.getInstance().getHabitsInSet(this.getHabitKeysSet(), new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(HashMap<String, Habit> habits) {
                habitList = habits;
                habitsLoaded = true;
                listener.onHabitsSuccess(habits);
            }

            @Override
            public void onHabitsFailed(String message) {
                listener.onHabitsFailed(message);
            }
        });
    }

    /**
     * gets a list of all the users' names that are following this user
     * @return
     */
    public ArrayList<String> getFollowers() {return this.followerList;}

    /**
     * gets a list of all the users this user is following
     * @return
     */
    public ArrayList<String> getFollowing() {return this.followingList;}

    /**
     * gets the list of follow requests that are pending
     * @return
     */
    public ArrayList<FollowNotification> getFollowRequests() {
        return this.followRequests;
    }

    /**
     * sets the list of follow requests that are pending
     * @param notifications
     */
    public void setFollowRequests(ArrayList<FollowNotification> notifications) {
        this.followRequests = notifications;
    }

    /**
     * removes a follower from the list of follow requests after the processing the request
     * @param removedUser
     * @param listener
     */
    public void removeFromFollowRequests(User removedUser, DatabaseManager.OnSaveListener listener){
        int i = 0;

        while(i < this.followRequests.size()) {
            Notification notification = followRequests.get(i);

            if (notification.getUsername().equals(removedUser.getUsername())) {
                this.followRequests.remove(notification);
            }else{
                i++;
            }
        }

        this.save(listener);
    }

    /**
     * adds a follow request
     * @param user
     * @param listener
     */
    public void addFollowRequest(User user, DatabaseManager.OnSaveListener listener){
        FollowNotification notification = new FollowNotification(user.getUsername(), new Date());
        this.followRequests.add(notification);

        this.save(listener);
    }

    /**
     * returns a list of notification objects that represent a single like
     * @return
     */
    public ArrayList<LikeNotification> getLikes() {
        return this.likes;
    }

    /**
     * sets the list of like objects
     * @param notifications
     */
    public void setLikes(ArrayList<LikeNotification> notifications) {
        this.likes = notifications;
    }

    /**
     * adds a new like object
     * @param user
     * @param listener
     */
    public void addLike(User user, DatabaseManager.OnSaveListener listener){
        LikeNotification notification = new LikeNotification(user.getUsername(), new Date());
        this.likes.add(notification);

        this.save(listener);
    }

    /**
     * makes this user follow another user
     * @param follower
     */
    public void addFollowing(User follower) {
        if (!hasFollowing(follower))
            this.followingList.add(follower.getUsername());

        return;
    }

    /**
     * checks if this user is being followerd by someone
     * @param follower
     * @return
     */
    public boolean hasFollower(User follower){
        return this.followerList.contains(follower.getUsername());

    }

    /**
     * adds another user as a follower of this user
     * @param follower
     */
    public void  addFollower(User follower){
        if (!hasFollower(follower))
            this.followerList.add(follower.getUsername());

        return;

    }

    /**
     * checks if some user is following this user
     * @param user
     * @return
     */
    private boolean hasFollowing(User user) {
        return this.followingList.contains(user.getUsername());
    }

    /**
     * removes this user's follower
     * @param follower
     */
    public void removeFollower(User follower) {
        this.followerList.remove(follower.getUsername());
        return;
    }

    /**
     * adds a new habit that this user is tracking
     * @param habit
     * @param listener
     */
    public void addHabit(final Habit habit, final DatabaseManager.OnSaveListener listener) {
        if (hasHabit(habit))
            throw new IllegalArgumentException("Habit already exists.");



        final User self = this;

        if(!this.habitsLoaded){
            this.getHabits(new DatabaseManager.OnHabitsListener() {
                @Override
                public void onHabitsSuccess(HashMap<String, Habit> habits) {
                    addHabitAfterLoad(habit, listener);
                }

                @Override
                public void onHabitsFailed(String message) {
                    listener.onSaveFailure(message);
                }
            });
        }else{
            addHabitAfterLoad(habit, listener);
        }

    }

    private void addHabitAfterLoad(final Habit habit, final DatabaseManager.OnSaveListener listener){

        final User self = this;

        if(habit.getSynced()){
            this.habitList.put(habit.getId(), habit);
            this.save(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    listener.onSaveSuccess();
                }

                @Override
                public void onSaveFailure(String message) {
                    listener.onSaveFailure(message);
                }
            });
        }else{

            habit.sync(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    self.habitList.put(habit.getId(), habit);
                    self.save(new DatabaseManager.OnSaveListener() {
                        @Override
                        public void onSaveSuccess() {
                            listener.onSaveSuccess();
                        }

                        @Override
                        public void onSaveFailure(String message) {
                            listener.onSaveFailure(message);
                        }
                    });
                }

                @Override
                public void onSaveFailure(String message) {
                    listener.onSaveFailure(message);
                }
            });
        }
    }

    private boolean hasHabit(Habit habit) {
        return this.habitList.containsKey(habit.getId());
    }

    /**
     * checks if this user has a habit being tracked
     * @param title
     * @return
     */
    public boolean hasHabit(String title) {
        for(Habit habit : this.habitList.values()){
            if(Objects.equals(habit.getName(), title))
                return true;
        }

        return false;
    }

    /**
     * removes a habit that this user is tracking
     * @param habit
     */
    public void removeHabit(Habit habit) {
        if(habitsLoaded){
            this.habitList.remove(habit.getId());
            this.save(new DatabaseManager.OnSaveListener() {
                @Override
                public void onSaveSuccess() {
                    // Nothing to be done.
                }

                @Override
                public void onSaveFailure(String message){
                    // Nothing to be done.
                }
            });
        }else{
            this.habitKeyList.remove(habit.getId());
        }

        habit.delete();

        return;
    }

    /**
     * sets the list of followers
     * @param followers
     */
    public void setFollowers(ArrayList<String> followers){
        this.followerList = (ArrayList<String>)followers.clone();
    }

    /**
     * sets the list of people the user is following
     * @param following
     */
    public void setFollowing(ArrayList<String> following){
        this.followingList = (ArrayList<String>)following.clone();
    }

    public ArrayList<Habit> filterHistoryByType(String keyword) {

        return null;
    }

    public ArrayList<Habit> HabitMissed(Habit habit){
        return null;
    }

    public void save(DatabaseManager.OnSaveListener listener) {
        DatabaseManager.getInstance().saveUserData(this, listener);
    }

    /**
     * gets the user's habit event history
     * @param listener
     */
    public void getHistory(final DatabaseManager.OnHabitEventsListener listener) {
        // We need to iterate over habits and gather HabitEvents into one array.
        User self = this;

        this.getHabits(new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(HashMap<String, Habit> habits) {
                // We have a HashMap of our habits. Need to retrieve HabitEvents of each.
                HashMap<String, HabitEvent> historyList = new HashMap<String, HabitEvent>();
                HashSet<String> historyKeyList = new HashSet<String>();

                for(Habit habit : habits.values()){
                    historyKeyList.addAll(habit.getHabitEventKeys());
                }

                DatabaseManager.getInstance().getHabitEventsInSet(historyKeyList, listener);
            }

            @Override
            public void onHabitsFailed(String message) {
                Log.e("User", "Failed to get habits!");
            }
        });
    }
}
