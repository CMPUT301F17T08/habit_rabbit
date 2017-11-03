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

    public void addFollower(User follower) { return; }
    public void removeFollower(User follower) { return; }
    public void addHabit(Habit habit) { return; }
    public void removeHabit(Habit habit) { return; }
    public ArrayList<Habit> filterHistory(String keyword, String filterType){return null;}
    public ArrayList<Habit> habitmissed(Habit habit){return null;}




    public ArrayList<User> getFollowers() { return null; }

    public ArrayList<Habit> getHabits() { return null; }

}
