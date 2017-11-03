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



    public ArrayList<User> getFollowers() { return null; }
    public void addFollower(User follower) { return; }
    public void removeFollower(User follower) { return; }

    public ArrayList<Habit> getHabits() { return null; }
    public void addHabit(Habit habit) { return; }
    public void removeHabit(Habit habit) { return; }

}
