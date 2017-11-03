package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.ArrayList;

/**
 * Created by mseneshen on 2017-10-23.
 */

public class User {
    private String username;
    private ArrayList<Habit> habitList;
    private ArrayList<User> followerList;
    private ArrayList<User> followingList;
    private ArrayList<User> followRequests;
    private ArrayList<Habit> historylist;

    public User(String username){
        this.username = username;
        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<User>();
        this.followingList = new ArrayList<User>();
        this.followRequests = new ArrayList<User>();
        this.historylist = new ArrayList<Habit>();

    }


    public String getUserName(){
        return this.username;
    }

    public ArrayList<Habit> getHabitList(){return this.habitList;}

    public ArrayList<User> getFollowerList() {return this.followerList;}

    public ArrayList<User> getFollowingList() {return this.followingList;}

    public ArrayList<User> getFollowRequests() {return this.followRequests;}

    public ArrayList<Habit> getHistorylist() {return this.historylist;}

    public void addFollower(User follower) {
        followerList.add(follower);
        return; }

    public void removeFollower(int follower_index) {
        // TODO
        // Need to get the index of the follower for the future developing
        followerList.remove(follower_index);
        return; }

    public void addHabit(Habit habit) {
        habitList.add (habit);
        return; }

    public void removeHabit(int habit_idex) {
        habitList.remove(habit_idex);
        return; }
    
    public ArrayList<Habit> filterHistory(String keyword, String filterType){return null;}
    public ArrayList<Habit> habitmissed(Habit habit){return null;}




    public ArrayList<User> getFollowers() { return null; }

    public ArrayList<Habit> getHabits() { return null; }

}
