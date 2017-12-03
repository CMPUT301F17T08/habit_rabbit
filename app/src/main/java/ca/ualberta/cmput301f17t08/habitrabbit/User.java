package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.TimeZone;


/**
 * The class for a singal user, has all user's property, like name, and user's habit
 */


public class User {
    private String username;
    private HashSet<String> habitKeyList;
    private HashMap<String, Habit> habitList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<String> followRequests;
    private ArrayList<User> likeList;

    private Boolean habitsLoaded;

    public User(){
        this.habitList = new HashMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.habitsLoaded = false;
        this.likeList = new ArrayList<User>();
    }

    public User(String username){
        this.username = username;
        this.habitList = new HashMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.habitsLoaded = false;
        this.likeList = new ArrayList<User>();
    }

    public void setUsername(String username) {this.username = username;}

    @Exclude
    public HashSet<String> getHabitKeysSet(){
        if(habitsLoaded){
            return new HashSet<String>(this.habitList.keySet());
        }else{
            return this.habitKeyList;
        }
    }

    public ArrayList<String> getHabitKeys(){
        if(habitsLoaded){
            return new ArrayList<String>(this.habitList.keySet());
        }else{
            return new ArrayList<String>(this.habitKeyList);
        }
    }

    public void setHabitKeys(ArrayList<String> habits){
        habitKeyList = new HashSet<String>(habits);
        habitsLoaded = false;
        habitList.clear();
    }

    public String getUsername(){
        return this.username;
    }

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

    public ArrayList<String> getFollowers() {return this.followerList;}

    public ArrayList<String> getFollowing() {return this.followingList;}

    public ArrayList<String> getFollowRequests() {return this.followRequests;}

    public void addFollower(User follower) {
        if (hasFollowing(follower))
            throw new IllegalArgumentException("Follower already existed.");

        this.followerList.add(follower.getUsername());
        return;
    }

    private boolean hasFollowing(User user) {
        return this.followingList.contains(user.getUsername());
    }


    public void removeFollower(User follower) {
        this.followerList.remove(follower.getUsername());
        return;
    }

    public void addHabit(final Habit habit, final DatabaseManager.OnSaveListener listener) {
        if (hasHabit(habit))
            throw new IllegalArgumentException("Habit already exists.");

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
            final User self = this;

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

    public boolean hasHabit(String title) {
        for(Habit habit : this.habitList.values()){
            if(Objects.equals(habit.getName(), title))
                return true;
        }

        return false;
    }

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

    public void setFollowers(ArrayList<String> followers){
        this.followerList = (ArrayList<String>)followers.clone();
    }

    public void setFollowing(ArrayList<String> following){
        this.followingList = (ArrayList<String>)following.clone();
    }

    public void addLikedUser(User newUser){

        this.likeList.add(newUser);
    }

    public void removeLikedUser(User newUser){

        this.likeList.remove(newUser);
    }

    public ArrayList<User> getLikeListener(){

        return likeList;
    }


    public ArrayList<Habit> filterHistoryByType(String keyword) {
        // TODO: We need to rework this by adding an extra parameter of a listener
        /*
        ArrayList<Habit> result = new ArrayList<>();


            for (Habit habit : habitKeyList) {
                if (habit.getName() == keyword) {
                    result.add(habit);
                }
            }
            return result;
        */

        return null;
    }

    public ArrayList<Habit> HabitMissed(Habit habit){
        return null;
    }

    public void save(DatabaseManager.OnSaveListener listener) {
        DatabaseManager.getInstance().saveUserData(this, listener);
    }

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

    /**
     * Checks if a streak has been broken
     */
    public void updateStreaks(){

        System.out.println("Updating Streaks");
        for (Habit habit : habitList.values()){
            System.out.println(habit.getName());
            if (habit.getLastCompleted() != null){

                System.out.println(habit.getName());
                // make the streak 0 if a day was missed in the middle
                int [] conversion_table = {0, 6, 0, 1, 2, 3, 4, 5};

                Calendar lastCompleted = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));
                lastCompleted.setTime(habit.getLastCompleted());
                lastCompleted.add(lastCompleted.DATE, 1);

                Calendar current = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));

                int lastCompletedPointer = lastCompleted.get(Calendar.DAY_OF_WEEK);
                int currentDayPointer = current.get(Calendar.DAY_OF_WEEK);

                // shift the days of the week to match the frequency array
                lastCompletedPointer = conversion_table[lastCompletedPointer];
                currentDayPointer = conversion_table[currentDayPointer];

                if (lastCompletedPointer == currentDayPointer + 1){
                    break;
                }

                System.out.println(lastCompleted.getTime() + " " + current.getTime());
                System.out.println("--");
                while(lastCompletedPointer != currentDayPointer){
                    System.out.println(lastCompleted.getTime() + " " + current.getTime());
                    // check if the last completed pointer is on a day that a frequency value of 1
                    if (habit.getFrequency().get(conversion_table[lastCompletedPointer]) == 1){
                        System.out.println("Break");
                        habit.resetStreak();
                        break;
                    }

                    // make it loop back to the start of the week
                    lastCompletedPointer = (lastCompletedPointer + 1) % 7;

                }

            }
        }
    }
}
