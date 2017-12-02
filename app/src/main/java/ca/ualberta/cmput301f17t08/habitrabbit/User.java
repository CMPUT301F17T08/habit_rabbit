package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;


/**
 * The class for a singal user, has all user's property, like name, and user's habit
 */


public class User {
    private String username;
    private HashSet<String> habitKeyList;
    private ArrayMap<String, Habit> habitList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<String> followRequests;
    private ArrayList<User> likeList;

    private Boolean habitsLoaded;

    public User(){
        this.habitList = new ArrayMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.habitsLoaded = false;
        this.likeList = new ArrayList<User>();
    }

    public User(String username){
        this.username = username;
        this.habitList = new ArrayMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.habitsLoaded = false;
        this.likeList = new ArrayList<User>();
    }

    public void setUsername(String username) {this.username = username;}


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
        if(this.habitsLoaded){
            listener.onHabitsSuccess(habitList);
            return;
        }

        DatabaseManager.getInstance().getHabitsInSet(this.habitKeyList, new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(ArrayMap<String, Habit> habits) {
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
            public void onHabitsSuccess(ArrayMap<String, Habit> habits) {
                // We have an ArrayMap of our habits. Need to retrieve HabitEvents of each.
                ArrayMap<String, HabitEvent> historyList = new ArrayMap<String, HabitEvent>();
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
