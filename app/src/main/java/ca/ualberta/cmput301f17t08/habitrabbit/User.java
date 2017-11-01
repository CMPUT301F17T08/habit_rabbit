package ca.ualberta.cmput301f17t08.habitrabbit;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private ArrayList<Habit> habits;

    public User(){
        habits = new ArrayList<Habit>();
    }

    public User(String username) {
        habits = new ArrayList<Habit>();
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public ArrayList<User> getFollowers() { return null; }
    public void addFollower(User follower) { return; }
    public void removeFollower(User follower) { return; }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }
    public void removeHabit(Habit habit) { return; }

}
