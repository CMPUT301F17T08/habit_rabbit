package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The defined adapter for the people layout
 */
public class FollowingHabitAdapter extends RecyclerView.Adapter<FollowingHabitAdapter.ViewHolder> {
    private ArrayList<Habit> habits;
    private Activity context;
    private String username;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView followingNameLabel;

        public ViewHolder(View peopleView) {
            super(peopleView);
            followingNameLabel = (TextView) peopleView.findViewById(R.id.user_name);
        }
    }

    /**
     * The adapter to show the habits for the people you followed
     * @param habits // the habits list that person has
     * @param context// the context
     * @param username // the clicked person's username
     */
    public FollowingHabitAdapter(ArrayList<Habit> habits, Activity context, String username) {
        this.habits = habits;
        this.context = context;
        this.username = username;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FollowingHabitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View peopleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        FollowingHabitAdapter.ViewHolder viewHolder = new FollowingHabitAdapter.ViewHolder(peopleView);

        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //update  the username on layout
        holder.followingNameLabel.setText(habits.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass the username of the person that user clicks
                Intent intent = new Intent(context, LastCompleteActivity.class);
                intent.putExtra("TheFollowName",username);
                intent.putExtra("habitId", habits.get(position).getId());
                intent.putExtra("habitObject", habits.get(position));
                context.startActivity(intent);
            }
        });

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return habits.size();
    }
}