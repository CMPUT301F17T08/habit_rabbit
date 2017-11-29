package ca.ualberta.cmput301f17t08.habitrabbit;

import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by mseneshen on 2017-10-23.
 */

public class User {
    private ArraySet<String> habitKeyList;
    private String username;
    private ArrayMap<String, Habit> habitList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<String> followRequests;
    private ArrayList<HabitEvent> historylist;

    private Boolean habitsLoaded;

    public User(){
        this.habitList = new ArrayMap<String, Habit>();
        this.habitKeyList = new ArraySet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.historylist = new ArrayList<HabitEvent>();
        this.habitsLoaded = false;
    }

    public User(String username){
        this.username = username;
        this.habitList = new ArrayMap<String, Habit>();
        this.habitKeyList = new ArraySet<String>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.historylist = new ArrayList<HabitEvent>();
        this.habitsLoaded = false;
    }

    public void setUsername(String username) {this.username = username;}

    public Set<String> getHabitKeys(){
        if(habitsLoaded){
            return this.habitList.keySet();
        }else{
            return this.habitKeyList;
        }
    }

    public void setHabitKeys(ArraySet<String> habits){
        habitKeyList = habits;
        habitsLoaded = false;
        habitList.clear();
    }

    public String getUsername(){
        return this.username;
    }

    @Exclude
    public void getHabits(DatabaseManager.OnHabitsListener listener){
        if(this.habitsLoaded){
            listener.onHabitsSuccess(habitList);
            return;
        }

        DatabaseManager.getInstance().getHabitsInSet(this.habitKeyList, new DatabaseManager.OnHabitsListener() {
            @Override
            public void onHabitsSuccess(ArrayMap<String, Habit> habits) {
                habitList = habits;
                habitsLoaded = true;
            }

            @Override
            public void onHabitsFailed(String message) {
                Log.e("DatabaseManager", message);
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

    public void addHabit(Habit habit, DatabaseManager.OnSaveListener listener) {
        if (hasHabit(habit))
            throw new IllegalArgumentException("Habit already exists.");

        if(habit.getSynced()){
            listener.onSaveSuccess();
        }else{
            habit.sync(listener);
        }

    }

    private boolean hasHabit(Habit habit) {
        return this.habitKeyList.contains(habit);
    }

    public void removeHabit(Habit habit) {
        if(habitsLoaded){
            this.habitList.remove(habit.getId());
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


}
