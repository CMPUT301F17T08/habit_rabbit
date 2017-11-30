package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * The class for a singal user, has all user's property, like name, and user's habit
 */


public class User {
    private HashSet<String> habitKeyList;
    private String username;
    private ArrayMap<String, Habit> habitList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<String> followRequests;
    private ArrayList<HabitEvent> historylist;

    private Boolean habitsLoaded;

    public User(){
        this.habitList = new ArrayMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.historylist = new ArrayList<HabitEvent>();
        this.habitsLoaded = false;
    }

    public User(String username){
        this.username = username;
        this.habitList = new ArrayMap<String, Habit>();
        this.habitKeyList = new HashSet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.historylist = new ArrayList<HabitEvent>();
        this.habitsLoaded = false;
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

    public ArrayList<HabitEvent> getHistory() {return this.historylist;}

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

    public void removeHabit(final Habit habit, final DatabaseManager.OnSaveListener listener) {
//        if(habitsLoaded){
//            this.habitList.remove(habit.getId());
//        }else{
//            this.habitKeyList.remove(habit.getId());
//        }

//        habit.delete();

        final User self = this;

        habit.sync(new DatabaseManager.OnSaveListener() {
            @Override
            public void onSaveSuccess() {
                self.habitList.remove(habit.getId());
                self.habitKeyList.remove(habit.getId());

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



        return;
    }

    public void setFollowers(ArrayList<String> followers){
        this.followerList = (ArrayList<String>)followers.clone();
    }

    public void setFollowing(ArrayList<String> following){
        this.followingList = (ArrayList<String>)following.clone();
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

    public void addToHistory(HabitEvent event){
        this.historylist.add(event);
    }

    // TODO we can maybe fix this since it's weird to call remove by index
    public void removeFromHistory(int position){
        this.historylist.remove(position);
    }

    public void editEventFromHistory(int position, HabitEvent newEvent){
        this.historylist.set(position, newEvent);
    }

}
