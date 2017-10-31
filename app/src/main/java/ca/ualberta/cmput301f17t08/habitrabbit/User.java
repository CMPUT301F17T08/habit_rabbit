package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.ArrayList;

public class User {

    private String username;

    public User(){
        //
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public ArrayList<User> getFollowers() { return null; }
    public void addFollower(User follower) { return; }
    public void removeFollower(User follower) { return; }

    public ArrayList<Habit> getHabits() { return null; }
    public void addHabit(Habit habit) { return; }
    public void removeHabit(Habit habit) { return; }

}
