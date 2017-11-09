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
    private ArrayList<HabitEvent> habiteventlist;

    public User(){
        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<User>();
        this.followingList = new ArrayList<User>();
        this.followRequests = new ArrayList<User>();
        this.historylist = new ArrayList<Habit>();
        this.habiteventlist = new ArrayList<HabitEvent>();

    }

    public User(String username){
        this.username = username;
        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<User>();
        this.followingList = new ArrayList<User>();
        this.followRequests = new ArrayList<User>();
        this.historylist = new ArrayList<Habit>();
        this.habiteventlist = new ArrayList<HabitEvent>();

    }
    public void setUsername (String username) {this.username = username;}

    public String getUsername(){
        return this.username;
    }

    public ArrayList<HabitEvent> getHabitEvents(){return this.habiteventlist;}

    public ArrayList<Habit> getHabits(){return this.habitList;}

    public ArrayList<User> getFollowers() {return this.followerList;}

    public ArrayList<User> getFollowing() {return this.followingList;}

    public ArrayList<User> getFollowRequests() {return this.followRequests;}

    public ArrayList<Habit> getHistory() {return this.historylist;}

    public void addFollower(User follower) {
        if (hasFollowing(follower))
            throw new IllegalArgumentException("Follower already existed.");

        this.followerList.add(follower);
        return;
    }

    private boolean hasFollowing(User user) {
        return this.followingList.contains(user);
    }


    public void removeFollower(User follower) {
        this.followerList.remove(follower);
        return;
    }

    public void addHabit(Habit habit) {
        if (hasHabit(habit))
            throw new IllegalArgumentException("Habit already existed.");

        this.habitList.add (habit);
        return;
    }


    private boolean hasHabit(Habit habit) {
        return this.habitList.contains(habit);
    }


    public void addHabitEvent(HabitEvent habitevent) {
        if (hasHabitEvent(habitevent))
            throw new IllegalArgumentException("HabitEvent already exists.");

        this.habiteventlist.add (habitevent);
        return;
    }


    private boolean hasHabitEvent(HabitEvent habitevent) {
        return this.habiteventlist.contains(habitevent);
    }

    public void removeHabit(Habit habit) {
        this.habitList.remove(habit);
        return;
    }


    
    //// TODO: change this to an activity
    public void viewDetail(Habit habit){
        return;
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

    public ArrayList<HabitEvent> filterHistoryByComment(String keyword) {

            ArrayList<HabitEvent> result = new ArrayList<>();

            for (HabitEvent habitevent : habiteventlist) {
                if (habitevent.getComment().contains(keyword)) {
                    result.add(habitevent);
                }
            }

        return result;
    }

    public ArrayList<Habit> HabitMissed(Habit habit){
        return null;
    }


}
