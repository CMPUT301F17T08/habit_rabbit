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

    public ArrayList<Habit> getHabits(){return this.habitList;}

    public ArrayList<User> getFollowers() {return this.followerList;}

    public ArrayList<User> getFollowing() {return this.followingList;}

    public ArrayList<User> getFollowRequests() {return this.followRequests;}

    public ArrayList<Habit> getHistory() {return this.historylist;}

    public void addFollower(User follower) {
        if (hasFollowing(follower))
            throw new IllegalArgumentException("Follower already existed.");
        this.followerList.add(follower);
        return;}

    public boolean hasFollowing(User user) {
        return this.followingList.contains(user);
    }



    public void removeFollower(User follower) {
        this.followerList.remove(follower);
        return; }

    public void addHabit(Habit habit) {
        if (hasHabit(habit))
            throw new IllegalArgumentException("Habit already existed.");
        this.habitList.add (habit);
        return; }


    public boolean hasHabit(Habit habit) {
        return this.habitList.contains(habit);
    }

    public void removeHabit(Habit habit) {
        this.habitList.remove(habit);
        return; }

    public ArrayList<Habit> filterHistory(String keyword, String filterType){
        return null;}
    public ArrayList<Habit> Habitmissed(Habit habit){
        return null;}



}
