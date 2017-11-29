package ca.ualberta.cmput301f17t08.habitrabbit;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * The class for a singal user, has all user's property, like name, and user's habit
 */


public class User {
    private String username;
    private ArrayList<Habit> habitList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private ArrayList<String> followRequests;
    private ArrayList<HabitEvent> historylist;
    public User(){
        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.historylist = new ArrayList<HabitEvent>();
    }

    public User(String username){
        this.username = username;
        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<String>();
        this.followingList = new ArrayList<String>();
        this.followRequests = new ArrayList<String>();
        this.historylist = new ArrayList<HabitEvent>();
    }

    public void setUsername(String username) {this.username = username;}

    public void setHabits(ArrayList<Habit> habits){
        this.habitList = habits;
    }

    public String getUsername(){
        return this.username;
    }

    public ArrayList<Habit> getHabits(){return this.habitList;}

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

    public void addHabit(Habit habit) {
        if (hasHabit(habit.getName()))
            throw new IllegalArgumentException("Habit already existed.");

        this.habitList.add(habit);
        return;
    }


    public boolean hasHabit(String habitName) {
        boolean result = false;
        for (Habit signalhabit : this.habitList) {
            if (signalhabit.getName().equals(habitName)) {
                result = true;
            }
        }
        return result;
    }

    public void removeHabit(String habitName) {
        for (Habit habit : this.habitList){
            if (habit.getName().equals(habitName)){
                habitList.remove(habit);
                break;
            }
        }
        return;
    }

    public void setFollowers(ArrayList<String> followers){
        this.followerList = (ArrayList<String>)followers.clone();
    }

    public void setFollowing(ArrayList<String> following){
        this.followingList = (ArrayList<String>)following.clone();
    }

    public ArrayList<Habit> filterHistoryByType(String keyword) {
        ArrayList<Habit> result = new ArrayList<>();

            for (Habit habit : habitList) {
                if (habit.getName() == keyword) {
                    result.add(habit);
                }
            }
            return result;
    }

    public ArrayList<Habit> HabitMissed(Habit habit){
        return null;
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
